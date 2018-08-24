package cn.wizzer.bugwk.commons.filter;

import cn.wizzer.bugwk.commons.base.Result;
import cn.wizzer.bugwk.commons.enums.Role;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.ActionContext;
import org.nutz.mvc.ActionFilter;
import org.nutz.mvc.View;
import org.nutz.mvc.view.UTF8JsonView;

/**
 * 权限拦截，如果是大型项目可以使用动作链结合shiro来做，这里是图省事，不要照搬！
 * Created by wizzer on 2018.08
 */
public class MyRoleFilter implements ActionFilter {
    private static final Log log = Logs.get();

    public View match(ActionContext ac) {
        String role = Strings.sNull(ac.getRequest().getSession().getAttribute("role"));
        log.debug("MyRoleFilter session id::"+ac.getRequest().getSession().getId());
        if (Role.ADMIN.value().equalsIgnoreCase(role)) {
            return null;
        } else {
            return new UTF8JsonView().setData(Result.error("没有权限或登录超时,请重新登录"));
        }
    }
}
