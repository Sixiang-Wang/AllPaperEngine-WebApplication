<script setup>
import SearchAside from "@/components/SearchAside.vue";
import {Search} from "@element-plus/icons-vue";
import {computed, onMounted, ref, watch} from "vue";
import SingleResult from "@/components/single/SingleResult.vue";
import SingleAuthor from "@/components/single/SingleAuthor.vue";
import SingleInstitution from "@/components/single/SingleInstitution.vue";
import router from "@/router/index.js";
import {useRoute} from 'vue-router';
import httpUtil from "@/api/http.js";
import SingleResearcher from "@/components/single/SingleResearcher.vue";
import baseUrl from "@/api/http.js";
import {ElMessage} from "element-plus";

let resStore = ref([])
let searchInput = ref("");
const totalLength = ref(0);
const searchResults = ref([]);
let authorIds = ref([]);
let suggestions = ref([]);
let showAutoComplete = ref(false);
const route = useRoute();
let hoveredIndex = ref(null);
let searchType = ref(route.query.type);

const currentPage = ref(Number(route.query.page) || 1); // 确保 currentPage 从 URL 获取
let isSearchingForAuthors = ref(false);
let isSearchingForResearchers = ref(false);
let isSearchingForResults = ref(false);
let isSearchingForInstitutions = ref(false);
let isSearchingForAdvancedResults = ref(false);
const pageSize = computed(() => Math.ceil(totalLength.value / 20));
const researcherInfos = ref([]);
const authorInfos = ref([]);


const handlePageChange = (page) => {
  router.push({path: "/search", query: {input: route.query.input, page: page, type: searchType.value}}).then(() => {
    currentPage.value = page;
    updateSearchResults();
    window.scrollTo({top: 0});
  });
};

const updateSearchResults = async () => {
  console.log(currentPage.value)
  const res = await httpUtil.get('/openalex/get/page', {
    page: currentPage.value
  });
  console.log(res.data);
  searchResults.value = res.data.works;
};

const changeInput = ()=> {
  router.push({path: "/search", query: {input: searchInput.value, page: 1, type: searchType.value}});
}

const search = async () => {
  isSearchingForAuthors = (searchType.value === '2'); // 设置查询类型
  isSearchingForResearchers = (searchType.value === '3');
  isSearchingForResults= (searchType.value === '1');
  isSearchingForAdvancedResults = (searchType.value === '4');
  isSearchingForInstitutions = (searchType.value === '7');

  switch (searchType.value) {
    case '1'://按标题查找
      //为方便测试，这里保留搜索所有结果的接口
      if(searchInput.value===null||searchInput.value === ''){
        const res = await httpUtil.get('/openalex/get/page',{
          page: currentPage.value
        })
        httpUtil.get('/elasticSearch/works/getByTitleAll',{
          title: searchInput.value
        })
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
      console.log(isSearchingForResearchers);
      console.log(isSearchingForResults);
      console.log(isSearchingForAuthors);

      break;
    case '2'://查找学者
      if(searchInput.value===null||searchInput.value === '') {
        const res = await httpUtil.get('/openalex/get/page', {
          page: currentPage.value
        });
        searchResults.value = res.data.works;
        const res2 = await httpUtil.get('/openalex/get/length');
        totalLength.value = res2.data.leng;
      } else {
        const res = await httpUtil.get('/author/getAuthorIdByAuthorName', {
          authorName: searchInput.value
        });
        authorIds.value = res.data.getAuthorIdByAuthorName || [];
        totalLength.value = authorIds.value.length;
        authorInfos.value = [];
        for (let authorId of authorIds.value) {
          const authorRes = await httpUtil.get('/author/getById', {
            id: authorId
          });
          authorInfos.value.push(authorRes.data.authors);
        }
        searchResults.value = authorInfos.value;
      }
      break;
    //
    // case '3'://查找关键词
    //   if(searchInput.value===null||searchInput.value === ''){
    //     const res = await httpUtil.get('/openalex/get/page',{
    //       page: currentPage.value
    //     })
    //     // console.log(res);
    //     searchResults.value = res.data.works;
    //     // console.log(searchResults.value);
    //     const res2 = await httpUtil.get('/openalex/get/length');
    //     totalLength.value = res2.data.leng;
    //     // console.log(totalLength.value);
    //   }else {
    //     // const res = await httpUtil.get('/elasticSearch/works/getByTitleOrAbstractOrKeywords', {
    //     //   word: searchInput.value,
    //     //   page: currentPage.value
    //     // })
    //     const res = await httpUtil.get('/elasticSearch/works/getByTitleByPage', {
    //       title: searchInput.value,
    //       page: currentPage.value
    //     })
    //     searchResults.value = res.data.works || [];
    //     console.log(searchResults.value);
    //     totalLength.value = searchResults.value.length;
    //
    //     for(let result of searchResults.value){
    //       if(result.highlightFields.hasOwnProperty('title')){
    //         result.content.title = result.highlightFields.title[0];
    //       }
    //       if(result.highlightFields.hasOwnProperty('abstract')){
    //         const startIndex = result.highlightFields.abstract[0].indexOf("<span style='color:red'>");
    //         if(startIndex!=-1){
    //           result.content.abstractText = result.highlightFields.abstract[0].substring(startIndex);
    //         }else{
    //           result.content.abstractText = result.highlightFields.abstract[0];
    //         }
    //       }
    //       // if(result.highlightFields.hasOwnProperty('keywordsText')){
    //       //   result.content.abstractText = result.highlightFields.abstract[0];
    //       // }
    //     }
    //   }
    //   break;

    case '3'://查找科研人员
      if(searchInput.value===null||searchInput.value === '') {
        const res = await httpUtil.get('/openalex/get/page', {
          page: currentPage.value
        });
        searchResults.value = res.data.works;
        const res2 = await httpUtil.get('/openalex/get/length');
        totalLength.value = res2.data.leng;
      } else {
        const res = await httpUtil.get('/user/getScholars', {
          name: searchInput.value
        });

        const authorRes = ref([]);
        researcherInfos.value = res.data.scholars || [];
        console.log(researcherInfos.value);
        totalLength.value = researcherInfos.value.length;
        searchResults.value = researcherInfos.value;

      }
      case '7'://查找机构
      if(searchInput.value===null||searchInput.value === ''){
        const res = await httpUtil.get('/openalex/get/page',{
          page: currentPage.value
        })}else {
        const res = await httpUtil.get('/elasticSearch/institutions/getByDisplayName', { 
          displayName: searchInput.value,
          timeout: 20000
        })
        searchResults.value = res.data.authors.hits;
        console.log('机构之前')
        console.log(searchResults.value);
        totalLength.value = searchResults.value.length;
        console.log(totalLength.value);
      }
      break;

    case '4':
      //高级检索
      ElMessage.success('111')
      console.log("111111111111111111111");
      const res = await httpUtil.post('/elasticSearch/works/AdvancedSearch', JSON.parse(sessionStorage.getItem('searchParams')));
      console.log(res.data);

      searchResults.value = res.data.works;
      totalLength.value = res.data.works.length;
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
  isSearchingForAuthors = (searchType.value === '5'); // 设置查询类型
  isSearchingForResearchers = (searchType.value === '6');
  isSearchingForInstitutions = (searchType.value === '7');
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
        //高级检索
        ElMessage.success('111')
        res = await httpUtil.post('/elasticSearch/works/AdvancedSearch', JSON.parse(sessionStorage.getItem('searchParams')));
        console.log(res.data);
        break;

      case '5':
        //学者
        // res = await httpUtil.get('/elasticSearch/works/autoCompleteScholarWithCompletionSuggester', { //接口todo
        //   searchContent: searchInput.value 
        // });
        // const scholarSuggest = res.data.suggestions.suggest.scholarSuggest;
        // if(scholarSuggest && scholarSuggest.length>0){
        //   console.log(scholarSuggest);
        //   suggestions  = scholarSuggest[0].options.map(option => option.text);
        //   showAutoComplete = true;
        // }else{
        //   showAutoComplete = false;
        // }
        totalLength.value = authorInfos.value.length;
        break;

      case '7':
        //机构
        totalLength.value = searchResults.value.length;
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
          <el-input v-model="searchInput" class="search-input" placeholder="请输入搜索内容">
            <template #prepend>
              <el-select v-model="searchType" style="width: 115px; background-color:#FFFFFF;">
                <el-option label="主题" value="1"/>
                <el-option label="学者" value="2"/>
                <el-option label="科研人员" value="3"/>
                <el-option label="高级检索" value="4"/>
                <el-option label="机构" value="7"/>
              </el-select>
            </template>
          </el-input>
          <el-button :icon="Search" @click="changeInput" class="search-button"/>
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
        <SearchAside :search-input="searchInput"/>
      </el-aside>
      <el-main style="margin-left: 2%;width: 100%;">
        <span class="search-result-statistic">共查询到{{ totalLength }}个结果，当前为第{{ currentPage }}页</span>
        <div v-if="isSearchingForAuthors" style="height: 20px;"></div>
        <div v-if="isSearchingForResearchers" style="height: 20px;"></div>
        <div v-if="searchResults.length !==0" style="display: flex;">
          <div >
            <SingleAuthor
              style="width: 800px;"
              v-if="isSearchingForAuthors"
              v-for="authorInfo in authorInfos"
              :key="authorInfo.id"
              :id="authorInfo.id"
              :name="authorInfo.displayName || '未知学者'"
              :citedByCount="authorInfo.citedByCount || 0"
              :worksCount="authorInfo.worksCount || 0"
              :workPlaece="lastKnownInstitution"
            />
            <!-- :
              :worksApiUrl="authorInfo.worksApiUrl || ''"
              :firstAuthor="authorInfo.firstPublishCount || 0"
              :highInflu="authorInfo.highQualityWorkCount || 0"
              :H_index="authorInfo.hnumber || 0" -->
          <SingleResearcher
              style="width: 800px;"
              v-if="isSearchingForResearchers"
              v-for="researcherInfo in researcherInfos"
              :key="researcherInfo.authorId"
              :authorId="researcherInfo.authorId"
              :authorName="researcherInfo.authorName || '未知学者'"
              :institution="researcherInfo.institution || '暂无'"
              :mail="researcherInfo.mail || ''"
              :avatar="'http://116.204.112.5:1145' + researcherInfo.avatar || ''"
          />
          <SingleInstitution
              style="width: 1000px;"
              v-if="isSearchingForInstitutions"
              v-for="result in searchResults"
              :key="result.id"
              :id="result.id"
              :name="result.sourceAsMap.display_name || '未知机构'"
              :citedByCount="result.sourceAsMap.cited_by_count || 0"
              :worksCount="result.sourceAsMap.works_count || 0"
              :score="result.score"
              :homepageUrl="result.sourceAsMap.homepage_url || ''"
              :country="result.sourceAsMap.country_code || ''"
              :updateDate="result.sourceAsMap.updated_date || ''"
              :type="result.sourceAsMap.type || ''"
            />
            <SingleResult
              v-if="isSearchingForResults"
                v-for="result in searchResults" :key="result.content.id" :author="result.paperInformation"
              :content="result.content.abstractText"
              :title="result.content.title" :cited="result.content.cited_by_count" :id="result.content.id"/>
            <SingleResult
              v-if="isSearchingForAdvancedResults"
                v-for="result in searchResults" :key="result.id" :author="result.workAuthorResultDtos.length==0?'未知作者':result.workAuthorResultDtos[0].authorResultDto.authorName
 || '未知学者'"
              :content="result.abstractText"
              :title="result.title" :cited="result.citedByCount" :id="result.id"/>
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