package com.taotao.search.service.impl;

import com.taotao.common.pojo.SearchResult;
import com.taotao.search.dao.SearchDao;
import com.taotao.search.service.SearchService;
import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *  搜索服务功能实现
 */
@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private SearchDao searchDao;   // 注意spring容器是否能够扫描到该dao

    @Override
    public SearchResult search(String queryString, int page, int rows) {
        SearchResult searchResult= new SearchResult();
        //根据查询条件拼装查询对象
        //创建一个SolrQuery对象
        SolrQuery query = new SolrQuery();
        //设置查询条件
        query.setQuery(queryString);
        //设置分页条件
        if (page <1) {   // 计算起始页
            page = 1;
        }else {
            page = (page -1)*rows;
        }
        query.setStart(page);
        if (rows <1) {
            rows = 10;
        }
        query.setRows(rows);
        //设置默认搜索域
        query.set("df","item_title");
        //设置高亮显示
        query.setHighlight(true);
        query.setHighlightSimplePre("<font color='red'>");
        query.setHighlightSimplePost("</font>");
        //调用dao执行查询
        try {
            searchResult = searchDao.search(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //计算查询结果的总页数
        long recordCount = searchResult.getRecordCount();
        long pages = recordCount/rows;
        if (recordCount%rows >0) {
            pages ++;
        }
        searchResult.setTotalPages(pages);
        //返回结果
        return searchResult;
    }
}
