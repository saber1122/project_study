package com.itcast.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.itcast.entity.HotelEntity;
import com.itcast.mapper.HotelMapper;
import com.itcast.service.HotelService;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.*;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.ParsedSum;
import org.elasticsearch.search.aggregations.metrics.SumAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class HotelServiceImpl implements HotelService {

    @Autowired
    private HotelMapper hotelMapper;

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    //批量导入
    @Override
    public int addDocToES() {

        BulkRequest bulkRequest = new BulkRequest();

        QueryWrapper<HotelEntity> wrapper = new QueryWrapper<>();
        wrapper.last("LIMIT 10000");
        List<HotelEntity> hotelEntityList = hotelMapper.selectList(wrapper);

        for (HotelEntity hotelEntity : hotelEntityList) {

            String data = JSON.toJSONStringWithDateFormat(hotelEntity,"yyyy-MM-dd", SerializerFeature.WriteDateUseDateFormat);
            IndexRequest indexRequest = new IndexRequest("hotel").source(data, XContentType.JSON);
            bulkRequest.add(indexRequest);
        }

        try {
            BulkResponse response = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);

            return response.status().getStatus();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return 0;
    }

    //查询全部
    @Override
    public Map<String, Object> matchAllQuery() {

        SearchRequest searchRequest = new SearchRequest("hotel");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        QueryBuilder queryBuilder = QueryBuilders.matchAllQuery();
        searchSourceBuilder.query(queryBuilder);
        searchRequest.source(searchSourceBuilder);
        try {
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

            SearchHits hits = searchResponse.getHits();

            long totalHits = hits.getTotalHits().value;

            SearchHit[] searchHits = hits.getHits();

            List<HotelEntity> list = new ArrayList<>();

            for (SearchHit searchHit : searchHits) {
                String sourceAsString = searchHit.getSourceAsString();
                list.add(JSON.parseObject(sourceAsString,HotelEntity.class));
            }
            Map<String, Object> map = new HashMap<>();
            map.put("list", list);
            map.put("totalResultSize", totalHits);

            return map;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    //分页查询
    @Override
    public Map<String, Object> pageQuery(int current, int size) {

        SearchRequest searchRequest = new SearchRequest("hotel");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        QueryBuilder queryBuilder = QueryBuilders.matchAllQuery();
        searchSourceBuilder.query(queryBuilder);

        //设置分页
        searchSourceBuilder.from((current-1)*size);
        searchSourceBuilder.size(size);

        searchRequest.source(searchSourceBuilder);
        try {
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

            SearchHits hits = searchResponse.getHits();

            long totalHits = hits.getTotalHits().value;

            SearchHit[] searchHits = hits.getHits();

            List<HotelEntity> list = new ArrayList<>();

            for (SearchHit searchHit : searchHits) {
                String sourceAsString = searchHit.getSourceAsString();
                list.add(JSON.parseObject(sourceAsString,HotelEntity.class));
            }

            Map<String, Object> map = new HashMap<>();
            map.put("list", list);
            map.put("totalResultSize", totalHits);
            map.put("current", current);
            //设置总页数
            map.put("totalPage",(totalHits+size-1)/size);

            return map;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    //按照品牌精确查询
    @Override
    public Map<String, Object> brandTermQuery(int current, int size, Map<String, Object> searchParam) {

        SearchRequest searchRequest = new SearchRequest("hotel");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        //设置精确查询
        if (!StringUtils.isEmpty(searchParam.get("brand"))) {
            QueryBuilder queryBuilder = QueryBuilders.termQuery("brand", searchParam.get("brand").toString());
            searchSourceBuilder.query(queryBuilder);
        }

        //设置分页
        searchSourceBuilder.from((current - 1) * size);
        searchSourceBuilder.size(size);

        searchRequest.source(searchSourceBuilder);
        try {
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

            SearchHits hits = searchResponse.getHits();

            long totalHits = hits.getTotalHits().value;

            SearchHit[] searchHits = hits.getHits();

            List<HotelEntity> list = new ArrayList<>();

            for (SearchHit searchHit : searchHits) {
                String sourceAsString = searchHit.getSourceAsString();
                list.add(JSON.parseObject(sourceAsString, HotelEntity.class));
            }

            Map<String, Object> map = new HashMap<>();
            map.put("list", list);
            map.put("totalResultSize", totalHits);
            map.put("current", current);
            //设置总页数
            map.put("totalPage", (totalHits + size - 1) / size);

            return map;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    //根据酒店名称匹配查询
    @Override
    public Map<String, Object> nameMatchQuery(Integer current, Integer size, Map<String, Object> searchParam) {

        //设置查询
        SearchRequest searchRequest = new SearchRequest("hotel");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();


        //todo 根据酒店名称匹配查询实现
        MatchQueryBuilder queryBuilder = QueryBuilders.matchQuery("name", searchParam.get("name"));
        searchSourceBuilder.query(queryBuilder);

        //设置分页
        searchSourceBuilder.from((current - 1) * size);
        searchSourceBuilder.size(size);

        searchRequest.source(searchSourceBuilder);

        //处理查询结果
        try {
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

            SearchHits hits = searchResponse.getHits();

            long totalHits = hits.getTotalHits().value;

            SearchHit[] searchHits = hits.getHits();

            List<HotelEntity> list = new ArrayList<>();

            for (SearchHit searchHit : searchHits) {
                String sourceAsString = searchHit.getSourceAsString();
                list.add(JSON.parseObject(sourceAsString, HotelEntity.class));
            }

            Map<String, Object> map = new HashMap<>();
            map.put("list", list);
            map.put("totalResultSize", totalHits);
            map.put("current", current);
            //设置总页数
            map.put("totalPage", (totalHits + size - 1) / size);

            return map;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //根据酒店品牌模糊查询
    @Override
    public Map<String, Object> nameWildcardQuery(Integer current, Integer size, Map<String, Object> searchParam) {

        //设置查询
        SearchRequest searchRequest = new SearchRequest("hotel");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        //todo 根据酒店名称模糊查询
        WildcardQueryBuilder queryBuilder = QueryBuilders.wildcardQuery("brand", searchParam.get("name")+"*");
        searchSourceBuilder.query(queryBuilder);


        //设置分页
        searchSourceBuilder.from((current - 1) * size);
        searchSourceBuilder.size(size);

        searchRequest.source(searchSourceBuilder);
        try {
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

            SearchHits hits = searchResponse.getHits();

            long totalHits = hits.getTotalHits().value;

            SearchHit[] searchHits = hits.getHits();

            List<HotelEntity> list = new ArrayList<>();

            for (SearchHit searchHit : searchHits) {
                String sourceAsString = searchHit.getSourceAsString();
                list.add(JSON.parseObject(sourceAsString, HotelEntity.class));
            }

            Map<String, Object> map = new HashMap<>();
            map.put("list", list);
            map.put("totalResultSize", totalHits);
            map.put("current", current);
            //设置总页数
            map.put("totalPage", (totalHits + size - 1) / size);

            return map;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //根据name,synopsis,area,address进行多域查询
    @Override
    public Map<String, Object> searchQueryStringQuery(Integer current, Integer size, Map<String, Object> searchParam) {

        //设置查询
        SearchRequest searchRequest = new SearchRequest("hotel");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        //todo 根据name,synopsis,area,address进行多域查询
        QueryStringQueryBuilder queryBuilder = QueryBuilders.queryStringQuery(searchParam.get("condition").toString())
                .field("name")
                .field("synopsis")
                .field("area")
                .field("address")
                .defaultOperator(Operator.OR);
        searchSourceBuilder.query(queryBuilder);

        //设置分页
        searchSourceBuilder.from((current - 1) * size);
        searchSourceBuilder.size(size);

        searchRequest.source(searchSourceBuilder);
        try {
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

            SearchHits hits = searchResponse.getHits();

            long totalHits = hits.getTotalHits().value;

            SearchHit[] searchHits = hits.getHits();

            List<HotelEntity> list = new ArrayList<>();

            for (SearchHit searchHit : searchHits) {
                String sourceAsString = searchHit.getSourceAsString();
                list.add(JSON.parseObject(sourceAsString, HotelEntity.class));
            }

            Map<String, Object> map = new HashMap<>();
            map.put("list", list);
            map.put("totalResultSize", totalHits);
            map.put("current", current);
            //设置总页数
            map.put("totalPage", (totalHits + size - 1) / size);

            return map;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //搜索框自动提示
    @Override
    public List<String> searchSuggestInfo(String key) {

        //定义结果集
        List<String> result = new ArrayList<>();

        //设置查询
        SearchRequest searchRequest = new SearchRequest("suggest");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        //todo 构建自动补全搜索
        SuggestBuilder suggestBuilder = new SuggestBuilder();
        CompletionSuggestionBuilder suggestion = SuggestBuilders.completionSuggestion("name").text(key).size(10);
        suggestBuilder.addSuggestion("my-suggest",suggestion);
        searchSourceBuilder.suggest(suggestBuilder);

        searchRequest.source(searchSourceBuilder);

        try {
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

            //todo 处理自动补全查询结果
            Suggest suggest = searchResponse.getSuggest();
            CompletionSuggestion completionSuggestion = suggest.getSuggestion("my-suggest");
            List<CompletionSuggestion.Entry> entries = completionSuggestion.getEntries();

            Map<String, Object> map = new HashMap<>();
            if (entries != null && entries.size()>0){
                entries.forEach(entry->{
                    if (!ObjectUtils.isEmpty(entry.getOptions())){
                        entry.forEach(action->{
                            String suggestInfo = action.getText().toString();
                            if (suggestInfo.equals(key)){
                                return;
                            }
                            result.add(suggestInfo);
                        });
                    }else {
                        map.put("name",key);
                    }
                });
            }

            //向索引库增量添加查询条件
            if (!ObjectUtils.isEmpty(map)){
                IndexRequest indexRequest = new IndexRequest("suggest").source(map);
                try {
                    restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("list" + result);

            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    //根据销量排序查询
    @Override
    public Map<String, Object> salesSortQuery(Integer current, Integer size, Map<String, Object> searchParam) {

        //设置查询
        SearchRequest searchRequest = new SearchRequest("hotel");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        //todo 设置按销量排序
        if ("desc".equalsIgnoreCase(searchParam.get("sortWay").toString())){
            searchSourceBuilder.sort("salesVolume", SortOrder.DESC);
        }else {
            searchSourceBuilder.sort("salesVolume",SortOrder.ASC);
        }

        //设置分页
        searchSourceBuilder.from((current - 1) * size);
        searchSourceBuilder.size(size);

        searchRequest.source(searchSourceBuilder);
        try {
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

            SearchHits hits = searchResponse.getHits();

            long totalHits = hits.getTotalHits().value;

            SearchHit[] searchHits = hits.getHits();

            List<HotelEntity> list = new ArrayList<>();

            for (SearchHit searchHit : searchHits) {
                String sourceAsString = searchHit.getSourceAsString();
                list.add(JSON.parseObject(sourceAsString, HotelEntity.class));
            }

            Map<String, Object> map = new HashMap<>();
            map.put("list", list);
            map.put("totalResultSize", totalHits);
            map.put("current", current);
            //设置总页数
            map.put("totalPage", (totalHits + size - 1) / size);
            map.put("sortWay", searchParam.get("sortWay"));


            return map;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //根据价格范围查询
    @Override
    public Map<String, Object> priceRangeQuery(Integer current, Integer size, Map<String, Object> searchParam) {

        //设置查询
        SearchRequest searchRequest = new SearchRequest("hotel");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        //todo 根据价格范围查询
        RangeQueryBuilder queryBuilder = QueryBuilders.rangeQuery("price")
                .gte(searchParam.get("minPrice"))
                .lte(searchParam.get("maxPrice"));
        searchSourceBuilder.query(queryBuilder);


        //设置分页
        searchSourceBuilder.from((current - 1) * size);
        searchSourceBuilder.size(size);

        searchRequest.source(searchSourceBuilder);

        //处理查询结果
        try {
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

            SearchHits hits = searchResponse.getHits();

            long totalHits = hits.getTotalHits().value;

            SearchHit[] searchHits = hits.getHits();

            List<HotelEntity> list = new ArrayList<>();

            for (SearchHit searchHit : searchHits) {
                String sourceAsString = searchHit.getSourceAsString();
                list.add(JSON.parseObject(sourceAsString, HotelEntity.class));
            }

            Map<String, Object> map = new HashMap<>();
            map.put("list", list);
            map.put("totalResultSize", totalHits);
            map.put("current", current);
            //设置总页数
            map.put("totalPage", (totalHits + size - 1) / size);

            map.put("minPrice", searchParam.get("minPrice"));
            map.put("maxPrice", searchParam.get("maxPrice"));

            return map;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    //多条件查询
    //搜索框多域、品牌精确、城市精确、星级精确、价格范围、销量排序
    @Override
    public Map<String, Object> searchBoolQuery(Integer current, Integer size, Map<String, Object> searchParam) {

        //设置查询
        SearchRequest searchRequest = new SearchRequest("hotel");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();


        //todo 多条件查询 ：多域、品牌精确、城市精确、星级精确、价格范围、销量排序
        //设置查询方式
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        //多域
        if (!StringUtils.isEmpty(searchParam.get("condition"))) {
            QueryBuilder queryBuilder = QueryBuilders.queryStringQuery(searchParam.get("condition").toString())
                    .field("name")
                    .field("synopsis")
                    .field("area")
                    .field("address")
                    .defaultOperator(Operator.OR);
            boolQueryBuilder.must(queryBuilder);
        }

        //品牌精确
        if (!StringUtils.isEmpty(searchParam.get("brand"))) {
            TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("brand", searchParam.get("brand"));
            boolQueryBuilder.filter(termQueryBuilder);
        }

        //城市精确
        if (!StringUtils.isEmpty(searchParam.get("area"))) {
            TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("area", searchParam.get("area"));
            boolQueryBuilder.filter(termQueryBuilder);
        }

        //星级精确
        if (!StringUtils.isEmpty(searchParam.get("specs"))) {
            TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("specs", searchParam.get("specs"));
            boolQueryBuilder.filter(termQueryBuilder);
        }

        //价格范围
        if (!StringUtils.isEmpty(searchParam.get("minPrice")) && !StringUtils.isEmpty(searchParam.get("maxPrice"))) {
            RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("price")
                    .gte(searchParam.get("minPrice"))
                    .lte(searchParam.get("maxPrice"));
            boolQueryBuilder.filter(rangeQueryBuilder);
        }

        //销量排序
        if (!StringUtils.isEmpty(searchParam.get("sortWay"))) {
            if ("desc".equalsIgnoreCase(searchParam.get("sortWay").toString())) {
                searchSourceBuilder.sort("salesVolume", SortOrder.DESC);
            } else {
                searchSourceBuilder.sort("salesVolume", SortOrder.ASC);
            }
        }

        searchSourceBuilder.query(boolQueryBuilder);


        //设置分页
        searchSourceBuilder.from((current - 1) * size);
        searchSourceBuilder.size(size);

        searchRequest.source(searchSourceBuilder);

        //处理查询结果
        try {
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

            SearchHits hits = searchResponse.getHits();

            long totalHits = hits.getTotalHits().value;

            SearchHit[] searchHits = hits.getHits();

            List<HotelEntity> list = new ArrayList<>();

            for (SearchHit searchHit : searchHits) {
                String sourceAsString = searchHit.getSourceAsString();
                list.add(JSON.parseObject(sourceAsString, HotelEntity.class));
            }

            Map<String, Object> map = new HashMap<>();
            map.put("list", list);
            map.put("totalResultSize", totalHits);
            map.put("current", current);
            //设置总页数
            map.put("totalPage", (totalHits + size - 1) / size);

            map.put("brand", searchParam.get("brand"));
            map.put("area", searchParam.get("area"));
            map.put("specs", searchParam.get("specs"));
            map.put("sortWay", searchParam.get("sortWay"));
            map.put("minPrice", searchParam.get("minPrice"));
            map.put("maxPrice", searchParam.get("maxPrice"));

            return map;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    //地址纠错查询
    @Override
    public Map<String, Object> searchFuzzyQuery(Integer current, Integer size, Map<String, Object> searchParam) {

        //设置查询
        SearchRequest searchRequest = new SearchRequest("hotel");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        //设置查询方式

        //todo 设置城市为纠错查询
        FuzzyQueryBuilder queryBuilder = QueryBuilders.fuzzyQuery("area", searchParam.get("area"))
                .fuzziness(Fuzziness.ONE)
                .prefixLength(1);

        searchSourceBuilder.query(queryBuilder);

        //设置分页
        searchSourceBuilder.from((current - 1) * size);
        searchSourceBuilder.size(size);

        searchRequest.source(searchSourceBuilder);

        // 处理查询结果
        try {
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

            SearchHits hits = searchResponse.getHits();

            long totalHits = hits.getTotalHits().value;

            SearchHit[] searchHits = hits.getHits();

            List<HotelEntity> list = new ArrayList<>();

            for (SearchHit searchHit : searchHits) {
                String sourceAsString = searchHit.getSourceAsString();
                list.add(JSON.parseObject(sourceAsString, HotelEntity.class));
            }

            Map<String, Object> map = new HashMap<>();
            map.put("list", list);
            map.put("totalResultSize", totalHits);
            map.put("current", current);

            map.put("brand", searchParam.get("brand"));
            map.put("area", searchParam.get("area"));
            map.put("specs", searchParam.get("specs"));
            map.put("sortWay", searchParam.get("sortWay"));
            map.put("minPrice", searchParam.get("minPrice"));
            map.put("maxPrice", searchParam.get("maxPrice"));
            //设置总页数
            map.put("totalPage", (totalHits + size - 1) / size);

            return map;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    //按名称高亮查询
    @Override
    public Map<String, Object> searchHighLight(Integer current, Integer size, Map<String, Object> searchParam) {

        //设置查询
        SearchRequest searchRequest = new SearchRequest("hotel");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        //设置查询方式
        if (!StringUtils.isEmpty(searchParam.get("condition"))) {
            QueryBuilder queryBuilder = QueryBuilders.queryStringQuery(searchParam.get("condition").toString())
                    .field("name")
                    .field("synopsis")
                    .field("area")
                    .field("address")
                    .defaultOperator(Operator.OR);
            searchSourceBuilder.query(queryBuilder);
        }
        //设置分页
        searchSourceBuilder.from((current - 1) * size);
        searchSourceBuilder.size(size);

        //todo 查询高亮设置
        HighlightBuilder highlightBuider = new HighlightBuilder();
        highlightBuider.field("name");
        highlightBuider.preTags("<font color='red'>");
        highlightBuider.postTags("</font>");
        searchSourceBuilder.highlighter(highlightBuider);

        searchRequest.source(searchSourceBuilder);
        try {
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

            SearchHits hits = searchResponse.getHits();

            long totalHits = hits.getTotalHits().value;

            SearchHit[] searchHits = hits.getHits();

            List<HotelEntity> list = new ArrayList<>();

            for (SearchHit searchHit : searchHits) {
                String sourceAsString = searchHit.getSourceAsString();
                HotelEntity hotelEntity = JSON.parseObject(sourceAsString, HotelEntity.class);

                //todo  处理高亮结果
                Map<String, HighlightField> highlightFields = searchHit.getHighlightFields();
                HighlightField highlightField = highlightFields.get("name");
                if (highlightField!=null){
                    Text[] fragments = highlightField.fragments();
                    hotelEntity.setName(fragments[0].toString());
                }

                list.add(hotelEntity);
            }

            Map<String, Object> map = new HashMap<>();
            map.put("list", list);
            map.put("totalResultSize", totalHits);
            map.put("current", current);
            //设置总页数
            map.put("totalPage", (totalHits + size - 1) / size);

            return map;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //品牌分组聚合
    @Override
    public Map<String, Object> searchBrandGroupQuery(Integer current, Integer size, Map<String, Object> searchParam) {

        //设置查询
        SearchRequest searchRequest = new SearchRequest("hotel");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        //设置查询方式
        if (!StringUtils.isEmpty(searchParam.get("condition"))) {
            QueryBuilder queryBuilder = QueryBuilders.queryStringQuery(searchParam.get("condition").toString())
                    .field("name")
                    .field("synopsis")
                    .field("area")
                    .field("address")
                    .defaultOperator(Operator.OR);
            searchSourceBuilder.query(queryBuilder);
        }

        //todo 按品牌分组聚合
        TermsAggregationBuilder termsAggregationBuilder = AggregationBuilders.terms("hotel_brand").field("brand").size(100);
        searchSourceBuilder.aggregation(termsAggregationBuilder);

        //设置分页
        searchSourceBuilder.from((current - 1) * size);
        searchSourceBuilder.size(size);

        searchRequest.source(searchSourceBuilder);
        try {
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

            SearchHits hits = searchResponse.getHits();

            long totalHits = hits.getTotalHits().value;

            SearchHit[] searchHits = hits.getHits();

            List<HotelEntity> list = new ArrayList<>();

            for (SearchHit searchHit : searchHits) {
                String sourceAsString = searchHit.getSourceAsString();
                list.add(JSON.parseObject(sourceAsString, HotelEntity.class));
            }

            //todo 获取并处理聚合查询结果
            Aggregations aggregations = searchResponse.getAggregations();
            Map<String, Aggregation> aggregationMap = aggregations.getAsMap();
            Terms terms = (Terms) aggregationMap.get("hotel_brand");
            List<? extends Terms.Bucket> buckets = terms.getBuckets();

            List<String> brandList = new ArrayList<>();
            buckets.forEach(bucket -> {

                brandList.add(bucket.getKeyAsString());

            });

            System.out.println(brandList);


            Map<String, Object> map = new HashMap<>();
            map.put("list", list);
            map.put("totalResultSize", totalHits);
            map.put("current", current);
            //设置总页数
            map.put("totalPage", (totalHits + size - 1) / size);

            //设置品牌分组列表
            map.put("brandList", brandList);

            return map;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //自定义日期时间段聚合统计某品牌的酒店销量
    @Override
    public List<Map<String, Object>> searchDateHistogram(Map<String, Object> searchParam) {

        //定义结果集
        List<Map<String, Object>> result = new ArrayList<>();

        //设置查询
        SearchRequest searchRequest = new SearchRequest("hotel");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        //todo 自定义日期时间段范围查询
        RangeQueryBuilder queryBuilder = QueryBuilders.rangeQuery("createTime")
                .gte(searchParam.get("minTime"))
                .lte(searchParam.get("maxTime"))
                .format("yyyy-MM-dd");
        searchSourceBuilder.query(queryBuilder);

        //todo 聚合查询设置
        TermsAggregationBuilder aggregationBuilder = AggregationBuilders.terms("hotel_brand").field("brand").size(100);

        //构建二级聚合
        SumAggregationBuilder secondAggregation = AggregationBuilders.sum("hotel_salesVolume").field("salesVolume");
        aggregationBuilder.subAggregation(secondAggregation);

        searchSourceBuilder.aggregation(aggregationBuilder);


        searchRequest.source(searchSourceBuilder);
        try {

            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

            //todo 获取聚合结果并处理
            Aggregations aggregations = searchResponse.getAggregations();
            Map<String, Aggregation> aggregationMap = aggregations.asMap();
            Terms terms = (Terms) aggregationMap.get("hotel_brand");
            List<? extends Terms.Bucket> buckets = terms.getBuckets();
            buckets.forEach(bucket -> {

                Map<String, Object> info = new HashMap<>();
                info.put("brand",bucket.getKeyAsString());

                //获取二级聚合数据
                ParsedSum parsedSum = bucket.getAggregations().get("hotel_salesVolume");
                Integer sumValue = (int) parsedSum.getValue();
                info.put("sumValue",sumValue);

                result.add(info);
            });

            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //加权查询
    @Override
    public Map<String, Object> searchScoreQuery(Integer current, Integer size, Map<String, Object> searchParam) {

        SearchRequest searchRequest = new SearchRequest("hotel");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        //todo 构建查询
        //构建主查询条件
        QueryStringQueryBuilder queryStringQueryBuilder = QueryBuilders.queryStringQuery(searchParam.get("condition").toString())
                .field("name")
                .field("synopsis")
                .field("area")
                .field("address")
                .defaultOperator(Operator.OR);

        //构建加权条件
        FunctionScoreQueryBuilder.FilterFunctionBuilder[] scoreFunctionBuilder = new FunctionScoreQueryBuilder.FilterFunctionBuilder[]{
                new FunctionScoreQueryBuilder.FilterFunctionBuilder(QueryBuilders.termQuery("isAd",1), ScoreFunctionBuilders.weightFactorFunction(100))
        };

        FunctionScoreQueryBuilder queryBuilder = QueryBuilders.functionScoreQuery(queryStringQueryBuilder, scoreFunctionBuilder);
        searchSourceBuilder.query(queryBuilder);


        //设置分页
        searchSourceBuilder.from((current - 1) * size);
        searchSourceBuilder.size(size);

        searchRequest.source(searchSourceBuilder);

        try {
            //处理查询结果
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

            SearchHits hits = searchResponse.getHits();

            long totalHits = hits.getTotalHits().value;

            SearchHit[] searchHits = hits.getHits();

            List<HotelEntity> list = new ArrayList<>();

            for (SearchHit searchHit : searchHits) {
                String sourceAsString = searchHit.getSourceAsString();
                list.add(JSON.parseObject(sourceAsString, HotelEntity.class));
            }

            Map<String, Object> map = new HashMap<>();
            map.put("list", list);
            map.put("totalResultSize", totalHits);
            map.put("current", current);
            //设置总页数
            map.put("totalPage", (totalHits + size - 1) / size);

            return map;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
