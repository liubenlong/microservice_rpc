package com.le.luffi.media.model.db;

/**
 * Created by liubenlong on 2016/11/21.
 */
public class BaseModel {

    private int pageSize = 10;
    private Long pageNum;
    private long offset;

    public int getPageSize() {
        return pageSize;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }

    public void setPageSize(int pageSize) {
        if (pageSize < 1)
            pageSize = 1;
        this.pageSize = pageSize;
        offset = (getPageNum() - 1) * getPageSize();
    }

    public long getOffset() {
        return offset;
    }

    public Long getPageNum() {
        if (pageNum == null)
            pageNum = 1L;
        return pageNum;
    }

    public void setPageNum(Long pageNum) {
        if (pageNum < 1)
            pageNum = 1L;
        this.pageNum = pageNum;
        offset = (getPageNum() - 1) * getPageSize();
    }
}
