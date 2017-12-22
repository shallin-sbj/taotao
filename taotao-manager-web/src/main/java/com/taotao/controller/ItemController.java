package com.taotao.controller;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.service.ItemService;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author:sucl
 * remark: 商品管理controller
 */
@Controller
public class ItemController {

    LogFactory log  = LogFactory.getFactory();

    @Autowired
    private ItemService itemService;
    /**
     * 测试调用
     * @param itemId
     * @return
     */
    @RequestMapping("/item/{itemId}")
    @ResponseBody
    public TbItem getItemById(@PathVariable Long itemId) {
        return itemService.getItemById(itemId);
    }

    @RequestMapping("/item/list")
    @ResponseBody
    public EasyUIDataGridResult getItemList(Integer page,Integer rows) {
        return itemService.getItemList(page, rows);
    }

    @RequestMapping(value = "/item/save",method = RequestMethod.POST)
    public TaotaoResult addItem(TbItem item,String desc) {
        TaotaoResult result = itemService.addItem(item,desc);
        System.out.println(result.getMsg() + ":" + result.getData());
        return result;
    }



}
