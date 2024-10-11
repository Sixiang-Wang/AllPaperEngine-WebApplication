<script setup>
import {onMounted, ref, watch} from "vue";
import router from "@/router/index.js";
import {Search} from '@element-plus/icons-vue'
import cookieUtil from "@/utils/cookie.js"
  const searchType = ref("主题");
  const searchInput = ref("");
  const fullText = "Ciallo, World～(∠・▽< )⌒☆";
  const displayedText = ref(""); // 动态显示的文字
  const search = () => {
    console.log(111);
    router.push({path: "/search", query: {input: searchInput.value, page: 1}});
  }
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
    <el-button :icon="Search" @click="search" class="search-button"  />
  </div>
</div>
</template>

<style scoped>
  @import "@/css/main.css";
  :deep(.el-input-group__append) {
    background-color: transparent !important;
    color: rgba(255, 255, 255, 0.7);
  }
  :deep(.el-input__wrapper){
    background-color:rgba(0,0,0,0) !important;
    --el-input-focus-border-color:#FFFFFF;
  }
  :deep(.el-input-group__prepend){
    background-color:rgba(0,0,0,0);
  }
  :deep(.el-input__inner) {
    background-color: rgba(0, 0, 0, 0) !important;
    color: #fff;
  }
  :deep(.el-input__inner::placeholder) {
    color: rgba(255, 255, 255, 0.7) !important;
    cursor: pointer;
  }
  :deep(.el-select__selected-item) {
    color: rgba(255, 255, 255, 0.7) !important;
    cursor: pointer;
  }
/*不要碰这里的代码！别放到.css中*/
  :deep(.el-select__wrapper){
    height: 40px;
    --el-input-focus-border-color: #FFFFFF;
  }
</style>