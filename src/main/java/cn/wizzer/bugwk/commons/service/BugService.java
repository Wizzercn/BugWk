package cn.wizzer.bugwk.commons.service;

import cn.wizzer.bugwk.modles.Bug;
import org.nutz.dao.Dao;
import org.nutz.integration.jedis.pubsub.PubSub;
import org.nutz.integration.jedis.pubsub.PubSubService;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;

/**
 * Created by wizzer on 2018.08
 */
@IocBean(create = "init")
public class BugService  implements PubSub {
    private static final Log log = Logs.get();
    @Inject
    private SearchService searchService;
    @Inject
    private Dao dao;
    @Inject
    private PubSubService pubSubService;

    public void init(){
        pubSubService.reg("ps:topic:*", this);
    }
    @Override
    public void onMessage(String channel, String message) {
        log.debugf("onMessage channel=%s, msg=%s", channel, message);
        switch (channel) {
            case "ps:topic:add":
                searchService.addIndex(dao.fetch(Bug.class, message), true);
                break;
            default:
                break;
        }
    }
}
