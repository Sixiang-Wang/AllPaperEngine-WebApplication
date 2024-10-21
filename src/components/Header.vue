<script setup xmlns="http://www.w3.org/1999/html">
import {computed, onMounted, ref, watch} from "vue";
import router from "@/router/index.js";
import defaultAvatar from "@/assets/image/user.gif";
import { useRoute } from 'vue-router';
import cookieUtil from "@/utils/cookie.js"
const button_index = ref("登录");
const user_name = ref(cookieUtil.getCookie("username"));
const back = ()=> {
  router.push('/main');
}
const goToUserInfo = () => {
  router.push('/user/info');
}
const avatar = ref({
  defaultAvatar: defaultAvatar,
  url : defaultAvatar
})
const route = useRoute();
const textColor = computed(() => {
  return route.path === '/main' ? '#000000' : '#000000'; // 选择颜色
});
const goToLogin = () => {
  if(cookieUtil.getCookie("token")===null||cookieUtil.getCookie("token")===''){
    router.push('/login');
  }else{
    cookieUtil.deleteCookie("token");
    cookieUtil.deleteCookie("username");
    location.reload();
  }
}
onMounted(() =>{
  if(cookieUtil.getCookie("token")===null || cookieUtil.getCookie("token")===''){
    button_index.value = "登录";
  }else{
    button_index.value = "退出";
  }
})
watch(cookieUtil.getCookie("username"),(oldValue,newValue)=> {
  user_name.value = newValue;
  console.log(newValue);
})
</script>

<template>
  <el-menu
      :default-active="activeIndex"
      class="el-menu"
      mode="horizontal"
      @select="handleSelect"
      :text-color="textColor"
      style="height: 100%;"
      router
      :ellipsis="false"
  >
    <div class="header-menu-left" @click="back">
      <img src="@/assets/image/logo.png" style="width: 45px; height: 45px;margin-top: 10px"/>
      <div style="font-size:9.5px;font-family: espano;text-align: center;margin-top: -13px;color: #06008c;user-select: none">
        AllPaper
        <br>
        Engine
      </div>
    </div>


    <el-sub-menu index="2">
      <template #title>读者服务</template>
      <el-menu-item index="2-1">item one</el-menu-item>
      <el-menu-item index="2-2">item two</el-menu-item>
      <el-menu-item index="2-3">item three</el-menu-item>
    </el-sub-menu>
    <el-sub-menu index="3">
      <template #title>作者服务</template>
      <el-menu-item index="/personalportal">个人门户</el-menu-item>
      <el-menu-item index="/personalportal/academicClaim">学术成果认领</el-menu-item>
    </el-sub-menu>
    <el-menu-item index="4">热点分析</el-menu-item>
    <el-menu-item index="5">联系我们</el-menu-item>
    <div class="header-menu-right" >
      <el-avatar :src="avatar.url" shape="circle" class="user-avatar" @click="goToUserInfo"></el-avatar>
      <span :style="{color: textColor}" v-if="user_name.value===''">欢迎, {{ user_name }}</span>
      <span :style="{color: textColor}" v-else>点此登录</span>
      <el-button @click="goToLogin"
                 style="margin-right: 30px;background-color: transparent">
        {{ button_index }}
      </el-button>
    </div>
  </el-menu>
  <div class="h-6"/>
</template>

<style scoped>
@import "@/css/header.css";

.el-menu--horizontal > .el-menu-item:nth-child(5) {
  margin-right: auto;
}

</style>