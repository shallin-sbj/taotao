package com.taotao.sso.service;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbUser;


/**
 * 用户服务
 * @author sucl
 */
public interface UserService {

    TaotaoResult checkData(String Data ,int type);

    /**
     * 注册对象
     * @param user
     * @return
     */
    TaotaoResult register(TbUser user);

    /**
     * 登录
     */
    TaotaoResult login(String username,String password);

    /**
     * 查询用户信息
     */
    TaotaoResult getUserByToken(String token);
}
