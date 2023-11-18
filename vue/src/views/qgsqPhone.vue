<template>
  <div style="padding: 10px">
    <!--    功能区-->
    <div style="margin: 20px 20px;">
      <div style="line-height: 40px;text-align: center;font-size: x-large;font-weight: bold;padding-bottom: 20px">
        校内勤工助学管理系统
      </div>
      <el-card>
        <div slot="header">
          <span style="font-size: x-large;font-weight: bold">{{ userinfo.name }}</span>
          <span v-if="userinfo.role === '2'">　同学</span>
          <span v-if="userinfo.role === '3'">　老师</span>
          <span>，您好！</span>
          <el-button size="small" type="danger" @click="$router.push('/login')">退出登录</el-button>
        </div>
        <el-divider></el-divider>
        <span style="font-size: small;line-height: 25px">
                <span v-if="userinfo.role === '2'">当前你的资助认定状况为：
            <el-tag v-if="user.type === '3'">特别困难</el-tag>
            <el-tag v-if="user.type === '2'">困难</el-tag>
            <el-tag v-if="user.type === '1'">一般困难</el-tag>
            <el-tag v-if="user.type === ''">未认定</el-tag>
            <el-tag v-if="user.type === null">未认定</el-tag>
            <el-tag
                v-if="user.type === '0'">未认定</el-tag>，</span><span>当前勤工助学学年批次：{{
            xnNow.pcmc
          }}，{{ xnNow.zssm }}</span></span>
        <div>
          <el-button :disabled="stuType" type="primary" @click="add()">新增申请</el-button>
          <el-button style="margin-left: 5px" type="primary" @click="$router.push('/departPhone')">岗位信息</el-button>
          <el-button style="margin-left: 5px" type="primary" @click="load">刷新状态</el-button>
        </div>
      </el-card>

      <!--    数据区-->
      <span style="line-height: 60px;font-size: larger;font-weight: bold">申请查看</span>
      <el-card>
        <el-table :cell-style="{'text-align':'center'}" :data="tableData" :header-cell-style="{'text-align':'center'}"
                  border stripe
                  style="width:auto">
          <el-table-column label="申请学年" prop="sqxn" width="auto"/>
          <el-table-column label="审批进度" width="auto">
            <template #default="scope">
              <el-tag v-if="scope.row.speed === 'save'" type="info">学生暂存</el-tag>
              <el-tag v-if="scope.row.speed === 'remain'" type="normal">确认留任</el-tag>
              <el-tag v-if="scope.row.speed === 'doing'" type="warning">待审批</el-tag>
              <el-tag v-if="scope.row.speed === 'refuse'" type="danger">审批驳回</el-tag>
              <el-tag v-if="scope.row.speed === 'finish'" type="success">审批通过</el-tag>
            </template>
          </el-table-column>
          <el-table-column fixed="right" label="操作" width="auto">
            <template #default="scope">
              <div>
                <el-button size="small" type="primary" @click="handleEdit(scope.row)">修改查看</el-button>
                <span v-if="scope.row.speed === 'finish' || scope.row.speed === 'remain'">　打印申请表请登录电脑端</span>
              </div>
            </template>
          </el-table-column>
        </el-table>
      </el-card>
      <span style="line-height: 60px;font-size: larger;font-weight: bold">通知公告</span>
      <el-card>
        <el-collapse v-for="index in (notice.length)" :key="index" v-model="activeName" accordion>
          <el-collapse-item
              :name="index-1"
              :title=" '[' + notice[notice.length-index].source + '] ' + notice[notice.length-index].title">
            <div v-html="(notice[notice.length-index].detail)"></div>
          </el-collapse-item>
        </el-collapse>
      </el-card>
    </div>
    <div style="align-items: center">
      <div style="font-size: 15px; text-align: center; ">　</div>
      <div style="font-size: 15px; text-align: center; ">浙江·杭州</div>
      <div style="font-size: 15px; text-align: center; ">Copyright © 校大学生事务管理与服务中心</div>
      <div style="font-size: 15px; text-align: center; ">技术支持：学生管理科网络信息部</div>
      <div style="font-size: 15px; text-align: center; ">　</div>
    </div>
    <div>
      <el-dialog v-model="dialogVisible" width="100%">
        <el-form ref="form" :model="form" label-width="120px">
          <el-form-item label="申请人">{{ form.name }}（{{ form.username }}）</el-form-item>
          <el-form-item label="学院班级">{{ form.college }} {{ form.classname }}</el-form-item>
          <el-form inline label-width="120px">
            <el-form-item label="申请学年">
              <el-select v-model="form.sqxn" disabled placeholder="">
                <el-option label="2022-2023学年（第一批）" value="2022-2023-1"></el-option>
                <el-option label="2021-2022学年（第二批）" value="2021-2022-2"></el-option>
                <el-option label="2021-2022学年（第一批）" value="2021-2022-2"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="资助认定状态">
              <el-select v-model="form.type" disabled placeholder="">
                <el-option label="特别困难" value="3"></el-option>
                <el-option label="困难" value="2"></el-option>
                <el-option label="一般困难" value="1"></el-option>
                <el-option label="未认定" value="0"></el-option>
              </el-select>
            </el-form-item>
          </el-form>
          <el-form-item label="上学年勤工经历">
            <el-select v-model="form.lastdepart" :disabled="useDisabled" filterable placeholder="">
              <el-option
                  v-for="item in selectOptions"
                  :key="item.id"
                  :label="item.name"
                  :value="item.name"
              />
            </el-select>
          </el-form-item>
          <el-form inline label-width="120px">
            <el-form-item label="联系电话">
              <el-input v-model="form.phone" :disabled="useDisabled" style="width: 100%"></el-input>
            </el-form-item>
            <el-form-item label="QQ号">
              <el-input v-model="form.qq" :disabled="useDisabled" style="width: 100%"></el-input>
            </el-form-item>
          </el-form>
          <el-form-item label="个人技能">
            <el-input v-model="form.skill" :autosize="{ minRows: 4}" :disabled="useDisabled" style="width: 80%"
                      type="textarea"></el-input>
          </el-form-item>
          <el-form inline label-width="120px">
            <el-form-item label="申请岗位1">
              <el-select v-model="form.depart" :disabled="useDisabled" filterable placeholder="">
                <el-option
                    v-for="item in selectOptions"
                    :key="item.id"
                    :label="item.name"
                    :value="item.name"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="申请岗位2">
              <el-select v-model="form.departb" :disabled="useDisabled" filterable placeholder="">
                <el-option
                    v-for="item in selectOptions"
                    :key="item.id"
                    :label="item.name"
                    :value="item.name"
                />
              </el-select>
            </el-form-item>
          </el-form>
          <el-form-item label="调剂">
            <el-select v-model="form.choosetj" :disabled="useDisabled" placeholder="">
              <el-option label="是" value="true"></el-option>
              <el-option label="否" value="false"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="可用工作时长">
            单位：小时，填写整数，单日工作时长最长为8小时
          </el-form-item>
          <el-form :disabled="useDisabled" inline label-width="120px">
            <el-form-item label="　">周一　
              <el-input-number v-model="form.timea" :max="8" :min="0"></el-input-number>
            </el-form-item>
            <el-form-item label="　">周二　
              <el-input-number v-model="form.timeb" :max="8" :min="0"></el-input-number>
            </el-form-item>
            <el-form-item label="　">周三　
              <el-input-number v-model="form.timec" :max="8" :min="0"></el-input-number>
            </el-form-item>
            <el-form-item label="　">周四　
              <el-input-number v-model="form.timed" :max="8" :min="0"></el-input-number>
            </el-form-item>
            <el-form-item label="　">周五　
              <el-input-number v-model="form.timee" :max="8" :min="0"></el-input-number>
            </el-form-item>
            <el-form-item label="　">周六　
              <el-input-number v-model="form.timef" :max="8" :min="0"></el-input-number>
            </el-form-item>
            <el-form-item label="　">周日　
              <el-input-number v-model="form.timeg" :max="8" :min="0"></el-input-number>
            </el-form-item>
          </el-form>
        </el-form>
        <template #footer>
            <span class="dialog-footer">
                <el-button @click="dialogVisible = false">取消</el-button>
                <el-button :disabled="useDisabled" type="primary" @click="saveTemp">暂存</el-button>
                  <el-popconfirm title="提交后将无法修改，是否提交？" @confirm="saveFinish">
                    <template #reference>
                      <el-button :disabled="useDisabled" type="primary">提交</el-button>
                    </template>
                  </el-popconfirm>
            </span>
        </template>
      </el-dialog>
      <el-dialog v-model="detailVisible" title="申请表打印" width="100%">
        <div>
          <!--<el-button type="" @click="$router.go(-1)">返回</el-button>-->
          <el-button type="primary" @click="handlePrint(this.form.username,this.form.name,this.form.sqxn)">导出PDF
          </el-button>
          <span>建议通过电脑端登录导出。</span>
        </div>
        <div id="printDetail">
          <div
              style="font-weight: 700; text-align: center; font-size: 10px; font-family: 黑体;line-height: 2em;color: black">
            <p>浙江水利水电学院校内勤工助学岗位个人申请表（{{ this.form.sqxn }}学年）</p>
          </div>
          <div align="center" style="padding: 10px" width="730">
            <table
                style="text-align: center;line-height: 2em;font-size:8px;font-family:宋体;border-collapse:collapse;color: black;">
              <tbody>
              <tr style="height: 30px">
                <td style="border: 1px solid;width: 70px">姓名</td>
                <td style="border: 1px solid;width: 100px;padding: 5px;">{{ this.form.name }}</td>
                <td style="border: 1px solid;width: 50px">学号</td>
                <td style="border: 1px solid;width: 80px;padding: 5px;">{{ this.form.username }}</td>
                <td style="border: 1px solid;width: 50px">性别</td>
                <td style="border: 1px solid;width: 80px;padding: 5px;">{{ this.form.male }}</td>
              </tr>
              <tr style="height: 30px">
                <td style="border: 1px solid">学院</td>
                <td style="border: 1px solid;padding: 5px;">{{ this.form.college }}</td>
                <td style="border: 1px solid">班级</td>
                <td style="border: 1px solid;padding: 5px;">{{ this.form.classname }}</td>
                <td style="border: 1px solid">联系方式</td>
                <td style="border: 1px solid;padding: 5px;">{{ this.form.phone }}</td>
              </tr>
              <tr style="height: 30px">
                <td style="border: 1px solid">申请学生类别</td>
                <td colspan="5" style="border: 1px solid">
                  <span v-if="this.form.type == 0">资助对象未认定</span>
                  <span v-if="this.form.type == 1">一般资助对象（一般困难）</span>
                  <span v-if="this.form.type == 2">一般资助对象（困难）</span>
                  <span v-if="this.form.type == 3">特别资助对象（特别困难）</span>
                </td>
              </tr>
              <tr style="height: 30px;">
                <td style="border: 1px solid;padding: 5px;">个人工作<br>技能介绍</td>
                <td colspan="5" style="border: 1px solid;padding: 5px;text-align: left">{{ this.form.skill }}</td>
              </tr>
              <tr style="height: 30px">
                <td style="border: 1px solid;padding: 10px;">意向部门一</td>
                <td colspan="2" style="border: 1px solid">{{ this.form.depart }}</td>
                <td style="border: 1px solid;padding: 10px;">意向部门二</td>
                <td colspan="2" style="border: 1px solid">{{ this.form.departb }}</td>
              </tr>
              <tr style="height: 30px">
                <td style="border: 1px solid;padding: 10px;">工作时间</td>
                <td colspan="5" style="border: 1px solid">
                  <p>周一：8:00-16:30，可提供的工作时间 {{ this.form.timea }} 小时。</p>
                  <p>周二：8:00-16:30，可提供的工作时间 {{ this.form.timeb }} 小时。</p>
                  <p>周三：8:00-16:30，可提供的工作时间 {{ this.form.timec }} 小时。</p>
                  <p>周四：8:00-16:30，可提供的工作时间 {{ this.form.timed }} 小时。</p>
                  <p>周五：8:00-16:30，可提供的工作时间 {{ this.form.timee }} 小时。</p>
                </td>
              </tr>
              <tr style="height: 20px">
                <td style="border: 1px solid;padding: 10px;">二级学院<br>意见</td>
                <td colspan="5" style="border: 1px solid">
                  <p v-if="this.form.speed === 'finish'" style="line-height: 20px">情况属实，同意参加校内勤工助学。</p>
                  <p v-if="this.form.speed === 'doing'" style="line-height: 20px">（二级学院审核中）</p>
                  <p v-if="this.form.speed === 'save'" style="line-height: 20px">（学生暂未提交申请）</p>
                  <p v-if="this.form.speed === 'refuse'" style="line-height: 20px">不同意参加校内勤工助学</p>
                  <p style="text-align: right">负责人：　　　　　盖章：　　　{{ this.replyTime.year }} 年
                    {{ this.replyTime.month }} 月 {{ this.replyTime.day }} 日　　</p>
                </td>
              </tr>
              <tr style="height: 20px">
                <td style="border: 1px solid;padding: 10px;">用工部门<br>（单位）<br>聘任意见</td>
                <td colspan="5" style="border: 1px solid">
                  <p v-if="this.form.speed === 'finish'" style="line-height: 20px">同意该生参加校内勤工助学。</p>
                  <p v-if="this.form.speed != 'finish'" style="line-height: 20px">（暂未通过二级学院审核）</p>
                  <p style="text-align: right">负责人：　　　　　盖章：　　　　　 年　　月　　日　　</p>
                </td>
              </tr>
              <tr style="height: 20px">
                <td style="border: 1px solid;padding: 10px;">学生资助<br>管理中心<br>审核意见</td>
                <td colspan="5" style="border: 1px solid">
                  <p v-if="this.form.speed === 'finish'" style="line-height: 20px">同意该生参加校内勤工助学。</p>
                  <p v-if="this.form.speed != 'finish'" style="line-height: 20px">（暂未通过二级学院审核）</p>
                  <p style="text-align: right">学生处（代章）：　　　　　 年　　月　　日　　</p>
                </td>
              </tr>
              </tbody>
            </table>
            <div>　</div>
          </div>
        </div>
      </el-dialog>
    </div>
  </div>
</template>

<script>


import request from "../utils/request";
import moment from "moment"
import htmlToPdf from "../utils/htmlToPdf";

export default {
  name: 'HomeView',
  components: {},
  data() {
    return {
      form: {},
      userinfo: {},
      user: {},
      tk: '',
      xnNow: {},
      dialogVisible: false,
      detailVisible: false,
      search: '',
      currentPage: 1,
      total: 0,
      pageSize: 10,
      tableData: [],
      activeNames: '',
      detail: '',
      file: '',
      useDisabled: true,
      stuType: false,
      selectOptions: [],
      replyTime: {year: '', month: '', day: ''},
      notice: {},
      activeName: '1',
    }

  },
  created() {
    //用户信息获取语句
    let userStr = sessionStorage.getItem("user") || "{}"
    this.tk = JSON.parse(userStr)
    request.post("/user/tokenLogin", this.tk).then(res => {
      if (res.code === 200) {
        this.user = res.user
        this.cxStuByUsername(this.user.username)
        this.load()
      }
    })
    //用户信息获取语句结束
  },
  methods: {
    exp() {
      window.open("/api/qgsq/export")
    },
    viewDeatil(detail, file) {
      this.file = file
      this.detail = detail
      this.detailVisible = true
    },
    add() {
      this.getDepartmentName()
      this.form = {
        sqxn: this.xnNow.pcdm,
        username: this.userinfo.username,
        name: this.userinfo.name,
        college: this.userinfo.college,
        classname: this.userinfo.classname,
        type: this.userinfo.type,
        male: this.userinfo.male,
        phone: this.userinfo.phone,
        qq: this.userinfo.qq,
      }
      this.dialogVisible = true
      this.$nextTick(() => {
        this.useDisabled = false
        if (this.$refs['upload']) {
          this.$refs['upload'].clearFiles()
        }
      })
    },
    load() {
      request.get("/qgsq", {
        params: {
          pageNum: this.currentPage,
          pageSize: this.pageSize,
          search: this.user.username || 'null'
        }
      }).then(res => {
        console.log(res)
        this.tableData = res.data.records
        this.total = res.data.total
      })
      this.getTermInfo()
      this.getNotice()
    },
    reload() {
      this.cxStuByUsername()
      this.load()
      this.setButtonUse()
      this.$message({
        type: "success",
        message: "更新成功"
      })
    },
    getTermInfo() {
      request.get("/term/").then(res => {
        console.log("terminfo", res[res.length - 1])
        this.xnNow = res[res.length - 1]
        this.setButtonUse()
      })
    },
    getNotice() {
      request.get("/news/").then(res => {
        console.log("notice", res)
        this.notice = res
      })
    },
    setButtonUse() {
      // this.stuType = true 是禁用
      if (this.xnNow.dqzt === '0') {
        this.stuType = true
      } else if (this.xnNow.dqzt === '2') {
        this.stuType = false
      } else if (this.xnNow.dqzt === '1') {
        if (this.userinfo.type === '3') {
          this.stuType = false
        } else if (this.userinfo.type === '2') {
          this.stuType = false
        } else if (this.userinfo.type === '1') {
          this.stuType = false
        } else {
          this.stuType = true
        }
      }
      console.log("button", this.stuType)
    },
    printDetail(row) {
      this.form = JSON.parse(JSON.stringify(row))
      this.replyTime.year = this.form.replytime.slice(0, 4)
      this.replyTime.month = this.form.replytime.slice(5, 7)
      this.replyTime.day = this.form.replytime.slice(8, 10)
      this.detailVisible = true
    },
    handlePrint(num, name, xn) {
      let pdfname = num + name + '_' + xn + '学年校内勤工助学岗位个人申请表'
      htmlToPdf.downloadPDF(document.querySelector('#printDetail'), pdfname)
      this.detailVisible = false
      this.$message.success("导出成功！")
    },
    save() {
      this.$refs['form'].validate((valid) => {
        if (valid) {
          if (this.form.id) {
            request.put("/qgsq", this.form).then(res => {
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
            request.post("/qgsq", this.form).then(res => {
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
    saveTemp() {
      this.form.speed = "save"
      if (this.form.uptime == null) this.form.uptime = moment(new Date()).format("YYYY-MM-DD hh:mm:ss")
      this.save()
    },
    saveFinish() {
      this.form.speed = "doing"
      this.form.uptime = moment(new Date()).format("YYYY-MM-DD hh:mm:ss")
      this.save()
    },
    savepass(id) {
      this.form = {
        id: id,
        speed: "finish",
        replytime: moment(new Date()).format("YYYY-MM-DD hh:mm:ss"),
        replyname: this.user.name,
        replynum: this.user.username
      }
      request.put("/qgsq", this.form).then(res => {
        console.log(res)
        if (res.code === '0') {
          this.$message({
            type: "success",
            message: "更新成功"
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
    saverefuse(id) {
      this.form = {
        id: id,
        speed: "refuse",
        replytime: moment(new Date()).format("YYYY-MM-DD hh:mm:ss"),
        replyname: this.user.name,
        replynum: this.user.username
      }
      request.put("/qgsq", this.form).then(res => {
        console.log(res)
        if (res.code === '0') {
          this.$message({
            type: "success",
            message: "更新成功"
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
    handleEdit(row) {
      this.form = JSON.parse(JSON.stringify(row))
      if (this.form.replyname == null) this.form.replyname = this.user.name
      if (this.form.replynum == null) this.form.replynum = this.user.username
      if (this.form.speed == "finish" || this.form.speed == "doing" || this.form.speed == "remain") {
        this.useDisabled = true
      } else {
        this.useDisabled = false
      }
      this.getDepartmentName()
      this.dialogVisible = true
      this.$nextTick(() => {
        if (this.$refs['upload']) {
          this.$refs['upload'].clearFiles()
        }
      })
    },
    handleDelete(id) {
      console.log(id)
      request.delete("/qgsq/del/" + id).then(res => {
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
    handleSizeChange(pageSize) {
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum) {
      this.pageNum = pageNum
      this.load()
    },
    filesUploadSuccess(res) {
      console.log("img", res)
      if (res.status == "success") {
        this.$message.success("附件上传成功")
        this.form.replyfile = res.realUrl
      } else {
        this.$message({
          type: "error",
          message: res.realUrl
        })
      }
    },
    getDepartmentName() {
      request.get("/department/").then((res) => {
        console.log(res)
        this.selectOptions = res;
      })
    },
    cxStuByUsername(username) {
      request.get("/user/username/" + username).then(res => {
        console.log("userinfo", res)
        this.userinfo = res.data
      })
    },
  }
}
</script>

<style>


</style>