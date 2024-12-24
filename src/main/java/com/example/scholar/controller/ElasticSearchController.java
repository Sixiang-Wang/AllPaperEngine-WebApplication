package com.example.scholar.controller;


import com.example.scholar.dao.WorkMapper;
import com.example.scholar.domain.constant.R;
import com.example.scholar.domain.openalexElasticsearch.Works;
import com.example.scholar.service.ElasticWorkService;
import com.example.scholar.service.impl.ElasticWorksServiceImpl;
import com.example.scholar.util.AbstractRestore;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value="/elasticSearch")
public class ElasticSearchController {

    @Resource
    private ElasticWorkService elasticWorkService;

    @GetMapping(value="/works/getByTitleTest")
    public R getWorksByTtile(@RequestParam("title") String title){
        try{
            return R.ok().put("works",elasticWorkService.searchByTitleTest(title));
        }catch (Exception e){
            return R.error(e.toString());
        }
    }

    @GetMapping(value="/works/getByTitleByPage")
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
