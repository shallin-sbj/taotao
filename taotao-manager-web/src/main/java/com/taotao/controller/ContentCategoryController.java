package com.taotao.controller;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.content.service.IContentCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 内容分类管理controller
 *
 * @author sucl
 */
@Controller
public class ContentCategoryController {

    @Autowired
    private IContentCategoryService iContentCategoryService;

    /**
     * 查询所有的
     * @param parentId
     * @return
     */
    @RequestMapping("/content/category/list")
    @ResponseBody
    public List<EasyUITreeNode> getContentCatrgoryList(@RequestParam(value = "id", defaultValue = "0") Long parentId) {
        return iContentCategoryService.getContentCategoryList(parentId);
    }

    /**
     * 添加内容
      * @param parentId
     * @param name
     * @return
     */

    @RequestMapping("/content/category/create")
    @ResponseBody
    public TaotaoResult addContentCatrgory(Long parentId,String name){
        return iContentCategoryService.addContentCategory(parentId,name);
    }

    /**
     * 删除内容
     * @param id ： 删除节点
     * @return
     */
    @RequestMapping("/content/category/delete")
    @ResponseBody
    public  TaotaoResult deleteContentCatory(Long parentId,Long id) {
        return iContentCategoryService.deleteContentCatory(parentId,id);
    }

    /**
     * 修改内容
     * @return
     */
    @RequestMapping("/content/category/update")
    @ResponseBody
    public TaotaoResult updateContentCatory(Long id,String name) {
        return iContentCategoryService.updateContentCatory(id,name);
    }

}
