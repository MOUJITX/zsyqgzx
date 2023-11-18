<template>
  <div v-if="this.user.role == 1" style="padding: 10px">
    <!--    功能区-->
    <div style="margin: 10px 0">
      <el-button type="primary" @click="add()">单条新增</el-button>
      <el-upload :on-progress="handleExcelImporting"
                 :on-success="handleExcelImportSuccess" :show-file-list="false"
                 accept="xlsx"
                 action="/api/department/import"
                 style="display: inline-block">
        <el-button style="margin-left: 10px;margin-right: 10px" type="primary">批量导入</el-button>
      </el-upload>
      <el-button type="primary" @click="exp">全部导出</el-button>
      <el-input v-model="search" clearable placeholder="请输入部门名称" style="width: 20%;margin-left: 20px"></el-input>
      <el-button style="margin-left: 5px" type="primary" @click="load">搜索</el-button>
    </div>
    <!--    数据区-->
    <el-table :data="tableData" :header-cell-style="{'text-align':'center'}" border stripe style="width:auto">
      <el-table-column label="部门编号" prop="number" width="100"/>
      <el-table-column label="部门名称" prop="name" width="200"/>
      <el-table-column label="部门说明" prop="detail" width="200"/>
      <el-table-column label="岗位说明" prop="gwsm" width="auto"/>
      <el-table-column label="用工需求" prop="pytj" width="auto"/>
      <el-table-column label="录用人数">
        <el-table-column label="钱塘校区" prop="qtrs" width="100"/>
        <el-table-column label="南浔校区" prop="nxrs" width="100"/>
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
          :page-sizes="[10, 20, 50, 100]"
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
          <el-form-item label="部门编号" prop="number">
            <el-input v-model="form.number" :disabled="useDisabled" style="width: 80%"></el-input>
          </el-form-item>
          <el-form-item label="部门名称" prop="name">
            <el-input v-model="form.name" style="width: 80%"></el-input>
          </el-form-item>
          <el-form-item label="部门说明">
            <el-input v-model="form.detail" style="width: 80%"></el-input>
          </el-form-item>
          <el-form-item label="岗位说明">
            <el-input v-model="form.gwsm" style="width: 80%" type="textarea"></el-input>
          </el-form-item>
          <el-form-item label="用工需求">
            <el-input v-model="form.pytj" style="width: 80%" type="textarea"></el-input>
          </el-form-item>
          <el-form-item style="font-weight: bold">钱塘校区</el-form-item>
          <el-form-item label="录用人数">
            <el-input v-model="form.qtrs" style="width: 80%"></el-input>
          </el-form-item>
          <el-form-item label="联系人">
            <el-input v-model="form.qtlxr" style="width: 80%"></el-input>
          </el-form-item>
          <el-form-item label="联系电话">
            <el-input v-model="form.qtdh" style="width: 80%"></el-input>
          </el-form-item>
          <el-form-item label="办公地址">
            <el-input v-model="form.qtbgdz" style="width: 80%"></el-input>
          </el-form-item>
          <el-form-item style="font-weight: bold">南浔校区</el-form-item>
          <el-form-item label="录用人数">
            <el-input v-model="form.nxrs" style="width: 80%"></el-input>
          </el-form-item>
          <el-form-item label="联系人">
            <el-input v-model="form.nxlxr" style="width: 80%"></el-input>
          </el-form-item>
          <el-form-item label="联系电话">
            <el-input v-model="form.nxdh" style="width: 80%"></el-input>
          </el-form-item>
          <el-form-item label="办公地址">
            <el-input v-model="form.nxbgdz" style="width: 80%"></el-input>
          </el-form-item>
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
      currentPage: 1,
      total: 0,
      pageSize: 10,
      tableData: [],
      menuRoleSet: true,
      activeNames: '',
      useDisabled: false,
      rules: {
        number: [
          {required: true, message: '请输入部门编号', trigger: 'blur'},
          {min: 1, max: 10, message: '长度在 1 到 10 个字符', trigger: 'blur'}
        ],
        name: [
          {required: true, message: '请输入部门名称', trigger: 'blur'},
          {min: 1, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur'}
        ],
      },
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
      request.get("/department", {
        params: {
          pageNum: this.currentPage,
          pageSize: this.pageSize,
          search: this.search
        }
      }).then(res => {
        console.log(res)
        this.tableData = res.data.records
        this.total = res.data.total
      })
    },
    add() {
      this.form = {}
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
            request.put("/department", this.form).then(res => {
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
            request.post("/department", this.form).then(res => {
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
      request.delete("/department/del/" + id).then(res => {
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
    exp() {
      window.open("/api/department/export")
    },
    handleExcelImportSuccess() {
      this.$message.success("导入成功")
      this.load()
    },
    handleExcelImporting() {
      this.$message.warning("导入中……")
      this.load()
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