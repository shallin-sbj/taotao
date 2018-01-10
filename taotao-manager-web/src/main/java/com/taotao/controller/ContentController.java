package com.taotao.controller;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.content.service.IContentService;
import com.taotao.pojo.TbContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 后台管理-内容管理Controller
 * @author sucl
 */
@Controller
public class ContentController {

    @Autowired
    private IContentService iContentService;

    @RequestMapping("/content/save")
    @ResponseBody
    public TaotaoResult addContent(TbContent content){
        return iContentService.addContent(content);
    }

    @RequestMapping("/content/delete")
    @ResponseBody
    public TaotaoResult deleteContent(String ids){
        return iContentService.deleteContent(ids);
    }

    @RequestMapping("/content/edit")
    @ResponseBody
    public  TaotaoResult updateContent(TbContent content){
        return iContentService.modifyContent(content);
    }
    @RequestMapping("/content/query/list")
    @ResponseBody
    public EasyUIDataGridResult queryAllContent(Long categoryId,Integer page ,Integer rows) {
        EasyUIDataGridResult result = iContentService.queryAllContent(categoryId,page,rows);
        return result;
    }

}
