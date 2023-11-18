<template>
  <div
      style="height: 60px;line-height: 60px; border-bottom: 1px solid #ccc; display: flex;background-image: linear-gradient(to top, #534bef, #017eff); ">
    <div style="width: auto; padding-left: 30px; font-weight: bold;">
      <a href="/" style="color: white; text-decoration:none;font-size: larger">
        浙江水利水电学院 · 校内勤工助学管理系统
      </a>
    </div>
    <div style="flex: 1"></div>
    <div style="width: 200px;">
      <el-dropdown>
                <span class="el-dropdown-link" style="color: white;line-height: 60px">
                  {{ user.name }} ({{ user.username }})
                    <el-icon style="left: 10px"><SwitchButton/></el-icon>
                </span>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item @click="$router.push('/login')">退出系统</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>
</template>

<script>
import request from "../utils/request";
import {SwitchButton} from '@element-plus/icons-vue';

export default {
  name: "Aside",
  components: {SwitchButton},
  data() {
    return {
      user: {},
      path: this.$route.path
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
  },
  methods: {
    logout() {
      sessionStorage.clear()
    },
  }
}
</script>

<style scoped>
.el-dropdown-link {
  cursor: pointer;
  color: var(--el-color-primary);
  display: flex;
  align-items: center;
  line-height: 50px;
}
</style>