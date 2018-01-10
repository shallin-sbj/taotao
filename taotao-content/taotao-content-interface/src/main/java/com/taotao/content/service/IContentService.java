package com.taotao.content.service;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbContent;

import java.util.List;

/**
 * 后台管理-内容接口
 * @author sucl
 */
public interface IContentService {

    TaotaoResult addContent(TbContent content);

    TaotaoResult deleteContent(String contentIds);

    TaotaoResult modifyContent(TbContent content);

    EasyUIDataGridResult queryAllContent(Long id, Integer page,Integer rows);

    /**
     * 查询首页信息
     * @param contentId
     * @return
     */
    List<TbContent> getContentByCid(Long contentId);


}
