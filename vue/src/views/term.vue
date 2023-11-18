<template>
  <div v-if="this.user.role == 1" style="padding: 10px">
    <!--    功能区-->
    <div style="margin: 10px 0">
      <el-button type="primary" @click="add()">单条新增</el-button>
    </div>
    <!--    数据区-->
    <el-table :cell-style="{'text-align':'center'}" :data="tableData" :header-cell-style="{'text-align':'center'}"
              border stripe
              style="width:auto">
      <el-table-column label="名称" prop="pcmc" width="200"/>
      <el-table-column label="代码" prop="pcdm" width="200"/>
      <el-table-column label="状态" width="100">
        <template #default="scope">
          <el-tag v-if="scope.row.dqzt === '0'" type="info">关闭</el-tag>
          <el-tag v-if="scope.row.dqzt === '1'" type="normal">资助可填</el-tag>
          <el-tag v-if="scope.row.dqzt === '2'" type="success">全部可填</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="显示说明" prop="zssm" width="auto"/>
      <el-table-column label="备注" prop="xmbz" width="auto"/>
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
          <el-form-item label="批次名称" prop="pcmc">
            <el-input v-model="form.pcmc" style="width: 80%"></el-input>
          </el-form-item>
          <el-form-item label="批次代码" prop="pcdm">
            <el-input v-model="form.pcdm" style="width: 80%"></el-input>
          </el-form-item>
          <el-form-item label="当前状态" prop="dqzt">
            <el-select v-model="form.dqzt" placeholder="">
              <el-option label="关闭" value="0"></el-option>
              <el-option label="资助可填" value="1"></el-option>
              <el-option label="全部可填" value="2"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="展示说明">
            <el-input v-model="form.zssm" style="width: 80%"></el-input>
          </el-form-item>
          <el-form-item label="备注">
            <el-input v-model="form.xmbz" style="width: 80%"></el-input>
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
      activeNames: '',
      rules: {
        pcmc: [
          {required: true, message: '请输入批次名称', trigger: 'blur'},
        ],
        pcdm: [
          {required: true, message: '请输入批次代码', trigger: 'blur'},
        ],
        dqzt: [
          {required: true, message: '请输入当前状态', trigger: 'blur'},
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
      request.get("/term", {
        params: {
          pageNum: this.currentPage,
          pageSize: this.pageSize,
          search: this.search
        }
      }).then(res => {
        console.log(res.data.records[res.data.records.length - 1])
        this.tableData = res.data.records
        this.total = res.data.total
      })
    },
    add() {
      this.form = {}
      this.dialogVisible = true
      this.$nextTick(() => {
        if (this.$refs['upload']) {
          this.$refs['upload'].clearFiles()
        }
      })
    },
    save() {
      this.$refs['form'].validate((valid) => {
        if (valid) {
          if (this.form.id) {
            request.put("/term", this.form).then(res => {
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
            request.post("/term", this.form).then(res => {
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
      request.delete("/term/del/" + id).then(res => {
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
    }
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