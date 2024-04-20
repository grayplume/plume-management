<script>
export default {
  name: "Menu",
  data() {
    return {
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      name: "",
      description: "",
      dialogFormVisible: false,
      menuDialogVisible: false,
      form: {},
      multipleSelection: [],
      headerBg: "headerBg",
      options: [],
    }
  },
  created() {
    // 请求分页查询数据
    console.log("测试")
    this.load()
  },
  methods: {
    load() {
      this.dialogFormVisible = false
      this.menuDialogVisible = false
      this.request.get("/menu/findAll", {
        params: {
          name: this.name,
        }
      })
          .then(res => {
            console.log(res)
            this.tableData = res.data
          })
    },
    reset() {
      this.name = ''
      this.load()
    },
    handleAdd(pid) {
      this.dialogFormVisible = true;
      this.form = {}
      if (pid) {
        this.form.pid = pid
      }
    },
    save() {
      this.dialogFormVisible = false
      this.request.post("/menu/save", this.form).then(res => {
        if (res.code === '200') {
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

      // 请求图标数据
      this.request.get("/menu/icons")
          .then(res => {
            this.options = res.data
          })
    },
    del(id) {
      this.request.post("/menu/delete/" + id).then(res => {
        if (res.code === '200') {
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
                v-model="name"></el-input>
      <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
      <el-button class="ml-5" type="warning" @click="reset">重置</el-button>
    </div>
    <!-- 新增/删除 -->
    <div style="margin: 10px 0">
      <el-button type="primary" @click="handleAdd()">新增<i class="el-icon-circle-plus-outline"></i></el-button>
      <el-popconfirm class="ml-5" confirm-button-text="确定" cancel-button-text="我再想想" icon="el-icon-info"
                     icon-color="red"
                     title="确定批量删除吗?" @confirm="delBatch()">
        <el-button type="danger" slot="reference">删除<i class="el-icon-remove-outline"></i></el-button>
      </el-popconfirm>

    </div>
    <!-- 表格 -->
    <el-table :data="tableData" border stripe :header-cell-class-name="headerBg" row-key='id' default-expand-all
              @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="id" label="ID" width="70"></el-table-column>
      <el-table-column prop="name" label="名称"></el-table-column>
      <el-table-column prop="path" label="路径"></el-table-column>
      <el-table-column label="图标" align="center">
        <template slot-scope="scope">
<!--          {{scope.row.icon}}-->
          <i :class="scope.row.icon" style="font-size: 18px"/>
        </template>
      </el-table-column>
      <el-table-column prop="description" label="描述"></el-table-column>

      <el-table-column label="操作" width="280  " align="center">
        <template slot-scope="scope">

          <el-button type="primary" @click="handleAdd(scope.row.id)" v-if="!scope.row.pid&&!scope.row.path">
            新增子菜单<i class="el-icon-circle-plus"></i></el-button>
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

    <!--     dialog   -->
    <el-dialog title="角色信息" :visible.sync="dialogFormVisible" width="40%">
      <el-form label-width="70px" size="small">
        <el-form-item label="名称">
          <el-input v-model="form.name" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="路径">
          <el-input v-model="form.path" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="图标">
          <el-select clearable v-model="form.icon" placeholder="请选择" style="width: 100%">
            <el-option v-for="item in options" :key="item.value" :label="item.name" :value="item.value">
              <i :class="item.value"> {{ item.name }} </i>
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="form.description" auto-complete="off"></el-input>
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