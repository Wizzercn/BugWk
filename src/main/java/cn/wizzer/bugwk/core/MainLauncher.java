package cn.wizzer.bugwk.core;

import cn.wizzer.bugwk.commons.enums.Role;
import cn.wizzer.bugwk.modles.User;
import org.nutz.boot.NbApp;
import org.nutz.dao.Dao;
import org.nutz.dao.util.Daos;
import org.nutz.ioc.Ioc;
import org.nutz.ioc.impl.PropertiesProxy;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Lang;
import org.nutz.lang.Times;
import org.nutz.lang.random.R;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.ChainBy;
import org.nutz.mvc.annotation.Encoding;
import org.nutz.mvc.annotation.Modules;

import javax.servlet.ServletContext;

/**
 * Created by wizzer on 2018.08
 */
@IocBean(create = "init", depose = "depose")
@Modules(packages = "cn.wizzer")
@Encoding(input = "UTF-8", output = "UTF-8")
@ChainBy(args = "chain/bugwk-mvc-chain.json")
public class MainLauncher {
    private static final Log log = Logs.get();
    @Inject("refer:$ioc")
    private Ioc ioc;
    @Inject
    private PropertiesProxy conf;

    public static void main(String[] args) throws Exception {
        NbApp nb = new NbApp().setArgs(args).setPrintProcDoc(true);
        nb.getAppContext().setMainPackage("cn.wizzer");
        nb.run();
    }

    public static NbApp warMain(ServletContext sc) {
        NbApp nb = new NbApp().setPrintProcDoc(true);
        nb.getAppContext().setMainPackage("cn.wizzer");
        return nb;
    }

    public void init() {
        try {
            Dao dao = ioc.get(Dao.class);
            Daos.createTablesInPackage(dao, "cn.wizzer.bugwk", false);
            if (0 == dao.count(User.class)) {
                User user = new User();
                user.setLoginname("superadmin");
                user.setNickname("大鲨鱼");
                user.setRealname("超级管理员");
                String salt = R.UU32();
                user.setSalt(salt);
                user.setLoginpass(Lang.md5(user.getLoginname() + user.getLoginpass() + salt));
                user.setCreateAt(Times.getTS());
                user.setRole(Role.ADMIN);
                user.setDisabled(false);
                dao.insert(user);
            }
        } catch (Exception e) {

        }
    }

    public void depose() {

    }
}
