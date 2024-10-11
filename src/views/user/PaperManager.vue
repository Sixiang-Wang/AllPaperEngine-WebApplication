
<template>
  <div style="display: flex">
    <div>
      <el-card style="max-width: 800px">
        <div class="card-header">
          <span>统计</span>
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
        <div class="card-header vertical-center">
          <span>您的论文</span>
          <el-icon style="margin-left: 10px"><CirclePlus /></el-icon>
        </div>
        <el-table :data="paperData" style="width: 100%" show-header="false">
          <el-table-column prop="feature" width="700px"></el-table-column>
          <el-table-column width="70px">
            <template #default="scope">
              <arrow-right style="width: 25px; height: 25px;"/>
            </template>
          </el-table-column>
        </el-table>
      </el-card>
    </div>
    <div style="margin-left: 5%">
      <Advertise/>
    </div>
  </div>
</template>
<script setup>

import {ArrowRight, CirclePlus} from "@element-plus/icons-vue";
import Advertise from "@/components/Advertisement.vue";
const tableData = [
  {
    feature: "论文篇数",
    value: 114
  },
  {
    feature: "被引用次数",
    value: 514
  }
]
const paperData = [
  {
    feature: "Blended Diffusion for Text-driven Editing of Natural Images"
  },
  {
    feature: "Prompt-to-Prompt Image Editing with Cross Attention Control"
  },
  {
    feature: "DiffEdit: Diffusion-based semantic image editing with mask guidance"
  }

]
</script>
<style scoped>
@import "@/css/user.css";
@import "@/css/basic.css";
</style>