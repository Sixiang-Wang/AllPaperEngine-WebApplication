<template>

  <UserAside></UserAside>

  <div class="common-layout">
    <el-container>
      <el-aside width="200px">Aside</el-aside>
      <el-main>Main</el-main>
    </el-container>
  </div>
</template>

<script setup>

import UserAside from "@/components/UserAside.vue";
</script>

<style scoped>
.el-menu-vertical-demo{
  margin-left: 0;
  margin-right: 0;

}
</style>