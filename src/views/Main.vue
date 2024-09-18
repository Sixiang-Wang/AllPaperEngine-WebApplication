<script setup>
import {onMounted, ref} from "vue";
  const searchType = ref("主题");

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
</script>

<template>
<div class="input">
  <div style="margin-bottom: 5%">
    <h2 class="title">{{displayedText}}</h2>
  </div>
  <div style="background-color: transparent !important;">
  <el-input style="height: 40px; max-width: 60%;" placeholder="请输入搜索内容">
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
  :deep(.el-input__wrapper){
    background-color:rgba(0,0,0,0);
  }
  :deep(.el-input-group__prepend){
    background-color:rgba(0,0,0,0);
  }
  :deep(.el-input__inner) {
    background-color: rgba(0, 0, 0, 0) !important;
    color: #fff;
  }
/*不要碰这里的代码！别放到.css中*/
  :deep(.el-select__wrapper){
    height: 40px;
  }

</style>