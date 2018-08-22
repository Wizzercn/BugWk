import Vue from 'vue'
import App from './App.vue'
import './plugins/element.js'
import VueRouter from 'vue-router'
import VueResource from 'vue-resource'
import Routers from './router'

Vue.use(VueRouter)
Vue.use(VueResource)
Vue.config.productionTip = false

const router = new VueRouter({
    routes: Routers,
    mode: 'history'
})

router.beforeEach((to, from, next) => {
    next()
})

new Vue({
    router: router,
    render: h => h(App)
}).$mount('#app')
