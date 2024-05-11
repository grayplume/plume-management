import Vue from "vue";
import Vuex from "vuex"
import router, {resetRouter} from "@/router";


Vue.use(Vuex)

const store = new Vuex.Store({
    state: {
        currentPathName: ''
    },
    mutations: {
        setPath(state) {
            state.currentPathName = localStorage.getItem("currentPathName")
        },
        logout() {
            let user = localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {}

            if (user.role === 'ROLE_STUDENTS'){
                // 清空缓存
                localStorage.removeItem("user")
                localStorage.removeItem("menus")
                router.push("/front/home")
            }else {
                router.push("/login")
            }


            // 清空缓存
            localStorage.removeItem("user")
            localStorage.removeItem("menus")
            // router.push("/login")

            //重置路由
            resetRouter()
        }
    }
})

export default store
