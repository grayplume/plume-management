<script>
export default {
  name: "File",
  data() {
    return {
      tableData: [],
      name: '',
      multipleSelection: [],
    }
  },
  created() {
  },
  methods: {
    load() {
      this.dialogFormVisible = false
      this.request.get("/file/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name,
          email: this.email,
          address: this.address
        }
      })
          .then(res => {
            console.log(res)
            this.tableData = res.records
            this.total = res.total
          })
    },
    reset() {
      this.name = ''
    },
    del(id) {
      this.request.post("/file/delete/" + id).then(res => {
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
    handleFileUploadSuccess(res) {

    }
  }
}
</script>

<template>
  <!-- 新增/删除 -->
  <div style="margin: 10px 0">

    <el-upload action="http://localhost:8080/file/upload" :show-file-list="false" accept="xlsx"
               :on-success="handleFileUploadSuccess" style="display: inline-block" class="mr-5">
      <el-button type="primary" @click="handleAdd">上传文件<i class="el-icon-circle-plus-outline"></i></el-button>
    </el-upload>

    <el-popconfirm class="ml-5" confirm-button-text="确定" cancel-button-text="我再想想" icon="el-icon-info"
                   icon-color="red"
                   title="确定批量删除吗?" @confirm="delBatch()">
      <el-button type="danger" slot="reference">删除<i class="el-icon-remove-outline"></i></el-button>
    </el-popconfirm>


  </div>
  <!-- 表格 -->
  <el-table :data="tableData" border stripe :header-cell-class-name="headerBg"
            @selection-change="handleSelectionChange">
    <el-table-column type="selection" width="55"></el-table-column>
    <el-table-column prop="id" label="ID" width="70"></el-table-column>
    <el-table-column prop="name" label="文件名称" width="140"></el-table-column>
    <el-table-column prop="type" label="文件类型" width="120"></el-table-column>
    <el-table-column prop="size" label="文件大小(KB))"></el-table-column>
    <el-table-column label="下载">
      <template slot-scope="scope">
        <el-button type="primary" @click="window.open(scope.row.url)">下载<i class="el-icon-download"></i></el-button>
      </template>>
    </el-table-column>
    <el-table-column label="启用">
      <template slot-scope="scope">
        <el-switch v-model="scope.row.enable" active-color="#13ce66" inactive-color="#ff4949"></el-switch>>
      </template>
    </el-table-column>

    <el-table-column label="操作">
      <template slot-scope="scope">

        <!--     删除提示       -->
        <el-popconfirm class="ml-5" confirm-button-text="确定" cancel-button-text="我再想想" icon="el-icon-info"
                       icon-color="red"
                       title="确定删除吗?" @confirm="del(scope.row.id)">
          <el-button type="danger" slot="reference">删除<i class="el-icon-remove-outline"></i></el-button>
        </el-popconfirm>
      </template>
    </el-table-column>
  </el-table>
</template>

<style scoped>

</style>