<script setup>
import {Document, Lock, Setting, UserFilled} from '@element-plus/icons-vue'
</script>

<template>
  <el-col>
    <el-menu
        active-text-color="#ffd04b"
        background-color="#ffffff"
        class="el-menu-vertical-demo"
        default-active="2"
        text-color="#000000"
        @open="handleOpen"
        @close="handleClose"
        style="width:250px"
    >
      <el-menu-item index="1" >
        <el-icon><UserFilled /></el-icon>
        <span class="fontStyle">个人信息</span>
      </el-menu-item>
      <el-menu-item index="2">
        <el-icon><Lock /></el-icon>
        <span class="fontStyle">安全隐私</span>
      </el-menu-item>
      <el-menu-item index="3">
        <el-icon><document /></el-icon>
        <span class="fontStyle">论文管理</span>
      </el-menu-item>
      <el-menu-item index="4">
        <el-icon><setting /></el-icon>
        <span class="fontStyle">成果统计</span>
      </el-menu-item>
    </el-menu>
  </el-col>
</template>

<style scoped>
.fontStyle{
  font-size: larger;
  text-align: center;
  display: flex;
  align-items: center;
}
</style>