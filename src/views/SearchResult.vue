<script setup>
import SearchAside from "@/components/SearchAside.vue";
import {Search} from "@element-plus/icons-vue";
import {computed, onMounted, ref, watch} from "vue";
import SingleResult from "@/components/single/SingleResult.vue";
import router from "@/router/index.js";
import {useRoute} from 'vue-router';
import httpUtil from "@/api/http.js";


let resStore = ref([])
let searchInput = ref("");
let searchType = ref('1');
const totalLength = ref(0);
let searchResults = ref([]);
let suggestions = ref([]);
let showAutoComplete = ref(false);
const route = useRoute();
let hoveredIndex = ref(null);
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
  console.log(searchInput.value)
  const res = await httpUtil.get('/openalex/get/page', {
    page: currentPage.value
  });
  console.log(res.data);
  searchResults.value = res.data.works;
};



const search = async () => {
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

    
      
  }

}
onMounted(async ()=>{
  searchType.value = route.query.type;
  searchInput.value = route.query.input;
  await search();
}, );
const mainSearch = ()=>{
  router.push({path: "/search", query: {input: searchInput.value, page: 1,type: searchType.value}});
  // location.reload();
}
// 监听路由变化，以便更新 currentPage
watch(route, (newRoute) => {
  currentPage.value = Number(newRoute.query.page) || 1;
  searchInput.value = Text(newRoute.query.input) || "";
  searchType.value = newRoute.query.type || '1';
  searchResults.value = newRoute.query.searchResult || [];
  // updateSearchResults();
});

const handleInputChange = async () => {
  console.log(searchInput.value)
  if (searchInput.value == '') {
    showAutoComplete = false;
  } else {
    showAutoComplete = true;
  }

  if (searchInput.value.length > 0) {
    let res = null;
    switch (searchType.value){
      case '1':
        //主题
        res = await httpUtil.get('/elasticSearch/works/autoCompleteTitleWithCompletionSuggester', {
          searchContent: searchInput.value 
        });
        const titleSuggest1 = res.data.suggestions.suggest.title_suggest;
        if(titleSuggest1 && titleSuggest1.length>0){
          console.log(titleSuggest1);
          suggestions  = titleSuggest1[0].options.map(option => option.text);
          showAutoComplete = true;
        }else{
          showAutoComplete = false;
        }
        break;
      case '2':
        //篇名
        res = await httpUtil.get('/elasticSearch/works/autoCompleteTitleWithCompletionSuggester', {
          searchContent: searchInput.value 
        });
        const titleSuggest = res.data.suggestions.suggest.title_suggest;
        if(titleSuggest && titleSuggest.length>0){
          console.log(titleSuggest);
          suggestions  = titleSuggest[0].options.map(option => option.text);
          showAutoComplete = true;
        }else{
          showAutoComplete = false;
        }


        break;
      case '3':
        //关键词
        res = await httpUtil.get('/elasticSearch/works/autoCompleteKeywordsWithCompletionSuggester', {
          searchContent: searchInput.value 
        });
        const keywordstextSuggest = res.data.suggestions.suggest.keywordstextSuggest;
        if(keywordstextSuggest && keywordstextSuggest.length>0){
          console.log(keywordstextSuggest);
          suggestions  = keywordstextSuggest[0].options.map(option => option.text);
          showAutoComplete = true;
        }else{
          showAutoComplete = false;
        }
        break;
      case '4':
        //摘要
        res = await httpUtil.get('/elasticSearch/works/autoCompleteAbstractWithCompletionSuggester', {
          searchContent: searchInput.value 
        });
        const abstractSuggest = res.data.suggestions.suggest.abstractSuggest;
        if(abstractSuggest && abstractSuggest.length>0){
          console.log(abstractSuggest);
          suggestions  = abstractSuggest[0].options.map(option => option.text);
          showAutoComplete = true;
        }else{
          showAutoComplete = false;
        }


        break;
    }
    
  } else {
    autocompleteSuggestions = [];
    showAutoComplete = false;
  }
};

const selectSuggestion = (suggestion) => {
  searchInput.value = suggestion;
  showAutoComplete = false;
  // console.log(showAutoComplete)
  // search();
};

const hoverSuggestion = (index) => {
  hoveredIndex.value = index;
};

const leaveSuggestion = (index) => {
  index = -1;
};


</script>

<template>
  <el-container>
    <el-header>
      <div>
        <div style="background-color: transparent !important;">
          <el-input v-model="searchInput" class="search-input" placeholder="请输入搜索内容"  @input="handleInputChange">
            <template #prepend>
              <el-select v-model="searchType" style="width: 115px; background-color:#FFFFFF;">
                <el-option label="主题" value="1"/>
                <el-option label="篇名" value="2"/>
                <el-option label="关键词" value="3"/>
                <el-option label="摘要" value="4"/>
              </el-select>
            </template>
          </el-input>
          <el-button :icon="Search" @click="search" class="search-button"/>
        </div>
        <div v-if="showAutoComplete" class="autocomplete-container">
          <div 
                v-for="(suggestion, index) in suggestions" 
                :key="index" 
                class="autocomplete-item" 
                @click="selectSuggestion(suggestion)"
                @mouseenter="hoverSuggestion(index)"
                @mouseleave="leaveSuggestion(index)"
                :class="{'hovered': hoveredIndex === index}"
            >
                <!-- 显示序号和 text -->
                {{ `${index + 1}. ${suggestion}` }}
            </div>
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
            <SingleResult v-for="result in searchResults" :key="result.content.id" :author="result.paperInformation"
                          :content="result.content.abstractText"
                          :title="result.content.title" :cited="result.content.cited_by_count" :id="result.content.id"/>
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




.autocomplete-container {
  position: absolute; 
  z-index: 1000; 
  width: 40.5%;
  background-color: #fff; 
  border: 1px solid #ccc; 
  border-top: none;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1); 
  max-height: 200px; 
  overflow-y: auto; 
}


.autocomplete-item {
  padding: 8px 10px; 
  cursor: pointer; 
}

/* 悬停样式 */
.autocomplete-item.hovered {
  background-color: #f5f5f5; 
}


.search-input {
  position: relative; 
}

/* 添加一些过渡效果，使悬停和点击更加平滑 */
.autocomplete-item {
  transition: background-color 0.2s ease;
}
</style>