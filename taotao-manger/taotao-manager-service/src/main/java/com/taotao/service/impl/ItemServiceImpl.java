package com.taotao.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utlis.IDUtils;
import com.taotao.common.utlis.JsonUtils;
import com.taotao.jedis.JedisClient;
import com.taotao.mapper.TbItemDescMapper;
import com.taotao.mapper.TbItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.pojo.TbItemExample;
import com.taotao.service.ItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.util.Date;
import java.util.List;

/**
 * @author:sucl
 */
@Service
public class ItemServiceImpl implements ItemService {

   private final static Logger log = LoggerFactory.getLogger(ItemServiceImpl.class);

    @Autowired
    private TbItemMapper itemMapper;
    @Autowired
    private TbItemDescMapper itemDescMapper;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private JedisClient jedisClient;

    @Resource(name="itemAddtopic")
    private Destination destination;

//    @Value("${ITEM_INFO_PRE")
//    private String ITEM_INFO_PRE;    // 同步商品添加的搜索库中的时间

    @Value("${TIEM_EXPIRE}")
    private int ITEM_EXTPIRE;           // 设置缓存时间

    @Override
    public TbItem getItemById(Long itemId) {
        // 做缓存处理
        log.info("============== getItemById id :" + itemId );
        try {
            String baseInfo = jedisClient.get(jedisClient.get(ITEM_EXTPIRE + ":" + itemId + ":BASE"));
            if (StringUtils.hasText(baseInfo)) {
                TbItem tbItem = JsonUtils.jsonToPojo(baseInfo, TbItem.class);
                return tbItem;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        TbItem item = itemMapper.selectByPrimaryKey(itemId);
        try {
            jedisClient.set(ITEM_EXTPIRE + ":" + itemId + ":BASE", JsonUtils.objectToJson(item));
            // 设置缓存时间，提高缓存利用率
            jedisClient.expire(ITEM_EXTPIRE + ":" + item.getId() + ":BASE", ITEM_EXTPIRE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return item;
    }

    @Override
    public EasyUIDataGridResult getItemList(int page, int rows) {
        EasyUIDataGridResult result =  new EasyUIDataGridResult();
        PageHelper.startPage(page,rows);
        TbItemExample example = new TbItemExample();
        List<TbItem> list =  itemMapper.selectByExample(example);
        PageInfo<TbItem> pageInfo = new PageInfo<TbItem>(list);
        result.setRows(list);
        result.setTotal(pageInfo.getTotal());
        return result;
    }

    @Override
    public TaotaoResult addItem(TbItem item, String desc) {

        final Long itemId = IDUtils.genItemId();
        item.setId(itemId);
        // 商品状态 1：正常2: 下架3：删除
        item.setStatus((byte)1);
        item.setCreated(new Date());
        item.setUpdated(new Date());
        itemMapper.insert(item);

        TbItemDesc itemDesc = new TbItemDesc();
        itemDesc.setItemId(itemId);
        itemDesc.setCreated(new Date());
        itemDesc.setUpdated(new Date());
        itemDesc.setItemDesc(desc);
        itemDescMapper.insert(itemDesc);

        // 想activeMq 发送商品添加事件
        jmsTemplate.send(destination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                // 发送商品ID
                return session.createTextMessage(itemId.toString());
            }
        });
        return TaotaoResult.ok();
    }

    @Override
    public TaotaoResult modifyItem(TbItem item) {
        return null;
    }

    @Override
    public TbItemDesc getItemDescById(long itemId) {
        // 做缓存处理
        try {
            String baseInfo = jedisClient.get(jedisClient.get(ITEM_EXTPIRE + ":" + itemId + ":BASE"));
            if (StringUtils.hasText(baseInfo)) {
                TbItemDesc tbItemDesc = JsonUtils.jsonToPojo(baseInfo, TbItemDesc.class);
                return  tbItemDesc;
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

        TbItemDesc tbItemDesc = itemDescMapper.selectByPrimaryKey(itemId);
        try {
            jedisClient.set(ITEM_EXTPIRE + ":" + itemId + ":DESC", JsonUtils.objectToJson(tbItemDesc));
            // 设置缓存时间，提高缓存利用率
            jedisClient.expire(ITEM_EXTPIRE + ":" + itemId + ":DESC",ITEM_EXTPIRE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tbItemDesc;
    }
}
