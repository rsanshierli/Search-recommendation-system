package com.javaproject.dianping.request;

public class PageQuery {

    private Integer page = 1;  // 页面初始序号

    private Integer size = 20; // 每页的大小

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}
