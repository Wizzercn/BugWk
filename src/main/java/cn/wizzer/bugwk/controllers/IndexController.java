package cn.wizzer.bugwk.controllers;

import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;

/**
 * Created by wizzer on 2018.08
 */
@At("")
@IocBean
public class IndexController {
    private static final Log log = Logs.get();

    @At(value = {"/", "/lol", "/lol/*", "/user/*", "/tag/*"}, top = true)
    @Ok("->:/index.html")
    public void index() {
    }

}
