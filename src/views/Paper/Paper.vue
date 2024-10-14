<script setup>

import {Search} from "@element-plus/icons-vue";
import {ref} from "vue";
import { useTransition } from '@vueuse/core'


const citedNum = ref(0);
const citeNum = ref(0);
const collectNum = ref(0);
const commentNum = ref(0);
let citedNumChange = useTransition(citedNum, {
  duration: 500,
})
let citeNumChange = useTransition(citeNum, {
  duration: 500,
})
let collectNumChange = useTransition(collectNum, {
  duration: 500,
})
let commentNumChange = useTransition(commentNum, {
  duration: 500,
})

//统计值在这里
citedNum.value = 129
citeNum.value = 51
collectNum.value = 273
commentNum.value = 9
//

const question = ref("")

const submitQuestion = ()=> {
  console.log("Question submitted:", question);
      // Handle the form submission logic
}
const abstract = ref("Despite declining incidence and mortality rates in many countries, the abdominal aortic aneurysm (AAA) continues to represent a life-threatening cardiovascular condition with an overall prevalence of about 2–3% in the industrialized world. While the risk of AAA development is considerably higher for men of advanced age with a history of smoking, screening programs serve to detect the often asymptomatic condition and prevent aortic rupture with an associated death rate of up to 80%. This review summarizes the current knowledge on identified risk factors, the multifactorial process of pathogenesis, as well as the latest advances in medical treatment and surgical repair to provide a perspective for AAA management.\n");


</script>

<template>
  <div id="app" class="container">
    <div class="up-part">
      <div class = "main">
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

        <!-- Abstract Section -->
<!--        <div class="abstract">-->
<!--          <h2></h2>-->
<!--          <p>-->
<!--          </p>-->
<!--        </div>-->
        <div></div>
      </div>

      <div class="side">
        <el-card style="width: 320px;margin-bottom: 30px" shadow="always">
          <el-row style="margin-bottom: 15px">
            <el-col :span="12" style="padding-left: 12px">
              <el-statistic title="被引用次数" :value="citedNumChange" :value-style="{ color: '#132fc1' }" />
            </el-col>
            <el-col :span="12" style="padding-left: 12px">
              <el-statistic title="引用文章数" :value="citeNumChange" :value-style="{ color: '#45a801' }"/>
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
.container {
  padding: 0 20px 20px 0;
  max-width: 1100px;
  margin: auto;
  font-family: Arial, sans-serif;
  line-height: 1.6;
}

.up-part {
  display: flex;
  width: 1100px; /* Ensure the width is exactly 1100px */
  margin: -10px auto; /* Center the content */

}

.main {
  flex: 900px; /* Take more space for the main section */
  padding: 20px;
  margin: 0;
}

.side {
  flex: 320px; /* Take less space for the sidebar */
  padding: 50px 20px 20px 20px;

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
