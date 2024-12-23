<script setup>
import router from "@/router/index.js";
import {useRoute} from "vue-router";

const props = defineProps({
  id: String,
  title: String,
  date: String
})
const route = useRoute();
const goToPaper = (id)=>{
  console.log(props);
  router.push({path: '/paper', query: {id: id, input: route.query.input}})
}
</script>

<template>
  <div>
    <el-divider/>
    <span class="recommend-text" @click="goToPaper(id)">{{ props.title }}</span>
    <span class="recommend-date">{{ props.date }}</span>
  </div>
</template>

<style scoped>
.recommend-text {
  font-size: 16px;
  font-weight: normal;
  max-width: 300px;
  display: -webkit-box;        /* 使用 flexbox 样式的变种 */
  -webkit-box-orient: vertical; /* 设置为垂直方向 */
  -webkit-line-clamp: 2;       /* 设置显示的最大行数为2行 */
  overflow: hidden;            /* 隐藏超出部分 */
  text-overflow: ellipsis;     /* 省略符号 (...) */
  transition: 0.5s;
}
.recommend-text:hover{
  color: #1F578F;
  cursor: pointer;
  text-decoration-line: underline;
}
.recommend-date {
  color: #7a7a7a;
  font-size: 12px;
  margin-top: 1%;
}
</style>