package com.taotao.sso.service.impl;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utlis.JsonUtils;
import com.taotao.jedis.JedisClient;
import com.taotao.mapper.TbUserMapper;
import com.taotao.pojo.TbUser;
import com.taotao.pojo.TbUserExample;
import com.taotao.sso.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 用户处理
 *
 * @author sucl
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private TbUserMapper userMapper;

    @Autowired
    private JedisClient jedisClient;

    @Value("${USER_SESSION}")
    private String USER_SESSION;
    @Value("${SESSION_EXPIRE}")
    private Integer SESSION_EXPIRE;

    @Override
    public TaotaoResult checkData(String data, int type) {

        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        // 1:用户名是否可用,2：判断手机号是否可用 3：邮箱是可用
        if (type == 1) {
            criteria.andUsernameEqualTo(data);
        } else if (type == 2) {
            criteria.andPhoneEqualTo(data);
        } else if (type == 3) {
            criteria.andEmailEqualTo(data);
        } else {
            return TaotaoResult.build(400, "请求参数包含非法数据");
        }
        List<TbUser> tbUsers = userMapper.selectByExample(example);
        if (!CollectionUtils.isEmpty(tbUsers)) {
            return TaotaoResult.ok(false);
        }
        return TaotaoResult.ok(true);
    }

    @Override
    public TaotaoResult register(TbUser user) {
        //检查数据的有效性
        if (StringUtils.isBlank(user.getUsername())) {
            return TaotaoResult.build(400, "用户名不能为空");
        }
        //判断用户名是否重复
        TaotaoResult taotaoResult = checkData(user.getUsername(), 1);
        if (!(boolean) taotaoResult.getData()) {
            return TaotaoResult.build(400, "用户名重复");
        }
        //判断密码是否为空
        if (StringUtils.isBlank(user.getPassword())) {
            return TaotaoResult.build(400, "密码不能为空");
        }
        if (StringUtils.isNotBlank(user.getPhone())) {
            //是否重复校验
            taotaoResult = checkData(user.getPhone(), 2);
            if (!(boolean) taotaoResult.getData()) {
                return TaotaoResult.build(400, "电话号码重复");
            }
        }
        //如果email不为空的话进行是否重复校验
        if (StringUtils.isNotBlank(user.getEmail())) {
            //是否重复校验
            taotaoResult = checkData(user.getEmail(), 3);
            if (!(boolean) taotaoResult.getData()) {
                return TaotaoResult.build(400, "email重复");
            }
        }
        //补全pojo的属性
        user.setCreated(new Date());
        user.setUpdated(new Date());
        //密码要进行md5加密
        String md5Pass = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        user.setPassword(md5Pass);
        //插入数据
        userMapper.insert(user);
        //返回注册成功
        return TaotaoResult.ok();
    }

    @Override
    public TaotaoResult login(String username, String password) {
        // 判断用户名和密码是否正确
        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(username);
        List<TbUser> tbUsers = userMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(tbUsers)) {
            return TaotaoResult.build(400,"用户名或密码不正确");
        }
        TbUser tbUser = tbUsers.get(0);
        if (!DigestUtils.md5DigestAsHex(password.getBytes())
                .equals(tbUser.getPassword())) {
            return TaotaoResult.build(400,"用户名或密码不正确");
        }
        // 生产token,使用UUID
        String tokem = UUID.randomUUID().toString();
        // 清空密码
        tbUser.setPassword(null);
        //把用户信息保存到redis，key就是token，value就是用户信息
        jedisClient.set(USER_SESSION+":"+ tokem, JsonUtils.objectToJson(tbUser));
        //设置key的过期时间
        jedisClient.expire(USER_SESSION+":"+tokem,SESSION_EXPIRE);
        //返回登录成功，其中要把token返回。
        return TaotaoResult.ok(tokem);
    }

    @Override
    public TaotaoResult getUserByToken(String tokem) {
        String userInfo = jedisClient.get(USER_SESSION + ":" + tokem);
        if (StringUtils.isBlank(userInfo)) {
            return TaotaoResult.build(400,"用户登录过期");
        }
        // 查询之后重置过期时间
        jedisClient.expire(USER_SESSION+":"+tokem,SESSION_EXPIRE);
        return TaotaoResult.ok(JsonUtils.jsonToPojo(userInfo,TbUser.class));
    }
}