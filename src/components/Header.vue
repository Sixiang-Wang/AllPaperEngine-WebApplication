<script setup>
import {ref} from "vue";
import router from "@/router/index.js";
import defaultAvatar from "@/assets/image/user.gif";

const button_index = ref("登录");
const user_name = ref("World～(∠・▽< )⌒☆");
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
</script>

<template>
  <el-menu
      :default-active="activeIndex"
      class="el-menu-demo"
      mode="horizontal"
      @select="handleSelect"
      style="height: 100%;"
      :ellipsis="false"
  >
    <div class="header-menu-left" @click="back">
      <img src="@/assets/image/logo.gif" style="width: 50px; height: 50px;"/>
    </div>
    <el-sub-menu index="2">
      <template #title>读者服务</template>
      <el-menu-item index="2-1">item one</el-menu-item>
      <el-menu-item index="2-2">item two</el-menu-item>
      <el-menu-item index="2-3">item three</el-menu-item>
    </el-sub-menu>
    <el-sub-menu index="3">
      <template #title>作者服务</template>
      <el-menu-item index="3-1">论文上传</el-menu-item>
      <el-menu-item index="3-2">学术成果认领</el-menu-item>
    </el-sub-menu>
    <el-menu-item index="4">联系我们</el-menu-item>
    <div class="header-menu-right">
      <el-avatar :src="avatar.url" shape="circle" class="user-avatar" @click="goToUserInfo"></el-avatar>
      <span>Ciallo, {{ user_name }}</span>
      <el-button style="margin-right: 30px">{{ button_index }}</el-button>
    </div>
  </el-menu>
  <div class="h-6"/>
</template>

<style scoped>
@import "@/css/header.css";

.el-menu--horizontal > .el-menu-item:nth-child(4) {
  margin-right: auto;
}
</style>