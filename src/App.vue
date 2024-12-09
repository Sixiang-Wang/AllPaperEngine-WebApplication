<script setup>
import {computed, onMounted} from 'vue';
import { useRoute } from 'vue-router';
import Header from '@/components/Header.vue';

const route = useRoute();

const isSpecificRoute = computed(() => {
  // 根据需要替换成你的特定路由
  return route.path === '/main'||route.path === '/login'||route.path === '/register';
});
const isLogin = computed(()=> {
  return route.path === '/login';
})
const isRegister = computed(()=> {
  return route.path === '/register';
})
onMounted(()=>{
  // 检测浏览器路由改变页面不刷新问题,hash模式的工作原理是hashchange事件
  window.addEventListener('hashchange', () => {
    console.log(window.location.hash,'window.location.hash')
    let currentPath = window.location.hash.slice(1)
    console.log(currentPath,'currentPath')
    if (this.$route.path !== currentPath) {
      this.$router.push(currentPath)
    }
  }, false)
})
const key = computed(() => {
  return route.path + Math.random();
});
</script>

<template>
  <div :class="['common-layout', { background: isSpecificRoute}]" style="margin:0;padding: 0;">
    <div v-if="isLogin" class="backgroundBlur"></div>
    <div v-if="isRegister" class="backgroundBlur"></div>
    <el-container style="margin:0;padding: 0;z-index: 1">
      <el-header height="80px" style="margin:0;padding: 0;">
        <Header/>
      </el-header>
      <el-main style="margin:0;padding: 0;"><router-view :key="key"/></el-main>
    </el-container>
  </div>

</template>

<style scoped>
@import "@/css/basic.css";
@font-face {
  font-family: "espano";
  src: url('./assets/font/espano.ttf');
  font-weight: normal;
  font-style: normal;
}
</style>
