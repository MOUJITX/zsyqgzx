<template>
  <div v-if="this.user.role == 1" style="padding: 10px">
    <!--    功能区-->
    <div style="margin: 10px 0">
      <el-button type="primary" @click="add()">单条新增</el-button>
      <el-input v-model="search" clearable placeholder="请输入关键字" style="width: 20%;margin-left: 20px"></el-input>
      <el-button style="margin-left: 5px" type="primary" @click="load">搜索</el-button>
    </div>
    <!--    数据区-->
    <el-table :cell-style="{'text-align':'center'}" :data="tableData" :header-cell-style="{'text-align':'center'}"
              border stripe
              style="width:auto">
      <el-table-column label="发布位置" prop="source" width="90"/>
      <el-table-column label="标题" prop="title" width="auto"/>
      <el-table-column label="作者" prop="author" width="auto"/>
      <el-table-column label="置顶" width="70">
        <template #default="scope">
          <span v-if="scope.row.top === 'true'" style="color:rgba(255,0,0,0);">置顶</span>
        </template>
      </el-table-column>
      <el-table-column label="发布状态" prop="publish" width="90"/>
      <el-table-column label="发布日期" prop="date" width="180"/>
      <el-table-column fixed="right" label="操作" width="250">
        <template #default="scope">
          <el-button size="small" type="primary" @click="viewRichText(scope.row.detail,scope.row.title)">查看内容
          </el-button>
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
      <el-dialog v-model="dialogVisible" width="60%">
        <el-form ref="form" :model="form" :rules="rules" label-width="120px">
          <el-form-item label="标题" prop="title" style="width: 90%">
            <el-input v-model="form.title"></el-input>
          </el-form-item>
          <el-form-item label="置顶" style="width: 40%">
            <el-switch v-model="form.top"></el-switch>
          </el-form-item>
          <el-form-item label="发布状态" prop="publish" style="width: 40%">
            <el-select v-model="form.publish" placeholder="">
              <el-option label="发布" selected value="已发布"></el-option>
              <el-option label="暂存" value="暂存"></el-option>
              <el-option label="草稿" value="草稿"></el-option>
              <el-option label="隐藏" value="隐藏"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="发布位置" prop="source" style="width: 40%">
            <el-select v-model="form.source" placeholder="">
              <el-option label="通知" value="通知"></el-option>
              <el-option label="公示" value="公示"></el-option>
              <el-option label="动态" value="动态"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="作者" prop="author" style="width: 90%">
            <el-input v-model="form.author"></el-input>
          </el-form-item>
          <el-form-item label="时间" prop="date">
            <el-date-picker
                v-model="form.date"
                :default-value="new Date()"
                format="YYYY-MM-DD hh:mm:ss"
                placeholder="选择日期时间"
                type="datetime"
                value-format="YYYY-MM-DD h:m:s"
            />
          </el-form-item>
          <el-form-item label="内容" style="width: 90%">
            <div id="richText"></div>
          </el-form-item>
        </el-form>
        <template #footer>
            <span class="dialog-footer">
                <el-button @click="dialogVisible = false">取消</el-button>
                <el-button type="primary" @click="save()">提交</el-button>
            </span>
        </template>
      </el-dialog>
      <el-dialog v-model="richTextVisible" :title="title">
        <el-card>
          <div v-html="detail"></div>
        </el-card>
      </el-dialog>
    </div>
  </div>
</template>

<script>
import request from "../utils/request";
import moment from "moment"
import E from 'wangeditor';

let editor;

export default {
  name: 'news',
  components: {},
  data() {
    return {
      user: {},
      form: {},
      tk: '',
      dialogVisible: false,
      richTextVisible: false,
      search: '',
      currentPage: 1,
      total: 0,
      pageSize: 10,
      tableData: [],
      detail: '',
      title: '',
      rules: {
        title: [
          {required: true, message: '请输入标题', trigger: 'blur'},
          {min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur'}
        ],
        publish: [
          {required: true, message: '请选择发布状态', trigger: 'blur'},
        ],
        source: [
          {required: true, message: '请选择发布位置', trigger: 'blur'},
        ],
        author: [
          {required: true, message: '请输入作者', trigger: 'blur'},
          {min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur'}
        ],
        date: [
          {required: true, message: '请选择发布日期', trigger: 'blur'},
        ]
      }
    }
  },
  created() {
    this.load()
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
  },
  methods: {
    viewRichText(detail, title) {
      this.title = title
      this.detail = detail
      this.richTextVisible = true
    },
    load() {
      request.get("/news", {
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
        if (!editor) {
          editor = new E('#richText')
          editor.config.uploadImgServer = '/api/UploadImg'
          editor.config.uploadFileName = 'file'
          editor.config.uploadImgMaxSize = 10 * 1024 * 1024
          editor.config.uploadImgAccept = ['jpg', 'jpeg', 'png', 'gif', 'bmp', 'webp']
          editor.config.uploadVideoServer = '/api/UploadVideo'
          editor.config.uploadVideoName = 'file'
          editor.config.uploadVideoMaxSize = 50 * 1024 * 1024 // 1024m
          editor.config.uploadVideoAccept = ['mp4', 'wmv', 'avi', 'mpg', 'webm', 'mov']
          editor.create()
        }
        editor.txt.html('')
        if (this.$refs['upload']) {
          this.$refs['upload'].clearFiles()
        }
      })
      this.form = {image: '', author: this.user.name, top: false}
    },
    save() {
      this.$refs['form'].validate((valid) => {
        if (valid) {
          const detail = editor.txt.html()
          const simple = editor.txt.text()
          this.form.detail = detail
          this.form.simple = simple
          if (!this.form.date) {
            this.form.date = moment(new Date()).format("YYYY-MM-DD hh:mm:ss");
          }
          if (this.form.id) {
            this.form.edituser = this.user.username
            request.put("/news", this.form).then(res => {
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
            this.form.adduser = this.user.username
            request.post("/news", this.form).then(res => {
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
      })
    },
    handleEdit(row) {
      this.form = JSON.parse(JSON.stringify(row))
      if (this.form.top == "true") {
        this.form.top = true
      } else {
        this.form.top = false
      }
      this.dialogVisible = true
      this.$nextTick(() => {
        if (!editor) {
          editor = new E('#richText')
          editor.config.uploadImgServer = '/api/UploadImg'
          editor.config.uploadFileName = 'file'
          editor.config.uploadImgMaxSize = 10 * 1024 * 1024
          editor.config.uploadImgAccept = ['jpg', 'jpeg', 'png', 'gif', 'bmp', 'webp']
          editor.config.uploadVideoServer = '/api/UploadVideo'
          editor.config.uploadVideoName = 'file'
          editor.config.uploadVideoMaxSize = 50 * 1024 * 1024 // 1024m
          editor.config.uploadVideoAccept = ['mp4', 'wmv', 'avi', 'mpg', 'webm', 'mov']
          editor.create()
        }
        editor.txt.html(this.form.detail)
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
      request.delete("/news/del/" + id).then(res => {
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
      console.log(res)
      if (res.errno == 0) {
        this.$message.success("图片上传成功")
        this.form.image = res.data[0].url
      } else {
        this.$message({
          type: "error",
          message: res.msg
        })
      }
    }
  },
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