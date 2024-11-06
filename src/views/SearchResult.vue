<script setup>
import SearchAside from "@/components/SearchAside.vue";
import {Search} from "@element-plus/icons-vue";
import {computed, onMounted, ref, watch} from "vue";
import SingleResult from "@/components/SingleResult.vue";
import router from "@/router/index.js";
import { useRoute } from 'vue-router';
import httpUtil from "@/api/http.js";

const searchInput = ref("");
const searchType = ref("主题");
const totalLength = ref(0);
const searchResults = ref([]);
const route = useRoute();
const currentPage = ref(Number(route.query.page) || 1); // 确保 currentPage 从 URL 获取

const pageSize = computed(() => Math.ceil(totalLength.value / 20));

const handlePageChange = (page) => {
  router.push({path: "/search", query: {input: route.query.input, page: page}}).then(() => {
    currentPage.value = page;
    updateSearchResults();
    window.scrollTo({top: 0});
  });
};

const updateSearchResults = async () => {
  const res = await httpUtil.get('/openalex/get/page', {
    page: currentPage.value
  });
  console.log(res.data);
  searchResults.value = res.data.works;
};

onMounted(async () => {
  const res = await httpUtil.get('/openalex/get/page', {
    page: currentPage.value
  });
  const res2 = await httpUtil.get('/openalex/get/length');
  totalLength.value = res2.data.leng;
  console.log(totalLength.value);
  searchResults.value = res.data.works;
  console.log(searchResults.value);
  console.log(res.data.works);
});

// 监听路由变化，以便更新 currentPage
watch(route, (newRoute) => {
  currentPage.value = Number(newRoute.query.page) || 1;
  updateSearchResults();
});
</script>

<template>
  <el-container>
    <el-header>
      <div>
        <div style="background-color: transparent !important;">
          <el-input v-model="searchInput" class="search-input" placeholder="请输入搜索内容">
            <template #prepend>
              <el-select v-model="searchType" style="width: 115px; background-color:#FFFFFF;">
                <el-option label="主题" value="1"/>
                <el-option label="篇名" value="2"/>
                <el-option label="关键词" value="3"/>
              </el-select>
            </template>
          </el-input>
          <el-button :icon="Search" @click="search" class="search-button"/>
        </div>
      </div>
    </el-header>
    <el-container>
      <el-aside>
        <SearchAside/>
      </el-aside>
      <el-main style="margin-left: 2%;">
        <span class="search-result-statistic">共查询到{{ totalLength }}个结果，当前为第{{ currentPage }}页</span>
        <div v-if="searchResults.length !== 0" style="display: flex;">
          <div>
            <SingleResult v-for="result in searchResults" :key="result.id" :author="result.paperInformation"
                          :content="result.abstractText"
                          :title="result.title" :cited="result.cited" :id="result.id"/>
          </div>
        </div>
        <div v-else style="margin-top: 4%; max-width: 70%">
          <el-skeleton :rows="3" animated/>
          <el-divider/>
          <el-skeleton :rows="3" animated/>
          <el-divider/>
          <el-skeleton :rows="3" animated/>
          <el-divider/>
          <el-skeleton :rows="3" animated/>
          <el-divider/>
          <el-skeleton :rows="3" animated/>
          <el-divider/>
        </div>
      </el-main>
    </el-container>
    <el-footer>
      <div style="display: flex; justify-content: center;">
        <el-pagination background layout="prev, pager, next"
                       :total="totalLength"
                       :page-size="20"
                       :current-page="currentPage"
                       @current-change="handlePageChange"
        />
      </div>
    </el-footer>
  </el-container>
</template>

<style scoped>
@import "@/css/searchResult.css";

:deep(.el-select__wrapper) {
  height: 40px;
  --el-input-focus-border-color: #FFFFFF;
}

.search-button {
  border-radius: 0 4px 4px 0; /* 使按钮圆角与输入框拼接 */
  height: 40px;
  background-color: transparent;
  border-left-width: 0;
}

.el-aside::-webkit-scrollbar {
  display: none;
}
</style>
