package com.taotao.search.dao;

import com.taotao.common.pojo.SearchItem;
import com.taotao.common.pojo.SearchResult;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 搜索索引库 通用
 *
 * @author sucl
 */
@Repository
public class SearchDao {

    @Autowired
    private SolrServer solrServer;

    /**
     * 查询
     * @param solrQuery
     * @return
     * @throws Exception
     */
    public SearchResult search(SolrQuery solrQuery) throws Exception {
        QueryResponse queryResponse = solrServer.query(solrQuery);
        SolrDocumentList solrDocumentList = queryResponse.getResults();
        SearchResult result = new SearchResult();
        result.setRecordCount(solrDocumentList.getNumFound());
        // 把查询结果封装到searchItem对象中
        List<SearchItem> itemList = new ArrayList<SearchItem>();
        for (SolrDocument solrDocument : solrDocumentList) {
            SearchItem item = new SearchItem();
            item.setCategory_name((String) solrDocument.get("item_category_name"));
            item.setId((String) solrDocument.get("id"));
            // 取一张图片时,解决当存在多张图片时，搜索结果不显示的问题
            String image = (String) solrDocument.get("item_image");
            if (!StringUtils.isEmpty(image)) {
                image = image.split(",")[0];
            }
            item.setImage(image);
            item.setPrice((long) solrDocument.get("item_price"));
            item.setSell_point((String) solrDocument.get("item_sell_point"));
            //取高亮显示
            Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
            List<String> list = highlighting.get(solrDocument.get("id")).get("item_title");
            String title = "";
            if (list != null && list.size() > 0) {
                title = list.get(0);
            } else {
                title = (String) solrDocument.get("item_title");
            }
            item.setTitle(title);
            //添加到商品列表
            itemList.add(item);
        }
        //把结果添加到SearchResult中
        result.setItemList(itemList);
        //返回
        return result;
    }
}
