<script setup xmlns="http://www.w3.org/1999/html">
import {computed, nextTick, onMounted, ref, watch} from "vue";
import router from "@/router/index.js";
import defaultAvatar from "@/assets/image/user.gif";
import { useRoute } from 'vue-router';
import cookieUtil from "@/utils/cookie.js"
import {ElMessage} from "element-plus";
import MessageCenter from "@/views/MessageCenter.vue";
import * as httpUtil from "@/api/http.js";
import http from "@/api/http.js";
import {useTokenStore, useUserStore, useUserIdStore} from "@/store/store.js";


const haveAvatar = ref(false)

const avatarUrl = computed(()=>{
  const avatar = localStorage.getItem('avatar') || '/hahashenmedoumeiyou';
  return http.getUrlWithoutSlash() + avatar;
});


const button_index = ref("登录");
const userStore = useUserStore();
const tokenStore = useTokenStore()
const userIdStore = useUserIdStore()
const userId = computed(()=>userIdStore.userId)
const user_name = computed(() => userStore.username);
const back = ()=> {
  router.push('/main');
}
const goToUserInfo = () => {
  if(cookieUtil.getCookie("token") === null || cookieUtil.getCookie("token") === ''){
    ElMessage.error("请先登录！");
    setTimeout(()=>{
      router.push('/login');
    },500);
    return;
  }
  router.push('/user/info');
}
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
    localStorage.removeItem('avatar');
    localStorage.removeItem('token');
    localStorage.removeItem('username');
    localStorage.removeItem('userId');
    localStorage.removeItem('authorId');
    ElMessage.success("已退出登录！");
    router.push('/main');
    location.reload();
  }
}
const goToMessage = ()=>{
  if(cookieUtil.getCookie("token")===null||cookieUtil.getCookie("token")===''){
    ElMessage.warning("请先登录！");
    setTimeout(()=>{
      router.push('/login');
    },500);
  }else{
    router.push('/message');
  }
}
const isRedPoint = ref(false);
onMounted(async() =>{
  if(userId.value==null||userId.value === ''){
    await preLogin()
  } else {
    console.log('no preLogin')
  }

  if(cookieUtil.getCookie("token")===null || cookieUtil.getCookie("token")===''){
    button_index.value = "登录";
  }else{
    button_index.value = "退出";
  }
  try{
    if(cookieUtil.getCookie("token") === null || cookieUtil.getCookie("token")===''){
      return;
    }
    const res = await httpUtil.get('/message/isRedPoint',{},{Authorization: cookieUtil.getCookie("token")});
    isRedPoint.value = res.data.isRed;
  }catch (e){
    console.error(e);
  }
  console.log("token:")
  console.log(cookieUtil.getCookie("token"))
})

const preLogin = async ()=>{
  if(cookieUtil.getCookie("token")===null||cookieUtil.getCookie("token")===''){
    if(localStorage.getItem("avatar")!==null&&localStorage.getItem("avatar")!==''){
      localStorage.setItem("avatar",'')
      window.location.reload();
    }
    localStorage.setItem("avatar",'')
    localStorage.setItem("token", '');
    localStorage.setItem("userId", '');
    localStorage.setItem("userName", '');
    localStorage.setItem("authorId", '')
    return
  }

  const res = await http.get('/user/preLogin',{},{Authorization:cookieUtil.getCookie("token")});
  console.log(res);

  userStore.setUsername(res.data.username);
  tokenStore.setToken(res.data.token);
  userIdStore.setUserId(res.data.userId);
  localStorage.setItem("userName", res.data.username);
  localStorage.setItem("token", res.data.token);
  localStorage.setItem("userId", res.data.userId);

  localStorage.setItem("avatar",res.data.avatar);
  localStorage.setItem("authorId", res.data.authorId);
  cookieUtil.setCookie("username", res.data.username); // 存储用户名在 Cookie 中
  cookieUtil.setCookie("avatar", res.data.avatar);

  haveAvatar.value = true

  const res2 = await http.get('/user/ifScholar',{userId: res.data.userId});
  localStorage.setItem("ifAuthentication",res2.data.judge === 1);
  ifAuth.value = res2.data.judge === 1;


}

const ifAuth = ref(false);

watch(cookieUtil.getCookie("username"),(oldValue,newValue)=> {
  user_name.value = newValue;
  console.log(newValue);
})
const drawer = ref(false);
const handleDrawer = ()=>{
  if(cookieUtil.getCookie("token") === null || cookieUtil.getCookie("token") === ""){
    ElMessage.error("请先登录！");
    return;
  }
  drawer.value = !drawer.value;
}

const errorHandler = () => true
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
      <img src="@/assets/image/logo.png"  style="width: 45px; height: 45px;margin-top: 8px"/>
      <div style="font-size:9.5px;font-family: espano;text-align: center;margin-top: -14px;color: #06008c;user-select: none">
        AllPaper
        <br>
        Engine
      </div>
    </div>



    <el-sub-menu index="3">
      <template #title>作者服务</template>
      <el-menu-item index="/user/personalInfo" v-if="ifAuth">个人门户</el-menu-item>
      <el-menu-item index="/user/academicClaim" v-if="ifAuth">学术成果认领</el-menu-item>
      <el-menu-item index="/scholarIdentify" v-if="ifAuth">更新认证</el-menu-item>
      <el-menu-item index="/scholarIdentify" v-else>学者认证</el-menu-item>
      <el-menu-item index="/scholarAppeal">学术申诉</el-menu-item>
    </el-sub-menu>
    <el-menu-item index="/hotpoint">热点分析</el-menu-item>
    <el-menu-item index="/user/info">用户主页</el-menu-item>
    <el-menu-item index="/advancedSearch">高级检索</el-menu-item>
    <div class="header-menu-right" >
      <div class="message-icon-container">
        <div>
          <el-tooltip class="box-item" effect="light" content="消息中心" placement="bottom">
          <img src="@/assets/svg/message.svg" style="width: 22px" class="box-item" alt="消息中心" @click="handleDrawer"/>
          </el-tooltip>
        </div>
        <!-- 红色原点 -->
        <div class="sss" v-if="isRedPoint"></div>
      </div>

      <el-avatar :src="avatarUrl" v-if="haveAvatar" shape="circle" class="user-avatar" @click="goToUserInfo" @error="errorHandler">
        <img
            src="http://116.204.112.5:1145/img/avatar/default.png"
        />
      </el-avatar>

      <span :style="{ color: textColor }">
        {{ user_name ? `欢迎, ${user_name}` : "点此登录" }}
      </span>
      <el-button @click="goToLogin"
                 style="margin-right: 30px;background-color: transparent">
        {{ button_index }}
      </el-button>
    </div>
  </el-menu>
  <el-drawer v-model="drawer" title="消息中心" direction="rtl" :before-close="handleDrawer">
    <MessageCenter/>
  </el-drawer>
  <div class="h-6"/>
</template>

<style scoped>
@import "@/css/header.css";

.el-menu--horizontal > .el-menu-item:nth-child(5) {
  margin-right: auto;
}
.message-icon-container {
  position: relative; /* 设置相对定位，作为红点的参考位置 */
}

.sss {
  position: absolute;
  top: 0;
  right: 0;
  width: 8px;
  height: 8px;
  background-color: red; /* 红色圆点 */
  border-radius: 50%; /* 圆形 */
  border: 2px solid white; /* 白色边框，使其更加突出 */
}
.box-item :hover{
  cursor: pointer;
}
</style>
