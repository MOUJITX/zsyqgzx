<template>
  <div style="padding: 10px">
    <!--    功能区-->
    <div style="margin: 10px 0">
      <el-input v-model="search" clearable placeholder="请输入部门名称" style="width: 20%;"></el-input>
      <el-button style="margin-left: 5px" type="primary" @click="load">搜索</el-button>
    </div>
    <!--    数据区-->
    <el-table :data="tableData" :header-cell-style="{'text-align':'center'}" border stripe style="width:auto">
      <el-table-column label="部门编号" prop="number" width="100"/>
      <el-table-column label="部门名称" prop="name" width="200"/>
      <el-table-column label="部门说明" prop="detail" width="200"/>
      <el-table-column label="录用人数">
        <el-table-column label="钱塘校区" prop="qtrs" width="100"/>
        <el-table-column label="南浔校区" prop="nxrs" width="100"/>
      </el-table-column>
      <el-table-column label="岗位说明" prop="gwsm" width="auto"/>
      <el-table-column label="用工需求" prop="pytj" width="auto"/>
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
    handleSizeChange(pageSize) {
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum) {
      this.pageNum = pageNum
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