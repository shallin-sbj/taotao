package com.taotao.search.controller;

import com.taotao.common.pojo.SearchResult;
import com.taotao.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 搜索服务控制器
 */
@Controller
public class SearchController {

    @Value("${SEARCH_RESULT_ROWS}")
    private Integer SEARCH_RESULT_ROWS;
    @Autowired
    private SearchService searchService;

//    /**
//     *  商品信息查询控制器
//     * @param queryStr 查询条件
//     * @param page     页面
//     * @param model    页面model
//     * @return
//     */
    @RequestMapping("/search")
    public String search(@RequestParam("q")String queryString,
                         @RequestParam(defaultValue="1")Integer page, Model model) throws Exception {
        //调用服务执行查询
        //把查询条件进行转码，解决get乱码问题
        queryString = new String(queryString.getBytes("iso8859-1"), "utf-8");
        SearchResult searchResult = searchService.search(queryString, page, SEARCH_RESULT_ROWS);
        //把结果传递给页面
        model.addAttribute("query", queryString);
        model.addAttribute("totalPages", searchResult.getTotalPages());
        model.addAttribute("itemList", searchResult.getItemList());
        model.addAttribute("page", page);
        //返回逻辑视图
        return "search";

    }


}
