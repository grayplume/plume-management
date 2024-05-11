<script>
export default {
  name: "Front",
  data() {
    return {
      activeIndex: '1',
      activeIndex2: '1',
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {}
    }
  },
  created() {
  },
  methods: {
    handleSelect(key, keyPath) {
      console.log(key, keyPath);
      switch (key) {
        case '1':
          this.$router.push('/front/home');
          break;
        case '2-1': // 系统后台
          this.$router.push('/');
          break;
        case '4': // 订单管理
          this.$router.push('/order-management');
          break;
          // 添加更多 case 以处理其他菜单项的跳转
        default:
          console.log('未处理的菜单项:', key);
          break;
      }
    },
    logout() {
      this.$store.commit("logout")
      this.$message.success("退出成功")
      location.reload()
    }
  }
}
</script>

<template>
  <div>
    <!--  头部  -->
    <div style="display: flex;height: 60px;line-height: 60px;border-bottom: 1px solid #ccc">
      <div style="width: 200px;text-align: center">欢迎来到xxx系统</div>
      <!--   菜单   -->
      <div style="flex: 1">
        <el-menu :default-active="activeIndex" class="el-menu-demo" mode="horizontal" @select="handleSelect">
          <el-menu-item index="1" >首页</el-menu-item>
          <el-submenu index="2">
            <template slot="title">系统</template>
                        <el-menu-item index="2-1">系统后台</el-menu-item>
            <!--            <el-menu-item index="2-2">选项2</el-menu-item>-->
            <!--            <el-menu-item index="2-3">选项3</el-menu-item>-->
            <!--            <el-submenu index="2-4">-->
            <!--              <template slot="title">选项4</template>-->
            <!--              <el-menu-item index="2-4-1">选项1</el-menu-item>-->
            <!--              <el-menu-item index="2-4-2">选项2</el-menu-item>-->
            <!--              <el-menu-item index="2-4-3">选项3</el-menu-item>-->
            <!--            </el-submenu>-->
          </el-submenu>
          <el-menu-item index="3" disabled>消息中心</el-menu-item>
          <el-menu-item index="4">可视化</el-menu-item>
        </el-menu>
        <div class="line"></div>
      </div>

      <!--  昵称    -->
      <div style="width: 200px">
        <div v-if="!user.userName" style="text-align: right;padding-right: 30px">
          <el-button @click="$router.push('/login')">登录</el-button>
          <el-button @click="$router.push('/register')">注册</el-button>
        </div>
        <div v-else>
          <div style="text-align: right; padding-right: 20px">
            <el-dropdown style="width: 90px;cursor: pointer">
              <div style="display: inline-block">
                <span> {{ user.nickname }}</span><i class="el-icon-arrow-down" style="margin-left: 5px"></i>
              </div>

              <el-dropdown-menu slot="dropdown" style="width: 100px;text-align: center">
                <el-dropdown-item style="font-size: 14px;padding: 5px 0">
                  <router-link to="/person" style="text-decoration: none;">个人信息</router-link>
                </el-dropdown-item>
                <el-dropdown-item style="font-size: 14px;padding: 5px 0">
                  <span style="text-decoration: none" @click="logout">安全退出</span>
                </el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </div>
        </div>
      </div>
    </div>
    <div style="width: 1000px;margin: 0 auto">
      <router-view/>
    </div>
  </div>
</template>

<style scoped>

</style>