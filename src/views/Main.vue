<script setup>
  import {ref} from "vue";

  const searchType = ref("主题");
</script>

<template>
<div class="input">
  <el-input style="height: 40px" placeholder="请输入搜索内容">
    <template #prepend>
      <el-select v-model="searchType" style="width: 115px">
        <el-option label="主题" value="1" />
        <el-option label="篇名" value="2" />
        <el-option label="关键词" value="3" />
      </el-select>
    </template>
  </el-input>
</div>
</template>

<style scoped>
  @import "@/css/main.css";
</style>