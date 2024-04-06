import Vue from 'vue'
import VueRouter from 'vue-router'
import User from "@/views/User.vue";
import store from "@/store";


Vue.use(VueRouter)

const routes = [
    {
        path: '/',
        // name: 'Manage',
        component: () => import('../views/Manage.vue'),
        redirect: "/home",
        children: [
            {path: 'home', name: '首页', component: () => import('../views/Home.vue')},
            {path: 'user', name: '用户管理', component: () => import('../views/User.vue')},
        ]
    },
    {
        path: '/login',
        name: 'Login',
        component: () => import('../views/Login.vue'),
    }
]


const router = new VueRouter({
    routes,
    mode: "history",
    base: process.env.BASE_URL,
})

// 路由守卫
router.beforeEach((to, from, next) => {
    localStorage.setItem("currentPathName", to.name)  // 设置当前路由名称
    store.commit("setPath")  // 触发store数据更新
    next() //放行路由
})

export default router
