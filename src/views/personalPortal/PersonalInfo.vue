<script setup>
import {computed, onMounted, ref} from "vue";
import { ArrowRight } from "@element-plus/icons-vue";
import defaultAvatar from "@/assets/image/user.gif";
import router from "@/router/index.js";
import httpUtil from "@/api/http.js";
import * as cookieUtil from "@/utils/cookie.js";
import {ElMessage} from "element-plus";
import http from "@/api/http.js";  // 引入 API 相关功能

// avatar 头像
const avatarUrl = computed(()=>{
  const avatar = localStorage.getItem('avatar') || '/hahashenmedoumeiyou';
  return http.getUrlWithoutSlash() + avatar;
});

const userId = localStorage.getItem("userId");
const userName = localStorage.getItem("userName");
const authorId = ref([]);
let user = ref([]);
console.log(userName);

// 用户角色
const role = ref(1);

// 控制当前激活的标签
const activeName = ref('first');

// 导航到认证页面
const goToCertification = () => {
  console.log("跳转到认证页面...");
  router.push('/ScholarIdentify');
};

// 存储成果
const myAchievement = ref([]);
const searchedPapers = ref([]);

const isLoading = ref(true);

const firstPublishWorkCount = ref(0);
const firstHighQualityWorksCount = ref(0);
const HNumber = ref(0);
const citedCount = ref(0);
const worksCount = ref(0);

// 获取个人成果
onMounted(async () => {
  if(cookieUtil.getCookie("token") === null || cookieUtil.getCookie("token") === ''){
    ElMessage.error("请先登录！");
    setTimeout(()=>{
      router.push('/login');
    },500);
    return;
  }
  try {
    const res = await httpUtil.get('/claim/get/personal', { scholarId: userId });
    if (res.data && res.data.works) {
      myAchievement.value = res.data.works;
    }
  } catch (error) {
    console.error('数据加载失败', error);
  } finally {
    isLoading.value = false; // 请求完成后，修改加载状态
  }

  // firstPublishWorkCount = await httpUtil.get("/author/getFirstPublishWorkCountByAuthorId", {authorId: userId.value});

  user = await httpUtil.get("/user/getById", {userId: userId});
  authorId.value = user.data.user.authorId;
  let firstPublishWorkCountdata = await httpUtil.get("/author/getFirstPublishWorkCountByAuthorId", {authorId: authorId.value});
  firstPublishWorkCount.value = firstPublishWorkCountdata.data.getFirstPublishWorkCountByAuthorId;
  let firstHighQualityWorksCountdata = await httpUtil.get("/author/getHighQualityWorksCountByAuthorId", {authorId: authorId.value});
  firstHighQualityWorksCount.value = firstHighQualityWorksCountdata.data.getHighQualityWorksCountByAuthorId;
  let HNumberData = await httpUtil.get("/author/getHNumberByAuthorId", {authorId: authorId.value});
  HNumber.value = HNumberData.data.getHNumberByAuthorId;
  let citedCountData = await httpUtil.get("/author/getCitedCountByAuthorId", {authorId: authorId.value});
  citedCount.value = citedCountData.data.getCitedCountByAuthorId;
  let worksCountData = await httpUtil.get("/author/getWorksCountByAuthorId", {authorId: authorId.value});
  worksCount.value = worksCountData.data.getWorksCountByAuthorId;
  console.log(firstPublishWorkCount);
  console.log(firstHighQualityWorksCount);
  console.log(HNumber);
  console.log(citedCount);
  console.log(worksCount);
  console.log(authorId);

});

// 跳转到具体论文页面
const goToPaper = (id) => {
  router.push({ path: '/paper', query: { id: id } });
};

// 搜索相关函数
const simpleSearchInput = ref("");
const isSearched = ref(false);
const simpleSearch = async () => {
  if (simpleSearchInput.value.length === 0) {
    ElMessage.warning("请输入搜索内容！");
    return;
  }
  const res = await httpUtil.get('/search/getWorkByTitleWord', {
    page: 1,
    word: simpleSearchInput.value,
  });
  searchedPapers.value = res.data.works;
  isSearched.value = true;
  searchedPapers.value.forEach(paper => {
    paper.isSelected = false;
  });
};
</script>

<template>
  <el-card v-if="role !== 1" class="box-card" style="max-width: 75%">
    <div slot="header" class="clearfix">
      <span>学者认证</span>
    </div>
    <div class="content">
      <p>请先认证成为学者</p>
      <el-button type="primary" @click="goToCertification" class="auth-btn">去认证</el-button>
    </div>
  </el-card>

  <div v-if="role === 1">
    <el-card style="max-width: 75%">
      <el-row>
        <el-col :span="12">
          <el-row>
            <el-avatar
                :src="avatarUrl"
                :size="70"
                shape="square"
                fit="cover"
                class="user-avatar"
            />
            <div style="margin-left: 10%; margin-top: 5%">
              <el-text style="font-size: large">{{userName}}</el-text><br>
              <el-text style="font-size: small">学者ID：{{ authorId }}</el-text>
            </div>
          </el-row>
          <el-row style="margin-top: 15%">
            <el-text style="font-size: small">关注：0</el-text>
            <el-text style="font-size: small; margin-left: 2%">粉丝：0</el-text>
            <el-text style="font-size: small; margin-left: 2%">浏览：0</el-text>
          </el-row>
        </el-col>
        <el-col :span="12">
          <el-divider direction="vertical" />
          <el-text style="font-size: medium">根据您所认领的文献计算，各项指标如下：</el-text>
          <el-row style="margin-top:3%">
            <el-col :span="7" style="font-size:small">总发文量</el-col>
            <el-col :span="5" style="font-size:small">{{ worksCount }}</el-col>
            <el-col :span="7" style="font-size:small">第一作者发文量</el-col>
            <el-col :span="5" style="font-size:small">{{ firstPublishWorkCount }}</el-col>
          </el-row>
          <el-row style="margin-top:3%">
            <el-col :span="7" style="font-size:small">高影响力论文数</el-col>
            <el-col :span="5" style="font-size:small">{{ firstHighQualityWorksCount }}</el-col>
            <el-col :span="7" style="font-size:small">总被引用数</el-col>
            <el-col :span="5" style="font-size:small">{{ citedCount }}</el-col>
          </el-row>
          <el-row style="margin-top:3%">
            <el-col :span="7" style="font-size:small">H指数</el-col>
            <el-col :span="5" style="font-size:small">{{ HNumber }}</el-col>
          </el-row>
        </el-col>
      </el-row>
    </el-card>

    <el-card style="max-width: 75%; margin-top: 1.5%">
      <el-tabs v-model="activeName" class="demo-tabs" @tab-click="handleClick" type="border-card">
        <el-tab-pane label="成果管理" name="first">
            <el-table :data="myAchievement" stripe @rowDblclick="goToPaper">
              <el-table-column prop="title" label="论文名称" width="400"></el-table-column>
              <el-table-column prop="publicationDate" label="发表时间" width="180"></el-table-column>
              <el-table-column prop="cited" label="引用次数" width="90"></el-table-column>
              <el-table-column>
                <template #default="scope">
                  <el-button @click="goToPaper(scope.row.id)" color="#1F578F">点击查看</el-button>
                </template>
              </el-table-column>
              <el-table-column>
                <template #default="scope">
                  <el-button @click="deleteClaimed(scope.row.id)" color="#C00000">点击删除</el-button>
                </template>
              </el-table-column>
            </el-table>
        </el-tab-pane>

        <el-tab-pane label="学术关系网" name="third">Role</el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<style scoped>
/* 原样保留了原有的样式 */
.box-card {
  width: 350px;
  padding: 20px;
  text-align: center;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.box-card .clearfix span {
  font-size: 20px;
  font-weight: bold;
  color: #409eff;
}

.content p {
  font-size: 16px;
  color: #333;
  margin: 20px 0;
}

.auth-btn {
  font-size: 16px;
  padding: 10px 20px;
  margin-top: 20px;
  border-radius: 5px;
  width: 100%;
  background-color: #409eff;
  border-color: #409eff;
}

.auth-btn:hover {
  background-color: #66b1ff;
  border-color: #66b1ff;
}
</style>