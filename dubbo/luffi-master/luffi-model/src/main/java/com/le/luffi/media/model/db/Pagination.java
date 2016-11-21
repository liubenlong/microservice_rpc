package com.le.luffi.media.model.db;

import java.io.Serializable;
import java.util.List;

/**
 * Created by rock on 15-7-2.
 */
public class Pagination<T> implements Serializable {

    private Long pageCount;
    private Long dataCount;
    private Integer pageSize;
    private Long pageNum;
    private List<T> data;

    public Long getPageCount() {
        if (dataCount % pageSize == 0) {
            pageCount = dataCount / pageSize;
        } else {
            pageCount = dataCount / pageSize + 1;
        }
        return pageCount;
    }

    public void setPageCount(Long pageCount) {
        this.pageCount = pageCount;
    }

    public Long getDataCount() {
        return dataCount;
    }

    public void setDataCount(Long dataCount) {
        this.dataCount = dataCount;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public Long getPageNum() {
        return pageNum;
    }

    public void setPageNum(Long pageNum) {
        this.pageNum = pageNum;
    }
}
