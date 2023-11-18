<template>
  <div style="padding: 10px">
    <!--    功能区-->
    <div style="margin: 20px 20px;line-height: 40px">
      <el-card>
        <div slot="header">
          <span style="font-size: x-large;font-weight: bold">{{ userinfo.name }}</span>
          <span v-if="userinfo.role === '2'">　同学</span>
          <span v-if="userinfo.role === '3'">　老师</span>
          <span>，您好！</span>
        </div>
        <el-divider></el-divider>
        <span v-if="userinfo.role === '2'">当前你的资助认定状况为：
            <el-tag v-if="user.type === '3'">特别困难</el-tag>
            <el-tag v-if="user.type === '2'">困难</el-tag>
            <el-tag v-if="user.type === '1'">一般困难</el-tag>
            <el-tag v-if="user.type === ''">未认定</el-tag>
            <el-tag v-if="user.type === null">未认定</el-tag>
            <el-tag
                v-if="user.type === '0'">未认定</el-tag>，</span><span>当前勤工助学学年批次：{{
          xnNow.pcmc
        }}，{{ xnNow.zssm }}</span>
        <div>
          <el-button type="normal" @click="$router.push('/departShow')">查看岗位信息</el-button>
          <el-button v-if="userinfo.role === '2'" type="normal" @click="$router.push('/qgsq')">勤工助学申请</el-button>
          <el-button v-if="userinfo.role === '3'" type="normal" @click="$router.push('/xysh')">勤工助学审核</el-button>
          <el-button type="normal" @click="changeInfo">个人信息维护</el-button>
          <!--<el-button type="danger" @click="changePassword">修改密码</el-button>-->
        </div>
      </el-card>
      <span style="line-height: 60px;font-size: larger;font-weight: bold">通知公告</span>
      <el-card>
        <el-collapse v-for="index in (notice.length)" :key="index" v-model="activeName" accordion>
          <el-collapse-item
              :name="index-1"
              :title=" '[' + notice[notice.length-index].source + '] ' + notice[notice.length-index].title + '　' + notice[notice.length-index].date">
            <div v-html="(notice[notice.length-index].detail)"></div>
          </el-collapse-item>
        </el-collapse>
      </el-card>
    </div>
    <div>
      <el-dialog v-model="passwordDialogVisible" title="密码修改" width="30%">
        <el-form ref="form" :model="form" :rules="rules" label-width="100px">
          <el-form-item label="原密码" prop="name">
            <el-input style="width: 80%"></el-input>
          </el-form-item>
          <el-form-item label="新密码" prop="name">
            <el-input style="width: 80%"></el-input>
          </el-form-item>
          <el-form-item label="新密码确认" prop="name">
            <el-input style="width: 80%"></el-input>
          </el-form-item>
        </el-form>
        <template #footer>
                    <span class="dialog-footer">
                        <el-button @click="passwordDialogVisible = false">取消</el-button>
                        <el-button type="primary" @click="savePassword">提交</el-button>
                    </span>
        </template>
      </el-dialog>
    </div>
    <div>
      <el-dialog v-model="infoDialogVisible" title="个人信息修改" width="30%">
        <el-form ref="userinfo" :model="userinfo" label-width="100px">
          <el-form-item label="手机号">
            <el-input v-model="userinfo.phone" style="width: 80%"></el-input>
          </el-form-item>
          <el-form-item label="QQ">
            <el-input v-model="userinfo.qq" style="width: 80%"></el-input>
          </el-form-item>
          <el-form-item label="密码修改">
            <el-input v-model="userinfo.password" style="width: 80%"></el-input>
            <div>* 若不修改密码，此处保持为空即可。</div>
          </el-form-item>
        </el-form>
        <template #footer>
                    <span class="dialog-footer">
                        <el-button @click="infoDialogVisible = false">取消</el-button>
                        <el-button type="primary" @click="saveInfo">提交</el-button>
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
      userinfo: {},
      xnNow: {},
      notice: {},
      activeName: '1',
      passwordDialogVisible: false,
      infoDialogVisible: false,
      tk: '',
      passTemp: '',
    }

  },
  created() {
    //用户信息获取语句
    let userStr = sessionStorage.getItem("user") || "{}"
    this.tk = JSON.parse(userStr)
    console.log("this.tk=", this.tk)
    request.post("/user/tokenLogin", this.tk).then(res => {
      console.log(res)
      if (res.code === 200) {
        this.$message({
          type: "success",
          message: res.msg
        })
        this.user = res.user
        console.log("this.user", this.user)
        this.load()
      } else {
        this.$message({
          type: "error",
          message: res.msg
        })
      }
    })
    //用户信息获取语句结束
  },
  methods: {
    load() {
      this.cxStuByUsername(this.user.username)
      this.getTermInfo()
      this.getNotice()
    },
    getTermInfo() {
      request.get("/term/").then(res => {
        console.log("terminfo", res[res.length - 1])
        this.xnNow = res[res.length - 1]
      })
    },
    cxStuByUsername(username) {
      request.get("/user/username/" + username).then(res => {
        console.log("userinfo", res)
        this.userinfo = res.data
      })
    },
    getNotice() {
      request.get("/news/").then(res => {
        console.log("notice", res)
        this.notice = res
      })
    },
    changePassword() {
      this.passwordDialogVisible = true
    },
    changeInfo() {
      this.passTemp = this.userinfo.password
      this.userinfo.password = ''
      this.infoDialogVisible = true
    },
    saveInfo() {
      if (this.userinfo.password === '') this.userinfo.password = this.passTemp
      request.put("/user", this.userinfo).then(res => {
        console.log(res)
        if (res.code === '0') {
          this.$message({
            type: "success",
            message: "更新成功"
          })
          this.infoDialogVisible = false
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
</style>