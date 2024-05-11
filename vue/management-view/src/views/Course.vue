<script>
export default {
  name: "Course",
  data() {
    return {
      tableData: [],
      form: {},
      pageNum: 1,
      pageSize: 10,
      total: 0,
      name: '',
      score: '',
      times: '',
      teacherId: '',
      dialogFormVisible: false,
      window: window,
      multipleSelection: [],
      headerBg: "headerBg",
      teachers: [],
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {}
    }
  },
  created() {
    this.load()
  },
  methods: {
    load() {
      this.dialogFormVisible = false
      this.request.get("/course/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name,
        }
      }).then(res => {
        console.log(res)
        this.tableData = res.data.records
        this.total = res.data.total
      })

      this.request.get("/user/role/ROLE_TEACHER").then(res => {
        console.log(this.user)
        this.teachers = res.data
      })
    },
    reset() {
      this.name = ''
    },
    save() {
      this.dialogFormVisible = false
      this.request.post("/course/save", this.form).then(res => {
        if (res.code === '200') {
          this.$message.success("保存成功")
          this.load()
        } else {
          this.$message.error("保存失败")
        }
      })
    },
    del(id) {
      this.request.post("/course/delete/" + id).then(res => {
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
    changeEnable(row) {
      this.request.post("/course/save", row).then(res => {
        if (res.code === '200') {
          this.$message.success("修改成功")
          this.load();
        } else {
          this.$message.error("修改失败")
        }
      })
    },
    handleAdd() {
      this.dialogFormVisible = true;
      this.form = {}
    },
    handleEdit(row) {
      this.form = row
      this.dialogFormVisible = true
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
    handleCourse(courseId) {
      this.request.post('/course/studentCourse/' + courseId + "/" + this.user.id).then(res => {
        if (res.code === '200') {
          this.$message.success("选课成功")
        } else {
          this.$message.success(res.msg)
        }

      })
    }
  }
}
</script>

<template>
  <!-- 新增/删除 -->
  <div style="margin: 10px 0">

    <el-button type="primary" v-if="user.role==='ROLE_ADMIN'" @click="handleAdd">新增<i
        class="el-icon-circle-plus-outline"></i></el-button>


    <el-popconfirm class="ml-5" confirm-button-text="确定" cancel-button-text="我再想想" icon="el-icon-info"
                   icon-color="red"
                   title="确定批量删除吗?" @confirm="delBatch()">
      <el-button type="danger" v-if="user.role==='ROLE_ADMIN'" slot="reference">批量删除<i
          class="el-icon-remove-outline"></i></el-button>
    </el-popconfirm>

    <!-- 表格 -->
    <el-table :data="tableData" border stripe :header-cell-class-name="headerBg"
              @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="id" label="ID" width="70"></el-table-column>
      <el-table-column prop="name" label="课程名称"></el-table-column>
      <el-table-column prop="score" label="课程学分"></el-table-column>
      <el-table-column prop="times" label="学时"></el-table-column>
      <el-table-column prop="teacher" label="老师"></el-table-column>
      <el-table-column label="状态">
        <template slot-scope="scope">
          <el-switch v-model="scope.row.state" active-color="#13ce66" inactive-color="#ff4949"
                     @change="changeEnable(scope.row)"></el-switch>
        </template>
      </el-table-column>

      <el-table-column label="操作" width="200" align="center">
        <template slot-scope="scope">
          <el-button type="primary" @click="handleCourse(scope.row.id)">选课</el-button>
          <el-button type="success" v-if="user.role==='ROLE_ADMIN'" @click="handleEdit(scope.row)">编辑<i
              class="el-icon-edit"></i></el-button>
          <!--     删除提示       -->
          <el-popconfirm class="ml-5" confirm-button-text="确定" cancel-button-text="我再想想" icon="el-icon-info"
                         icon-color="red"
                         title="确定删除吗?" @confirm="del(scope.row.id)">
            <el-button type="danger" v-if="user.role==='ROLE_ADMIN'" slot="reference">删除<i
                class="el-icon-remove-outline"></i></el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <!--     dialog   -->
    <el-dialog title="课程信息" :visible.sync="dialogFormVisible" width="40%">
      <el-form label-width="70px" size="small">
        <el-form-item label="课程名称">
          <el-input v-model="form.name" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="课程学分">
          <el-input v-model="form.score" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="学时">
          <el-input v-model="form.times" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="授课老师">
          <el-select clearable v-model="form.teacherId" placeholder="请选择">
            <el-option v-for="item in teachers" :key="item.id" :label="item.nickname" :value="item.id"/>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="load">取 消</el-button>
        <el-button type="primary" @click="save()">确 定</el-button>
      </div>
    </el-dialog>

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
  </div>
</template>

<style scoped>

</style>