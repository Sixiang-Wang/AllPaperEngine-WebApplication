<script setup>
import { computed } from 'vue';
import { useRoute } from 'vue-router';
import Header from '@/components/Header.vue';

const route = useRoute();

const isSpecificRoute = computed(() => {
  // 根据需要替换成你的特定路由
  return route.path === '/main'||route.path === '/login';
});
const isLogin = computed(()=> {
  return route.path === '/login';
})
</script>

<template>
  <div :class="['common-layout', { background: isSpecificRoute}]" style="margin:0;padding: 0;">
    <div v-if="isLogin" class="backgroundBlur"></div>
    <el-container style="margin:0;padding: 0;z-index: 1">
      <el-header height="80px" style="margin:0;padding: 0;">
        <Header/>
      </el-header>
      <el-main style="margin:0;padding: 0;"><router-view/></el-main>
    </el-container>
  </div>

</template>

<style scoped>
@import "@/css/basic.css";
</style>
