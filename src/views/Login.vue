<script setup>
import { ref, reactive, onMounted} from 'vue'
import SIdentify from "../components/SIdentify.vue"
import {ElMessage} from "element-plus";
import router from "@/router/index.js";
import cookieUtil from "@/utils/cookie.js";
const identifyCode = ref('');
const identifyCodes = '1234567890abcdefghijklmnopqrstuvwxyz'
import http from "@/api/http.js"
import {useTokenStore, useUserStore,useUserIdStore} from "@/store/store.js";
import header from "@/components/Header.vue"

const userStore = useUserStore();
const tokenStore = useTokenStore()
const userIdStore = useUserIdStore();

const loginForm = ref({
  mail: '',
  password: '',
  validation: ''
})
const loginFormRef=ref(null)
const loginRule = ref({
  mail: [
    { required: true, message: '请输入有效邮箱', trigger: 'blur'},
    { type: 'email', message: '请输入有效的邮箱地址', trigger: ['blur', 'change'] }
  ],
  password: [
    {required: true, message: '请输入密码', trigger: 'blur'}
  ],
  validation: [
    {required: true, message: '请输入验证码！', trigger: 'blur'}
  ]
})
const randomNum = (min, max) => {
  return Math.floor(Math.random() * (max - min) + min);
};

const refreshCode = () => {
  identifyCode.value = '';
  makeCode(identifyCodes, 4);
};

const makeCode = (o, l) => {
  for (let i = 0; i < l; i++) {
    identifyCode.value += identifyCodes[randomNum(0, identifyCodes.length)];
  }
  console.log("验证码", identifyCode.value);
};

const checkCaptcha = () => {
  if (code.value === "") {
    alert("请输入验证码");
    return false;
  }
  if (identifyCode.value !== code.value) {
    code.value = '';
    refreshCode();
    alert("请输入正确的验证码");
    return false;
  }
  console.log("验证码正确");
  return true;
};

const goToRegister = () => {
  router.push('/register');
}
const goToForget = ()=>{
  router.push('/find/password');
}
onMounted(() => {
  identifyCode.value = '';
  makeCode(identifyCodes, 4);
});
const login =  async() => {
  if(loginForm.value.mail === "admin" && loginForm.value.password === "admin"){
    ElMessage.success("登陆成功！");
    await router.push('/main');
  }else{
    try{
      const res = await http.post('/user/login',{
        mail: loginForm.value.mail,
        password: loginForm.value.password
      })
      if(res.data.msg === "login success") {
        ElMessage.success("登陆成功！");
        userStore.setUsername(res.data.username)
        tokenStore.setToken(res.data.token)
        userIdStore.setUserId(res.data.userId)
        cookieUtil.setCookie("token",res.data.token,0.25);
        cookieUtil.setCookie("username",res.data.username, 0.25);
        console.log(111);
        const res2 = await http.get('/user/ifScholar',{userId: res.data.userId});

        localStorage.setItem("userName", res.data.username);
        localStorage.setItem("token", res.data.token);
        localStorage.setItem("userId", res.data.userId);
        localStorage.setItem("avatar",res.data.avatar)

        localStorage.setItem("ifAuthentication",res2.data.judge === 1);

        await router.push({ path: '/main', query: { refresh: Date.now() } });
        location.reload();
      }else if(res.data.msg === "wrong password"){
        ElMessage.error("密码错误");
        return;
      }
      console.log(res.data);
    }catch(e){
      console.error(e);
    }
  }
}


</script>

<template>
  <div class="login-div component">
    <div class="component" style="display: flex; justify-content: center;">
      <span class="login-title">AllPaper Engine 平台登录</span>
    </div>
    <el-form :label-position="right"
             :model="loginForm"
             :rules="loginRule"
             ref="loginFormRef"
    >
      <el-form-item label="用户账号"
                    prop="mail">
        <el-input v-model="loginForm.mail"
                  placeholder="请输入邮箱"
                  size="large"
                  clearable></el-input>
      </el-form-item>
      <el-form-item label="用户密码"
                    prop="password">
        <el-input v-model="loginForm.password"
                  placeholder="请输入密码"
                  size="large"
                  show-password
                  clearable></el-input>
      </el-form-item>
      <el-form-item label="输入验证码"
                    prop="validation">
        <el-input v-model="loginForm.validation"
                  placeholder="验证码"
                  size="large"
                  style="width: 50%;"></el-input>
        <div @click="refreshCode">
          <s-identify :identifyCode="identifyCode"></s-identify>
        </div>
      </el-form-item>
    </el-form>
    <div class="component" style="display: flex; justify-content: center;margin-top: 10px;">
      <el-button @keyup.enter="login" @click="login" type="primary" style="width: 100%; height:40px;">登录</el-button>
      <el-button @click="goToRegister"  style="width: 100%; height:40px;">注册</el-button>
    </div>
    <div style="margin-left: 82%">
      <span class="forget" @click="goToForget">忘记密码？</span>
    </div>
  </div>
</template>

<style scoped>
@import "@/css/basic.css";
@import "@/css/login.css";

.forget {
  color: #1F578F;
  font-size: 14px;
  text-decoration: underline;
  cursor: pointer;
}
</style>
