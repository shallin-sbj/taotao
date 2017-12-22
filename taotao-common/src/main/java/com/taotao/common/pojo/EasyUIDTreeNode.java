package com.taotao.common.pojo;

import java.io.Serializable;

/**
 * 商品类目存放的节点
 * @author sucl
 */
public class EasyUIDTreeNode implements Serializable {

    // id
    private long id;
    // 文本内容
    private String text;
    // 状态
    private String state;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
