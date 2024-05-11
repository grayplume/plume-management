<script>
export default {
  name: "Role",
  data() {
    return {
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      name: "",
      description: "",
      flag: "",
      dialogFormVisible: false,
      menuDialogVisible: false,
      form: {},
      multipleSelection: [],
      headerBg: "headerBg",
      menuData: [],
      roleId: 0,
      roleFlag: "",
      props: {
        label: "name",
      },
      expandedKeys: [],
      checkedKeys: [],
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
      this.request.get("/role/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name,
          description: this.description,
        }
      })
          .then(res => {
            console.log(res)
            this.tableData = res.data.records
            this.total = res.data.total
            this.description = res.data.description
          })
    },
    reset() {
      this.name = ''
      this.load()
    },
    handleAdd() {
      this.dialogFormVisible = true;
      this.form = {}
    },
    handleMenu(role) {
      this.menuDialogVisible = true;
      this.roleId = role.id
      this.roleFlag = role.flag
      // 请求菜单数据
      this.request.get("/menu/findAll", {
        params: {
          name: '',
        }
      }).then(res => {
        if (res.code === '200') {
          this.menuData = res.data
          // 后台获取菜单数据
          this.expandedKeys = this.menuData.map(item => item.id)
          // this.load()
        } else {
          this.$message.error("获取失败")
        }
      })
      // 请求菜单选中数据
      this.request.get("/role/roleMenu/" + this.roleId).then(res => {
        if (res.code === '200') {
          this.checkedKeys = res.data
          // 后台获取菜单数据
          this.expandedKeys = this.menuData.map(item => item.id)
        } else {
          this.$message.error("获取失败")
        }

      })
    },
    handleCheckChange(data, checked, indeterminate) {
      console.log(data, checked, indeterminate);
    },
    save() {
      this.dialogFormVisible = false
      this.request.post("/role/save", this.form).then(res => {
        if (res.code === '200') {
          this.$message.success("保存成功")
          this.load()
        } else {
          this.$message.error("保存失败")
        }
      })
    },
    saveRoleMenu() {
      this.request.post("/role/roleMenu/" + this.roleId, this.$refs.tree.getCheckedKeys()).then(res => {
        if (res.code === '200') {
          // 操作管理员角色时需要重新登录
          if (this.roleFlag === 'ROLE_ADMIN') {
            this.$store.commit("logout")
          } else {
            this.$message.success("保存成功")
            this.load()
          }

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
      this.request.post("/role/delete/" + id).then(res => {
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
    exp() {
      window.open("http://localhost:8080/role/export")
    },
    handleExcelImportSuccess() {
      this.$message.success("文件导入成功")
      this.load()
    }
  }
}
</script>

<template>
  <div>
    <!-- 搜索框-->
    <div style="margin: 10px 0">
      <el-input style="width: 200px" placeholder="请输入名称" suffix-icon="el-icon-search"
                v-model="name"></el-input>
      <!--      <el-input style="width: 200px" placeholder="请输入描述" suffix-icon="el-icon-message" v-model="description"-->
      <!--                class="ml-5"></el-input>-->
      <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
      <el-button class="ml-5" type="warning" @click="reset">重置</el-button>
    </div>
    <!-- 新增/删除 -->
    <div style="margin: 10px 0">
      <el-button type="primary" @click="handleAdd">新增<i class="el-icon-circle-plus-outline"></i></el-button>
      <el-popconfirm class="ml-5" confirm-button-text="确定" cancel-button-text="我再想想" icon="el-icon-info"
                     icon-color="red"
                     title="确定批量删除吗?" @confirm="delBatch()">
        <el-button type="danger" slot="reference">删除<i class="el-icon-remove-outline"></i></el-button>
      </el-popconfirm>

      <!--            <el-upload action="http://localhost:8080/user/import" :show-file-list="false" accept="xlsx"-->
      <!--                       :on-success="handleExcelImportSuccess" style="display: inline-block" class="mr-5">-->
      <!--              <el-button type="primary" class="ml-5">导入<i class="el-icon-bottom"></i></el-button>-->
      <!--            </el-upload>-->

      <!--            <el-button type="primary" @click="exp">导出<i class="el-icon-top"></i></el-button>-->
    </div>
    <!-- 表格 -->
    <el-table :data="tableData" border stripe :header-cell-class-name="headerBg"
              @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="id" label="ID" width="70"></el-table-column>
      <el-table-column prop="name" label="名称"></el-table-column>
      <el-table-column prop="flag" label="唯一标识"></el-table-column>
      <el-table-column prop="description" label="描述"></el-table-column>

      <el-table-column label="操作" width="280  " align="center">
        <template slot-scope="scope">
          <el-button type="info" @click="handleMenu(scope.row)">分配菜单<i class="el-icon-menu"></i></el-button>
          <el-button type="success" @click="handleEdit(scope.row)">编辑<i class="el-icon-edit"></i></el-button>

          <!--     删除提示       -->
          <el-popconfirm class="ml-5" confirm-button-text="确定" cancel-button-text="我再想想" icon="el-icon-info"
                         icon-color="red" title="确定删除吗?" @confirm="del(scope.row.id)">
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
          :small="true"
          :disabled="false"
          :background="true"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
      />
    </div>

    <!--     dialog   -->
    <el-dialog title="角色信息" :visible.sync="dialogFormVisible" width="40%">
      <el-form label-width="70px" size="small">
        <el-form-item label="名称">
          <el-input v-model="form.name" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="唯一标识">
          <el-input v-model="form.flag" auto-complete="off"></el-input>
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
    <!--  菜单分配dialog  -->
    <el-dialog title="菜单分配" :visible.sync="menuDialogVisible" width="40%">
      <el-tree
          :props="props"
          :data="menuData"
          show-checkbox
          node-key="id"
          ref="tree"
          :check-strictly="true"
          :default-expanded-keys="expandedKeys"
          :default-checked-keys="checkedKeys"
          @check-change="handleCheckChange">
        <!-- :check-strictly = "true"-->
        <span class="custom-tree-node" slot-scope="{ node, data }">
          <span> <i :class="data.icon"></i>{{ data.name }}</span>
        </span>
      </el-tree>

      <div slot="footer" class="dialog-footer">
        <el-button @click="load">取 消</el-button>
        <el-button type="primary" @click="saveRoleMenu()">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<style>
.headerBg {
  background: #eeeeee !important;
}

</style>