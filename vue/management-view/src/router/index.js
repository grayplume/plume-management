import Vue from 'vue'
import VueRouter from 'vue-router'
import User from "@/views/User.vue";
import store from "@/store";


Vue.use(VueRouter)

const routes = [
    {
        path: '/login',
        name: 'Login',
        component: () => import('../views/Login.vue'),
    },
    {
        path: '/register',
        name: 'Register',
        component: () => import('../views/Register.vue'),
    },
    {
        path: '*',
        name: 'NotFound',
        component: () => import('../views/404.vue')
    }
]


const router = new VueRouter({
    routes,
    mode: "history",
    base: process.env.BASE_URL,
})
// 刷新页面会导致路由重置
export const setRoutes = () => {
    const storeMenus = localStorage.getItem("menus");
    if (storeMenus) {
        // 拼装动态路由
        const mangeRoute = {
            path: '/',
            name: "Manage",
            component: () => import('../views/Manage.vue'),
            redirect: "/home",
            children: []
        }
        const menus = JSON.parse(storeMenus)
        menus.forEach(item => {
            // {path: 'home', name: '首页', component: () => import('../views/Home.vue')},
            if (item.path) {  // path不为空
                let itemMenu = {
                    path: item.path.replace("/", ""),
                    name: item.name,
                    component: () => import('../views/' + item.pagePath + '.vue')
                }
                mangeRoute.children.push(itemMenu)
            } else if (item.children.length) {
                item.children.forEach(item => {
                    if (item.path) {  // path不为空
                        let itemMenu = {
                            path: item.path.replace("/", ""),
                            name: item.name,
                            component: () => import('../views/' + item.pagePath + '.vue')
                        }
                        mangeRoute.children.push(itemMenu)
                    }
                })
            }
        })
        // 防止重复设置路由
        const currentRouteNames = router.getRoutes().map(v => v.name)
        if (!currentRouteNames.includes('Manage')) {
            router.addRoute(mangeRoute)
        }
    }
}
setRoutes()

// 路由守卫
router.beforeEach((to, from, next) => {
    localStorage.setItem("currentPathName", to.name)  // 设置当前路由名称
    store.commit("setPath")  // 触发store数据更新
    next() //放行路由
})

export default router
