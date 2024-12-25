package com.example.scholar.controller;


import com.example.scholar.dao.WorkMapper;
import com.example.scholar.domain.constant.R;
import com.example.scholar.domain.openalexElasticsearch.Works;
import com.example.scholar.pojo.AdvancedSearchPojo;
import com.example.scholar.service.ElasticAuthorService;
import com.example.scholar.service.ElasticWorkService;
import com.example.scholar.service.impl.ElasticWorksServiceImpl;
import com.example.scholar.util.AbstractRestore;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@CrossOrigin
@RestController
@RequestMapping(value="/elasticSearch")
public class ElasticSearchController {

    @Resource
    private ElasticWorkService elasticWorkService;
    @Resource
    private ElasticAuthorService elasticAuthorService;


    @PostMapping(value = "/works/AdvancedSearch")
    @Cacheable(value = "AdvancedSearchWorksCache", key = "#advancedSearchPojo.toString()")
    public R getWorksByAdvancedSearch(@RequestBody(required = true) AdvancedSearchPojo advancedSearchPojo){


        try{


            System.out.print(advancedSearchPojo);

            return R.ok().put("works",elasticWorkService.advancedSearch(advancedSearchPojo.getAndTitles(),advancedSearchPojo.getAndTitlesFuzzy(),advancedSearchPojo.getOrTitles(),advancedSearchPojo.getOrTitlesFuzzy(),advancedSearchPojo.getNotTitles(),advancedSearchPojo.getNotTitlesFuzzy(),
                                                                        advancedSearchPojo.getAndTopics(),advancedSearchPojo.getAndTopicsFuzzy(),advancedSearchPojo.getOrTopics(),advancedSearchPojo.getOrTopicsFuzzy(),advancedSearchPojo.getNotTopics(),advancedSearchPojo.getNotTopicsFuzzy(),
                                                                        advancedSearchPojo.getAndAuthors(),advancedSearchPojo.getAndAuthorsFuzzy(),advancedSearchPojo.getOrAuthors(),advancedSearchPojo.getOrAuthorsFuzzy(),advancedSearchPojo.getNotAuthors(),advancedSearchPojo.getNotAuthorsFuzzy(),
                                                                        advancedSearchPojo.getAndFirstAuthors(),advancedSearchPojo.getAndFirstAuthorsFuzzy(),advancedSearchPojo.getOrFirstAuthors(),advancedSearchPojo.getOrFirstAuthorsFuzzy(),advancedSearchPojo.getNotFirstAuthors(),advancedSearchPojo.getNotFirstAuthorsFuzzy(),
                                                                        advancedSearchPojo.getAndInstitutions(),advancedSearchPojo.getAndInstitutionsFuzzy(),advancedSearchPojo.getOrInstitutions(),advancedSearchPojo.getOrInstitutionsFuzzy(),advancedSearchPojo.getNotInstitutions(),advancedSearchPojo.getNotInstitutionsFuzzy(),
                                                                        advancedSearchPojo.getAndAbstracts(),advancedSearchPojo.getAndAbstractsFuzzy(),advancedSearchPojo.getOrAbstracts(),advancedSearchPojo.getOrAbstractsFuzzy(),advancedSearchPojo.getNotAbstracts(),advancedSearchPojo.getNotAbstractsFuzzy(),
                                                                        advancedSearchPojo.getAndDOI(),advancedSearchPojo.getAndDOIFuzzy(),advancedSearchPojo.getOrDOI(),advancedSearchPojo.getOrDOIFuzzy(),advancedSearchPojo.getNotDOI(),advancedSearchPojo.getNotDOIFuzzy(),
                                                                        advancedSearchPojo.getStartDate(),advancedSearchPojo.getEndDate()));
        }catch (Throwable e){
            return R.error(e.toString());
        }
    }



    @GetMapping(value="/works/getByTitleTest")
    public R getWorksByTtile(@RequestParam("title") String title){
        try{
            return R.ok().put("works",elasticWorkService.searchByTitleTest(title));
        }catch (Exception e){
            return R.error(e.toString());
        }
    }

    @GetMapping(value="/works/getByTitleByPage")
    @Cacheable(value = "getByTitleByPageCache", key = "#title+ '_' + #page")
    public R getWorksByTitleByPage(@RequestParam("title")String title, @RequestParam("page")int page){
        try{
            List<SearchHit<Works>> list = elasticWorkService.searchByTitleByPage(title,page);
            for(SearchHit<Works> tmp: list){
                Works works = tmp.getContent();
                works.setAbstractText(AbstractRestore.restoreAbstract(works.getAbstract_inverted_index()));
            }
            return R.ok().put("works", list).put("page", ElasticWorksServiceImpl.count);
        }catch (Exception e){
            return R.error(e.toString());
        }
    }



    @GetMapping(value="/authors/getByDisplayName")
    @Cacheable(value = "getAuthorsByDisplayNameCache", key = "#displayName + '_' + #page")
    public R getByDisplayName(@RequestParam("displayName") String displayName,@RequestParam("page") int page){
        try{
            List<org.elasticsearch.search.SearchHit> res = elasticAuthorService.searchByDisplayNameByPage(displayName,page);
            return R.ok().put("authors",res).put("sum",res.size());
        }catch (Exception e){
            return R.error(e.toString());
        }
    }

    @GetMapping(value="/institutions/getByDisplayName")
    @Cacheable(value = "getInstitutionByDisplayNameCache", key = "#displayName + '_' + #page")
    public R getInstitutionsByDisplayName(@RequestParam("displayName") String displayName,@RequestParam("page") int page){
        try{
            return R.ok().put("authors",elasticAuthorService.searchByDisplayName(displayName,page));
        }catch (Exception e){
            return R.error(e.toString());
        }
    }

    @GetMapping(value="/works/getByTitle")
    public R getWorksByTitle(@RequestParam("title") String title){
        try{
            return R.ok().put("works",elasticWorkService.searchByTitle(title));
        }catch (Exception e){
            return R.error(e.toString());
        }
    }

    @GetMapping(value="/works/searchAssistant")
    public R searchAssistant(@RequestParam("title") String title){
        try{
            elasticWorkService.searchAssistant(title);
            return R.ok();
        }catch (Exception e){
            return R.error(e.toString());
        }
    }






//    @GetMapping(value="/works/searchAutocomplete")
//    public R searchAutocomplete(@RequestParam("value") String value ,@RequestParam("fuzziness")String fuzziness,@RequestParam("transpositions")boolean transposition, @RequestParam("prefixLength") int prefixLength){
//        try{
//            return R.ok().put("probability",elasticWorkService.fuzzyAutocomplete(value, fuzziness, transposition, prefixLength));
//        }catch (Throwable e){
//            return R.error(e.toString());
//        }
//    }



//    @GetMapping(value = "/works/autoCompletionWithSuggester")
//    public R searchAutoCompleteWithSuggester(@RequestParam("searchContent") String searchContent){
//        try{
//            return R.ok().put("works",elasticWorkService.AutoCompleteWithSuggester("openalex_works_index_addingcompletion",searchContent));
//        }catch (Exception e){
//            return R.error(e.toString());
//        }
//    }


    @GetMapping(value = "/works/autoCompletionWithCompletionSuggester")
    public R searchAutoCompleteWithCompletionSuggester(@RequestParam("searchContent") String searchContent){
        try{
            return R.ok().put("suggestions",elasticWorkService.AutoCompleteWithCompletionSuggester(searchContent));
        }catch (Exception e){
            return R.error(e.toString());
        }
    }

    @GetMapping(value = "/works/autoCompleteTitleWithCompletionSuggester")
    public R autoCompleteTitleWithCompletionSuggester(@RequestParam("searchContent") String searchContent){
        try{
            return R.ok().put("suggestions",elasticWorkService.AutoCompleteTitleWithCompletionSuggester(searchContent));
        }catch (Exception e){
            return R.error(e.toString());
        }
    }

    @GetMapping(value = "/works/autoCompleteAbstractWithCompletionSuggester")
    public R autoCompleteAbstractWithCompletionSuggester(@RequestParam("searchContent") String searchContent){
        try{
            return R.ok().put("suggestions",elasticWorkService.AutoCompleteAbstractWithCompletionSuggester(searchContent));
        }catch (Exception e){
            return R.error(e.toString());
        }
    }

    @GetMapping(value = "/works/autoCompleteKeywordsWithCompletionSuggester")
    public R autoCompleteKeywordsWithCompletionSuggester(@RequestParam("searchContent") String searchContent){
        try{
            return R.ok().put("suggestions",elasticWorkService.AutoCompleteKeywordsWithCompletionSuggester(searchContent));
        }catch (Exception e){
            return R.error(e.toString());
        }
    }

    @GetMapping(value = "/works/autoFixSuggester")
    public R autoFixSuggester(@RequestParam("searchContent") String searchContent){
        try{
            return R.ok().put("suggestions",elasticWorkService.AutoFixSuggester(searchContent));
        }catch (Exception e){
            return R.error(e.toString());
        }
    }



    @GetMapping(value = "/authors/autoCompleteAuthorsWithCompletionSuggester")
    public R autoCompleteAuthorWithCompletionSuggester(@RequestParam("searchContent") String searchContent) {
        try {
            return R.ok().put("suggestions", elasticAuthorService.AutoCompleteAuthorsWithCompletionSuggester(searchContent));
        } catch (Exception e) {
            return R.error(e.toString());
        }
    }

    @GetMapping(value = "/institutions/autoCompleteInstitutionsWithCompletionSuggester")
    public R autoCompleteInstitutionsWithCompletionSuggester(@RequestParam("searchContent") String searchContent) {
        try {
            return R.ok().put("suggestions", elasticAuthorService.AutoCompleteInstitutionsWithCompletionSuggester(searchContent));
        } catch (Exception e) {
            return R.error(e.toString());
        }
    }


    private CacheManager cacheManager;

    @GetMapping(value="/works/getByTitleAll")
    public R getWorksByTitleAll(@RequestParam("title")String title){
        try{
            List<SearchHit<Works>> list = elasticWorkService.searchByTitleAll(title);
            for(SearchHit<Works> tmp: list){
                Works works = tmp.getContent();
                works.setAbstractText(AbstractRestore.restoreAbstract(works.getAbstract_inverted_index()));
            }
            return R.ok().put("works", list);
        }catch (Exception e){
            return R.error(e.toString());
        }
    }


    @GetMapping(value = "/works/getWorksPageNumByYear")
    public R getWorksPageNumByYear(@RequestParam("title")String title,@RequestParam("year") int year) {
        try {
            List<SearchHit<Works>> list = elasticWorkService.searchByTitleAll(title);
            List<SearchHit<Works>> retList = new ArrayList<>();
            if(year==2015){
                for(SearchHit<Works> tmp: list){
                    Works works = tmp.getContent();
                    if(works.getPublication_year()>year){
                        continue;
                    }
                    works.setAbstractText(AbstractRestore.restoreAbstract(works.getAbstract_inverted_index()));
                    retList.add(tmp);
                }
            }else {
                for(SearchHit<Works> tmp: list){
                    Works works = tmp.getContent();
                    if(works.getPublication_year()!=year){
                        continue;
                    }
                    works.setAbstractText(AbstractRestore.restoreAbstract(works.getAbstract_inverted_index()));
                    retList.add(tmp);
                }
            }


            return R.ok().put("works", retList);
        } catch (Exception e) {
            return R.error(e.toString());
        }
    }

    @GetMapping(value = "/works/getWorksByYear")
    public R getWorksByYear(@RequestParam("title")String title,@RequestParam("year") int year,@RequestParam("page")int page) {
        try {
            int pageSize = 20;
            int from = (page - 1) * pageSize; // 起始记录
            int end = (page ) * pageSize-1; // 起始记录
            int now = 0;

            List<SearchHit<Works>> list = elasticWorkService.searchByTitleAll(title);
            List<SearchHit<Works>> retList = new ArrayList<>();
            if(year==2015){
                for(SearchHit<Works> tmp: list){
                    Works works = tmp.getContent();
                    if(works.getPublication_year()>year){
                        continue;
                    }
                    works.setAbstractText(AbstractRestore.restoreAbstract(works.getAbstract_inverted_index()));
                    retList.add(tmp);
                }
            }else {
                for(SearchHit<Works> tmp: list){
                    Works works = tmp.getContent();
                    if(works.getPublication_year()!=year){
                        continue;
                    }
                    if(now>=from&&now<=end) {
                        works.setAbstractText(AbstractRestore.restoreAbstract(works.getAbstract_inverted_index()));
                        retList.add(tmp);
                    }
                    now++;
                }
            }


            return Objects.requireNonNull(R.ok().put("works", retList)).put("length",now);
        } catch (Exception e) {
            return R.error(e.toString());
        }
    }

    @GetMapping(value = "/works/getWorksByType")
    public R getWorksByType(@RequestParam("title")String title,@RequestParam("type") String type,@RequestParam("page")int page) {
        int pageSize = 20;
        int from = (page - 1) * pageSize; // 起始记录
        int end = (page ) * pageSize-1; // 起始记录
        int now = 0;

        try {
            List<SearchHit<Works>> list = elasticWorkService.searchByTitleAll(title);
            List<SearchHit<Works>> retList = new ArrayList<>();

            for(SearchHit<Works> tmp: list){
                Works works = tmp.getContent();
                if(works.getType().equals(type)){
                    continue;
                }
                if(now>=from&&now<=end){
                    works.setAbstractText(AbstractRestore.restoreAbstract(works.getAbstract_inverted_index()));
                    retList.add(tmp);
                }
                now++;
            }


            return Objects.requireNonNull(R.ok().put("works", retList)).put("length",now);
        } catch (Exception e) {
            return R.error(e.toString());
        }
    }

}
