<template>
  <div>
    <el-menu
        :default-active="path"
        class="el-menu-vertical-demo"
        router
        style="width: 200px; min-height: calc(100vh - 50px)">
      <el-menu-item index="/index">
        <el-icon>
          <House/>
        </el-icon>
        首页
      </el-menu-item>
      <el-sub-menu :disabled="menuRoleSet" :hidden="menuRoleSet" index="/infoPub">
        <template #title>
          <span><el-icon><MessageBox/></el-icon>信息发布</span>
        </template>
        <el-menu-item index="/news">
          <el-icon>
            <DocumentAdd/>
          </el-icon>
          通知公告
        </el-menu-item>
      </el-sub-menu>
      <el-sub-menu :disabled="menuRoleSet" :hidden="menuRoleSet" index="/people">
        <template #title>
          <span><el-icon><connection/></el-icon>人员管理</span>
        </template>
        <el-menu-item index="/user">
          <el-icon>
            <Postcard/>
          </el-icon>
          所有人员信息
        </el-menu-item>
        <el-menu-item index="/teacher">
          <el-icon>
            <Star/>
          </el-icon>
          辅导员信息
        </el-menu-item>
        <el-menu-item index="/student">
          <el-icon>
            <Star/>
          </el-icon>
          学生信息
        </el-menu-item>
      </el-sub-menu>
      <el-sub-menu index="/staff">
        <template #title>
          <span><el-icon><User/></el-icon>勤工管理</span>
        </template>
        <div :hidden="menuRoleSet">
          <el-menu-item :disabled="menuRoleSet" index="/term">
            <el-icon>
              <Suitcase/>
            </el-icon>
            批次设置
          </el-menu-item>
        </div>
        <div :hidden="menuRoleSet">
          <el-menu-item :disabled="menuRoleSet" index="/department">
            <el-icon>
              <Suitcase/>
            </el-icon>
            部门岗位
          </el-menu-item>
        </div>
        <div>
          <el-menu-item index="/departShow">
            <el-icon>
              <Suitcase/>
            </el-icon>
            岗位查看
          </el-menu-item>
        </div>
        <div :hidden="menuRoleSetFDY">
          <el-menu-item :disabled="menuRoleSetFDY" index="/xysh">
            <el-icon>
              <Postcard/>
            </el-icon>
            勤工审核
          </el-menu-item>
        </div>
        <div :hidden="menuRoleSetSTU">
          <el-menu-item :disabled="menuRoleSetSTU" index="/qgsq">
            <el-icon>
              <ChatDotRound/>
            </el-icon>
            勤工申请
          </el-menu-item>
        </div>
      </el-sub-menu>
      <div :hidden="menuRoleSetSTU">
        <el-menu-item :disabled="menuRoleSetSTU" index="/qgsqPhone">
          <el-icon>
            <Iphone/>
          </el-icon>
          手机端
        </el-menu-item>
      </div>
    </el-menu>
  </div>
</template>

<script>
import request from "../utils/request";
import {
  ChatDotRound,
  Connection,
  CopyDocument,
  DocumentAdd,
  EditPen,
  Filter,
  House,
  Iphone,
  MessageBox,
  Picture,
  Postcard,
  Setting,
  Star,
  Suitcase,
  Tickets,
  User
} from '@element-plus/icons-vue';

export default {
  name: "Aside",
  components: {
    MessageBox, Picture, DocumentAdd, Tickets, Suitcase, Postcard, EditPen, ChatDotRound, Star, Filter,
    House, Setting, User, CopyDocument, Connection, Iphone
  },
  data() {
    return {
      user: {},
      path: this.$route.path,
      menuRoleSet: true,
      menuRoleSetFDY: true,
      menuRoleSetSTU: true
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
        this.user = res.user
        if (this.user.role == 1) {
          this.menuRoleSet = false;
          this.menuRoleSetFDY = false;
          this.menuRoleSetSTU = false
        } else if (this.user.role == 2) {
          this.menuRoleSet = true;
          this.menuRoleSetFDY = true;
          this.menuRoleSetSTU = false
        } else if (this.user.role == 3) {
          this.menuRoleSet = true;
          this.menuRoleSetFDY = false;
          this.menuRoleSetSTU = true
        }
      }
    })
    //用户信息获取语句结束
  }
}
</script>


<style>


</style>