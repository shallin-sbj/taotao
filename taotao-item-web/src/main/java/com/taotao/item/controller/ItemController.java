package com.taotao.item.controller;

import com.taotao.item.pojo.Item;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.jws.WebParam;

/**
 * 商品详情界面controller
 */
@Controller
public class ItemController {

    @Autowired
    private ItemService itemService;

    @RequestMapping("/item/{itemId}")
    public String showItem(@PathVariable long itemId, Model model) {
        TbItem tbItem = itemService.getItemById(itemId); // 基本信息
        Item item = new Item(tbItem);
        TbItemDesc desc = itemService.getItemDescById(itemId); // 详情
        model.addAttribute("item", item);
        model.addAttribute("itemDesc", desc);
        return "item";
    }
}
