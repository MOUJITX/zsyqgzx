<template>
  <div style="padding: 10px">
    <div>
      <div v-if="this.webShow == false" style="line-height: 300px;text-align: center;font-size: xx-large">
        暂无权限访问此页面！{{ this.user.username }}{{ this.formDetail.username }}
      </div>
    </div>
    <div v-if="this.webShow == true">
      <div>
        <!--<el-button type="" @click="$router.go(-1)">返回</el-button>-->
        <el-button type="primary">打印</el-button>
        <el-button type="primary">导出PDF</el-button>
      </div>
      <div>
        <div style="font-weight: 700; text-align: center; font-size: 20px; font-family: 黑体;line-height: 2em;">
          <p>浙江水利水电学院校内勤工助学岗位个人申请表（{{ this.formDetail.sqxn }}学年）</p>
        </div>
        <div align="center" width="730">
          <table style="text-align: center;line-height: 2em;font-size:16px;font-family:宋体;border-collapse:collapse;">
            <tbody>
            <tr style="height: 60px">
              <td style="border: 1px solid;width: 120px">姓名</td>
              <td style="border: 1px solid;width: 170px;padding: 10px;">{{ this.formDetail.name }}</td>
              <td style="border: 1px solid;width: 100px">学号</td>
              <td style="border: 1px solid;width: 150px;padding: 10px;">{{ this.formDetail.username }}</td>
              <td style="border: 1px solid;width: 100px">性别</td>
              <td style="border: 1px solid;width: 150px;padding: 10px;">{{ this.formDetail.male }}</td>
            </tr>
            <tr style="height: 60px">
              <td style="border: 1px solid">学院</td>
              <td style="border: 1px solid;padding: 10px;">{{ this.formDetail.college }}</td>
              <td style="border: 1px solid">班级</td>
              <td style="border: 1px solid;padding: 10px;">{{ this.formDetail.classname }}</td>
              <td style="border: 1px solid">联系方式</td>
              <td style="border: 1px solid;padding: 10px;">{{ this.formDetail.phone }}</td>
            </tr>
            <tr style="height: 60px">
              <td style="border: 1px solid">申请学生类别</td>
              <td colspan="5" style="border: 1px solid">
                <span v-if="this.formDetail.type == 0">资助对象未认定</span>
                <span v-if="this.formDetail.type == 1">一般资助对象（一般困难）</span>
                <span v-if="this.formDetail.type == 2">一般资助对象（困难）</span>
                <span v-if="this.formDetail.type == 3">特别资助对象（特别困难）</span>
              </td>
            </tr>
            <tr style="height: 60px;">
              <td style="border: 1px solid;padding: 10px;">个人工作技能介绍</td>
              <td colspan="5" style="border: 1px solid;padding: 10px;text-align: left">{{ this.formDetail.skill }}</td>
            </tr>
            <tr style="height: 60px">
              <td style="border: 1px solid;padding: 10px;">意向部门一</td>
              <td colspan="2" style="border: 1px solid">{{ this.formDetail.depart }}</td>
              <td style="border: 1px solid;padding: 10px;">意向部门二</td>
              <td colspan="2" style="border: 1px solid">{{ this.formDetail.departb }}</td>
            </tr>
            <tr style="height: 60px">
              <td style="border: 1px solid;padding: 10px;">工作时间</td>
              <td colspan="5" style="border: 1px solid">
                <p>周一：8:00-16:30，可提供的工作时间 {{ this.formDetail.timea }} 小时。</p>
                <p>周二：8:00-16:30，可提供的工作时间 {{ this.formDetail.timeb }} 小时。</p>
                <p>周三：8:00-16:30，可提供的工作时间 {{ this.formDetail.timec }} 小时。</p>
                <p>周四：8:00-16:30，可提供的工作时间 {{ this.formDetail.timed }} 小时。</p>
                <p>周五：8:00-16:30，可提供的工作时间 {{ this.formDetail.timee }} 小时。</p>
              </td>
            </tr>
            <tr style="height: 100px">
              <td style="border: 1px solid;padding: 10px;">二级学院意见</td>
              <td colspan="5" style="border: 1px solid">
                <p v-if="this.formDetail.speed === 'finish'" style="line-height: 50px">
                  情况属实，同意参加校内勤工助学。</p>
                <p v-if="this.formDetail.speed === 'doing'" style="line-height: 50px">（二级学院审核中）</p>
                <p v-if="this.formDetail.speed === 'save'" style="line-height: 50px">（学生暂未提交申请）</p>
                <p v-if="this.formDetail.speed === 'refuse'" style="line-height: 50px">不同意参加校内勤工助学</p>
                <p style="text-align: right">负责人：　　　　　盖章：　　　{{ this.replyTime.year }} 年 {{ this.replyTime.month }}
                  月 {{ this.replyTime.day }} 日　　</p>
              </td>
            </tr>
            <tr style="height: 100px">
              <td style="border: 1px solid;padding: 10px;">用工部门（单位）聘任意见</td>
              <td colspan="5" style="border: 1px solid">
                <p v-if="this.formDetail.speed === 'finish'" style="line-height: 50px">同意该生参加校内勤工助学。</p>
                <p v-if="this.formDetail.speed != 'finish'" style="line-height: 50px">（暂未通过二级学院审核）</p>
                <p style="text-align: right">负责人：　　　　　盖章：　　　　　 年　　月　　日　　</p>
              </td>
            </tr>
            <tr style="height: 100px">
              <td style="border: 1px solid;padding: 10px;">学生资助管理中心审核意见</td>
              <td colspan="5" style="border: 1px solid">
                <p v-if="this.formDetail.speed === 'finish'" style="line-height: 50px">同意该生参加校内勤工助学。</p>
                <p v-if="this.formDetail.speed != 'finish'" style="line-height: 50px">（暂未通过二级学院审核）</p>
                <p style="text-align: right">学生处（代章）：　　　　　 年　　月　　日　　</p>
              </td>
            </tr>
            </tbody>
          </table>
          <div>　</div>
        </div>
      </div>
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
      user: {},
      tk: '',
      webShow: true,
      formId: '',
      formDetail: {},
      replyTime: {year: '　　', month: '　', day: '　'},
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
    load() {
      this.formId = this.$route.query.id
      request.get("/qgsq/" + this.formId).then(res => {
        console.log("formDetail", res.data)
        this.formDetail = res.data
        this.replyTime.year = res.data.replytime.slice(0, 4)
        this.replyTime.month = res.data.replytime.slice(5, 7)
        this.replyTime.day = res.data.replytime.slice(8, 10)
      })
    }
  }
}
</script>

<style>
</style>