<script setup>
import {computed, onMounted, ref,watch} from "vue";
import {Search,Plus,Minus} from "@element-plus/icons-vue";
import httpUtil from "@/api/http.js"
import router from "@/router/index.js";
import {ElMessage} from "element-plus";
import * as cookieUtil from "@/utils/cookie.js";
import SingleResultInAcademicClaim from "@/components/single/SingleResultInAcademicClaim.vue";
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

const searchInput = ref("");
const searchType = ref('1');
const totalLength = ref(0);
let searchResults = ref([]);

const simpleSearch = async () => {
  console.log(searchType.value);
  switch (searchType.value) {
    case '1'://按标题查找
      //为方便测试，这里保留搜索所有结果的接口
      console.log("begin search")
      if(searchInput.value===null||searchInput.value === ''){
        const res = await httpUtil.get('/openalex/get/page',{
          page: currentPage.value
        })
        console.log("search in openalex/get/page");
        searchResults.value = res.data.works;
        const res2 = await httpUtil.get('/openalex/get/length');
        totalLength.value = res2.data.leng;
      }else {
        // const res = await httpUtil.get('/elasticSearch/works/getByTitleOrAbstractOrKeywords', {
        //   word: searchInput.value,
        //   page: currentPage.value
        // })
        const res = await httpUtil.get('/elasticSearch/works/getByTitleByPage', {
          title: searchInput.value,
          page: currentPage.value
        })

        searchResults.value = res.data.works || [];
        console.log(searchResults.value);
        totalLength.value = res.data.page;

        for(let result of searchResults.value){
          if(result.highlightFields.hasOwnProperty('title')){
            result.content.title = result.highlightFields.title[0];
          }
          if(result.highlightFields.hasOwnProperty('abstract')){
            const startIndex = result.highlightFields.abstract[0].indexOf("<span style='color:red'>");
            if(startIndex!=-1){
              result.content.abstractText = result.highlightFields.abstract[0].substring(startIndex);
            }else{
              result.content.abstractText = result.highlightFields.abstract[0];
            }
          }
          // if(result.highlightFields.hasOwnProperty('keywordsText')){
          //   result.content.abstractText = result.highlightFields.abstract[0];
          // }
        }
      }

      break;
    case '2'://查找篇名
      console.log(2)
      if(searchInput.value===null||searchInput.value === ''){
        const res = await httpUtil.get('/openalex/get/page',{
          page: currentPage.value
        })
        // console.log(res);
        searchResults.value = res.data.works;
        // console.log(searchResults.value);
        const res2 = await httpUtil.get('/openalex/get/length');
        totalLength.value = res2.data.leng;
        // console.log(totalLength.value);
      }else {
        // const res = await httpUtil.get('/elasticSearch/works/getByTitleOrAbstractOrKeywords', {
        //   word: searchInput.value,
        //   page: currentPage.value
        // })
        const res = await httpUtil.get('/elasticSearch/works/getByTitleByPage', {
          title: searchInput.value,
          page: currentPage.value
        })
        searchResults.value = res.data.works || [];
        console.log(searchResults.value);
        totalLength.value = searchResults.value.length;

        for(let result of searchResults.value){
          if(result.highlightFields.hasOwnProperty('title')){
            result.content.title = result.highlightFields.title[0];
          }
          if(result.highlightFields.hasOwnProperty('abstract')){
            const startIndex = result.highlightFields.abstract[0].indexOf("<span style='color:red'>");
            result.content.abstractText = result.highlightFields.abstract[0].substring(startIndex);
          }else{
            result.content.abstractText = result.highlightFields.abstract[0];
          }
        }
        // if(result.highlightFields.hasOwnProperty('keywordsText')){
        //   result.content.abstractText = result.highlightFields.abstract[0];
        // }
      }

      break;

    case '3'://查找关键词
      if(searchInput.value===null||searchInput.value === ''){
        const res = await httpUtil.get('/openalex/get/page',{
          page: currentPage.value
        })
        // console.log(res);
        searchResults.value = res.data.works;
        // console.log(searchResults.value);
        const res2 = await httpUtil.get('/openalex/get/length');
        totalLength.value = res2.data.leng;
        // console.log(totalLength.value);
      }else {
        // const res = await httpUtil.get('/elasticSearch/works/getByTitleOrAbstractOrKeywords', {
        //   word: searchInput.value,
        //   page: currentPage.value
        // })
        const res = await httpUtil.get('/elasticSearch/works/getByTitleByPage', {
          title: searchInput.value,
          page: currentPage.value
        })
        searchResults.value = res.data.works || [];
        console.log(searchResults.value);
        totalLength.value = searchResults.value.length;

        for(let result of searchResults.value){
          if(result.highlightFields.hasOwnProperty('title')){
            result.content.title = result.highlightFields.title[0];
          }
          if(result.highlightFields.hasOwnProperty('abstract')){
            const startIndex = result.highlightFields.abstract[0].indexOf("<span style='color:red'>");
            if(startIndex!=-1){
              result.content.abstractText = result.highlightFields.abstract[0].substring(startIndex);
            }else{
              result.content.abstractText = result.highlightFields.abstract[0];
            }
          }
          // if(result.highlightFields.hasOwnProperty('keywordsText')){
          //   result.content.abstractText = result.highlightFields.abstract[0];
          // }
        }
      }
      break;
    case '4'://查找摘要
      if(searchInput.value===null||searchInput.value === ''){
        const res = await httpUtil.get('/openalex/get/page',{
          page: currentPage.value
        })
        // console.log(res);
        searchResults.value = res.data.works;
        // console.log(searchResults.value);
        const res2 = await httpUtil.get('/openalex/get/length');
        totalLength.value = res2.data.leng;
        // console.log(totalLength.value);
      }else {
        const res = await httpUtil.get('/elasticSearch/works/getByTitleOrAbstractOrKeywords', {
          word: searchInput.value,
          page: currentPage.value
        })

        searchResults.value = res.data.works || [];
        console.log(searchResults.value);
        totalLength.value = searchResults.value.length;

        for(let result of searchResults.value){
          if(result.highlightFields.hasOwnProperty('title')){
            result.content.title = result.highlightFields.title[0];
          }
          if(result.highlightFields.hasOwnProperty('abstract')){
            const startIndex = result.highlightFields.abstract[0].indexOf("<span style='color:red'>");
            if(startIndex!=-1){
              result.content.abstractText = result.highlightFields.abstract[0].substring(startIndex);
            }else{
              result.content.abstractText = result.highlightFields.abstract[0];
            }
          }
          // if(result.highlightFields.hasOwnProperty('keywordsText')){
          //   result.content.abstractText = result.highlightFields.abstract[0];
          // }
        }
      }
      //searchResults = res;
      break;
    case '5'://查找学者
      if(searchInput.value===null||searchInput.value === '') {
        const res = await httpUtil.get('/openalex/get/page', {
          page: currentPage.value
        });
        console.log("search in openalex/get/page");
        searchResults.value = res.data.works;
        const res2 = await httpUtil.get('/openalex/get/length');
        totalLength.value = res2.data.leng;
      } else {
        const res = await httpUtil.get('/author/getAuthorIdByAuthorName', {
          authorName: searchInput.value
        });
        authorIds.value = res.data.getAuthorIdByAuthorName || [];
        console.log(authorIds.value);
        totalLength.value = authorIds.value.length;
        authorInfos.value = [];
        for (let authorId of authorIds.value) {
          const authorRes = await httpUtil.get('/author/getById', {
            id: authorId
          });
          console.log(1);
          console.log(authorRes.data.authors);
          authorInfos.value.push(authorRes.data.authors);
        }
        searchResults.value = authorInfos.value;
        console.log(authorInfos);
      }
      break;
  }
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

const simpleCheckList = ref(["1", "2"])
const id = ref("");
const submit = async () => {
  const res = await httpUtil.get('/claim/add',{
    "userId": 1,//这里后续从cookie读取
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
  const res = await httpUtil.get('/claim/get/personal',
      {scholarId: 1//TODO：后续修改为从cookie读取
      });
  myAchievement.value = res.data.works;
})
const deleteClaimed = async (id )=>{
  const res = await httpUtil.get('/claim/delete',{
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
onMounted(()=>{
  if(cookieUtil.getCookie("token") === null || cookieUtil.getCookie("token") === ''){
    ElMessage.error("请先登录！");
    setTimeout(()=>{
      router.push('/login');
    },500);
  }
})
</script>

<template>
  <el-card v-model="searchDialog" style="height: 100%">
    <span class="academic-dialog-title">检索认领</span>
    <el-divider/>
    <el-tabs tab-position="left" style="height: 100%" class="demo-tabs">
      <el-tab-pane label="普通检索">
        <el-input v-model="searchInput" class="search-input" placeholder="请输入搜索内容" @keyup.enter="simpleSearch">
          <template #prepend>
            <el-select v-model="searchType" style="width: 115px">
              <el-option label="标题" value="1"/>
            </el-select>
          </template>
        </el-input>
        <el-button :icon="Search" @click="simpleSearch" class="claim-button"/>
        <div v-if="isSearched" style="margin-top: 3%; max-height: 100%; overflow-y: auto;">
          <div style="max-height: 100%; overflow-y: auto;">
            <SingleResultInAcademicClaim v-for="result in searchResults" :key="result.content.id" :author="result.paperInformation"
                          :content="result.content.abstractText"
                          :title="result.content.title" :cited="result.content.cited_by_count" :id="result.content.id"/>
            <!-- <el-table :data="searchedPapers" @row-click="handleRowDbClick" show-overflow-tooltip :row-style="rowStyle" >
              <el-table-column prop="title" label="论文名称" width="250"></el-table-column>
              <el-table-column prop="publicationDate" label="发表时间" width="150"></el-table-column>
              <el-table-column prop="publication" label="发表期刊" width="100"></el-table-column>
              <el-table-column prop="search">
                <template #default="scope">
                  <el-button @click="goToPaper(scope.row.id)" color="#1F578F">查看论文</el-button>
                </template>
              </el-table-column>
            </el-table> -->
          </div>
        </div>
      </el-tab-pane>
    </el-tabs>
  </el-card>
</template>

<style scoped>
@import "@/css/basic.css";
@import "@/css/academicClaim.css";
.search-input{
  height: 40px;
}
.plus-minus-button{
  color:rgb(105, 104, 104);
  size:5px;
}
.head{
  background-color: rgb(41, 154, 236);
  width:100px;
  height:35px;
  color: white;
  padding: 10px;
  border-radius: 3px;
  margin-top: 10px;
  font-size: 24px;
  margin-left: 9%;
}
.hStyle{
  font-size: 20px;
  margin-left: 5%;
  color:rgb(77, 76, 76);
}
.pStyle{
  font-size: 14px;
  font-weight: lighter;
  margin-left: 5%;
  margin-right: 5%;
  color:grey;
}
.reset-button {
  border-radius: 0 4px 4px 0;
  height: 40px;
  color: rgba(0, 0, 0, 0.526);
  background-color: transparent;
  border: none;
}
.reset-button:hover {
  color: rgb(78, 109, 210);
  background-color: rgba(255, 255, 255, 0.05);
}
.search-button {
  border-radius: 0 4px 4px 0;
  height: 30px;
  width:140px;
  font-size: large;
  color: white;
  background-color: #f77605 ;
}
.search-button:hover {
  color: white;
  background-color: #cb6e1c;
}
.line{
  border: none;
  height: 0.5px;
  width:90%;
  background-color: grey;
}
.el-table__row:hover > td {
  background-color: transparent !important;
}

</style>

