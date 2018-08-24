package cn.wizzer.bugwk.commons.filter;

import org.nutz.ioc.impl.PropertiesProxy;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.ActionContext;
import org.nutz.mvc.ActionFilter;
import org.nutz.mvc.View;
import org.nutz.mvc.view.VoidView;

import javax.servlet.http.HttpServletResponse;

/**
 * 跨站权限配置，开发模式的时候启用，MVVM打包成单页运行时禁用
 * Created by wizzer on 2018.08
 */
public class MyCrossOriginFilter implements ActionFilter {
    private static final Log log = Logs.get();
    protected String origin;
    protected String methods;
    protected String headers;
    protected String credentials;
    protected PropertiesProxy conf;

    public MyCrossOriginFilter() {
        this("GET, POST, PUT, DELETE, OPTIONS, PATCH", "Origin, Content-Type, Accept, X-Requested-With", "true");
    }

    public MyCrossOriginFilter(String methods, String headers, String credentials) {
        this.methods = methods;
        this.headers = headers;
        this.credentials = credentials;
    }

    public View match(ActionContext ac) {
        if (conf == null) {
            conf = ac.getIoc().get(PropertiesProxy.class, "conf");
        }
        //如果没有启用跨站访问，则直接跳过
        if (!conf.getBoolean("cross-origin.enabled", false)) {
            return null;
        }
        HttpServletResponse resp = ac.getResponse();
        resp.setHeader("Access-Control-Allow-Origin", ac.getRequest().getHeader("Origin"));


        if (!Strings.isBlank(this.methods)) {
            resp.setHeader("Access-Control-Allow-Methods", this.methods);
        }

        if (!Strings.isBlank(this.headers)) {
            resp.setHeader("Access-Control-Allow-Headers", this.headers);
        }

        if (!Strings.isBlank(this.credentials)) {
            resp.setHeader("Access-Control-Allow-Credentials", this.credentials);
        }

        if ("OPTIONS".equals(ac.getRequest().getMethod())) {
            if (log.isDebugEnabled()) {
                log.debugf("Feedback -- [%s] [%s] [%s] [%s]", new Object[]{this.origin, this.methods, this.headers, this.credentials});
            }

            return new VoidView();
        } else {
            return null;
        }
    }
}
