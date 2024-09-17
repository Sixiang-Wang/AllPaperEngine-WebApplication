<script setup>
import Header from "./components/Header.vue";
</script>

<template>
  <div class="common-layout" style="margin:0;padding: 0;">
    <el-container style="margin:0;padding: 0;">
      <el-header height="80px" style="margin:0;padding: 0;">
        <Header/>
      </el-header>
      <el-main style="margin:0;padding: 0;"><router-view/></el-main>
    </el-container>
  </div>
</template>

<style scoped>
</style>
