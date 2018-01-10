package com.taotao.content.service;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.common.pojo.TaotaoResult;

import java.util.List;

/**
 *  后台管理 - 内容分类管理
 *  内容服务(内容列表)
 *  @author sucl
 */
public interface IContentCategoryService {

    /**
     * 查询所有内容
     * @param parentId
     * @return
     */
    public  List<EasyUITreeNode> getContentCategoryList(Long parentId);

    /**
     * 添加内容
     * @param parentId
     * @param name
     * @return
     */
    TaotaoResult addContentCategory(Long parentId, String name);

    /**
     * 删除节点
     * @param parentId
     * @param id
     * @return
     */
    TaotaoResult deleteContentCatory(Long parentId,Long id);

    /**
     * 修改内容
     * @param parentId
     * @param name
     * @return
     */
    TaotaoResult updateContentCatory(Long parentId, String name);

}
