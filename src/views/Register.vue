<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from "element-plus"
import router from "@/router/index.js"
import http from "@/api/http.js"

const registerForm = ref({
  mail: '',
  password: '',
  confirmPassword: ''
})

const registerFormRef = ref(null)
const registerRule = ref({
  mail: [
    { required: true, message: '请输入有效邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入有效的邮箱地址', trigger: ['blur', 'change'] }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度至少为6位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    { validator: (rule, value, callback) => {
        if (value !== registerForm.value.password) {
          callback(new Error('两次输入的密码不一致'))
        } else {
          callback()
        }
      }, trigger: 'blur' }
  ]
})

const register = async () => {
  try {
    const res = await http.post('/user/register', {
      account: registerForm.value.mail,
      password: registerForm.value.password
    })
    if (res.data.msg === "register success") {
      ElMessage.success("注册成功！");
      await router.push('/login');
    }
    console.log(res.data);
  } catch (e) {
    console.error(e);
    ElMessage.error("注册失败，请重试");
  }
}
</script>

<template>
  <div class="register-div component">
    <div class="component" style="display: flex; justify-content: center;">
      <span class="register-title">IScholar平台注册</span>
    </div>
    <el-form :label-position="right"
             :model="registerForm"
             :rules="registerRule"
             ref="registerFormRef"
    >
      <el-form-item label="用户账号" prop="mail">
        <el-input v-model="registerForm.mail"
                  placeholder="请输入邮箱"
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
