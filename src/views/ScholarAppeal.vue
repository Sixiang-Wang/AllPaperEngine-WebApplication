<script setup>
import {Edit, Picture, Search, Setting, SuccessFilled, Upload} from "@element-plus/icons-vue";
import {onMounted, ref} from 'vue';
import {ElMessage} from "element-plus";
import httpUtil from "@/api/http.js";
import router from "@/router/index.js";
import * as cookieUtil from "@/utils/cookie.js";

// 表单数据
const form = ref({
  name: '',
  organization: '',
  email: '',
  sentCode: '',
  verificationCode: ''
});
const currentPage = ref(1);

// 表单验证规则
const rules = {
  name: [{required: true, message: '请输入真实姓名', trigger: 'blur'}],
  organization: [{required: true, message: '请输入工作单位', trigger: 'blur'}],
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
const simpleSearch = async() => {
  if(simpleSearchInput.value.length===0){
    ElMessage.warning("请输入搜索内容！")
    return;
  }
  const res = await httpUtil.get('/search/getWorkByTitleWord',{
    page: currentPage.value,
    word: simpleSearchInput.value
  })
  console.log(res.data);
  searchedPapers.value = res.data.works;
  console.log(searchedPapers.value);
  isSearched.value = true;
  // 给每一行初始化 isSelected 属性
  searchedPapers.value.forEach(paper => {
    paper.isSelected = false;
  });
  isSearched.value = true;
}
const oneToTwo = () => {
  if (form.value.verificationCode !== form.value.sentCode || !verificationCodeisValid.value) {
    ElMessage.error("验证码不正确或已过期");
    return;
  } else {
    currentStep.value = '2';
  }
}
const id = ref();
const goToPaper = (id)=>{
  router.push({path:'/paper', query:{id: id}})
}
const rowStyle=({row})=>{
  if(id.value === row.id){
    return  {'background-color': '#F7EDED', cursor: 'pointer'};
  }
  return {cursor: 'pointer'}

}
const handleRowDbClick = (row)=>{
  console.log(row);
  console.log("row.id"+row.id);
  id.value = row.id;
  console.log("id.value"+id.value);
}
const searchedPapers = ref([]);

const adminOneToTwo = () => {
  console.log(currentStep.value);
  currentStep.value = '2';
}
const adminTwoToOne = () => {
  currentStep.value = '1';
}
const isSearched = ref(false);

const twoToThree = () => {
  currentStep.value = '3';
}
const threeToTwo = () => {
  currentStep.value = '2';
}
const backToMain = ()=>{
  router.push('/main');
}
const simpleSearchInput = ref("");
const simpleSearchType = ref('1');
const simpleCheckList = ref(["1", "2"])
const reasonForm = ref({
  appealReason: '',
  otherReason: '',
  addtionReason: ''
});
const defaultReasons = ref([
  { value: 'unfairReview', label: '同行评审不公' },
  { value: 'plagiarismAccusation', label: '学术不端行为指控错误' },
  { value: 'paperRejection', label: '论文退稿理由不充分' },
  { value: 'misclassification', label: '论文被误分类或拒绝' },
  { value: 'editorProcess', label: '编辑处理过程不透明' },
  { value: 'other', label: '其他' },
]);
const otherReason = ref("");
const additionReason = ref("");
const submitForm = ()=>{
  currentStep.value = '4';
}
const reasonRules = ref({
  appealReason: [
    { required: true, message: '请选择原因！', trigger: 'blur' }
  ]
})
onMounted(()=>{
  if(cookieUtil.getCookie("token")===null || cookieUtil.getCookie("token") === ''){
    ElMessage.error("请先登录！");
    setTimeout(()=>{
      router.push('/login');
    },500);
  }
})
</script>

<template>
  <div class="scholar-identify-main">
    <el-card style="height: 500px;">
      <div style="display: flex; flex-direction: column; align-items: center;">
        <div>
          <el-steps style="width: 600px; margin-top: 3%;" :active="currentStep" finish-status="success">
            <el-step title="填写申诉人信息" :icon="Edit"/>
            <el-step title="填写被申诉论文信息" :icon="Edit"/>
            <el-step title="填写申诉原因" :icon="Setting"/>
            <el-step title="等待审批" :icon="SuccessFilled"/>
          </el-steps>
        </div>
        <div class="scholar-identify-input" v-if="currentStep === '1'">
          <el-form :model="form" :rules="rules" ref="scholarForm" rules="reasonRules" label-width="100px" >
            <el-form-item label="真实姓名" prop="name">
              <el-input v-model="form.name" placeholder="请输入真实姓名"></el-input>
            </el-form-item>
            <el-form-item label="工作单位" prop="organization">
              <el-input v-model="form.organization" placeholder="请输入工作单位"></el-input>
            </el-form-item>
            <el-form-item label="工作邮箱" prop="email">
              <el-input v-model="form.email" placeholder="请输入研究领域"></el-input>
            </el-form-item>
<!--            <el-form-item label="邮箱验证" prop="verificationCode">-->
<!--              <el-input v-model="form.verificationCode" placeholder="请输入邮箱">-->
<!--                <template #append>-->
<!--                  <el-button :disabled="isCounting" @click="startCountdown" style="width: 130px" class="no-modify-button">-->
<!--                    {{ isCounting ? `${countdown}秒后重发` : '获取验证码' }}-->
<!--                  </el-button>-->
<!--                </template>-->
<!--              </el-input>-->
<!--            </el-form-item>-->
          </el-form>
          <div style="display: flex;">
            <el-button color="#1F578F" style="width: 125px; height: 36px" @click="oneToTwo">点击提交</el-button>
            <el-button type="danger" style="width: 125px; height: 36px" @click="adminOneToTwo">开发专用！</el-button>
          </div>
        </div>
        <div v-else-if="currentStep === '2'" class="scholar-identify-input">
          <div style="display: flex; flex-direction: column; align-items: center">
            <span>
            <el-input v-model="simpleSearchInput" class="search-input" style="width: 550px" placeholder="请输入搜索内容" @keyup.enter="simpleSearch">
              <template #prepend>
                <el-select v-model="simpleSearchType" style="width: 115px">
                  <el-option label="主题" value="1"/>
                  <el-option label="篇名" value="2"/>
                  <el-option label="关键词" value="3"/>
                </el-select>
              </template>
            </el-input>
            <el-button :icon="Search" @click="simpleSearch" class="claim-button"/>
              </span>
            <el-checkbox-group v-model="simpleCheckList" style="margin-top: 2%">
              <el-checkbox label="期刊" value="1"/>
              <el-checkbox label="学位论文" value="2"/>
              <el-checkbox label="会议论文" value="3"/>
              <el-checkbox label="报纸" value="4"/>
              <br/>
              <el-checkbox label="科技成果" value="5"/>
              <el-checkbox label="图书" value="6"/>
              <el-checkbox label="标准" value="7"/>
              <el-checkbox label="专利" value="8"/>
            </el-checkbox-group>
            <div v-if="isSearched" style="margin-top: 3%; max-height: 200px; overflow-y: auto;">
              <div style="max-height: 150px; overflow-y: auto;">
                <el-table :data="searchedPapers" @row-click="handleRowDbClick" show-overflow-tooltip :row-style="rowStyle"  style="width: 620px">
                  <el-table-column prop="title" label="论文名称" width="250"></el-table-column>
                  <el-table-column prop="publicationDate" label="发表时间" width="150"></el-table-column>
                  <el-table-column prop="publication" label="发表期刊" width="100"></el-table-column>
                  <el-table-column prop="search">
                    <template #default="scope">
                      <el-button @click="goToPaper(scope.row.id)" color="#1F578F">查看论文</el-button>
                    </template>
                  </el-table-column>
                </el-table>
              </div>
            </div>
          </div>
          <div style="display: flex; margin-top: 5%">

            <el-button style="width: 125px; height: 36px" @click="adminTwoToOne">上一步</el-button>
            <el-button color="#1F578F" style="width: 125px; height: 36px" @click="twoToThree">下一步</el-button>
          </div>
        </div>
        <div v-else-if="currentStep === '3'" class="scholar-identify-input">
          <el-form :model="reasonForm" label-width="120px" :rules="reasonRules">
            <!-- 第一项：选择申诉原因 -->
            <el-form-item label="申诉原因">
              <el-select v-model="reasonForm.appealReason" placeholder="请选择申诉原因" size="large" style="width: 440px">
                <el-option v-for="item in defaultReasons" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </el-form-item>

            <!-- 第二项：其他原因 -->
            <el-form-item label="其他原因" v-if="reasonForm.appealReason === 'other'">
              <el-input v-model="reasonForm.otherReason" placeholder="请输入其他原因" size="large" style="width: 440px" />
            </el-form-item>

            <!-- 第三项：补充说明 -->
            <el-form-item label="补充说明">
              <el-input type="textarea" v-model="reasonForm.addtionReason" placeholder="请输入补充说明" size="large" style="width: 440px" rows="4" />
            </el-form-item>

            <!-- 提交按钮 -->
            <el-form-item>
              <el-button style="width: 125px; height: 36px" @click="adminTwoToOne" >上一步</el-button>
              <el-button type="primary" @click="submitForm" style="width: 125px; height: 36px" color="#1F578F">提交申诉</el-button>

            </el-form-item>
          </el-form>

        </div>
        <div v-if="currentStep === '4'" style="display:flex;flex-direction: column;align-items: center">
          <img src="@/assets/image/identifySuccess.png">
          <h1 style="color:#7a7a7a">恭喜。您已提交成功！请耐心等待审核</h1>
          <div style="display:flex;">
            <el-button color="#1F578F" style="width: 125px; height: 36px" @click="backToMain">返回主页</el-button>
            <el-button style="width: 125px; height: 36px" @click="currentStep = '1'">开发专用！</el-button>
          </div>
        </div>
      </div>
    </el-card>
  </div>
</template>

<style scoped>
@import "@/css/basic.css";
@import "@/css/scholarIdentify.css";
@import "@/css/academicClaim.css";
.no-modify-button {
  background-color: transparent !important;  /* 避免被 .el-button 样式覆盖 */
  border-radius: 4px !important;
  color: inherit !important;
}
</style>
