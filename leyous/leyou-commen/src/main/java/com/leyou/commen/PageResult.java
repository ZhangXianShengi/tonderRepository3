package com.leyou.commen;

import java.util.List;

//这是一个工具类
public class PageResult<T> {
    //封装一个所有都可用的分页方法
    private Long  total;//总条数
    private Integer totalPahe;//总页数
    private List<T> items;//当前页显示的数据
    //空参构造方法
    public PageResult() {
    }
    //两参构造方法
    public PageResult(Long total, List<T> items) {
        this.total = total;
        this.items = items;
    }
    //三参构造方法
    public PageResult(Long total, Integer totalPahe, List<T> items) {
        this.total = total;
        this.totalPahe = totalPahe;
        this.items = items;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Integer getTotalPahe() {
        return totalPahe;
    }

    public void setTotalPahe(Integer totalPahe) {
        this.totalPahe = totalPahe;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }
}
