<script setup>
import {ArrowRight} from "@element-plus/icons-vue";
import Advertise from "@/components/Advertisement.vue";

const tableData = [
  {
    feature: "账号名",
    value: "1145141919810"
  },
  {
    feature: "密码",
    value: "**********"
  },
  {
    feature: "密保问题",
    value: ""
  },
  {
    feature: "电子邮箱",
    value: "912948333@qq.com"
  }
]
const tableData2 = [
  {
    feature: "个人信息公开"
  },
  {
    feature: "登录时邮箱验证"
  }
]
</script>

<template>
  <div style="display: flex">
    <div>
      <el-card style="max-width: 800px">
        <div class="card-header">
          <span>账号安全</span>
        </div>
        <el-table :data="tableData" style="width: 100%" show-header="false">
          <el-table-column prop="feature" width="100px"></el-table-column>
          <el-table-column prop="value" width="600px"></el-table-column>
          <el-table-column width="50px">
            <template #default="scope">
              <arrow-right style="width: 25px; height: 25px;"/>
            </template>
          </el-table-column>
        </el-table>
      </el-card>
      <el-card style="max-width: 800px;margin-top: 5%">
        <div class="card-header">
          <span>设置</span>
        </div>
        <el-table :data="tableData2" style="width: 100%" show-header="false">
          <el-table-column prop="feature" width="700px"></el-table-column>
          <el-table-column prop="value" width="70px">
            <template #default="scope">
              <el-switch
                  v-model="scope.row.value"
                  class="ml-2"
                  style="--el-switch-on-color: #13ce66;"
              />
            </template>
          </el-table-column>
        </el-table>
      </el-card>
    </div>
    <div style="margin-left: 5%">
<!--      <Advertise/>-->
    </div>
  </div>
</template>

<style scoped>
@import "@/css/user.css";
@import "@/css/basic.css";
</style>