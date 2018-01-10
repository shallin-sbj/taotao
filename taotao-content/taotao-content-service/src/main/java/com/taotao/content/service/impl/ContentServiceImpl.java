package com.taotao.content.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utlis.ExceptionUtil;
import com.taotao.content.service.IContentService;
import com.taotao.mapper.TbContentMapper;
import com.taotao.pojo.TbContent;
import com.taotao.pojo.TbContentExample;
import com.taotao.pojo.TbContentExample.Criteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ContentServiceImpl implements IContentService {

    @Autowired
    private TbContentMapper contentMapper;

    @Override
    public TaotaoResult addContent(TbContent content) {
        content.setCreated(new Date());
        content.setUpdated(new Date());
        contentMapper.insert(content);
        return TaotaoResult.ok();
    }

    @Override
    public TaotaoResult deleteContent(String contentIds) {
        try {
            String[] idArray = contentIds.split(",");
            List<Long> values = new ArrayList<Long>();
            for (String id : idArray) {
                values.add(Long.valueOf(id));
            }
            TbContentExample example = new TbContentExample();
            Criteria criteria = example.createCriteria();
            criteria.andIdIn(values);
            contentMapper.deleteByExample(example);
            return TaotaoResult.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
        }
    }

    @Override
    public TaotaoResult modifyContent(TbContent content) {
        try {
            TbContentExample example = new TbContentExample();
            Criteria criteria = example.createCriteria();
            criteria.andIdEqualTo(content.getId());
            TbContent tbContent = contentMapper.selectByPrimaryKey(content.getId());
            content.setCreated(tbContent.getCreated());
            content.setUpdated(new Date());
            contentMapper.updateByExampleSelective(content, example);
            return TaotaoResult.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
        }
    }

    @Override
    public EasyUIDataGridResult queryAllContent(Long id, Integer page, Integer rows) {
        TbContentExample example = new TbContentExample();
        Criteria criteria = example.createCriteria();
        criteria.andCategoryIdEqualTo(id);
        PageHelper.startPage(page, rows);
        List<TbContent> list = contentMapper.selectByExample(example);
        EasyUIDataGridResult result = new EasyUIDataGridResult();
        result.setRows(list);
        PageInfo<TbContent> pageInfo = new PageInfo<TbContent>(list);
        result.setTotal(pageInfo.getTotal());
        return result;
    }

    @Override
    public List<TbContent> getContentByCid(Long contentId) {
        TbContentExample example = new TbContentExample();
        Criteria criteria = example.createCriteria();
        criteria.andCategoryIdEqualTo(contentId);
        List<TbContent> contents = contentMapper.selectByExample(example);
        return contents;
    }
}
