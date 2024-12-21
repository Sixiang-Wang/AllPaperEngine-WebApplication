<script setup>
import {Avatar, Edit, Picture, Search, Setting, SuccessFilled, Upload} from "@element-plus/icons-vue";
import {onMounted, ref} from 'vue';
import {ElMessage} from "element-plus";
import httpUtil from "@/api/http.js";
import router from "@/router/index.js";
import * as cookieUtil from "@/utils/cookie.js";

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
  console.log(form.value.email);
  const res = await httpUtil.get('/sendMail', {
    to: form.value.email,
  });
  if (!res.data.verifyCode) {
    throw new Error("发送验证码失败");
  }
  form.value.sentCode = res.data.verifyCode;
}
const oneToTwo = async() => {

  // const form = ref({
  //   name: '',
  //   organization: '',
  //   researchField: '',
  //   email: '',
  //   sentCode: '',
  //   verificationCode: ''
  // });
  if(form.value.name === ''){
    ElMessage.error("请输入真实姓名！");
    return;
  }
  if(form.value.organization === ''){
    ElMessage.error("请输入单位名称！");
    return;
  }
  if(form.value.researchField === ''){
    ElMessage.error("请输入研究领域！");
    return;
  }
  if(form.value.email === ''){
    ElMessage.error("请输入邮箱！");
    return;
  }
  if (form.value.verificationCode !== form.value.sentCode || !verificationCodeisValid.value) {

    ElMessage.error("验证码不正确或已过期");
    return;
  }
  try{
    const res = await httpUtil.get("/authentication/put/token",{
      nameReal: form.value.name,
      workplace: form.value.organization,
      field: form.value.researchField,
      mail: form.value.email
    },{Authorization: cookieUtil.getCookie("token")});
    console.log(res.data);
    if(res.data.msg === 'success'){
      ElMessage.success("提交成功！");
      currentStep.value = '2';
    }else{
      ElMessage.warning("提交失败；请稍后再试")
    }
  }catch (e){
    ElMessage.error("提交失败，请稍后再试");
    console.error(e);
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
onMounted(async()=>{
  if(cookieUtil.getCookie("token") === null || cookieUtil.getCookie("token") === ''){
    ElMessage.error("请先登录！");
    await router.push('/login');
    return;
  }
  try{
    console.log('哈哈哈');
    const res = await httpUtil.get('/authentication/ifauthenticated',{},{
      Authorization: cookieUtil.getCookie("token")
    });
    if(res.data.msg === 'authenticated'){
      ElMessage.warning("您已申请成为学者；请耐心等待审核结果");
      setTimeout(()=>{
        router.push('/main');
      },1500);
    }
  }catch (e){
    console.error(e);
  }
})

const input = ref();
const ifTable = ref(false);
const authorResults = ref([]);
const search = async()=>{
    ifTable.value = true;
    try{
      console.log(input.value);
      const res = await httpUtil.get('/author/getByName',{
        name: input.value
      })
      authorResults.value = res.data.authors;
    }catch (e){
      console.error(e);
    }
}
const id = ref();
const handleRowDbClick = (row)=>{

  id.value = row.authorId;
  console.log("id.value"+id.value);
}
const rowStyle=({row})=>{
  if(id.value === row.authorId){
    return  {'background-color': '#dfe6e6', cursor: 'pointer'};
  }
  return {cursor: 'pointer'}

}
const submitAndJump = async()=>{
  try{
    const res = await httpUtil.get('/authentication/put',{
      userId: localStorage.getItem("userId"),
      nameReal: form.value.name,
      workplace: form.value.organization,
      field: form.value.researchField,
      mail: form.value.email,
      authorId: id.value
    })
    console.log(res.data);
    if(res.data.msg === 'success'){
      ElMessage.success("提交成功，请耐心等待");
      setTimeout(()=>{
        currentStep.value = '4';
      },300);
    }else{
      ElMessage.error("提交失败，请稍后再试");
    }
  }catch (e){
    console.error(e);
  }
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
            <el-step title="选择学者身份" :icon="Avatar"/>
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
              <el-input v-model="form.email" placeholder="请输入邮箱"></el-input>
            </el-form-item>
            <el-form-item label="邮箱验证" prop="verificationCode">
              <el-input v-model="form.verificationCode" placeholder="请输入验证码">
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
          <span>
            <el-input v-model="input" class="search-input" style="width: 550px" placeholder="请输入用户名称"
                      @keyup.enter="search"/>
            <el-button :icon="Search" @click="search" class="claim-button"/>
          </span>
          <div style="margin-top: 3%; max-height: 200px; overflow-y: auto;">
            <el-table :data="authorResults" v-if="ifTable" :row-style="rowStyle" @row-click="handleRowDbClick" show-overflow-tooltip>
              <el-table-column prop="authorName[0]" label="作者姓名" width="180" />
              <el-table-column prop="worksCount" label="论文数量" width="180" />
              <el-table-column prop="citedByCount" label="被引用数" width="180" />
            </el-table>
          </div>
          <div style="display:flex; margin-top: 5%">
            <el-button style="width: 125px; height: 36px" @click="currentStep = '2'">上一步</el-button>
            <el-button color="#1F578F" style="width: 125px; height: 36px" @click="submitAndJump">下一步</el-button>
          </div>
        </div>
        <div v-if="currentStep === '4'" class="scholar-identify-input">
          <img src="@/assets/image/identifySuccess.png">
          <h1 style="color:#7a7a7a">恭喜。您已提交成功！请耐心等待审核</h1>
          <div style="display:flex;">
            <el-button color="#1F578F" style="width: 125px; height: 36px" @click="backToMain">返回主页</el-button>
            <el-button style="width: 125px; height: 36px" @click="currentStep = '3'">开发专用！</el-button>
          </div>
        </div>
      </div>
    </el-card>
  </div>
</template>

<style scoped>
@import "@/css/basic.css";
@import "@/css/scholarIdentify.css";

:deep(.el-tabs__nav-wrap::after) {
  position: static !important;
}
.search-input{
  height: 40px;
  max-width: 80%;
}

.claim-button {
  border-radius: 0 4px 4px 0; /* 使按钮圆角与输入框拼接 */
  height: 40px;
  background-color: transparent;
  border-left-width: 0;
}
.claim-button:hover {
  border: 1px solid #1F578F;
  border-left-width: 0;
  color: #1F578F;
  background-color: rgba(255, 255, 255, 0.05);
}

:deep(.el-input__wrapper){
  background-color:rgba(0,0,0,0) !important;
  border-radius: 0 !important;
  --el-input-focus-border-color: #1F578F;
  --el-input-hover-border-color: #1F578F;
}
:deep(.el-input-group__prepend){
  background-color:rgba(0,0,0,0);
}
:deep(.el-input__inner) {
  background-color: rgba(0, 0, 0, 0) !important;
}
:deep(.el-input__inner::placeholder) {
  color: rgba(200, 200, 200, 0.7) !important;
  cursor: pointer;
}
:deep(.el-select__selected-item) {
  color: rgba(200, 200, 200, 0.7) !important;
  cursor: pointer;
}
/*不要碰这里的代码*/
:deep(.el-select__wrapper){
  height: 40px;
  --el-select-border-color: rgba(200, 200, 200, 0.7);
  --el-select-focus-border-color:rgba(235, 235, 235, 0.8);
}
:deep(.el-input-group__append) {
  background-color: transparent !important;
  color: rgba(255, 255, 255, 1);
  --el-input-border-color: rgba(255, 255, 255, 0.7);
}

.academic-claim-title {
  padding: 1% 1%;
}
.academic-dialog-title{
  font-size: 1.3em;
  color: black;
  margin-left: 2%;
}
:deep(.el-tabs__item){
  color: #1F578F;
  opacity: 0.5;
}
:deep(.el-tabs__item.is-active){
  color: #1F578F;
  opacity: 1;
}
:deep(.el-tabs__active-bar){
  background-color: #1F578F;
}
:deep(.el-checkbox__input.is-checked+.el-checkbox__label){
  color: #1F578F;
}
:deep(.el-checkbox__input.is-checked .el-checkbox__inner){
  background-color: #1F578F;
}
</style>
