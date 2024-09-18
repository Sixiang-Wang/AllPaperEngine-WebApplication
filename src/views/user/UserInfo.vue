<script setup>
import {ArrowRight} from "@element-plus/icons-vue";

const tableData = [
  {
    feature: '姓名',
    value: '呆唯今天学习了吗',
  },
  {
    feature: '生日',
    value: '2011-1-1',
  },

]
</script>

<template>

  <el-card style="max-width: 800px" >
    <div class="card-header">
      <span>基本信息</span>
    </div>

    <br>
    <p class="text-item">个人资料照片</p>
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
</template>


<style scoped>
@import "@/css/user.css";
.el-table__body tr:last-child{
  border-bottom: none !important;
}
</style>