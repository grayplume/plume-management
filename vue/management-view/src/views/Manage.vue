<template>
  <el-container style="min-height: 100vh">

    <el-aside :width="sideWidth + 'px'"
              style="background-color: rgb(238, 241, 246);box-shadow: 2px 0 6px rgb(0,21,41,0.35);">
      <Aside :is-collapse="isCollapse" :logo-text-show="logoTextShow"/>
    </el-aside>

    <el-container>

      <el-header style="border-bottom: 1px solid #ccc;">
        <Header :collapse-btn-class="collapseBtnClass" :collapse="collapse"/>
      </el-header>

      <el-main>
        <!--   表示当前页面的子路由会在 router-view 里面展示     -->
        <router-view/>
      </el-main>

    </el-container>
  </el-container>
</template>

<script>


import Aside from '@/components/Aside.vue'
import Header from "@/components/Header.vue";

export default {
  name: 'HomeView',
  components: {
    Header,
    Aside
  },
  data() {
    return {
      isCollapse: false,
      collapseBtnClass: 'el-icon-s-fold',
      sideWidth: 200,
      logoTextShow: true,
    }
  },

  methods: { // 点击收缩
    collapse() {
      this.isCollapse = !this.isCollapse;
      if (this.isCollapse) { // 收缩
        this.sideWidth = 64
        this.collapseBtnClass = 'el-icon-s-unfold'
        this.logoTextShow = false
      } else {  //展开
        this.sideWidth = 200
        this.collapseBtnClass = 'el-icon-s-fold'
        this.logoTextShow = true
      }
    },

  }
}
</script>


