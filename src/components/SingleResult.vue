<script setup>

import {Compass, Star} from "@element-plus/icons-vue";

defineProps({
  title: String,
  author: String,
  content: String,
  cited: String
})
</script>

<template>
  <div style="display: flex; flex-direction: column; max-width: 90%;">
    <span class="search-result-title component">{{ title }}</span>
    <span class="search-result-author component">{{ author }}</span>
    <span class="search-result-content component">{{ content }}</span>
    <div style="display: flex">
      <div class="component search-result-bottom" style="display:flex;align-items: center">
        <el-icon><Star/></el-icon>
        <span>收藏</span>
      </div>
      <div class="component search-result-bottom" style="display:flex;align-items: center">
        <el-icon><Compass/></el-icon>
        <span>引用</span>
      </div>
      <div class="component search-result-bottom" style="display:flex;align-items: center">
        <span>引用次数：{{cited}}</span>
      </div>
      <div>

      </div>
    </div>
    <el-divider/>
  </div>
</template>

<style scoped>
@import "@/css/searchResult.css";

:deep(.el-divider--horizontal) {
  margin: 12px 0;
}
</style>