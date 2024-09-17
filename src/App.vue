<script setup>
import Header from "./components/Header.vue";
</script>

<template>
  <div class="common-layout">
    <el-container>
      <el-header height="100px">
        <Header/>
      </el-header>
      <el-main><router-view/></el-main>
    </el-container>
  </div>
</template>

<style scoped>
</style>
