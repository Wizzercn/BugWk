package cn.wizzer.bugwk.controllers;

import cn.wizzer.bugwk.commons.base.Result;
import cn.wizzer.bugwk.commons.filter.MyCrossOriginFilter;
import cn.wizzer.bugwk.commons.filter.MyUserRoleFilter;
import cn.wizzer.bugwk.commons.service.BugService;
import cn.wizzer.bugwk.commons.service.LuceneSearchResult;
import cn.wizzer.bugwk.commons.service.SearchService;
import cn.wizzer.bugwk.commons.utils.Markdowns;
import cn.wizzer.bugwk.commons.utils.Toolkit;
import cn.wizzer.bugwk.modles.Bug;
import cn.wizzer.bugwk.modles.Reply;
import cn.wizzer.bugwk.modles.Tag;
import cn.wizzer.bugwk.modles.User;
import org.nutz.aop.interceptor.async.Async;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.QueryResult;
import org.nutz.dao.Sqls;
import org.nutz.dao.entity.Record;
import org.nutz.dao.pager.Pager;
import org.nutz.dao.sql.Sql;
import org.nutz.integration.jedis.pubsub.PubSubService;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.lang.Times;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.adaptor.JsonAdaptor;
import org.nutz.mvc.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by wizzer on 2018.08
 */
@IocBean
@At("/platform/bug")
@Filters({@By(type = MyCrossOriginFilter.class), @By(type = MyUserRoleFilter.class)})
public class BugController {
    private static final Log log = Logs.get();
    @Inject
    private Dao dao;
    @Inject
    private PubSubService pubSubService;
    @Inject
    private BugService bugService;
    @Inject
    private SearchService searchService;

    @At("/tag")
    @Ok("json")
    @AdaptBy(type = JsonAdaptor.class)
    public Object get(@Param("name") String name) {
        try {
            Cnd cnd = Cnd.NEW();
            if (Strings.isNotBlank(name)) {
                cnd.and("name", "like", "%" + name + "%");
            }
            cnd.limit(1, 10).desc("repeats");
            return Result.success(dao.query(Tag.class, cnd));
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At("/user")
    @Ok("json")
    @AdaptBy(type = JsonAdaptor.class)
    public Object user() {
        try {
            Sql sql = Sqls.create("select id,realname from user where loginname<>'superadmin'");
            sql.setCallback(Sqls.callback.records());
            dao.execute(sql);
            return Result.success(sql.getList(Record.class));
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At("/add")
    @Ok("json")
    @AdaptBy(type = JsonAdaptor.class)
    public Object add(@Param("..") NutMap map, HttpSession session) {
        try {
            String userId = map.getString("userId", "");
            String title = map.getString("title", "");
            String note = map.getString("note", "");
            int lv = map.getInt("lv", 0);
            List<String> tags = map.getList("tag", String.class);
            if (Strings.isBlank(userId)) {
                return Result.error("请先登录");
            }
            if (Strings.isBlank(title) || title.length() > 1024 || title.length() < 5) {
                return Result.error("标题长度不合法");
            }
            if (Strings.isBlank(note)) {
                return Result.error("内容不合法");
            }
            if (tags != null && tags.size() > 10) {
                return Result.error("最多只能有10个tag");
            }
            if (0 != dao.count(Bug.class, Cnd.where("title", "=", title.trim()))) {
                return Result.error("相同标题已经发过了");
            }
            Set<String> tag = new HashSet<>();
            for (String t : tags) {
                tag.add(t);
                addTag(t);
            }
            //new HashSet<>(Lang.list(Strings.splitIgnoreBlank(tags)));
            String loginname = Strings.sNull(session.getAttribute("loginname"));
            Bug bug = new Bug();
            bug.setTitle(Strings.escapeHtml(title.trim()));
            bug.setCreateAt(Times.getTS());
            bug.setUpdateAt(Times.getTS());
            bug.setCreateBy(loginname);
            bug.setTag(tag);
            bug.setNote(Toolkit.filteContent(note));
            bug.setLevel(lv);
            bug.setUserId(userId);
            User u = dao.fetch(User.class, Cnd.where("id", "=", userId));
            if (u != null) {
                bug.setNickname(u.getNickname());
            }
            dao.insert(bug);
            pubSubService.fire("ps:topic:add", bug.getId());
            return Result.success();
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At("/reset")
    @Ok("json")
    @AdaptBy(type = JsonAdaptor.class)
    public Object reset() {
        try {
            searchService.rebuild();
            return Result.success();
        } catch (Exception e) {
            return Result.error();
        }
    }

    @Async
    public void addTag(String t) {
        Tag tag = dao.fetch(Tag.class, Cnd.where("name", "=", t));
        if (tag == null) {
            Tag tag1 = new Tag();
            tag1.setCreateAt(Times.getTS());
            tag1.setName(t);
            tag1.setRepeats(0);
            dao.insert(tag1);
        } else {
            tag.setRepeats(tag.getRepeats() + 1);
            dao.update(tag);
        }
    }

    @At("/data")
    @Ok("json:{locked:'loginpass|salt',ignoreNull:false}")
    @AdaptBy(type = JsonAdaptor.class)
    @Filters({@By(type = MyCrossOriginFilter.class)})
    public Object data(@Param(value = "size", df = "10") int size, @Param(value = "page", df = "1") int page, @Param("keyword") String keyword) {
        try {
            if (Strings.isNotBlank(keyword)) {
                List<LuceneSearchResult> results = searchService.search(keyword, size);
                List<Bug> list = new ArrayList<Bug>();
                for (LuceneSearchResult result : results) {
                    Bug bug = dao.fetch(Bug.class, result.getId());
                    if (bug == null || bug.isDisabled())
                        continue;
                    bug.setTitle(result.getTitle());
                    list.add(bug);
                }
                Pager pager = dao.createPager(page, size);
                pager.setRecordCount(list.size());
                return Result.success(new QueryResult(list, pager));
            } else {
                Cnd cnd = Cnd.NEW();
                cnd.and("disabled", "=", false);
                cnd.desc("updateAt");
                Pager pager = new Pager();
                pager.setPageNumber(page);
                pager.setPageSize(size);
                pager.setRecordCount(dao.count(Bug.class, cnd));
                List<Bug> list = dao.query(Bug.class, cnd, pager);
                return Result.success(new QueryResult(list, pager));
            }
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At("/s")
    @Ok("json")
    @AdaptBy(type = JsonAdaptor.class)
    @Filters({@By(type = MyCrossOriginFilter.class)})
    public Object s(@Param("id") String id) {
        try {
            Bug bug = dao.fetch(Bug.class, id);
            bug.setNote(Markdowns.toHtml(bug.getNote(), ""));
            return Result.success(dao.fetchLinks(bug, null));
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error();
        }
    }

    @At("/reply")
    @Ok("json")
    @AdaptBy(type = JsonAdaptor.class)
    public Object reply(@Param("bugId") String bugId, @Param("note") String note, HttpSession session) {
        try {
            String loginname = Strings.sNull(session.getAttribute("loginname"));
            String nickname = Strings.sNull(session.getAttribute("nickname"));
            String userid = Strings.sNull(session.getAttribute("userid"));
            Reply reply = new Reply();
            reply.setNote(note);
            reply.setBugId(bugId);
            reply.setCreateAt(Times.getTS());
            reply.setUserId(userid);
            reply.setCreateBy(loginname);
            reply.setNickname(nickname);
            dao.insert(reply);
            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error();
        }
    }
}
