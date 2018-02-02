package com.taotao.search.service.impl;

import com.taotao.common.pojo.SearchItem;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.search.mapper.SearchItemMapper;
import com.taotao.search.service.SearchItemService;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 商品导入索引
 *
 * @author sucl
 */
@Service
public class SearchItemServiceImpl implements SearchItemService {

    private final static Logger log = Logger.getLogger(SearchItemServiceImpl.class);

    /**
     * 该romMapper 比较特殊，注意spring是否能够扫描到该包
     */
    @Autowired
    private SearchItemMapper searchItemMapper;
    @Autowired
    private SolrServer solrServer;

    @Override
    public TaotaoResult importItemToIndex() {
        try {
            //1、先查询所有商品数据
            List<SearchItem> itemList = searchItemMapper.getItemList();
            //2、遍历商品数据添加到索引库
            log.info("importItemToIndex import start:" + new Date());
            for (SearchItem searchItem : itemList) {
                //创建文档对象
                SolrInputDocument document = new SolrInputDocument();
                //向文档中添加域
                document.addField("id", searchItem.getId());
                document.addField("item_title", searchItem.getTitle());
                document.addField("item_sell_point", searchItem.getSell_point());
                document.addField("item_price", searchItem.getPrice());
                document.addField("item_image", searchItem.getImage());
                document.addField("item_category_name", searchItem.getCategory_name());
                document.addField("item_desc", searchItem.getItem_desc());
                //把文档写入索引库
                solrServer.add(document);
            }
            //3、提交
            solrServer.commit();
            log.info("importItemToIndex import end:" + new Date());
        } catch (Exception e) {
            e.printStackTrace();
            return TaotaoResult.build(500, "数据导入失败");
        }
        //4、返回添加成功
        return TaotaoResult.ok();
    }
}
