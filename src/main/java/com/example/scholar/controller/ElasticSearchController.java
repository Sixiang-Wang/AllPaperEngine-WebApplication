package com.example.scholar.controller;


import com.example.scholar.dao.WorkMapper;
import com.example.scholar.domain.constant.R;
import com.example.scholar.service.ElasticWorkService;
import com.example.scholar.service.impl.ElasticWorksServiceImpl;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value="/elasticSearch")
public class ElasticSearchController {

    @Resource
    private ElasticWorkService elasticWorkService;


    @GetMapping(value = "/works/AdvancedSearch")
    public R getWorksByAdvancedSearch(@RequestParam(value = "andTitles",required = false) List<String> andTitles,@RequestParam(value = "andTitlesFuzzy",required = false) List<Boolean> andTitlesFuzzy,@RequestParam(value = "orTitles",required = false) List<String> orTitles,@RequestParam(value = "orTitlesFuzzy",required = false) List<Boolean> orTitlesFuzzy,@RequestParam(value = "notTitles",required = false) List<String> notTitles,@RequestParam(value = "notTitlesFuzzy",required = false) List<Boolean> notTitlesFuzzy,
                                      @RequestParam(value = "andTopics",required = false) List<String> andTopics,@RequestParam(value = "andTopicsFuzzy",required = false) List<Boolean> andTopicsFuzzy,@RequestParam(value = "orTopics",required = false) List<String> orTopics,@RequestParam(value = "orTopicsFuzzy",required = false) List<Boolean> orTopicsFuzzy,@RequestParam(value = "notTopics",required = false) List<String> notTopics,@RequestParam(value = "notTopicsFuzzy",required = false) List<Boolean> notTopicsFuzzy,
                                      @RequestParam(value = "andAuthors",required = false) List<String> andAuthors,@RequestParam(value = "andAuthorsFuzzy",required = false) List<Boolean> andAuthorsFuzzy,@RequestParam(value = "orAuthors",required = false) List<String> orAuthors,@RequestParam(value = "orAuthorsFuzzy",required = false) List<Boolean> orAuthorsFuzzy,@RequestParam(value = "notAuthors",required = false) List<String> notAuthors,@RequestParam(value = "notAuthorsFuzzy",required = false) List<Boolean> notAuthorsFuzzy,//author名字
                                      @RequestParam(value = "andFirstAuthors",required = false) List<String> andFirstAuthors,@RequestParam(value = "andFirstAuthorsFuzzy",required = false) List<Boolean> andFirstAuthorsFuzzy,@RequestParam(value = "orFirstAuthors",required = false) List<String> orFirstAuthors,@RequestParam(value = "orFirstAuthorsFuzzy",required = false) List<Boolean> orFirstAuthorsFuzzy,@RequestParam(value = "notFirstAuthors",required = false) List<String> notFirstAuthors,@RequestParam(value = "notFirstAuthorsFuzzy",required = false) List<Boolean> notFirstAuthorsFuzzy,
                                      @RequestParam(value = "andInstitutions",required = false) List<String> andInstitutions,@RequestParam(value = "andInstitutionsFuzzy",required = false) List<Boolean> andInstitutionsFuzzy,@RequestParam(value = "orInstitutions",required = false) List<String> orInstitutions,@RequestParam(value = "orInstitutionsFuzzy",required = false) List<Boolean> orInstitutionsFuzzy,@RequestParam(value = "notInstitutions",required = false) List<String> notInstitutions,@RequestParam(value = "notInstitutionsFuzzy",required = false) List<Boolean> notInstitutionsFuzzy,
                                      @RequestParam(value = "andAbstracts",required = false) List<String> andAbstracts,@RequestParam(value = "andAbstractsFuzzy",required = false) List<Boolean> andAbstractsFuzzy,@RequestParam(value = "orAbstracts",required = false) List<String> orAbstracts,@RequestParam(value = "orAbstractsFuzzy",required = false) List<Boolean> orAbstractsFuzzy,@RequestParam(value = "notAbstracts",required = false) List<String> notAbstracts,@RequestParam(value = "notAbstractsFuzzy",required = false) List<Boolean> notAbstractsFuzzy,
                                      @RequestParam(value = "andDOI",required = false) List<String> andDOI,@RequestParam(value = "andDOIFuzzy",required = false) List<Boolean> andDOIFuzzy,@RequestParam(value = "orDOI",required = false) List<String> orDOI,@RequestParam(value = "orDOIFuzzy",required = false) List<Boolean> orDOIFuzzy,@RequestParam(value = "notDOI",required = false) List<String> notDOI,@RequestParam(value = "notDOIFuzzy",required = false) List<Boolean> notDOIFuzzy,
                                      @RequestParam(value = "startDate",required = false) java.sql.Date startDate,@RequestParam(value = "endDate",required = false) java.sql.Date endDate){


        try{
            return R.ok().put("works",elasticWorkService.advancedSearch(andTitles,andTitlesFuzzy,orTitles,orTitlesFuzzy,notTitles,notTitlesFuzzy,
                                                                        andTopics,andTopicsFuzzy,orTopics,orTopicsFuzzy,notTopics,notTopicsFuzzy,
                                                                        andAuthors,andAuthorsFuzzy,orAuthors,orAuthorsFuzzy,notAuthors,notAuthorsFuzzy,
                                                                        andFirstAuthors,andFirstAuthorsFuzzy,orFirstAuthors,orFirstAuthorsFuzzy,notFirstAuthors,notFirstAuthorsFuzzy,
                                                                        andInstitutions,andInstitutionsFuzzy,orInstitutions,orInstitutionsFuzzy,notInstitutions,notInstitutionsFuzzy,
                                                                        andAbstracts,andAbstractsFuzzy,orAbstracts,orAbstractsFuzzy,notAbstracts,notAbstractsFuzzy,
                                                                        andDOI,andDOIFuzzy,orDOI,orDOIFuzzy,notDOI,notDOIFuzzy,
                                                                        startDate,endDate));
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


    @GetMapping(value="/works/getByTitle")
    public R getWorksByTitle(@RequestParam("title") String title){
        try{
            return R.ok().put("works",elasticWorkService.searchByTitle(title));
        }catch (Exception e){
            return R.error(e.toString());
        }
    }


    @GetMapping(value="/works/getByTitleOrAbstractOrKeywords")
    public R getByTitleOrAbstractOrKeywords(@RequestParam("word") String searchterm,@RequestParam("page") int pageIndex){
        try{
            return R.ok().put("works",elasticWorkService.findByTitleOrKeywordsTextOrAbstract(searchterm,pageIndex));
        }catch (Exception e){
            return R.error(e.toString());
        }
    }


    @GetMapping(value="/works/getLenthByFindTitleOrAbstractOrKeywords")
    public R getByTitleOrAbstractOrKeywords(@RequestParam("word") String searchterm){
        try{
            return R.ok().put("totallenth",elasticWorkService.getLenthOfFindTitleOrKeywordsTextOrAbstract(searchterm));
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

}
