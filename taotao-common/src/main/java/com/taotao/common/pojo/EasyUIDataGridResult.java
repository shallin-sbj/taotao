package com.taotao.common.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * EasyUI 相应数据返回数据格式
 * @author sucl
 */
public class EasyUIDataGridResult implements Serializable {

    private Long total;

    private List rows;

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List getRows() {
        return rows;
    }

    public void setRows(List rows) {
        this.rows = rows;
    }
}
