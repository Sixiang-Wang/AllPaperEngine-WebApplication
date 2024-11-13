<script setup>
import {computed, onMounted, ref,watch} from "vue";
import {Search,Plus,Minus} from "@element-plus/icons-vue";
import httpUtil from "@/api/http.js"
import router from "@/router/index.js";
import {ElMessage} from "element-plus";
const myAchievement = ref([

]);
//  {
// name: "Searching for Storiness: Story-Generation from a Reader's Perspective",
//     date: "2024-10-20",
//     publication: "xxx期刊",
//     cited: "114"
// }
const searchedPapers = ref([]);
const currentPage = ref(1);
const isSearched = ref(false);
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
const goToPaper = (id)=>{
  router.push({path:'/paper', query:{id: id}})
}
const activeName = ref("1");
const searchDialog = ref(false);
const handleDialogChange = () => {
  searchDialog.value = !searchDialog.value;
}
const simpleSearchInput = ref("");
const simpleSearchType = ref('1');
const simpleCheckList = ref(["1", "2"])
const id = ref("");
const submit = async () => {
  const res = await httpUtil.get('/scholar/claim/add',{
    "scholarId": 1,//这里后续从cookie读取
    "workId": id.value
  })
  console.log(res.data);
  ElMessage.success("申领成功！");
  location.reload();
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
onMounted(async()=>{
  const res = await httpUtil.get('/scholar/claim/get/personal',
      {scholarId: 1//TODO：后续修改为从cookie读取
      });
  myAchievement.value = res.data.works;
})
const deleteClaimed = async (id )=>{
  const res = await httpUtil.get('/scholar/claim/delete',{
    scholarId: 1,//TODO：后续修改为从cookie读取
    workId: id
  })
  console.log(res.data);
  if(res.data.msg === "delete success"){
    ElMessage.success("删除成功！");
    location.reload();
  }else{
    ElMessage.error("删除失败，请稍后再试")
  }
}

//高级检索部分
const searchRows = ref([
    { searchInput: '', searchType: '1', logicOperator: ''},
    { searchInput: '', searchType: '2', logicOperator: ''},
    { searchInput: '', searchType: '3', logicOperator: ''}
]);

const minusSearchBox = (index) => {
    if (searchRows.value.length > 1) {
        searchRows.value.splice(index, 1);
    }
};

const addSearchBox = () => {
    if (searchRows.value.length < 10) { // 最多可保持十行
        searchRows.value.push({ searchInput: '', searchType: '1', logicOperator: ''});
    } else {
        console.log('最多只能添加10行搜索条件。');
    }
};

// 监听搜索行变化
watch(searchRows, (newVal, oldVal) => {
    console.log('Search rows changed:', newVal);
});

const checked1 = ref(true)
const checked2 = ref(false)
const checked3 = ref(false)
const checked4 = ref(false)
const checked5 = ref(false)
const checked6 = ref(false)

const timeRange = ref(null);

const shortcuts = [
{
text: 'Last week',
value: () => {
    const end = new Date()
    const start = new Date()
    start.setTime(start.getTime() - 3600 * 1000 * 24 * 7)
    return [start, end]
},
},
{
text: 'Last month',
value: () => {
    const end = new Date()
    const start = new Date()
    start.setTime(start.getTime() - 3600 * 1000 * 24 * 30)
    return [start, end]
},
},
{
text: 'Last 3 months',
value: () => {
    const end = new Date()
    const start = new Date()
    start.setTime(start.getTime() - 3600 * 1000 * 24 * 90)
    return [start, end]
},
},
]

// 方法：重置条件
const resetConditions = () => {
    searchRows.value = [
        { searchInput: '', searchType: '1', logicOperator: '' },
        { searchInput: '', searchType: '2', logicOperator: ''},
        { searchInput: '', searchType: '3', logicOperator: ''}
    ];
    checked1.value = false;
    checked2.value = false;
    checked3.value = false;
    checked4.value = false;
    checked5.value = false;
    checked6.value = false;
    timeRange.value = null; // 重置日期选择器
};

const advancedSearch = () => {
    console.log(111);
    router.push({path: "/search", query: {input: searchRows.value[0].searchInput, page: 1}});
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
    <el-table :data="myAchievement" stripe @rowDblclick="goToPaper">
      <el-table-column prop="title" label="论文名称" width="400"></el-table-column>
      <el-table-column prop="publicationDate" label="发表时间" width="180"></el-table-column>
<!--      <el-table-column prop="publication" label="发表期刊" width="300"></el-table-column>-->
      <el-table-column prop="cited" label="引用次数" width="180"></el-table-column>
      <el-table-column>
        <template #default="scope">
          <el-button @click="goToPaper(scope.row.id)" color="#1F578F">点击查看</el-button>
        </template>
      </el-table-column>
      <el-table-column>
        <template #default="scope">
<!--          TODO: 后续改成cookie读取-->
          <el-button @click="deleteClaimed(scope.row.id)" color="#C00000">点击删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-card>
  <el-dialog v-model="searchDialog" style="height: 500px">
    <span class="academic-dialog-title">检索认领</span>
    <el-divider/>
    <el-tabs tab-position="left" style="height: 330px" class="demo-tabs">
      <el-tab-pane label="普通检索">
        <el-input v-model="simpleSearchInput" class="search-input" placeholder="请输入搜索内容" @keyup.enter="simpleSearch">
          <template #prepend>
            <el-select v-model="simpleSearchType" style="width: 115px">
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
            <el-table :data="searchedPapers" @row-click="handleRowDbClick" show-overflow-tooltip :row-style="rowStyle" >
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
      </el-tab-pane>
      <el-tab-pane label="高级检索">
        <div :style="{height:'300px',overflowY: 'auto'}">
            <el-row :gutter="10" v-for="(row, index) in searchRows" :key="index" style="margin: 20px;">                
              <el-col :span="index === 0 ? 0 : 3">
                  <el-select v-if="index !== 0" v-model="row.logicOperator" placeholder="AND">
                      <el-option label="AND" value="AND" />
                      <el-option label="OR" value="OR" />
                      <el-option label="NOT" value="NOT" />
                  </el-select>
              </el-col>
              <el-col :span="index === 0 ? 3 : 0"/>
              <el-col :span="17">
                  <el-input v-model="row.searchInput" :style="{width:'100%'}" placeholder="请输入搜索内容">
                      <template #prepend>
                      <el-select v-model="row.searchType" style="width: 115px" placeholder="主题">
                        <el-option label="主题" value="1" />
                        <el-option label="篇名" value="2" />
                        <el-option label="关键词" value="3" />
                        <el-option label="作者" value="4" />
                        <el-option label="第一作者" value="5" />
                        <el-option label="作者单位" value="6" />
                        <el-option label="摘要" value="7" />
                        <el-option label="DOI" value="8" />
                        <el-option label="参考文献" value="9" />
                        <el-option label="文献来源" value="10" />
                      </el-select>
                      </template>
                  </el-input>
              </el-col>
              <el-col :span="index === 0 ? 0 : 2" v-if="index !== 0">
                  <el-button type="text" :icon="Minus" class="plus-minus-button" @click="minusSearchBox(index)"  />
              </el-col>
              <el-col :span="2" v-if="index === searchRows.length - 1">
                  <el-button type="text" :icon="Plus"  class="plus-minus-button" @click="addSearchBox"  />
              </el-col>
          </el-row>
          <el-row style="margin: 20px;margin-left: 9%;">
              <el-checkbox v-model="checked1" label="A类期刊会议" size="large" />
              <el-checkbox v-model="checked2" label="网络首发" size="large" />
              <el-checkbox v-model="checked3" label="免费" size="large" />
              <el-checkbox v-model="checked4" label="基金文献" size="large" />
              <el-checkbox v-model="checked5" label="中英文扩展" size="large" />
              <el-checkbox v-model="checked6" label="同义词扩展" size="large" />
          </el-row>
          <el-row  type="flex" justify="center">
              <div class="block">
                  <span style="font-size: 14px;margin-right: 20px;margin-left: 20px;">发表时间范围:</span>
                  <el-date-picker
                      v-model="timeRange"
                      type="daterange"
                      unlink-panels
                      range-separator="To"
                      start-placeholder="Start date"
                      end-placeholder="End date"
                      :shortcuts="shortcuts"
                      :size="size"
                  />
              </div>
          </el-row>
          <hr style="width:90%;height:0.5px;background-color: grey;margin-top: 20px;border:none">
          <el-row type="flex" justify="center" style="margin-top:20px">
              <el-button class="reset-button" @click="resetConditions">重置条件</el-button>
              <el-button class="search-button"@click="advancedSearch">检索</el-button>
          </el-row>
        </div>
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
@import "@/css/advancedSearch.css";
.el-table__row:hover > td {
  background-color: transparent !important;
}

</style>

