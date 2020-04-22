package com.leyou.common.pojo;

import java.util.List;

public class PageResult<T> {
    private long total;//总条数
    private Integer totalPage;//总页数
    private List<T> items;//当前页数据

    public PageResult() {
    }

    public PageResult(long total, List<T> items) {
        this.total = total;
        this.items = items;
    }

    public PageResult(long total, Integer totalPage, List<T> items) {
        this.total = total;
        this.totalPage = totalPage;
        this.items = items;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }
}
