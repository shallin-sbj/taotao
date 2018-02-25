package com.taotao.item.controller;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 *  网页静态化处理控制器
 * @author sucl
 *
 */
@Controller
public class htmlGenController {

    @Autowired
    private FreeMarkerConfig freeMarkerConfig;

    @RequestMapping("/genhtml")
    @ResponseBody
    public String genHtml() throws IOException {
        Configuration configuration = freeMarkerConfig.getConfiguration();
        Template template = configuration.getTemplate("hello.ftl");
        Map date = new HashMap();
        date.put("hello","do test freemarker for spring!");
        Writer out = new FileWriter("D:/out/hello.html");
        try {
            template.process(date,out);
        } catch (TemplateException e) {
            e.printStackTrace();
        }
        out.close();
        return "Ok";




    }
}
