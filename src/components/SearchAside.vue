<template>
  <div class="search-aside">
    <el-collapse v-model="activeName">
      <ThemeCollapseItem
          v-for="(themeGroup, index) in themeGroups"
          :key="index"
          :title="themeGroup.title"
          :name="themeGroup.name"
          :themes="themeGroup.themes"
          :activeTheme="activeThemes[index]"
      />
    </el-collapse>
  </div>
</template>

<script setup>
import { ref, onMounted} from 'vue'
import ThemeCollapseItem from "@/components/ThemeCollapseItem.vue";
import http from "@/api/http.js";
import {ElMessage} from "element-plus";


const activeName = ref(["1"]);
const activeThemes = ref([[], [], [], [], []]); // Stores the selection state of each collapse item

const themeGroups = ref([
  {
    title: "主题",
    name: "1",
    themes: [
      // { label: "性能研究", value: "1" },
      // { label: "复合材料", value: "2" },
      // { label: "光催化", value: "3" },
    ],
  },
  {
    title: "发表年份",
    name: "2",
    themes: [
      // { label: "纳米材料", value: "4" },
      // { label: "生物材料", value: "5" },
      // { label: "高分子材料", value: "6" },
    ],
  },
  {
    title: "文章类型",
    name: "3",
    themes: [
      // { label: "人工智能", value: "7" },
      // { label: "机器学习", value: "8" },
      // { label: "深度学习", value: "9" },
    ],
  },
  {
    title: "语言",
    name: "4",
    themes: [
      // { label: "大数据", value: "10" },
      // { label: "云计算", value: "11" },
      // { label: "物联网", value: "12" },
    ],
  },
  {
    title: "主题 5",
    name: "5",
    themes: [
      { label: "区块链", value: "13" },
      { label: "量子计算", value: "14" },
      { label: "边缘计算", value: "15" },
    ],
  },
]);


const fetchKeywords = async () => {
  try {
    const response = await http.get("/Connective/getSortedKeywords");
    const keywords = response.data.keywords;

    themeGroups.value[0].themes = keywords.map((keyword, idx) => ({
      label: keyword,
      value: (idx + 1).toString(),
    }));

  } catch (error) {
    console.error("Failed to fetch keywords:", error);
    ElMessage.error("获取关键词失败！");
  }
};

onMounted(fetchKeywords);

const fetchYears = async () => {
  try {
    const response = await http.get("/Connective/getSortedPublicationYears");
    const years = response.data.years;

    themeGroups.value[1].themes = years.map((year, idx) => ({
      label: year,
      value: (idx + 1).toString(),
    }));

  } catch (error) {
    console.error("Failed to fetch years:", error);
    ElMessage.error("获取发表年份失败！");
  }
};

onMounted(fetchYears);

const fetchTypes = async () => {
  try {
    const response = await http.get("/Connective/getSortedTypes");
    const types = response.data.types;

    themeGroups.value[2].themes = types.map((type, idx) => ({
      label: type,
      value: (idx + 1).toString(),
    }));

  } catch (error) {
    console.error("Failed to fetch types:", error);
    ElMessage.error("获取文章类型失败！");
  }
};

onMounted(fetchTypes);

const fetchLanguages = async () => {
  try {
    const response = await http.get("/Connective/getSortedLanguages");
    const languages = response.data.languages;

    themeGroups.value[3].themes = languages.map((language, idx) => ({
      label: language,
      value: (idx + 1).toString(),
    }));

  } catch (error) {
    console.error("Failed to fetch languages:", error);
    ElMessage.error("获取语言失败！");
  }
};

onMounted(fetchLanguages);

</script>

<style scoped>
@import "@/css/search.css";

.search-aside {
  padding: 20px;
}
</style>