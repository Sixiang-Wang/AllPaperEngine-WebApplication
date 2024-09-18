<script setup>
import {ArrowRight} from "@element-plus/icons-vue";
import * as https from "node:https";
import defaultAvatar from "@/assets/image/user.gif";
import {ref} from "vue";
import * as url from "node:url";

const avatar = ref({
  defaultAvatar: defaultAvatar,
  url : defaultAvatar
})

const tableData = [
  {
    feature: '姓名',
    value: '下北泽第一突破手后藤独',
  },
  {
    feature: '生日',
    value: '2007-02-21',
  },
  {
    feature: '性别',
    value: '保密',
  },
]

const tableData2 = [
  {
    feature: '学术领域',
    value: '软件工程',
  },
  {
    feature: '在职单位',
    value: '南京航空航天大学',
  },
  {
    feature: '职业',
    value: '教授',
  },
]

const tableData3 = [
  {
    feature: '电子邮件',
    value: '0d000721@163.com',
  },
  {
    feature: '电话',
    value: '12345678910',
  },
]
</script>

<template>

  <el-card style="max-width: 800px" >
    <div class="card-header">
      <span>基本信息</span>
    </div>

    <br>
    <div style="display: flex; align-items: center;">
      <p class="text-item">个人资料照片</p>
      <el-avatar
          :src=avatar.url
          :size="70"
          shape="square"
          fit="cover"
          class="user-avatar">
      </el-avatar>
    </div>
    <el-divider style="margin: 0"/>
    <el-table :data="tableData" style="width: 100%" :show-header="false">
      <el-table-column prop="feature" label="Feature" width="100" />
      <el-table-column prop="value" label="Value" width="600" />
      <el-table-column width="50px">
        <template #default="scope">
          <arrow-right style="width: 25px; height: 25px;"/>
        </template>
      </el-table-column>
    </el-table>
  </el-card>

  <el-card style="max-width: 800px;margin-top: 45px" >
    <div class="card-header">
      <span>学术信息</span>
    </div>
    <br>
    <el-table :data="tableData2" style="width: 100%" :show-header="false">
      <el-table-column prop="feature" label="Feature" width="100" />
      <el-table-column prop="value" label="Value" width="600" />
      <el-table-column width="50px">
        <template #default="scope">
          <arrow-right style="width: 25px; height: 25px;"/>
        </template>
      </el-table-column>
    </el-table>
  </el-card>

  <el-card style="max-width: 800px;margin-top: 45px" >
    <div class="card-header">
      <span>联系信息</span>
    </div>
    <br>
    <el-table :data="tableData3" style="width: 100%" :show-header="false">
      <el-table-column prop="feature" label="Feature" width="100" />
      <el-table-column prop="value" label="Value" width="600" />
      <el-table-column width="50px">
        <template #default="scope">
          <arrow-right style="width: 25px; height: 25px;"/>
        </template>
      </el-table-column>
    </el-table>
  </el-card>
</template>


<style scoped>
@import "@/css/user.css";
.el-table__body tr:last-child{
  border-bottom: none !important;
}
</style>