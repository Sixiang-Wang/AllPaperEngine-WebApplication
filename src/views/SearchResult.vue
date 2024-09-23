<script setup>

import SearchAside from "@/components/SearchAside.vue";
import {Search} from "@element-plus/icons-vue";
import {ref} from "vue";
import SingleResult from "@/components/SingleResult.vue";
import Advertisement from "@/components/Advertisement.vue";

const searchInput = ref("");
const searchType = ref("主题");
const searchResults = ref([
  {
    title: "Diffusion in binary solutions. Variation of diffusion coefficient with composition",
    author: "A Vignes - Industrial & Engineering Chemistry Fundamentals, 1966 - ACS Publications",
    content: "… The coefficient of interdiffusion, experimentally determined from Pick's second law, is the\n" +
        "product of an activity-corrected diffusion coefficient and a thermodynamic factor which …",
    cited: "114"
  },
  {
    title: "Gaseous diffusion coefficients",
    author: "TR Marrero, EA Mason - Journal of Physical and Chemical Reference …, 1972 - pubs.aip.org",
    content: "… Diffusion coefficients of binary mixtures of dilute gases are … Almost every gaseous diffusion\n" +
        "coefficient which was experimentally … In addition, diffusion coefficients for several mixtures are …",
    cited: "115"
  }
]);
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
        <span class="search-result-statistic">共查询到{{ searchResults.length }}个结果</span>
        <div style="display: flex;">
          <div>
            <SingleResult v-for="result in searchResults" :author="result.author" :content="result.content"
                          :title="result.title" :cited="result.cited "></SingleResult>
          </div>
          <div>
            <Advertisement/>
          </div>
        </div>
      </el-main>
    </el-container>
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