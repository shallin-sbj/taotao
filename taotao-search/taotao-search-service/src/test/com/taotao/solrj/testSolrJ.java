package com.taotao.solrj;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class testSolrJ {
    @Test
    public void testAndDocument() throws Exception {
        //创建一个SolrServer对象。创建一个HttpSolrServer对象
        //需要指定solr服务的url
        SolrServer solrServer = new HttpSolrServer("http://120.79.130.20:8082/solr/Order");
        //创建一个文档对象SolrInputDocument
        SolrInputDocument document = new SolrInputDocument();
        //向文档中添加域，必须有id域，域的名称必须在schema.xml中定义
        document.addField("id", "1234");
        document.addField("item_title", "测试商品3");
        document.addField("item_price", 1000);
        //把文档对象写入索引库
        solrServer.add(document);
        //  提交
        solrServer.commit();
    }

    @Test
    public void deleteDocument() throws Exception{
        SolrServer solrServer = new HttpSolrServer("http://120.79.130.20:8082/solr/Order");
        UpdateResponse id = solrServer.deleteById(String.valueOf(123));
        System.out.println(id);
        solrServer.commit();
    }
    @Test
    public void deleteDocment2() throws Exception{
        SolrServer solrServer = new HttpSolrServer("http://120.79.130.20:8082/solr/Order");
        solrServer.deleteByQuery("id", Integer.parseInt(String.valueOf(123)));
        solrServer.commit();
    }

    @Test
    public void searchDocument() throws Exception {
        //创建一个SolrServer对象
        //创建一个SolrQuery对象
        //设置查询条件、过滤条件、分页条件、排序条件、高亮
        //分页条件
        //设置默认搜索域
        //设置高亮
        //高亮显示的域
        //执行查询，得到一个Response对象
        //取查询结果
        //取查询结果总记录数
        SolrServer solrServer = new HttpSolrServer("http://120.79.130.20:8082/solr/Order");
        SolrQuery solrQuery = new SolrQuery();
        solrQuery.setQuery("荣耀");
        solrQuery.setStart(0);
        solrQuery.setRows(100);
        solrQuery.set("df", "item_keywords");
        solrQuery.setHighlight(true);
        solrQuery.addHighlightField("item_title");
        solrQuery.setHighlightSimplePre("<div>");
        solrQuery.setHighlightSimplePost("</div>");
        QueryResponse response = solrServer.query(solrQuery);
        SolrDocumentList solrDocumentList = response.getResults();
        System.out.println("查询结果总记录数：" + solrDocumentList.getNumFound());
        for (SolrDocument solrDocument : solrDocumentList) {
            System.out.println(solrDocument.get("id"));
            //取高亮显示
            Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
            List<String> list = highlighting.get(solrDocument.get("id")).get("item_title");
            String itemTitle = "";
            if (list != null && list.size() > 0) {
                itemTitle = list.get(0);
            } else {
                itemTitle = (String) solrDocument.get("item_title");
            }
            System.out.println(itemTitle);
            System.out.println(solrDocument.get("item_sell_point"));
            System.out.println(solrDocument.get("item_price"));
            System.out.println(solrDocument.get("item_image"));
            System.out.println(solrDocument.get("item_category_name"));
            System.out.println("=============================================");
        }
    }
}


