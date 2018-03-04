package com.taotao.sso.controller;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utlis.CookieUtils;
import com.taotao.pojo.TbUser;
import com.taotao.sso.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户管理controller
 * @author sucl
 */
@Controller
public class UserController {

    @Autowired
    private UserService userService;
    @Value("${TOKEN_KEY}")
    private String TOKEN_KEY;

    @RequestMapping("/user/check/{param}/{type}")
    @ResponseBody
    private TaotaoResult checkUserData(@PathVariable String param,@PathVariable Integer type) {
        TaotaoResult taotaoResult = userService.checkData(param, type);
        return taotaoResult;
    }

    @RequestMapping(value = "/user/register",method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult regitster(TbUser user) {
        return userService.register(user);
    }


    @RequestMapping(value="/user/login", method=RequestMethod.POST)
    @ResponseBody
    public TaotaoResult login(String username, String password,
                              HttpServletResponse response, HttpServletRequest request) {
        TaotaoResult result = userService.login(username, password);
        //登录成功后写cookie
        if (result.getStatus() == 200) {
            //把token写入cookie
            CookieUtils.setCookie(request, response, TOKEN_KEY, result.getData().toString());
        }
        return result;
    }

//    /**
//     * spring 4.1 之前使用这个方法
//     * @param token
//     * @param callback
//     * @return
//     */
//    @RequestMapping(value="/user/token/{token}", method=RequestMethod.GET,
//			//指定返回响应数据的content-type
//			produces= MediaType.APPLICATION_JSON_VALUE)
//	@ResponseBody
//	public String getUserByToken(@PathVariable String token, String callback) {
//		TaotaoResult result = userService.getUserByToken(token);
//		//判断是否为jsonp请求
//		if (StringUtils.isNotBlank(callback)) {
//			return callback + "(" + JsonUtils.objectToJson(result) + ");";
//		}
//		return JsonUtils.objectToJson(result);
//	}
    /**
     * spring 4.1 之后使用这个方法
     * @param token
     * @param callback
     * @return
     */
    @RequestMapping(value="/user/token/{token}", method=RequestMethod.GET)
    @ResponseBody
    public Object getUserByToken(@PathVariable String token, String callback) {
        TaotaoResult result = userService.getUserByToken(token);
        //判断是否为jsonp请求
        if (StringUtils.isNotBlank(callback)) {
            MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(result);
            //设置回调方法
            mappingJacksonValue.setJsonpFunction(callback);
            return mappingJacksonValue;
        }
        return result;
    }

    @RequestMapping(value = "/user/logout/{token}",method = RequestMethod.GET)
    @ResponseBody
    public TaotaoResult  logout(@PathVariable String token) {

        return TaotaoResult.ok();
    }

}
