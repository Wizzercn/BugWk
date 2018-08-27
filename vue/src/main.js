import Vue from 'vue'
import App from './App.vue'
import './plugins/element.js'
import VueRouter from 'vue-router'
import axios from 'axios';
import VueAxios from 'vue-axios'
import VueCookie from 'vue-cookie'
import Routers from './router'

// Tell Vue to use the plugin
Vue.use(VueCookie)
Vue.use(VueRouter)
Vue.use(VueAxios, axios)
Vue.use(VueCookie)
Vue.config.productionTip = false


const router = new VueRouter({
    routes: Routers,
    mode: 'history'
})

router.beforeEach((to, from, next) => {
    console.log("from::"+from.fullPath+" to::"+to.fullPath)
    next();
})

//跨站访问,让前后端session ID保持一致
axios.defaults.withCredentials = true
axios.interceptors.response.use(function (response) {
    if(555 == response.data.code){//后台session失效
        VueCookie.set("role","");//设置cookie
        router.replace({path: '/lol'})//跳转到首页
        window.location.reload()//刷新页面重新初始化菜单
    }
    return response;
}, function (error) {
    return Promise.reject(error);
});

new Vue({
    router: router,
    render: h => h(App)
}).$mount('#app')
