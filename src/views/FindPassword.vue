<script setup>
import {ref, reactive, onMounted} from 'vue'
import SIdentify from "../components/SIdentify.vue"
import {ElMessage} from "element-plus";
import router from "@/router/index.js";
import cookieUtil from "@/utils/cookie.js";
import httpUtil from "@/api/http.js";

const identifyCode = ref('');
const identifyCodes = '1234567890abcdefghijklmnopqrstuvwxyz'

const loginForm = ref({
  mail: '',
  password: '',
  repeat: '',
  validation: ''
})
const loginFormRef = ref(null)
const loginRule = ref({
  mail: [
    {required: true, message: '请输入有效邮箱', trigger: 'blur'},
    {type: 'email', message: '请输入有效的邮箱地址', trigger: ['blur', 'change']}
  ],
  password: [
    {required: true, message: '请输入密码', trigger: 'blur'}
  ],
  repeat: [
    {required: true, message: '请再次输入密码', trigger: 'blur'}
  ],
  validation: [
    {required: true, message: '请输入验证码！', trigger: 'blur'}
  ]
})

const countdown = ref(60);
const isCounting = ref(false);
let verificationCodeisValid = ref(false);

const startCountdown = async () => {
  if (isCounting.value) return;

  try {
    await sendVerificationCode();
    isCounting.value = true;
    countdown.value = 60;
    verificationCodeisValid.value = true;

    const interval = setInterval(() => {
      countdown.value--;
      if (countdown.value <= 0) {
        clearInterval(interval);
        isCounting.value = false;
        verificationCodeisValid = false;
      }
    }, 1000);
  } catch (error) {
    ElMessage.error("发送验证码失败");
  }
}

const sendVerificationCode = async () => {
  const res = await httpUtil.get('/sendMail', {
    to: registerForm.value.mail,
  });
  if (!res.data.verifyCode) {
    throw new Error("发送验证码失败");
  }
  registerForm.value.sentCode = res.data.verifyCode;
}

const modify = async () => {
  if (loginForm.value.validation !== loginForm.value.sentCode) {
    ElMessage.error("验证码不正确");
    return;
  }

  if (!verificationCodeisValid.value) {
    ElMessage.error("验证码已过期，请重试");
    return;
  }

  try {
    const res = await httpUtil.post('/user/register', {
      mail: loginForm.value.mail,
      password: loginForm.value.password,
      mail: loginForm.value.mail,
    });

    if (res.data.msg === "register success") {
      ElMessage.success("注册成功！");
      await router.push('/login');
    } else {
      ElMessage.error("注册失败，请重试");
    }
  } catch (e) {
    console.error(e);
    ElMessage.error("注册失败，请重试");
  }
}

onMounted(() => {
  identifyCode.value = '';
  makeCode(identifyCodes, 4);
});
const login = async () => {
}
const backToLogin = () => {
  router.push('/login');
}

const getValidCode = async() => {

}
</script>

<template>
  <div class="login-div component">
    <div class="component" style="display: flex; justify-content: center;">
      <span class="login-title">找回密码</span>
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
      <el-form-item label="新的密码"
                    prop="password">
        <el-input v-model="loginForm.password"
                  placeholder="请输入新密码"
                  size="large"
                  show-password
                  clearable></el-input>
      </el-form-item>
      <el-form-item label="再次输入"
                    prop="repeat">
        <el-input v-model="loginForm.repeat"
                  placeholder="请再次输入新密码"
                  size="large"
                  show-password
                  clearable></el-input>
      </el-form-item>
      <el-form-item label="填写验证码"
                    prop="validation">
        <el-input v-model="loginForm.validation"
                  placeholder="请输入验证码"
                  size="large"
                  style="width: 70%"
                  clearable></el-input>
        <el-button style="height: 40px; width: 30%" @click="getValidCode">获取验证码</el-button>
      </el-form-item>
    </el-form>
    <div class="component" style="display: flex; justify-content: center;margin-top: 10px;">
      <el-button @click="backToLogin" style="width: 100%; height:40px;">返回</el-button>
      <el-button @keyup.enter="modify" @click="modify" type="primary" style="width: 100%; height:40px;">修改密码
      </el-button>
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
