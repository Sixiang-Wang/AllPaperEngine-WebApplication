<script setup>
  import {ref} from "vue";

  const searchType = ref("主题");
</script>

<template>
<div class="input">
  <div style="margin-bottom: 5%">
    <h2 class="title">Make Academic Visible</h2>
  </div>
  <div>
  <el-input style="height: 40px; max-width: 60%" placeholder="请输入搜索内容">
    <template #prepend>
      <el-select v-model="searchType" style="width: 115px">
        <el-option label="主题" value="1" />
        <el-option label="篇名" value="2" />
        <el-option label="关键词" value="3" />
      </el-select>
    </template>
  </el-input>
  </div>
</div>
</template>

<style scoped>
  @import "@/css/main.css";
</style>