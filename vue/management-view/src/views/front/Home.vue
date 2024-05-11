<script>
export default {
  name: "FrontHome",
  data() {
    return {
      imgs: [
        'https://plume-picgo.oss-cn-beijing.aliyuncs.com/tuku-master/blog/2024511145%7BS%7D2otsin7um8dabaotop_a_girl_dressed_in_a_cloak_and_hat_and_a_dragon_in_the__3f47b31c-83b5-4f4e-a3dd-53d924a20b00_x2.JPG',
        'https://plume-picgo.oss-cn-beijing.aliyuncs.com/tuku-master/blog/2024511145%7BS%7Dztyew1uedldabaotop_a_girl_dressed_in_a_cloak_and_hat_and_a_dragon_in_the__81d6c9c9-ffdd-4320-b230-2da7669cd07d_x2.JPG',
        'https://plume-picgo.oss-cn-beijing.aliyuncs.com/tuku-master/blog/2024511145%7BS%7Dt40d8p4gm3dabaotop_a_girl_dressed_in_a_cloak_and_hat_and_a_dragon_in_the__7173432b-c2db-4cfd-996e-6d207b8008f5_x2.JPG',
        'https://plume-picgo.oss-cn-beijing.aliyuncs.com/tuku-master/blog/2024511145%7BS%7Dmtynm37jpgdabaotop_an_anime_person_in_a_black_night_scene_with_birds_in_t_77862974-fd77-418d-8f79-1065b4757548_x2.JPG'
      ]
    }
  }
}
</script>

<template>
  <div>
    <div style="margin: 10px 0">
      <div class="block">
<!--        <span class="demonstration">默认 Hover 指示器触发</span>-->
        <el-carousel height="400px" >
          <el-carousel-item v-for="item in imgs" :key="item">
            <img :src="item" alt="" style="width: 100%;object-fit: cover;object-position: center center;">
          </el-carousel-item>
        </el-carousel>
      </div>
    </div>
  </div>
</template>

<style scoped>

</style>