package com.taotao.portal.controller;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.taotao.common.utlis.JsonUtils;
import com.taotao.content.service.IContentService;
import com.taotao.pojo.TbContent;
import com.taotao.portal.pojo.AD1Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页控制器
 * 首页
 * @author sucl
 */
@Controller
public class IndexController {

    private final static Logger log = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    private IContentService iContentService;

    /**
     *  首页大广告位内容id
     */
    @Value("${AD1_CATEGORY_ID}")
    private Long AD1_CATEGORY_ID;

    @Value("${AD1_WIDTH}")
    private String AD1_WIDTH;
    @Value("${AD1_WIDTH_B}")
    private String AD1_WIDTH_B;
    @Value("${AD1_HEIGHT}")
    private Integer AD1_HEIGHT;
    @Value("${AD1_HEIGHT_B}")
    private Integer AD1_HEIGHT_B;


    @RequestMapping("/index")
    public String showIndex(Model model) {
        // 根据contentId查询（轮播图）内容列表
        List<TbContent> contentList = iContentService.getContentByCid(AD1_CATEGORY_ID);
        // 把列表转换为Ad1Node列表
        List<AD1Node> ad1Nodes = new ArrayList<AD1Node>();
        // 把列表转换为json数据
        for (TbContent data:contentList ) {
            AD1Node  node = new AD1Node();
            node.setHeight(AD1_HEIGHT);
            node.setHeightB(AD1_HEIGHT_B);
            node.setWidth(AD1_WIDTH);
            node.setWidthB(AD1_WIDTH_B);
            node.setSrc(data.getPic());
            node.setSrcB(data.getPic2());
            node.setHref(data.getUrl());
            ad1Nodes.add(node);
        }
        // 传输数据
        String adStr = JsonUtils.objectToJson(ad1Nodes);
        log.info("====== showIndex adStr : " + adStr);
        // 把json 传递给页面 ad1 是index里头的一个参数
        model.addAttribute("ad1",adStr);
        return "index";
    }

}
