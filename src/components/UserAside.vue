<template>
  <el-col>
    <el-menu
        active-text-color="#000000"
        background-color="#ffffff"
        class="el-menu-vertical-demo"
        default-active="2"
        text-color="#000000"
        @open="handleOpen"
        @close="handleClose"
        style="width:250px;border-right: none;box-shadow: none"
    >
      <el-menu-item index="1">
        <el-icon class="aside-icon"><User /></el-icon>
        <span class="fontStyle">个人信息</span>
      </el-menu-item>
      <el-menu-item index="2">
        <el-icon class="aside-icon"><Lock /></el-icon>
        <span class="fontStyle">安全隐私</span>
      </el-menu-item>
      <el-menu-item index="3">
        <el-icon class="aside-icon"><document /></el-icon>
        <span class="fontStyle">论文管理</span>
      </el-menu-item>
      <el-menu-item index="4">
        <el-icon class="aside-icon"><data-line/></el-icon>
        <span class="fontStyle">成果统计</span>
      </el-menu-item>
    </el-menu>
  </el-col>
</template>


<script setup>
import {DataLine, Document, Lock, Setting, User} from '@element-plus/icons-vue'

const handleOpen = (key, keyPath) => {
  console.log(key, keyPath)
}
const handleClose = (key, keyPath) => {
  console.log(key, keyPath)
}
</script>


<style scoped>
.fontStyle {
  font-size: medium;
  display: flex;
  margin-right: 20px;
}

.aside-icon {
  width: 2em;
}

.el-menu-item {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 50px; /* 调整菜单栏目的高度 */
  line-height: 50px; /* 确保文字垂直居中 */
  padding: 0 20px; /* 调整左右内边距，使内容更居中 */
  border-radius: 0 26px 26px 0;
}

.el-menu-item:hover {
  font-size: medium;
}

.el-menu-item.is-active{
  background-color: #C3EEFD;
}

</style>