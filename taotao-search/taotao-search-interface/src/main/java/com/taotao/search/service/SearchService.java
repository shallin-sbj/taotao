package com.taotao.search.service;

import com.taotao.common.pojo.SearchResult;

/**
 * 查询商品服务
 * @author sucl
 */
public interface SearchService {
    /**
     *  查询商品
     * @param queryString
     * @return
     */
    SearchResult search(String queryString ,int page,int rows);
}
