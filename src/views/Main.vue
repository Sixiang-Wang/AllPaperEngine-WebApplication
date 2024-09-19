<script setup>
import {onMounted, ref} from "vue";
import {Search} from '@element-plus/icons-vue'
  const searchType = ref("主题");
  const searchInput = ref("");
  const fullText = "Ciallo, World～(∠・▽< )⌒☆";
  const displayedText = ref(""); // 动态显示的文字

  onMounted(() => {
    let index = 0;
    const interval = setInterval(() => {
      if (index < fullText.length) {
        displayedText.value += fullText[index];
        index++;
      } else {
        clearInterval(interval); // 打字完成后停止
      }
    }, 150); // 设置打字速度，150ms显示一个字符
  });

const clickSearch = () => {

};
</script>

<template>
<div class="input">
  <div style="margin-bottom: 5%">
    <h2 class="title">{{displayedText}}</h2>
  </div>
  <div style="background-color: transparent !important;">
  <el-input v-model="searchInput" class="search-input" placeholder="请输入搜索内容">
    <template #prepend>
      <el-select v-model="searchType" style="width: 115px">
        <el-option label="主题" value="1" />
        <el-option label="篇名" value="2" />
        <el-option label="关键词" value="3" />
      </el-select>
    </template>

  </el-input>
    <el-button :icon="Search" @click="clickSearch" class="search-button"  />
  </div>
</div>
</template>

<style scoped>
  @import "@/css/main.css";
</style>