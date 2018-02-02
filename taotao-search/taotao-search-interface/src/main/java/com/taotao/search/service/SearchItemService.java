package com.taotao.search.service;

import com.taotao.common.pojo.TaotaoResult;

/**
 *  sorl 实现商品导入
 * @author sucl
 */
public interface SearchItemService {
    /**
     * 实现产品导入到solr 库
     * @return
     */
    TaotaoResult importItemToIndex();

}
