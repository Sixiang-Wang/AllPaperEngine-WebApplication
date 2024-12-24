<!-- <template>
  <div class="hotspot-aside">
    <el-collapse v-model="activeName">
      <ThemeCollapseItem
          v-for="(themeGroup, index) in themeGroups"
          :key="index"
          :title="themeGroup.title"
          :name="themeGroup.name"
          :themes="themeGroup.themes"
          v-model:activeTheme="activeThemes[index]"
      />
    </el-collapse>
  </div>
</template> -->

<script setup>
// import { ref } from "vue";
// import ThemeCollapseItem from "@/components/ThemeCollapseItem.vue";

// const activeName = ref(["1"]);
// const activeThemes = ref([[], [], []]); // 存储每个折叠项的选择状态

// const themeGroups = ref([
//   {
//     title: "学科筛选",
//     name: "1",
//     themes: [
//       { label: "Social Sciences", value: "1" },
//       { label: "Physical Sciences", value: "2" },
//       { label: "Life Sciences", value: "3" },
//       { label: "Health Sciences", value: "4" },
//     ],
//   },
// ]);
</script>
<!-- 
<style scoped>
@import "@/css/search.css";

.hotspot-aside {
  padding: 20px;
}
</style> -->
