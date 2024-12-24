<script setup>

import {Compass, Star} from "@element-plus/icons-vue";
import router from "@/router/index.js";
import {useRoute} from "vue-router";
import httpUtil from "@/api/http.js";
import {ElMessage} from "element-plus";

defineProps({
  title: String,
  author: String,
  content: String,
  cited: String,
  id: String
})
const route = useRoute();
const userId = localStorage.getItem("userId");
const goToPaper = (id)=> {
  // console.log(id)
  router.push({path: '/paper', query: {id: id, input: route.query.input}})
}
const submit = async () => {
  const res = await httpUtil.get('/claim/add',{
    "userId": userId.value,
    "workId": id
  })
  console.log(res.data);
  ElMessage.success("申领成功！");
  location.reload();
}
</script>

<template>
  <div style="display: flex; flex-direction: column; max-width: 70%;">
    <!-- <span class="search-result-title component" @click="goToPaper(id)">{{ title }}</span>
    <span class="search-result-author component">{{ author }}</span>
    <span class="search-result-content component multiline-ellipsis">{{ content }}</span> -->
    <!-- 添加对title、content的html渲染以便直接突出高亮词 -->
    <span class="search-result-title component" @click="goToPaper(id)" v-html="title"></span>
    <span class="search-result-author component" v-html="author"></span>
    <span class="search-result-content component multiline-ellipsis" v-html="content"></span>
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
    <div style="margin-left: auto;">
      <el-button type="primary" @click="submit">认领此文</el-button>
    </div>
    <el-divider/>
  </div>

</template>

<style scoped>
@import "@/css/searchResult.css";

:deep(.el-divider--horizontal) {
  margin: 12px 0;
}
.multiline-ellipsis {
  display: -webkit-box;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
  -webkit-line-clamp: 2; /* 显示两行 */
  line-height: 1.5em;    /* 每行的高度 */
  max-height: 3em;       /* 最大高度，两行的行高总和 */
}
</style>