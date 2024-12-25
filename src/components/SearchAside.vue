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
          @change="updateActiveThemes(index, $event)"
          :search-input="searchInput"
      />
    </el-collapse>
  </div>
</template>

<script setup>
import { ref } from "vue";
import ThemeCollapseItem from "@/components/ThemeCollapseItem.vue";
import {ElMessage} from "element-plus";

const activeName = ref(["1"]);
const activeThemes = ref([[], []]); // 每个折叠项的选中项，长度与 themeGroups 保持一致
defineProps({
  searchInput: String
})
const yearTag = ref([
  {value: "2024" },
  {value: "2023" },
  {value: "2022" },
  {value: "2021" },
  {value: "2020" },
  {value: "2019" },
  {value: "2018" },
  {value: "2017" },
  {value: "2016" },
  {value: "2015" },
])
const themeGroups = ref([
  {
    title: "发表年份",
    name: "0",
    themes: [
      { label: "2024", value: "2024" },
      { label: "2023", value: "2023" },
      { label: "2022", value: "2022" },
      { label: "2021", value: "2021" },
      { label: "2020", value: "2020" },
      { label: "2019", value: "2019" },
      { label: "2018", value: "2018" },
      { label: "2017", value: "2017" },
      { label: "2016", value: "2016" },
      { label: "2015及以前", value: "2015" },
    ],
  },
  {
    title: "文章类型",
    name: "1",
    themes: [
      {label: "文章", value: "article"},
      {label: "书籍", value: "book-chapter"},
      {label: "报告", value: "report"},
      {label: "专题", value: "dissertation"},
      {label: "其他", value: "other"},
    ],
  },
]);

// 更新 activeThemes 中的选中项
const updateActiveThemes = (index, selectedThemes) => {
  console.log(index)
  activeThemes.value[index] = selectedThemes;
};
</script>

<style scoped>
@import "@/css/search.css";

.search-aside {
  padding: 20px;
}
</style>
