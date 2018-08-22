package cn.wizzer.bugwk.controllers;

import cn.wizzer.bugwk.commons.base.Result;
import cn.wizzer.bugwk.commons.filter.MyCrossOriginFilter;
import cn.wizzer.bugwk.modles.User;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Lang;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.adaptor.JsonAdaptor;
import org.nutz.mvc.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * 用户登录并获取角色名
 * Created by wizzer on 2018.08
 */
@IocBean
@At("/platform/login")
@Filters({@By(type = MyCrossOriginFilter.class)})
public class LoginController {
    private static final Log log = Logs.get();
    @Inject
    private Dao dao;

    @At("/do_login")
    @Ok("json")
    @AdaptBy(type = JsonAdaptor.class)
    public Object do_login(@Param("loginname") String loginname, @Param("loginpass") String loginpass, HttpSession session) {
        try {
            User user = dao.fetch(User.class, Cnd.where("loginname", "=", loginname));
            if (user == null) {
                return Result.error("用户不存在");
            }
            if (user.isDisabled()) {
                return Result.error("用户已被禁用");
            }
            if (!user.getLoginpass().equals(Lang.md5(loginname + user.getSalt()))) {
                return Result.error("用户密码不正确");
            }
            session.setAttribute("loginname", user.getLoginname());
            session.setAttribute("nickname", user.getNickname());
            session.setAttribute("realname", user.getRealname());
            session.setAttribute("role", user.getRole());
            return Result.success("用户登录成功",
                    NutMap.NEW().addv("loginname", user.getLoginname())
                            .addv("nickname", user.getNickname())
                            .addv("realname", user.getRealname())
                            .addv("role", user.getRole())
            );
        } catch (Exception e) {
            return Result.error();
        }
    }
}
