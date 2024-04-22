<script>
export default {
  name: "Person",
  data(){
    return{
      form:{},
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")):{}
    }
  },
  created() {
   this.request.get("/user/username/"+this.user.userName).then(res=>{
     if (res.code === '200'){
       this.form = res.data
     }
   })
  },
  methods:{
    save() {
      this.dialogFormVisible = false
      this.request.post("/user/save", this.form).then(res => {
        if (res) {
          this.$message.success("保存成功")
          this.$router.push("/home")
        } else {
          this.$message.error("保存失败")
        }
      })
    },
  }
}
</script>

<template>
  <el-card style="width: 500px">
    <el-form label-width="70px" size="small">
      <el-form-item label="用户名">
        <el-input v-model="form.username" auto-complete="off"></el-input>
      </el-form-item>
      <el-form-item label="昵称">
        <el-input v-model="form.nickname" auto-complete="off"></el-input>
      </el-form-item>
      <el-form-item label="电话">
        <el-input v-model="form.phone" auto-complete="off"></el-input>
      </el-form-item>
      <el-form-item label="邮箱">
        <el-input v-model="form.email" auto-complete="off"></el-input>
      </el-form-item>
      <el-form-item label="地址">
        <el-input v-model="form.address" auto-complete="off"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="save()">确 定</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>

<style scoped>

</style>