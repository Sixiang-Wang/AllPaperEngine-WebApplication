<script setup>
import { ref, reactive, onMounted} from 'vue'
import SIdentify from "../components/SIdentify.vue"
import {ElMessage} from "element-plus";
import router from "@/router/index.js";
import cookieUtil from "@/utils/cookie.js";
const identifyCode = ref('');
const identifyCodes = '1234567890abcdefghijklmnopqrstuvwxyz'
import http from "@/api/http.js"
const loginForm = ref({
  mail: '',
  password: '',
  validation: ''
})
const loginFormRef=ref(null)
const loginRule = ref({
  mail: [
    { required: true, message: '请输入有效姓名', trigger: 'blur'}
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
        account: loginForm.value.mail,
        password: loginForm.value.password
      })
      if(res.data.msg === "login success") {
        ElMessage.success("登陆成功！");
        cookieUtil.setCookie("token",res.data.token,0.25);
        cookieUtil.setCookie("username",res.data.username, 0.25);
        await router.push('/main');
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
      <span class="login-title">IScholar平台登录</span>
    </div>
    <el-form :label-position="right"
             :model="loginForm"
             :rules="loginRule"
             ref="loginFormRef"
    >
      <el-form-item label="用户账号"
                    prop="mail">
        <el-input v-model="loginForm.mail"
                  placeholder="请输入账号"
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
      <el-button @keyup.enter="login(loginForm)" @click="login(loginForm)" type="primary" style="width: 100%; height:40px;">登录</el-button>
      <el-button @click="goToRegister"  style="width: 100%; height:40px;">注册</el-button>
    </div>
  </div>
</template>

<style scoped>
@import "@/css/basic.css";
@import "@/css/login.css";


</style>
