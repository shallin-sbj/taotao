package com.taotao.search.mapper;

import com.taotao.common.pojo.SearchItem;

import java.util.List;

/**
 * 搜索mapper,solr 查询方法实现类
 * @author sucl
 */
public interface SearchItemMapper {
    /**
     *  查询商品
     */
    List<SearchItem> getItemList();

    SearchItem getItemById(long itemId);

}
