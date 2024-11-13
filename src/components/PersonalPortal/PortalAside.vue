<script setup>

</script>

<template>
  <div class="portal-aside-head">
    <span class="portal-aside-title">个人门户系统</span>
  </div>
  <el-menu
      :default-active="$route.path"
      active-text-color="#000000"
      background-color="#ffffff"
      class="el-menu-vertical-demo"
      text-color="#000000"
      @open="handleOpen"
      @close="handleClose"
      router
      style="width:250px;border-right: none;box-shadow: none"
  >
    <el-menu-item index="/personalportal/personalInfo">
      <span>个人主页</span>
    </el-menu-item>
    <el-menu-item index="/user/academicClaim">
      <span>成果认领</span>
    </el-menu-item>
    <el-menu-item index="/personalportal/otherService">
      <span>增值服务</span>
    </el-menu-item>
  </el-menu>
</template>

<style scoped>
@import "@/css/portalaside.css";
</style>