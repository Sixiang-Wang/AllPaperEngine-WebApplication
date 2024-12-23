package com.example.scholar.repository;

import com.example.scholar.domain.openalexElasticsearch.Works;
import org.elasticsearch.client.ElasticsearchClient;
import org.springframework.data.elasticsearch.annotations.*;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ElasticSearchRepository extends ElasticsearchRepository<Works, String> {

    /**
     * 查询内容标题查询
     * @param title 标题
    //     * @param keywordsText 关键词
    //     * @param abstract 摘要内容
     * @return 返回关键字高亮的结果集
     */
    @Highlight(
            fields = {
                    @HighlightField(name = "title"),
            },
            parameters = @HighlightParameters(
                    preTags = {"<span style='color:red'>"},
                    postTags = {"</span>"},
                    numberOfFragments = 0
            )
    )
    List<SearchHit<Works>> findByTitle(String title);
//    List<SearchHit<Works>> findByTitleOrKeywordsTextOrAbstractText(String title, String keywords_text, String abstract_text);




    /**
     * 查询内容标题查询
     *
     *
     // @param title 标题
     //     * @param keywordsText 关键词
     //     * @param abstract 摘要内容
     * @return 返回关键字高亮的结果集
     */
    @Highlight(
            fields = {
                    @HighlightField(name = "title"),
                    @HighlightField(name = "keywordsText"),
                    @HighlightField(name = "abstract")
            },
            parameters = @HighlightParameters(
                    preTags = {"<span style='color:red'>"},
                    postTags = {"</span>"},
                    numberOfFragments = 0
            )
    )

    @Query("{\"bool\": " +
            "{\"should\": " +
            "[{\"match\": {\"title\": {\"query\":\"?0\",\"minimum_should_match\": \"5<80% 6<4\"}}}, " +
            "{\"match\": {\"keywordsText\": {\"query\":\"?0\",\"minimum_should_match\": \"5<80% 6<4\"}}}," +
            " {\"match\": {\"abstract\": {\"query\":\"?0\",\"minimum_should_match\": \"5<80% 6<4\"}}}]}}")
    List<SearchHit<Works>> findByTitleOrKeywordsTextOrAbstract(String searchTerm);



//
//    /**
//     * 模糊自动补全查询
//     * @param prefix 查询前缀
//     * @param fuzziness 模糊匹配的宽松程度  "AUTO":自动根据输入的长度决定模糊匹配的程度。对于较短的字符串（如 1-2 个字符），fuzziness 设置为 0；对于中等长度的字符串（如 3-5 个字符），fuzziness 设置为 1；对于更长的字符串，fuzziness 设置为 2
//     * @param fuzziness 模糊匹配的宽松程度  "0" "1" "2"分别表示允许的最大编辑距离为 0、1 或 2 (1指替换、插入或删除一个字符,依次类推)
//     * @param transpositions 是否允许交换相邻字符的匹配
//     * @param minLength 最小匹配长度
//     * @param prefixLength 前缀长度
//     * @param unicodeAware 是否考虑Unicode字符
//     * @param slop 控制短语查询中词序灵活性的参数
//     * @return 返回模糊自动补全建议的结果集
//     */
//    @Query("{\"bool\": " +
//            "{\"should\": " +
//            "[{\"fuzzy\": {\"title\": {\"value\":\"?0\",\"fuzziness\": \"?1\",\"transpositions\": \"?2\",\"min_length\": \"?3\",\"prefix_length\": \"?4\",\"unicode_aware\": \"?5\"}}}, " +
//            "{\"fuzzy\": {\"keywordsText\": {\"value\":\"?0\",\"fuzziness\": \"?1\",\"transpositions\": \"?2\",\"min_length\": \"?3\",\"prefix_length\": \"?4\",\"unicode_aware\": \"?5\"}}}," +
//            " {\"fuzzy\": {\"abstract\": {\"value\":\"?0\",\"fuzziness\": \"?1\",\"transpositions\": \"?2\",\"min_length\": \"?3\",\"prefix_length\": \"?4\",\"unicode_aware\": \"?5\"}}}]}}")
//    List<SearchHit<Works>> fuzzyAutocomplete(String value, String fuzziness, boolean transpositions, int minLength, int prefixLength, boolean unicodeAware,int slop);
//

    //还有问题
//    @Query("{\"bool\": " +
//            "{\"should\": " +
//            "[{\"fuzzy\": {\"title\": {\"value\":\"?0\",\"fuzziness\": \"?1\",\"transpositions\": \"?2\",\"prefix_length\": \"?3\"}}}, " +
//            "{\"fuzzy\": {\"keywordsText\": {\"value\":\"?0\",\"fuzziness\": \"?1\",\"transpositions\": \"?2\",\"prefix_length\": \"?3\"}}}, " +
//            "{\"fuzzy\": {\"abstract\": {\"value\":\"?0\",\"fuzziness\": \"?1\",\"transpositions\": \"?2\",\"prefix_length\": \"?3\"}}}]}}")
//    List<SearchHit<Works>> fuzzyAutocomplete(String value, String fuzziness, boolean transpositions, int prefixLength);
}