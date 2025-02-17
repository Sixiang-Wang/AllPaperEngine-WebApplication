<script setup>
import { ref } from 'vue'
import { ElMessage } from "element-plus"
import router from "@/router/index.js"
import httpUtil from "@/api/http.js";
const registerForm = ref({
  name: '',
  password: '',
  confirmPassword: '',
  mail: '',
  verificationCode: '',
  sentCode: ''
})

const registerFormRef = ref(null)
const registerRule = ref({
  name: [
    { required: true, message: '请输入用户名', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度至少为6位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    { validator: (rule, value, callback) => {
        if (value !== registerForm.value.password) {
          callback(new Error('两次输入的密码不一致'));
        } else {
          callback();
        } 
      }, trigger: 'blur' }
  ],
  mail: [
    { required: true, message: '请输入有效邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入有效的邮箱地址', trigger: ['blur', 'change'] }
  ],
  verificationCode: [
    { required: true, message: '请输入验证码', trigger: 'blur' },
    { len: 6, message: '验证码长度为6位', trigger: 'blur' }
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
        //verificationCodeisValid = false;
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

const register = async () => {
  if (registerForm.value.verificationCode !== registerForm.value.sentCode) {
    ElMessage.error("验证码不正确");
    return;
  }

  if (!verificationCodeisValid.value) {
    ElMessage.error("验证码已过期，请重试");
    return;
  }
  
  try {
    const res = await httpUtil.post('/user/register', {
      name: registerForm.value.name,
      password: registerForm.value.password,
      mail: registerForm.value.mail,
    });
    
    if (res.data.msg === "register success") {
      ElMessage.success("注册成功！");

      setTimeout(async () => {
        await router.push('/login');
      }, 1000);
      window.location.reload();
    } else {
      ElMessage.error("注册失败，请重试");
    }
  } catch (e) {
    console.error(e);
    ElMessage.error("注册失败，请重试");
  }
}
</script>

<template>
  <div class="register-div component" style="margin-top: -50px;">
    <div class="component" style="display: flex; justify-content: center;">
      <span class="register-title">IScholar平台注册</span>
    </div>
    <el-form :label-position="right"
             :model="registerForm"
             :rules="registerRule"
             ref="registerFormRef"
    >
      <el-form-item label="用户名" prop="name">
        <el-input v-model="registerForm.name"
                  placeholder="请输入用户名"
                  size="large"
                  clearable></el-input>
      </el-form-item>
      <el-form-item label="用户密码" prop="password">
        <el-input v-model="registerForm.password"
                  placeholder="请输入密码"
                  size="large"
                  show-password
                  clearable></el-input>
      </el-form-item>
      <el-form-item label="确认密码" prop="confirmPassword">
        <el-input v-model="registerForm.confirmPassword"
                  placeholder="请确认密码"
                  size="large"
                  show-password
                  clearable></el-input>
      </el-form-item>
      <el-form-item label="用户邮箱" prop="mail">
        <div style="display: flex; align-items: center;">
          <el-input v-model="registerForm.mail"
                    placeholder="请输入邮箱"
                    size="large"
                    style="width: 300px;"
                    clearable></el-input>
          <el-button :disabled="isCounting" @click="startCountdown" style="margin-left: 15px" size="big">
            {{ isCounting ? `${countdown}秒后重发` : '获取验证码' }}
          </el-button>
        </div>
      </el-form-item>
      <el-form-item label="输入验证码" prop="verificationCode">
        <el-input v-model="registerForm.verificationCode"
                  placeholder="请输入验证码"
                  size="large"
                  clearable></el-input>
      </el-form-item>
    </el-form>
    <div class="component" style="display: flex; justify-content: center; margin-top: 10px;">
      <el-button @click="register" type="primary" style="width: 100%; height:40px;">注册</el-button>
      <el-button @click="$router.push('/login')" style="width: 100%; height:40px;">返回登录</el-button>
    </div>
  </div>
</template>

<style scoped>
@import "@/css/basic.css";
@import "@/css/register.css";
</style>
