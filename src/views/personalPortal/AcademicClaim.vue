<script setup>
import {computed, onMounted, ref} from "vue";
import {Search} from "@element-plus/icons-vue";

const myAchievement = [
  {
    name: "Searching for Storiness: Story-Generation from a Reader's Perspective",
    date: "2024-10-20",
    publication: "xxx期刊",
    cited: "114"
  }
];
let searchedPapers = [];
const isSearched = ref(false);
const simpleSearch = () => {
  searchedPapers = [
    {
      name: "Searching for Storiness: Story-Generation from a Reader's Perspective",
      date: "2024-10-20",
      publication: "xxx期刊",
      cited: "114"
    },
    {
      name: "Searching for Storiness: Story-Generation from a Reader's Perspective",
      date: "2024-10-20",
      publication: "xxx期刊",
      cited: "114"
    },
    {
      name: "Searching for Storiness: Story-Generation from a Reader's Perspective",
      date: "2024-10-20",
      publication: "xxx期刊",
      cited: "114"
    },
  ];
  isSearched.value = true;
}
const activeName = ref("1");
const searchDialog = ref(false);
const handleDialogChange = () => {
  searchDialog.value = !searchDialog.value;
}
const simpleCheckList = ref(["1", "2"])
const submit = async () => {
  await console.log("submit success");
}
</script>

<template>
  <el-card style="height: 690px;">
    <template #header>
      <el-row class="component-span-align">
        <el-col :span="2">
          <span class="academic-claim-title">成果认领</span>
        </el-col>
        <el-col :span="3">
          <el-button color="#1F578F" @click="handleDialogChange">检索认领</el-button>
        </el-col>
      </el-row>
    </template>
    <el-table :data="myAchievement" stripe>
      <el-table-column prop="name" label="论文名称" width="360"></el-table-column>
      <el-table-column prop="date" label="发表时间" width="180"></el-table-column>
      <el-table-column prop="publication" label="发表期刊" width="300"></el-table-column>
      <el-table-column prop="cited" label="引用次数" width="180"></el-table-column>
    </el-table>
  </el-card>
  <el-dialog v-model="searchDialog">
    <span class="academic-dialog-title">检索认领</span>
    <el-divider/>
    <el-tabs tab-position="left" style="height: 200px" class="demo-tabs">
      <el-tab-pane label="普通检索">
        <el-input v-model="searchInput" class="search-input" placeholder="请输入搜索内容">
          <template #prepend>
            <el-select v-model="searchType" style="width: 115px">
              <el-option label="主题" value="1"/>
              <el-option label="篇名" value="2"/>
              <el-option label="关键词" value="3"/>
            </el-select>
          </template>
        </el-input>
        <el-button :icon="Search" @click="simpleSearch" class="search-button"/>
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
            <el-table :data="searchedPapers" show-overflow-tooltip>
              <el-table-column prop="name" label="论文名称" width="300"></el-table-column>
              <el-table-column prop="date" label="发表时间" width="180"></el-table-column>
              <el-table-column prop="publication" label="发表期刊" width="100"></el-table-column>
            </el-table>
          </div>
        </div>
      </el-tab-pane>
      <el-tab-pane label="高级检索">
      </el-tab-pane>
    </el-tabs>
    <template #footer>
      <el-button @click="handleDialogChange" color="#1F578F" plain>返回</el-button>
      <el-button @click="submit" color="#1F578F">提交申请</el-button>
    </template>
  </el-dialog>
</template>

<style scoped>
@import "@/css/basic.css";
@import "@/css/academicClaim.css";
</style>

