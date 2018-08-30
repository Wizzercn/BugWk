package cn.wizzer.bugwk.commons.processor;

import cn.wizzer.bugwk.commons.base.Result;
import org.nutz.integration.jedis.RedisService;
import org.nutz.ioc.impl.PropertiesProxy;
import org.nutz.json.JsonFormat;
import org.nutz.lang.Lang;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.ActionContext;
import org.nutz.mvc.ActionInfo;
import org.nutz.mvc.NutConfig;
import org.nutz.mvc.impl.processor.AbstractProcessor;
import org.nutz.mvc.view.ServerRedirectView;
import org.nutz.mvc.view.UTF8JsonView;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by wizzer on 2018.08
 */
public class FirewallProcessor extends AbstractProcessor {

    private static final Log log = Logs.get();
    private RedisService redisService;
    private PropertiesProxy conf;
    private String qpsUri = "/platform/login/qps";

    public void init(NutConfig config, ActionInfo ai) throws Throwable {
        redisService = config.getIoc().get(RedisService.class);
        conf = config.getIoc().get(PropertiesProxy.class, "conf");
    }

    public void process(ActionContext ac) throws Throwable {
        if (conf.getBoolean("firewall.ip.enabled", false) && !isQpsUri(ac.getRequest())) {
            String ip = Lang.getIP(ac.getRequest());
            long current = redisService.incr(ip);
            if (current == 1L) {
                redisService.expire(ip, 1);
            }
            if (current > conf.getInt("firewall.ip.qps", 10)) {
                if (isAjax(ac.getRequest())) {
                    new UTF8JsonView(JsonFormat.compact()).render(ac.getRequest(), ac.getResponse(), Result.error("too many requests per second"));
                } else {
                    new ServerRedirectView(qpsUri).render(ac.getRequest(), ac.getResponse(), null);
                }
                return;
            }
        }
        doNext(ac);
    }

    private boolean isAjax(ServletRequest req) {
        String value = ((HttpServletRequest) req).getHeader("X-Requested-With");
        return value != null && "XMLHttpRequest".equalsIgnoreCase(value.trim());
    }

    private boolean isQpsUri(ServletRequest req) {
        return qpsUri.equals(((HttpServletRequest) req).getRequestURI());
    }
}
