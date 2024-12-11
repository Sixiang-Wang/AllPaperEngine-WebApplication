package com.example.scholar.service.impl;

import com.example.scholar.dao.AuthorMapper;
import com.example.scholar.dao.ConceptsMapper;
import com.example.scholar.dao.KeywordMapper;
import com.example.scholar.dao.WorkMapper;
import com.example.scholar.domain.openalex.Work;
import com.example.scholar.dto.WorkResultDto;
import com.example.scholar.dto.WorkSpecificResultDto;
import com.example.scholar.service.AuthorService;
import com.example.scholar.service.WorkService;
import com.example.scholar.util.AbstractRestore;
import com.example.scholar.util.JsonDisposer;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service("workService")
public class WorkServiceImpl implements WorkService {
    @Resource
    private WorkMapper workMapper;
    @Resource
    private AuthorService authorService;
    @Resource
    private ConceptsMapper conceptsMapper;
    @Resource
    private KeywordMapper keywordMapper;
    @Resource
    private WorkService workService;
    @Override
    public List<WorkResultDto> getWorks() {
        List<Work> works = workMapper.selectAllWorks();
        List<WorkResultDto> workResultDtos = new ArrayList<>();
        for(Work work: works){
            WorkResultDto workResultDto = new WorkResultDto();
            workResultDto.setId(work.getId());
            workResultDto.setAbstractText(AbstractRestore.restoreAbstract(work.getAbstractInvertedIndex()));
            workResultDto.setTitle(work.getTitle());
            workResultDto.setPaperInformation("A Vignes - Industrial & Engineering Chemistry Fundamentals, 1966 - ACS Publications");
            workResultDto.setGrants(work.getGrants());
            workResultDto.setKeywords(JsonDisposer.disposeWorkKeywords(work.getKeywords()));
            //这里后续需要修改
            workResultDtos.add(workResultDto);
        }
        return workResultDtos;
    }

    @Override
    public List<WorkResultDto> getRecommends() {
        List<Work> works = workMapper.selectRecommendTest();
        List<WorkResultDto> workResultDtos = new ArrayList<>();
        for(Work work: works){
            WorkResultDto workResultDto = new WorkResultDto();
            workResultDto.setId(work.getId());
            workResultDto.setAbstractText(AbstractRestore.restoreAbstract(work.getAbstractInvertedIndex()));
            workResultDto.setTitle(work.getTitle());
            workResultDto.setPaperInformation("A Vignes - Industrial & Engineering Chemistry Fundamentals, 1966 - ACS Publications");
            workResultDto.setGrants(work.getGrants());
            workResultDto.setKeywords(JsonDisposer.disposeWorkKeywords(work.getKeywords()));
            //这里后续需要修改
            workResultDtos.add(workResultDto);
        }
        return workResultDtos;    }

    @Override
    public List<WorkResultDto> getWorksByPage(int page) {
        int from = page*20-20;//设置前端每页最多20条
        List<Work> works = workMapper.selectAllWorksByPage(from);
        List<WorkResultDto> workResultDtos = new ArrayList<>();
        for(Work work: works){
            WorkResultDto workResultDto = new WorkResultDto();
            workResultDto.setAbstractText(AbstractRestore.restoreAbstract(work.getAbstractInvertedIndex()));
            workResultDto.setTitle(work.getTitle());
            workResultDto.setId(work.getId());
            workResultDto.setCited(work.getCitedByCount());
            workResultDto.setPaperInformation(workService.ToMainInformation(work));
            workResultDto.setGrants(work.getGrants());
            workResultDto.setKeywords(JsonDisposer.disposeWorkKeywords(work.getKeywords()));
            workResultDtos.add(workResultDto);
        }
        return workResultDtos;    }

    @Override
    public WorkSpecificResultDto getWorkById(String workId) {
        Work work = workMapper.getWorkById(workId);
        WorkSpecificResultDto workSpecificResultDto = new WorkSpecificResultDto();

        workSpecificResultDto.setWorkAuthorResultDtos(authorService.getAuthorsByWorkId(workId));
        workSpecificResultDto.setLanguage(work.getLanguage());
        workSpecificResultDto.setCitedByApiUrl(work.getCitedByApiUrl());
        workSpecificResultDto.setType(work.getType());
        workSpecificResultDto.setPublicationDate(work.getPublicationDate());
        workSpecificResultDto.setAbstractText(AbstractRestore.restoreAbstract(work.getAbstractInvertedIndex()));
        workSpecificResultDto.setCitedByCount(work.getCitedByCount());
        workSpecificResultDto.setTitle(work.getTitle());
        workSpecificResultDto.setPublicationYear(work.getPublicationYear());
        workSpecificResultDto.setGrants(work.getGrants());
        workSpecificResultDto.setKeywords(JsonDisposer.disposeWorkKeywords(work.getKeywords()));
        workSpecificResultDto.setWorksConceptsList(conceptsMapper.getWorksConceptsListById(workId));
        workSpecificResultDto.setDoi(work.getDoi());
        return workSpecificResultDto;
    }

    @Override
    public List<WorkResultDto> getWorksByTitleWords(String word,int page) {

        int from = page*20-20;//设置前端每页最多20条
        int to = page*20;
        List<Work> works = workMapper.selectWorksByTitleWord(word,from,to);
        List<WorkResultDto> workResultDtoList = new ArrayList<>();

        for(Work work: works){

            WorkResultDto workResultDto = new WorkResultDto();
            workResultDto.setAbstractText(AbstractRestore.restoreAbstract(work.getAbstractInvertedIndex()));
            workResultDto.setTitle(work.getTitle());
            workResultDto.setCited(work.getCitedByCount());
            workResultDto.setId(work.getId());
            workResultDto.setPublicationDate(work.getPublicationDate());
            workResultDto.setPaperInformation(workService.ToMainInformation(work));
            //这里后续需要修改
            workResultDto.setGrants(work.getGrants());
            workResultDto.setKeywords(JsonDisposer.disposeWorkKeywords(work.getKeywords()));
            workResultDtoList.add(workResultDto);
        }

        return workResultDtoList;
    }

    @Override
    public String ToMainInformation(Work work) {
        StringBuilder sb = new StringBuilder();
        sb.append("Publiced on "+work.getPublicationDate()+" KeyWords: ");
        Map<String,Float> map = JsonDisposer.disposeWorkKeywords(work.getKeywords());
        int size = map.size();
        int i=0;
        for(Map.Entry<String,Float> entry:map.entrySet()){
            String keyword = entry.getKey();
            if(i<size-1){
                sb.append(keyword+" && ");
            }else{
                sb.append(keyword);
            }
            i++;
        }
        return sb.toString();
    }

    @Override
    public List<WorkResultDto> getWorksByPublicationYear(int from, int to, int page) {
        int frompage = page*20-20;//设置前端每页最多20条
        int topage = page*20;
        List<Work> works = workMapper.selectWorksByPublicationYear(from,to,frompage,topage);
        List<WorkResultDto> workResultDtoList = new ArrayList<>();
        for(Work work: works){

            WorkResultDto workResultDto = new WorkResultDto();
            workResultDto.setAbstractText(AbstractRestore.restoreAbstract(work.getAbstractInvertedIndex()));
            workResultDto.setTitle(work.getTitle());
            workResultDto.setCited(work.getCitedByCount());
            workResultDto.setPaperInformation(workService.ToMainInformation(work));
            //这里后续需要修改
            workResultDto.setGrants(work.getGrants());
            workResultDto.setKeywords(JsonDisposer.disposeWorkKeywords(work.getKeywords()));
            workResultDtoList.add(workResultDto);
        }
        return workResultDtoList;
    }

    @Override
    public List<WorkResultDto> getWorkByTitleAndPublicationYear(String word, int from, int to, int page) {
        int frompage = page*20-20;//设置前端每页最多20条
        int topage = page*20;
        List<Work> works = workMapper.selectWorkByTitleAndPublicationYear(word,from,to,frompage,topage);
        List<WorkResultDto> workResultDtoList = new ArrayList<>();
        for(Work work: works){

            WorkResultDto workResultDto = new WorkResultDto();
            workResultDto.setAbstractText(AbstractRestore.restoreAbstract(work.getAbstractInvertedIndex()));
            workResultDto.setTitle(work.getTitle());
            workResultDto.setCited(work.getCitedByCount());
            workResultDto.setPaperInformation(workService.ToMainInformation(work));
            //这里后续需要修改
            workResultDto.setGrants(work.getGrants());
            workResultDto.setKeywords(JsonDisposer.disposeWorkKeywords(work.getKeywords()));
            workResultDtoList.add(workResultDto);
        }
        return workResultDtoList;
    }

    @Override
    public List<WorkResultDto> getWorkByKeywords(String word, int page) {
        int from = page*20-20;//设置前端每页最多20条
        int to = page*20;
        List<Work> works = workMapper.selectWorkByKeywords(word,from,to);
        List<WorkResultDto> workResultDtoList = new ArrayList<>();

        for(Work work: works){

            WorkResultDto workResultDto = new WorkResultDto();
            workResultDto.setAbstractText(AbstractRestore.restoreAbstract(work.getAbstractInvertedIndex()));
            workResultDto.setTitle(work.getTitle());
            workResultDto.setCited(work.getCitedByCount());
            workResultDto.setPaperInformation(workService.ToMainInformation(work));
            //这里后续需要修改
            workResultDto.setGrants(work.getGrants());
            workResultDto.setKeywords(JsonDisposer.disposeWorkKeywords(work.getKeywords()));
            workResultDtoList.add(workResultDto);
        }

        return workResultDtoList;
    }

    @Override
    public int getWorkLengthByTitleWords(String word) {
        return workMapper.getWorkLengthByTitle(word);
    }

    @Override
    public List<WorkResultDto> getTopNWorkByKeywords(String word, int n) {
        List<Work> works = keywordMapper.getTopNWorksByKeywords(word,n);
        List<WorkResultDto> workResultDtoList = new ArrayList<>();
        for(Work work: works){

            WorkResultDto workResultDto = new WorkResultDto();
            workResultDto.setId(work.getId());
            workResultDto.setAbstractText(AbstractRestore.restoreAbstract(work.getAbstractInvertedIndex()));
            workResultDto.setTitle(work.getTitle());
            workResultDto.setCited(work.getCitedByCount());
            workResultDto.setPaperInformation(workService.ToMainInformation(work));
            //这里后续需要修改
            workResultDto.setGrants(work.getGrants());
            workResultDto.setKeywords(JsonDisposer.disposeWorkKeywords(work.getKeywords()));
            workResultDtoList.add(workResultDto);
        }
        return workResultDtoList;
    }

    @Override
    public List<WorkResultDto> getWorkItsReferenced(String workId) {
        List<Work> works = workMapper.selectWorkByItsReference(workId);
        List<WorkResultDto> workResultDtoList = new ArrayList<>();
        for(Work work: works){
            WorkResultDto workResultDto = new WorkResultDto();
            workResultDto.setId(work.getId());
            workResultDto.setAbstractText(AbstractRestore.restoreAbstract(work.getAbstractInvertedIndex()));
            workResultDto.setTitle(work.getTitle());
            workResultDto.setCited(work.getCitedByCount());
            workResultDto.setPaperInformation(workService.ToMainInformation(work));
            //这里后续需要修改
            workResultDto.setGrants(work.getGrants());
            workResultDto.setKeywords(JsonDisposer.disposeWorkKeywords(work.getKeywords()));
            workResultDtoList.add(workResultDto);
        }
        return workResultDtoList;
    }

    @Override
    public List<WorkResultDto> getWorkReferenceIt(String workId) {
        List<Work> works = workMapper.selectWorkByReferenceIt(workId);
        List<WorkResultDto> workResultDtoList = new ArrayList<>();
        for(Work work: works){
            WorkResultDto workResultDto = new WorkResultDto();
            workResultDto.setId(work.getId());
            workResultDto.setAbstractText(AbstractRestore.restoreAbstract(work.getAbstractInvertedIndex()));
            workResultDto.setTitle(work.getTitle());
            workResultDto.setCited(work.getCitedByCount());
            workResultDto.setPaperInformation(workService.ToMainInformation(work));
            //这里后续需要修改
            workResultDto.setGrants(work.getGrants());
            workResultDto.setKeywords(JsonDisposer.disposeWorkKeywords(work.getKeywords()));
            workResultDtoList.add(workResultDto);
        }
        return workResultDtoList;
    }

    @Override
    public void updateKeywordsAndAbstract() {
        List<Work> works = workMapper.selectAllWorks();
        for(Work work:works){
            String id = work.getId();
            String keywords = JsonDisposer.getKeywords(JsonDisposer.disposeWorkKeywords(work.getKeywords()));
            String abstractText = AbstractRestore.restoreAbstract(work.getAbstractInvertedIndex());
            workMapper.insertKeywordsAndAbstract(id,keywords,abstractText);
        }
    }


}
