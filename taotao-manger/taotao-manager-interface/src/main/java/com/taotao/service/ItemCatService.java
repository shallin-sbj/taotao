package com.taotao.service;

import com.taotao.common.pojo.EasyUIDTreeNode;

import java.util.List;

/**
 * 淘淘后台 - 商品类目
 *
 * @author sucl
 */
public interface ItemCatService {

    /**
     * 根据父节点ID查询子节点
     * @param parentId
     * @return
     */
    List<EasyUIDTreeNode> getItemCatList(long parentId);
}
