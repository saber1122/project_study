package com.itcast.service;

import java.util.List;
import java.util.Map;

public interface HotelService {

    /**
     * 批量添加
     * @return
     */
    int addDocToES();

    /**
     * 查询所有
     * @return
     */
    Map<String, Object> matchAllQuery();

    /**
     * 分页查询
     * @param currentPage 当前页
     * @param size 每页显示多少条
     * @return
     */
    Map<String, Object> pageQuery(int currentPage, int size);

    /**
     * 根据品牌精确查询
     * @param searchParam
     * @return
     */
    Map<String, Object> brandTermQuery(int currentPage, int size, Map<String, Object> searchParam);

    /**
     * 根据酒店名称条件分词查询
     * @param current
     * @param size
     * @param searchParam
     * @return
     */
    Map<String, Object> nameMatchQuery(Integer current, Integer size, Map<String, Object> searchParam);

    /**
     * 根据酒店名称模糊查询
     * @param current
     * @param size
     * @param searchParam
     * @return
     */
    Map<String, Object> nameWildcardQuery(Integer current, Integer size, Map<String, Object> searchParam);

    /**
     * 根据name,synopsis,area,address进行多域查询
     * @param current
     * @param size
     * @param searchParam 前端查询条件
     * @return
     */
    Map<String, Object> searchQueryStringQuery(Integer current, Integer size, Map<String, Object> searchParam);

    /**
     * 根据销量排序查询
     * @param current
     * @param size
     * @param searchParam
     * @return
     */
    Map<String, Object> salesSortQuery(Integer current, Integer size, Map<String, Object> searchParam);

    /**
     * 根据价格范围查询
     * @param current
     * @param size
     * @param searchParam
     * @return
     */
    Map<String, Object> priceRangeQuery(Integer current, Integer size, Map<String, Object> searchParam);

    /**
     * 多条件查询
     * 多域、品牌精确、城市精确、星级精确、价格范围、销量排序
     * @param current
     * @param size
     * @param searchParam
     * @return
     */
    Map<String, Object> searchBoolQuery(Integer current, Integer size, Map<String, Object> searchParam);

    /**
     * 按名称高亮查询
     * @param current
     * @param size
     * @param searchParam
     * @return
     */
    Map<String, Object> searchHighLight(Integer current, Integer size, Map<String, Object> searchParam);

    /**
     * 搜索框自动提示
     * @param key 前端输入查询
     * @return
     */
    List<String> searchSuggestInfo(String key);

    /**
     * 地址纠错查询
     * @param current
     * @param size
     * @param searchParam
     * @return
     */
    Map<String, Object> searchFuzzyQuery(Integer current, Integer size, Map<String, Object> searchParam);

    /**
     * 品牌分组聚合查询
     * @param current
     * @param size
     * @param searchParam
     * @return
     */
    Map<String, Object> searchBrandGroupQuery(Integer current, Integer size, Map<String, Object> searchParam);

    /**
     *自定义日期时间段聚合统计某品牌的酒店销量
     * @param searchParam
     * @return
     */
    List<Map<String, Object>> searchDateHistogram(Map<String, Object> searchParam);
	
	/**
     * 加权查询
     * @param current
     * @param size
     * @param searchParam
     * @return
     */
    Map<String, Object> searchScoreQuery(Integer current, Integer size, Map<String, Object> searchParam);
}
