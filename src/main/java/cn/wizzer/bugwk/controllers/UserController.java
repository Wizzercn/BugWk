package cn.wizzer.bugwk.controllers;

import cn.wizzer.bugwk.commons.base.Result;
import cn.wizzer.bugwk.commons.filter.MyAdminRoleFilter;
import cn.wizzer.bugwk.commons.filter.MyCrossOriginFilter;
import cn.wizzer.bugwk.commons.filter.MyUserRoleFilter;
import cn.wizzer.bugwk.modles.User;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.QueryResult;
import org.nutz.dao.pager.Pager;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Lang;
import org.nutz.lang.Times;
import org.nutz.lang.random.R;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.adaptor.JsonAdaptor;
import org.nutz.mvc.annotation.*;

import java.util.List;

/**
 * Created by wizzer on 2018.08
 */
@IocBean
@At("/platform/user")
@Filters({@By(type = MyCrossOriginFilter.class), @By(type = MyUserRoleFilter.class)})
public class UserController {
    private static final Log log = Logs.get();
    @Inject
    private Dao dao;

    @At("/add")
    @Ok("json")
    @AdaptBy(type = JsonAdaptor.class)
    @Filters({@By(type = MyCrossOriginFilter.class), @By(type = MyAdminRoleFilter.class)})
    public Object add(@Param("::") User user) {
        try {
            if (dao.count(User.class, Cnd.where("loginname", "=", user.getLoginname())) > 0) {
                return Result.error("用户已存在");
            }
            String salt = R.UU32();
            user.setSalt(salt);
            user.setLoginpass(Lang.md5(user.getLoginname() + user.getLoginpass() + salt));
            user.setCreateAt(Times.getTS());
            dao.insert(user);
            return Result.success();
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At("/get")
    @Ok("json")
    @AdaptBy(type = JsonAdaptor.class)
    public Object get(@Param("id") String id) {
        try {
            return Result.success(dao.fetch(User.class, Cnd.where("id", "=", id)));
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At("/edit")
    @Ok("json")
    @AdaptBy(type = JsonAdaptor.class)
    public Object edit(@Param("::") User user) {
        try {
            dao.updateIgnoreNull(user);
            return Result.success();
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At("/update")
    @Ok("json")
    @AdaptBy(type = JsonAdaptor.class)
    public Object update(@Param("::") User user) {
        try {
            String salt = R.UU32();
            dao.update(User.class, Chain.make("nickname", user.getNickname()).add("realname", user.getRealname())
                            .add("salt", salt).add("loginpass", Lang.md5(user.getLoginname() + user.getLoginpass() + salt))
                    , Cnd.where("loginname", "=", user.getLoginname()));
            return Result.success(user);
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At("/del")
    @Ok("json")
    @AdaptBy(type = JsonAdaptor.class)
    @Filters({@By(type = MyCrossOriginFilter.class), @By(type = MyAdminRoleFilter.class)})
    public Object del(@Param("id") String id) {
        try {
            dao.delete(User.class, id);
            return Result.success();
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At("/pass")
    @Ok("json")
    @AdaptBy(type = JsonAdaptor.class)
    @Filters({@By(type = MyCrossOriginFilter.class), @By(type = MyAdminRoleFilter.class)})
    public Object pass(@Param("loginname") String loginname) {
        try {
            String salt = R.UU32();
            String loginpass = R.captchaNumber(6);
            dao.update(User.class, Chain.make("salt", salt).add("loginpass", Lang.md5(loginname + loginpass + salt))
                    , Cnd.where("loginname", "=", loginname));
            return Result.success().addData(loginpass);
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At("/status")
    @Ok("json")
    @AdaptBy(type = JsonAdaptor.class)
    @Filters({@By(type = MyCrossOriginFilter.class), @By(type = MyAdminRoleFilter.class)})
    public Object status(@Param("loginname") String loginname, @Param("status") boolean status) {
        try {
            dao.update(User.class, Chain.make("disabled", status)
                    , Cnd.where("loginname", "=", loginname));
            return Result.success();
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At("/data")
    @Ok("json:{locked:'loginpass|salt',ignoreNull:false}")
    @AdaptBy(type = JsonAdaptor.class)
    public Object data(@Param(value = "size", df = "10") int size, @Param(value = "page", df = "1") int page) {
        try {
            Cnd cnd = Cnd.NEW();
            cnd.asc("loginname");
            Pager pager = new Pager();
            pager.setPageNumber(page);
            pager.setPageSize(size);
            pager.setRecordCount(dao.count(User.class, cnd));
            List<User> list = dao.query(User.class, cnd, pager);
            return Result.success(new QueryResult(list, pager));
        } catch (Exception e) {
            return Result.error();
        }
    }
}
