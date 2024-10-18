<script setup>

import {Download, Paperclip, Search, Share, Star, StarFilled} from "@element-plus/icons-vue";
import {computed, onMounted, ref} from "vue";
import { useTransition } from '@vueuse/core'
import {ElMessage, ElNotification} from 'element-plus'
import SingleResult from "@/components/SingleResult.vue";
import httpUtil from "@/api/http.js";

const citeNum = ref(0);
const referenceNum = ref(0);
const collectNum = ref(0);
const commentNum = ref(0);
const isCollected = ref(false);
const activeName = ref('first')
const pageSize = 5;  // 每页显示条目数
const referenceResults = ref([]);
const referencePageResults = ref([]);
const referenceTotalLength = ref(0);
const referenceCurrentPage = ref(1);
const citeResults = ref([]);
const citePageResults = ref([]);
const citeTotalLength = ref(0);
const citeCurrentPage = ref(1);

const updateReferencePageResults = () => {
  const start = (referenceCurrentPage.value - 1) * pageSize;
  const end = start + pageSize;
  referencePageResults.value = referenceResults.value.slice(start, end);
};

const updateCitePageResults = () => {
  const start = (citeCurrentPage.value - 1) * pageSize;
  const end = start + pageSize;
  citePageResults.value = citeResults.value.slice(start, end);
};
const handleReferencePageChange = (newPage) => {
  referenceCurrentPage.value = newPage;
  updateReferencePageResults();  // 更新当前页的数据
};
const handleCitePageChange = (newPage) => {
  citeCurrentPage.value = newPage;
  updateCitePageResults();  // 更新当前页的数据
};

onMounted(async()=>{

  const res = await httpUtil.get('/openalex/getall');
  referenceTotalLength.value = res.data.works.length;
  referenceResults.value = res.data.works;

  citeTotalLength.value = res.data.works.length;
  citeResults.value = res.data.works;

  updateReferencePageResults();
  updateCitePageResults();
  }
)

let citeNumChange = useTransition(citeNum, {
  duration: 300,
})
let referenceNumChange = useTransition(referenceNum, {
  duration: 300,
})
let collectNumChange = useTransition(collectNum, {
  duration: 300,
})
let commentNumChange = useTransition(commentNum, {
  duration: 300,
})

//统计值在这里
citeNum.value = 129
referenceNum.value = 51
collectNum.value = 273
commentNum.value = 9
isCollected.value = false
//

const question = ref("")

const submitQuestion = ()=> {
  console.log("Question submitted:", question);
      // Handle the form submission logic
}
const abstract = ref("Despite declining incidence and mortality rates in many countries, the abdominal aortic aneurysm (AAA) continues to represent a life-threatening cardiovascular condition with an overall prevalence of about 2–3% in the industrialized world. While the risk of AAA development is considerably higher for men of advanced age with a history of smoking, screening programs serve to detect the often asymptomatic condition and prevent aortic rupture with an associated death rate of up to 80%. This review summarizes the current knowledge on identified risk factors, the multifactorial process of pathogenesis, as well as the latest advances in medical treatment and surgical repair to provide a perspective for AAA management.\n");

const collectContent = computed(() =>
    isCollected.value ? '取消收藏' : '添加到收藏'
);

const toggleDownload = ()=>{

}
const toggleShare = ()=>{
  const currentUrl = window.location.href;  // 获取当前网址
  navigator.clipboard.writeText(currentUrl)  // 将网址复制到剪切板
  ElMessage({
    message: '网址链接已复制',
    type: 'success',
  })
}
const toggleCite = ()=>{

}


const toggleCollect = () => {
  if(isCollected.value){
    collectNum.value--;
  }else {
    collectNum.value++;
  }
  isCollected.value = !isCollected.value;
}




</script>

<template>
  <div id="app" class="container">
    <div class="main-part">
      <div class = "left-part">
        <!-- Header Section -->
        <div class="header">
          <h1>AAA Revisited: A Comprehensive Review of Risk Factors, Management, and Hallmarks of Pathogenesis</h1>
          <p>V. Kessler, J. Klopf, C. Brostjan +2 authors • Published in Biomedicines 1 January 2022 • Medicine</p>
          <p><strong>DOI:</strong> 10.3390/biomedicines10010094</p>
        </div>

        <!-- TLDR Section -->
        <div class="abstract">
          <strong>Abstract:</strong>
          <br>
          {{abstract}}
        </div>
        <div class="option-part" >


          <el-button
              class="download-button"
              :icon="Download"
              type="danger"
              @click = "toggleDownload"
          >
            下载PDF
          </el-button>


          <el-tooltip
              effect="dark"
              content="分享">
            <el-button
                :icon="Share"
                class="share-button"
                circle
                @click = "toggleShare"
            >
            </el-button>
          </el-tooltip>

          <el-tooltip
              effect="dark"
              content="引用">
            <el-button
                :icon="Paperclip"
                class="cite-button"
                circle
                @click = "toggleCite"
            >
            </el-button>
          </el-tooltip>



          <el-tooltip
              effect="dark"
              :content="collectContent">
          <el-button
              :icon="isCollected? StarFilled:Star"
              :class="['collect-button', { 'collect-button-collected': isCollected }]"
              circle
              @click = "toggleCollect"
          >
          </el-button>
          </el-tooltip>
        </div>

        <el-tabs v-model="activeName" class="down-tabs" type="card">
          <el-tab-pane label="参考文献" name="first">
            <div v-if="referenceResults.length!==0" style="display: flex;">
              <div>
                <SingleResult style="max-width: 100%"
                              v-for="result in referencePageResults" :author="result.paperInformation" :content="result.abstractText"
                              :title="result.title" :cited="result.cited" :id="result.id"
                ></SingleResult>
              </div>
            </div>
            <el-pagination
                v-if="referenceTotalLength > 0"
                background
                layout="prev, pager, next"
                :page-size="pageSize"
                :total="referenceTotalLength"
                @current-change="handleReferencePageChange"
                :current-page="referenceCurrentPage"
            />
          </el-tab-pane>
          <el-tab-pane label="引用" name="second">
            <div v-if="citeResults.length!==0" style="display: flex;">
              <div>
                <SingleResult style="max-width: 100%"
                              v-for="result in citePageResults" :author="result.paperInformation" :content="result.abstractText"
                              :title="result.title" :cited="result.cited" :id="result.id"
                ></SingleResult>
              </div>
            </div>
            <el-pagination
                v-if="citeTotalLength > 0"
                background
                layout="prev, pager, next"
                :page-size="pageSize"
                :total="citeTotalLength"
                @current-change="handleCitePageChange"
                :current-page="citeCurrentPage"
            />
          </el-tab-pane>
        </el-tabs>

      </div>
      <div class="side">
        <el-card class="stat-part"  shadow="always">
          <el-row style="margin-bottom: 15px">
            <el-col :span="12" style="padding-left: 12px">
              <el-statistic title="被引用次数" :value="citeNumChange" :value-style="{ color: '#132fc1' }" />
            </el-col>
            <el-col :span="12" style="padding-left: 12px">
              <el-statistic title="参考文献数" :value="referenceNumChange" :value-style="{ color: '#45a801' }"/>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12" style="padding-left: 12px">
              <el-statistic title="平台收藏量" :value="collectNumChange" :value-style="{ color: '#b10101' }"/>
            </el-col>
            <el-col :span="12" style="padding-left: 12px">
              <el-statistic title="文章评论量" :value="commentNumChange" :value-style="{ color: '#dd8500' }"/>
            </el-col>
          </el-row>

        </el-card>
        <!-- Form Section -->
        <div class="ai-assistant">
          <h3>AI Assistant</h3>
          <p>
            让AI辅助你深入了解文章内容
          </p>
          <el-input
              v-model="question"
              style="width: 230px;padding-right: 10px"
              :autosize="{ minRows: 4, maxRows: 10 }"
              type="textarea"
              placeholder="询问一个文章相关问题"
          />
          <el-button :icon="Search"
                     type="info"
                     circle
                     class="search-button"
                     @click = "submitQuestion"
          />
        </div>
      </div>
    </div>

  </div>
</template>



<style scoped>
.el-popper.is-customized {
  /* Set padding to ensure the height is 32px */
  padding: 6px 12px;
  background: linear-gradient(90deg, rgb(159, 229, 151), rgb(204, 229, 129));
}

.el-popper.is-customized .el-popper__arrow::before {
  background: linear-gradient(45deg, #b2e68d, #bce689);
  right: 0;
}

.container {
  padding: 0 20px 20px 0;
  max-width: 1200px;
  margin: auto;
  font-family: Arial, sans-serif;
  line-height: 1.6;
}

.main-part {
  display: flex;
  width: 1200px; /* Ensure the width is exactly 1100px */
  margin: -10px auto; /* Center the content */

}



.left-part {
  flex: 800px; /* Take more space for the main section */
  padding: 20px;
  margin: 0;
}

.option-part{
  flex: 800px;
  display: flex;
  align-items: center;
}

.download-button{
  background: #e10000;
}
.download-button:hover{
  background: #ec5454;
}

.collect-button{
  color: #7a7a7a;
  background-color: #ffffff;
}
.collect-button-collected{
  color: #fadd21;
  background-color: #ffffff;
}

.collect-button:hover{
  color: #fff14e;
  background-color: #ffffff;
}

:deep(.collect-button .el-icon){
  font-size: 28px;
}

.share-button{
  color: #FFFFFF;
  background-color: #ff5892;
}

.share-button:hover{
  color: #ffe595;
  background-color: #ff7ca9;
}

.cite-button{
  color: #ffffff;
  background: #006eff;
}
.cite-button:hover{
  color: rgb(255, 255, 255);
  background: #338aff;
}

.down-tabs {
  padding-top: 32px;
  color: #6b778c;
  font-size: 32px;
  font-weight: 600;
}

.side {
  flex: 320px; /* Take less space for the sidebar */
  padding: 50px 20px 20px 20px;
}

.stat-part{
  width: 320px;
  margin-bottom: 30px;
}

.header h1 {
  font-size: 24px;
  margin-bottom: 10px;
}

.header p {
  font-size: 14px;
  color: #666;
}

.abstract {
  background-color: #f9f9f9;
  padding: 10px;
  margin: 20px 0;
  border-left: 4px solid #C3EEFD;
}

.reference {
  margin: 20px 0;
}
.ai-assistant {
  background-color: rgba(240, 240, 240, 0.5);
  padding: 5px 20px 20px 20px;
  border-radius: 5px;
  border: #e1e1e1 1px solid;
}


.textarea {
  margin-bottom: 15px;
  padding: 10px;
  font-size: 14px;
  width: 100%;
  height: 80px;

}

button {
  padding: 10px 15px;
  background-color: #007bff;
  color: white;
  border: none;
  cursor: pointer;
}

button:hover {
  background-color: #0056b3;
}

.search-button {
  color: rgb(0, 58, 255);
  background-color: rgba(255, 255, 255, 0.5);
  border: 1px solid #656565;
}
.search-button:hover {
  color: rgb(255, 255, 255);
  background-color: rgb(14, 136, 255);
  border: 1px solid rgb(14, 136, 255);

}
</style>
