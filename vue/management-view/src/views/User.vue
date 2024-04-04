<script>
export default {
  name: "User",
  data(){
    return{
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      userName: "",
      email: "",
      address: "",
      dialogFormVisible: false,
      form: {},
      multipleSelection: [],
      headerBg: "headerBg"
    }
  },
  created() {
    // 请求分页查询数据
    console.log("测试")
    this.load()
  },
  methods:{
    load() {
      this.dialogFormVisible = false
      this.request.get("/user/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          userName: this.userName,
          email: this.email,
          address: this.address
        }
      })
          .then(res => {
            console.log(res)
            this.tableData = res.records
            this.total = res.total
          })
      // fetch("http://localhost:8080/user/page?pageNum="+this.pageNum+"&pageSize="+this.pageSize+"&userName="+this.userName+"&email="+this.email+"&address="+this.address).then(res => res.json()).then(res=>{
      //   console.log(res)
      //   this.tableData = res.data
      //   this.total = res.total
      // })
    },
    reset() {
      this.userName = ''
      this.email = ''
      this.address = ''
      this.load()
    },
    handleAdd() {
      this.dialogFormVisible = true;
      this.form = {}
    },
    save() {
      this.dialogFormVisible = false
      this.request.post("/user/save", this.form).then(res => {
        if (res) {
          this.$message.success("保存成功")
          this.load()
        } else {
          this.$message.error("保存失败")
        }
      })
    },
    handleEdit(row) {
      this.form = row
      this.dialogFormVisible = true
    },
    del(id) {
      this.request.post("/user/delete/" + id).then(res => {
        if (res) {
          this.$message.success("删除成功")
          this.load();
        } else {
          this.$message.error("删除失败")
        }
      })
    },
    delBatch() {
      let ids = this.multipleSelection.map(value => value.id)
      this.del(ids)
    },
    handleSelectionChange(val) {
      console.log(val)
      this.multipleSelection = val
    },
    handleSizeChange(pageSize) {
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum) {
      this.pageNum = pageNum
      this.load()
    },
  }
}
</script>

<template>
  <div>
    <!-- 搜索框-->
    <div style="margin: 10px 0">
      <el-input style="width: 200px" placeholder="请输入名称" suffix-icon="el-icon-search"
                v-model="userName"></el-input>
      <el-input style="width: 200px" placeholder="请输入邮箱" suffix-icon="el-icon-message" v-model="email"
                class="ml-5"></el-input>
      <el-input style="width: 200px" placeholder="请输入地址" suffix-icon="el-icon-location" v-model="address"
                class="ml-5"></el-input>
      <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
      <el-button class="ml-5" type="warning" @click="reset">重置</el-button>
    </div>
    <!-- 新增/删除 -->
    <div style="margin: 10px 0">
      <el-button type="primary" @click="handleAdd">新增<i class="el-icon-circle-plus-outline"></i></el-button>
      <el-popconfirm class="ml-5" confirm-button-text="确定" cancel-button-text="我再想想" icon="el-icon-info"
                     icon-color="red"
                     title="确定批量删除吗?" @confirm="delBatch()">
        <el-button type="danger" slot="reference" >删除<i class="el-icon-remove-outline"></i></el-button>
      </el-popconfirm>

      <el-button type="primary" class="ml-5">导入<i class="el-icon-bottom"></i></el-button>
      <el-button type="primary">导出<i class="el-icon-top"></i></el-button>
    </div>
    <!-- 表格 -->
    <el-table :data="tableData" border stripe :header-cell-class-name="headerBg"
              @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="id" label="ID" width="70"></el-table-column>
      <el-table-column prop="username" label="用户名" width="140"></el-table-column>
      <el-table-column prop="nickname" label="昵称" width="120"></el-table-column>
      <el-table-column prop="email" label="邮箱"></el-table-column>
      <el-table-column prop="phone" label="电话"></el-table-column>
      <el-table-column prop="address" label="地址"></el-table-column>

      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button type="success" @click="handleEdit(scope.row)">编辑<i class="el-icon-edit"></i></el-button>

          <!--     删除提示       -->
          <el-popconfirm class="ml-5" confirm-button-text="确定" cancel-button-text="我再想想" icon="el-icon-info"
                         icon-color="red"
                         title="确定删除吗?" @confirm="del(scope.row.id)">
            <el-button type="danger" slot="reference">删除<i class="el-icon-remove-outline"></i></el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页 -->
    <div style="padding: 10px 0">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pageNum"
          :page-size="pageSize"
          :page-sizes="[5, 10, 15, 20]"
          :small= "true"
          :disabled="false"
          :background="true"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
      />
    </div>

    <!--     dialog   -->
    <el-dialog title="用户信息" :visible.sync="dialogFormVisible" width="40%">
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

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="load">取 消</el-button>
        <el-button type="primary" @click="save()">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<style>
.headerBg {
  background: #eeeeee !important;
}

</style>