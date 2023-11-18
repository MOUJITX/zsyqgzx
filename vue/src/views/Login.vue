<template>
  <div style="width: 100%; height: 100vh; background-image: url(/background.png);
    background-size:100% 100%; filter:blur(5px);z-index:-1;overflow: hidden">
  </div>
  <div style="width: 100%; height: 100vh;position: absolute;left: 0px;top: 0px">
    <div
        style="width: 400px; margin: 100px auto;background-color:  rgb(94, 198, 252, 0.7);padding: 40px;border-radius:10px">
      <img src="../../public/zjweu.png" width="250"/>
      <div style="color: white; font-size: 30px; text-align: center; margin: 30px 0">校内勤工助学管理系统</div>
      <el-form ref="form" :model="form" :rules="rules" size="normal" style="align-items: center;">
        <el-form-item prop="username" style="">
          <el-icon style="color: white;font-size: 15px">
            <User/>
          </el-icon>
          <div style="color: white;font-size: large;margin: 5px">账号</div>
          <el-input v-model="form.username" placeholder="请输入账号，学生账号为学号" style="width:80%"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-icon style="color: white;font-size: 15px">
            <Lock/>
          </el-icon>
          <div style="color: white;font-size: large;margin: 5px">密码</div>
          <el-input v-model="form.password" placeholder="请输入密码" show-password style="width: 80%"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button style="width: 20%;margin: 0 auto;font-size: medium" type="primary" @click="login">登 录</el-button>
        </el-form-item>
      </el-form>
      <div style="color: white; font-size: 15px; text-align: center; ">浙江·杭州</div>
      <div style="color: white; font-size: 15px; text-align: center; ">Copyright © 校大学生事务管理与服务中心</div>
      <div style="color: white; font-size: 15px; text-align: center; ">技术支持：学生管理科网络信息部</div>
    </div>
  </div>
</template>

<script>
import request from "../utils/request";
import {Lock, User} from '@element-plus/icons-vue';

export default {
  name: "Login",
  components: {User, Lock},
  data() {
    return {
      form: {},
      rules: {
        username: [
          {required: true, message: '请输入用户名', trigger: 'blur'}
        ],
        password: [
          {required: true, message: '请输入密码', trigger: 'blur'}
        ],
      }
    }
  },

  methods: {
    login() {
      const flag = navigator.userAgent.match(/(phone|pad|pod|iPhone|iPod|ios|iPad|Android|Mobile|BlackBerry|IEMobile|MQQBrowser|JUC|Fennec|wOSBrowser|BrowserNG|WebOS|Symbian|Windows Phone)/i)
      this.$refs['form'].validate((valid) => {
        if (valid) {
          request.post("/user/login", this.form).then(res => {
            if (res.code === 200) {
              sessionStorage.setItem("user", JSON.stringify(res.token))
              if (flag) {
                this.$router.push("/qgsqPhone")
              } else {
                this.$router.push("/")
              }
            } else {
              this.$message({
                type: "error",
                message: res.msg
              })
            }
          })
        }
      })
    }
  }
}
</script>

<style scoped>

</style>