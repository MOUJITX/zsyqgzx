<template>
  <div v-if="this.user.role == 1" style="padding: 10px">
    <!--    功能区-->
    <div style="margin: 10px 0">
      <el-button type="primary" @click="add()">单条新增</el-button>
      <el-upload :on-progress="handleExcelImporting"
                 :on-success="handleExcelImportSuccess" :show-file-list="false"
                 accept="xlsx"
                 action="/api/user/import"
                 style="display: inline-block">
        <el-button style="margin-left: 10px;margin-right: 10px" type="primary">批量导入</el-button>
      </el-upload>
      <el-link href="../../simple/用户信息导入模板.xlsx" target="_blank" type="primary">(导入模板下载)</el-link>
      <el-button style="margin-left: 20px" type="primary" @click="exp">导出</el-button>
      <el-input v-model="search" clearable placeholder="请输入姓名" style="width: 20%;margin-left: 20px"
                @change="load"></el-input>
      <el-input v-model="searchUsername" clearable placeholder="请输入学号" style="width: 20%;margin-left: 20px"
                @change="load"></el-input>
      <el-button style="margin-left: 5px" type="primary" @click="load">搜索</el-button>
    </div>
    <!--    数据区-->
    <el-table :cell-style="{'text-align':'center'}" :data="tableData" :header-cell-style="{'text-align':'center'}"
              border stripe
              style="width:auto">
      <el-table-column label="学号/账号" prop="username" width="150"/>
      <el-table-column label="姓名" prop="name" width="auto"/>
      <el-table-column label="性别" prop="male" width="auto"/>
      <el-table-column label="学院" prop="college" width="180"/>
      <el-table-column label="班级" prop="classname" width="auto"/>
      <el-table-column label="年级" prop="grade" width="auto"/>
      <el-table-column label="联系方式" prop="phone" width="auto"/>
      <el-table-column label="层次" width="90">
        <template #default="scope">
          <span v-if="scope.row.level === '1'">预科</span>
          <span v-if="scope.row.level === '2'">专升本</span>
          <span v-if="scope.row.level === '3'">专科</span>
          <span v-if="scope.row.level === '4'">本科</span>
        </template>
      </el-table-column>
      <el-table-column label="资助认定" prop="type" width="100">
        <template #default="scope">
          <el-tag v-if="scope.row.type === '3'" type="danger">特别困难</el-tag>
          <el-tag v-if="scope.row.type === '2'" type="warning">困难</el-tag>
          <el-tag v-if="scope.row.type === '1'" type="">一般困难</el-tag>
          <el-tag v-if="scope.row.type === ''" type="success">未认定</el-tag>
          <el-tag v-if="scope.row.type === null" type="success">未认定</el-tag>
          <el-tag v-if="scope.row.type === '0'" type="success">未认定</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="校区" prop="campus" width="90"/>
      <el-table-column label="权限" width="90">
        <template #default="scope">
          <span v-if="scope.row.role === '1'">管理员</span>
          <span v-if="scope.row.role === '2'">学生</span>
          <span v-if="scope.row.role === '3'">辅导员</span>
          <span v-if="scope.row.role === '9'">其他</span>
        </template>
      </el-table-column>
      <el-table-column fixed="right" label="操作" width="170">
        <template #default="scope">
          <el-button size="small" type="primary" @click="handleEdit(scope.row)">编辑</el-button>
          <el-popconfirm title="确认删除?" @confirm="handleDelete(scope.row.id)">
            <template #reference>
              <el-button size="small" type="danger">删除</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <div style="margin: 10px;">
      <el-pagination
          v-model:currentPage="currentPage"
          v-model:page-size="pageSize"
          :background=true
          :disabled=false
          :page-sizes="[15, 30, 50, 100]"
          :small=false
          :total="total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
      />
    </div>
    <div>
      <el-dialog v-model="dialogVisible" width="50%">
        <el-form ref="form" :model="form" :rules="rules" label-width="120px">
          <el-form-item label="学号/账号" prop="username">
            <el-input v-model="form.username" :disabled="useDisabled" style="width: 80%"></el-input>
          </el-form-item>
          <el-form-item label="姓名" prop="name">
            <el-input v-model="form.name" style="width: 80%"></el-input>
          </el-form-item>
          <el-form-item label="权限" prop="role">
            <el-select v-model="form.role" placeholder="">
              <el-option label="系统管理员" value="1"></el-option>
              <el-option label="学生" value="2"></el-option>
              <el-option label="辅导员" value="3"></el-option>
              <el-option label="其他" value="9"></el-option>
            </el-select>
          </el-form-item>
          <el-collapse style="width: 100%" @change="handleChange">
            <el-collapse-item name="1" title="更多信息">
              <el-form-item label="性别">
                <el-select v-model="form.male" placeholder="">
                  <el-option label="男" value="男"></el-option>
                  <el-option label="女" value="女"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="资助认定">
                <el-select v-model="form.type" placeholder="">
                  <el-option label="特别困难" value="3"></el-option>
                  <el-option label="困难" value="2"></el-option>
                  <el-option label="一般困难" value="1"></el-option>
                  <el-option label="未认定" value="0"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="校区">
                <el-select v-model="form.campus" placeholder="">
                  <el-option label="钱塘校区" value="钱塘校区"></el-option>
                  <el-option label="南浔校区" value="南浔校区"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="学院">
                <el-select v-model="form.college" placeholder="">
                  <el-option
                      v-for="item in selectOptions"
                      :key="item.id"
                      :label="item.name"
                      :value="item.name"
                  />
                </el-select>
              </el-form-item>
              <el-form-item label="班级">
                <el-input v-model="form.classname" style="width: 80%"></el-input>
              </el-form-item>
              <el-form-item label="年级">
                <el-select v-model="form.grade" placeholder="">
                  <el-option label="2022" value="2022"></el-option>
                  <el-option label="2021" value="2021"></el-option>
                  <el-option label="2020" value="2020"></el-option>
                  <el-option label="2019" value="2019"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="联系电话">
                <el-input v-model="form.phone" style="width: 80%"></el-input>
              </el-form-item>
              <el-form-item label="QQ">
                <el-input v-model="form.qq" style="width: 80%"></el-input>
              </el-form-item>
            </el-collapse-item>
          </el-collapse>
        </el-form>
        <template #footer>
            <span class="dialog-footer">
                <el-button @click="dialogVisible = false">取消</el-button>
                <el-button type="primary" @click="save">提交</el-button>
            </span>
        </template>
      </el-dialog>
    </div>
  </div>
</template>

<script>


import request from "../utils/request";

export default {
  name: 'HomeView',
  components: {},
  data() {
    return {
      form: {},
      user: '',
      tk: '',
      dialogVisible: false,
      search: '',
      searchUsername: '',
      currentPage: 1,
      total: 0,
      pageSize: 15,
      tableData: [],
      activeNames: '',
      useDisabled: false,
      rules: {
        username: [
          {required: true, message: '请输入学号/账号', trigger: 'blur'},
          {min: 5, max: 10, message: '长度在 5 到 10 个字符', trigger: 'blur'}
        ],
        name: [
          {required: true, message: '请输入姓名', trigger: 'blur'},
          {min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur'}
        ],
        role: [
          {required: true, message: '请选择角色权限', trigger: 'blur'}
        ],
      },
      selectOptions: [],
    }

  },
  created() {
    //用户信息获取语句
    let userStr = sessionStorage.getItem("user") || "{}"
    this.tk = JSON.parse(userStr)
    request.post("/user/tokenLogin", this.tk).then(res => {
      if (res.code === 200) {
        this.user = res.user
        this.load()
      }
    })
    //用户信息获取语句结束
    this.load()
  },
  methods: {
    handleChange(val) {
      console.log(val);
    },
    load() {
      request.get("/user", {
        params: {
          pageNum: this.currentPage,
          pageSize: this.pageSize,
          search: this.search,
          username: this.searchUsername
        }
      }).then(res => {
        console.log(res)
        this.tableData = res.data.records
        this.total = res.data.total
      })
    },
    add() {
      this.form = {}
      this.getDepartmentName()
      this.dialogVisible = true
      this.$nextTick(() => {
        this.useDisabled = false
        if (this.$refs['upload']) {
          this.$refs['upload'].clearFiles()
        }
      })
    },
    save() {
      this.$refs['form'].validate((valid) => {
        if (valid) {
          if (this.form.id) {
            request.put("/user", this.form).then(res => {
              console.log(res)
              if (res.code === '0') {
                this.$message({
                  type: "success",
                  message: "更新成功"
                })
                this.dialogVisible = false
                this.load()
              } else {
                this.$message({
                  type: "error",
                  message: res.msg
                })
              }
            })
          } else {
            request.post("/user", this.form).then(res => {
              console.log(res)
              if (res.code === '0') {
                this.$message({
                  type: "success",
                  message: "新增成功"
                })
                this.dialogVisible = false
                this.load()
              } else {
                this.$message({
                  type: "error",
                  message: res.msg
                })
              }
            })
          }
        }
      });
    },
    handleEdit(row) {
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogVisible = true
      this.$nextTick(() => {
        this.useDisabled = true
        this.getDepartmentName()
        if (this.$refs['upload']) {
          this.$refs['upload'].clearFiles()
        }
      })
    },
    handleSizeChange(pageSize) {
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum) {
      this.pageNum = pageNum
      this.load()
    },
    handleDelete(id) {
      console.log(id)
      request.delete("/user/del/" + id).then(res => {
        console.log(res)
        if (res.code === '0') {
          this.$message({
            type: "success",
            message: "删除成功"
          })
          this.load()
        } else {
          this.$message({
            type: "error",
            message: res.msg
          })
        }
      })
    },
    filesUploadSuccess(res) {
      console.log("img", res)
      if (res.errno == 0) {
        this.$message.success("图片上传成功")
        this.form.photo = res.data[0].url
      } else {
        this.$message({
          type: "error",
          message: res.msg
        })
      }
    },
    exp() {
      this.$message.warning("导出中")
      window.open("/api/user/export?name=" + this.search + "&username=" + this.searchUsername,)
      this.$message.success("导出成功")
    },
    handleExcelImportSuccess() {
      this.$message.success("导入成功")
      this.load()
    },
    handleExcelImporting() {
      this.$message.warning("导入中……")
      this.load()
    },
    getDepartmentName() {
      request.get("/department/").then((res) => {
        console.log(res)
        this.selectOptions = res;
      })
    },
  }
}
</script>

<style>
.avatar-uploader {
  border: 1px dashed var(--el-border-color);
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}

.avatar-uploader :hover {
  border-color: var(--el-color-primary);
}

.avatar-uploader-icon {
  font-size: 20px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  text-align: center;
  font-style: normal;
}

.avatar {
  height: 178px;
}
</style>