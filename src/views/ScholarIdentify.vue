<script setup>
import {Edit, Picture, Setting, SuccessFilled, Upload} from "@element-plus/icons-vue";
import {ref} from 'vue';
import {ElMessage} from "element-plus";
import httpUtil from "@/api/http.js";
import router from "@/router/index.js";

// 表单数据
const form = ref({
  name: '',
  organization: '',
  researchField: '',
  email: '',
  sentCode: '',
  verificationCode: ''
});
const getValidCode = async () => {
}

// 表单验证规则
const rules = {
  name: [{required: true, message: '请输入真实姓名', trigger: 'blur'}],
  organization: [{required: true, message: '请输入工作单位', trigger: 'blur'}],
  researchField: [{required: true, message: '请输入研究领域', trigger: 'blur'}],
  email: [
    {required: true, message: '请输入邮箱', trigger: 'blur'},
    {type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur'}
  ],
  verificationCode: [{required: true, message: '请输入邮箱验证码',trigger: 'blur'}]
};
const currentStep = ref('1');
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
    to: form.value.email,
  });
  if (!res.data.verifyCode) {
    throw new Error("发送验证码失败");
  }
  form.value.sentCode = res.data.verifyCode;
}
const oneToTwo = () => {
  if (form.value.verificationCode !== form.value.sentCode || !verificationCodeisValid.value) {
    ElMessage.error("验证码不正确或已过期");
    return;
  } else {
    currentStep.value = '2';
  }
}
const adminOneToTwo = () => {
  currentStep.value = '2';
}
const adminTwoToOne = () => {
  currentStep.value = '1';
}
const twoToThree = () => {
  currentStep.value = '3';
}
const threeToTwo = () => {
  currentStep.value = '2';
}
const tableData2 = [
  {
    feature: "个人门户公开",
    value: true
  },
  {
    feature: "显示相关作者",
    value: true
  }
]
const backToMain = ()=>{
  router.push('/main');
}
</script>

<template>
  <div class="scholar-identify-main">
    <el-card style="height: 500px;">
      <div style="display: flex; flex-direction: column; align-items: center;">
        <div>
          <el-steps style="width: 600px; margin-top: 3%;" :active="currentStep" finish-status="success">
            <el-step title="填写个人信息" :icon="Edit"/>
            <el-step title="个人门户设置" :icon="Setting"/>
            <el-step title="等待审批" :icon="SuccessFilled"/>
          </el-steps>
        </div>
        <div class="scholar-identify-input" v-if="currentStep === '1'">
          <el-form :model="form" :rules="rules" ref="scholarForm" label-width="100px">
            <el-form-item label="真实姓名" prop="name">
              <el-input v-model="form.name" placeholder="请输入真实姓名"></el-input>
            </el-form-item>
            <el-form-item label="工作单位" prop="organization">
              <el-input v-model="form.organization" placeholder="请输入工作单位"></el-input>
            </el-form-item>
            <el-form-item label="研究领域" prop="researchField">
              <el-input v-model="form.researchField" placeholder="请输入研究领域"></el-input>
            </el-form-item>
            <el-form-item label="工作邮箱" prop="email">
              <el-input v-model="form.email" placeholder="请输入研究领域"></el-input>
            </el-form-item>
            <el-form-item label="邮箱验证" prop="verificationCode">
              <el-input v-model="form.verificationCode" placeholder="请输入邮箱">
                <template #append>
                  <el-button :disabled="isCounting" @click="startCountdown" style="width: 130px">
                    {{ isCounting ? `${countdown}秒后重发` : '获取验证码' }}
                  </el-button>
                </template>
              </el-input>
            </el-form-item>
          </el-form>
          <div style="display: flex;">
            <el-button color="#1F578F" style="width: 125px; height: 36px" @click="oneToTwo">点击提交</el-button>
            <el-button type="danger" style="width: 125px; height: 36px" @click="adminOneToTwo">开发专用！</el-button>
          </div>
        </div>
        <div v-if="currentStep === '2'" class="scholar-identify-input">
          <div style="display: flex">
            <el-table :data="tableData2" style="width: 100%" show-header="false">
              <el-table-column prop="feature" width="700px"></el-table-column>
              <el-table-column prop="value" width="70px">
                <template #default="scope">
                  <el-switch
                      v-model="scope.row.value"
                      class="ml-2"
                      style="--el-switch-on-color: #13ce66;"
                  />
                </template>
              </el-table-column>
            </el-table>
          </div>
          <div style="display: flex; margin-top: 5%">
            <el-button style="width: 125px; height: 36px" @click="adminTwoToOne">上一步</el-button>
            <el-button color="#1F578F" style="width: 125px; height: 36px" @click="twoToThree">下一步</el-button>
          </div>
        </div>
        <div v-if="currentStep === '3'" class="scholar-identify-input">
          <img src="@/assets/image/identifySuccess.png">
          <h1 style="color:#7a7a7a">恭喜。您已提交成功！请耐心等待审核</h1>
          <div style="display:flex;">
            <el-button color="#1F578F" style="width: 125px; height: 36px" @click="backToMain">返回主页</el-button>
            <el-button style="width: 125px; height: 36px" @click="threeToTwo">开发专用！</el-button>
          </div>
        </div>
      </div>
    </el-card>
  </div>
</template>

<style scoped>
@import "@/css/basic.css";
@import "@/css/scholarIdentify.css";
</style>
