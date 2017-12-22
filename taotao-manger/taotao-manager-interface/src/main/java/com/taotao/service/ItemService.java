package com.taotao.service;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;

/**
 *  @商品相关
 *  商品管理服务
 *  author:sucl
 */
public interface ItemService {

    /**
     * 根据商品ID查询商品
     * @param itemId
     * @return
     */
    TbItem getItemById(Long itemId);

    /**
     *
     * @分页查询商品
     */
    EasyUIDataGridResult getItemList(int page, int rows);

    /**
     * @添加商品
     */
    public TaotaoResult addItem(TbItem item,String desc);
}
