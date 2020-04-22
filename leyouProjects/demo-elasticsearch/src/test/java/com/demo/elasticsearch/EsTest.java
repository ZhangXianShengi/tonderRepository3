package com.demo.elasticsearch;

import com.demo.elasticsearch.pojo.Item;
import com.demo.elasticsearch.repository.ItemRepository;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@RunWith(SpringRunner.class)
public class EsTest {
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;
    @Autowired
    private ItemRepository itemRepository;
    //创建索引库和映射
    @Test
    public void testIndex(){
        //根据实体类创建索引库
        this.elasticsearchTemplate.createIndex(Item.class);
        //根据实体类创建映射
        this.elasticsearchTemplate.putMapping(Item.class);

    }
    //使用es做增删改查  根据id做增加 如果没有做增加  有则做修改
    @Test
    public void TestSave(){
        Item item = new Item(4L, "小米手机8", " 手机",
                "小米", 3499.00, "http://image.leyou.com/13123.jpg");
        this.itemRepository.save(item);
    }
    //做批量增加
    @Test
    public void indexList() {
        List<Item> list = new ArrayList<>();
        list.add(new Item(2L, "坚果手机R1", " 手机", "锤子", 3699.00, "http://image.leyou.com/123.jpg"));
        list.add(new Item(3L, "华为META10", " 手机", "华为", 4499.00, "http://image.leyou.com/3.jpg"));
        // 接收对象集合，实现批量新增
        itemRepository.saveAll(list);
    }
    //查询
    @Test
    public void TestFind(){
        Optional<Item> item = this.itemRepository.findById(1l);
        if (item.isPresent()){
            System.out.println(item.get());
        }
    }
    //查询所有 并做升序查询
    @Test
    public void TestFindAll(){
        Iterable<Item> items = this.itemRepository.findAll(Sort.by(Sort.Direction.ASC,"price","id"));
       items.forEach(item -> System.out.println(item));
    }
    @Test
    public void testFinnByTitle(){
        List<Item> byTitle = this.itemRepository.findByTitle("手机");
        /*byTitle.forEach(item -> {
            System.out.println(item);
        });*/
        byTitle.forEach(System.out::println);
    }

    @Test
    public void findByPriceBetween(){
        List<Item> items = this.itemRepository.findByPriceBetween(3499d, 3500d);
        items.forEach(item -> {
            System.out.println(item);
        });
    }
    //删除
    @Test
    public void deleteId(){
        itemRepository.deleteById(1l);
    }
    @Test
    public void indexLists() {
        List<Item> list = new ArrayList<>();
        list.add(new Item(5L, "小米手机7", "手机", "小米", 3299.00, "http://image.leyou.com/13123.jpg"));
        list.add(new Item(6L, "坚果手机R1", "手机", "锤子", 3699.00, "http://image.leyou.com/13123.jpg"));
        list.add(new Item(7L, "华为META10", "手机", "华为", 4499.00, "http://image.leyou.com/13123.jpg"));
        list.add(new Item(8L, "小米Mix2S", "手机", "小米", 4299.00, "http://image.leyou.com/13123.jpg"));
        list.add(new Item(9L, "荣耀V10", "手机", "华为", 2799.00, "http://image.leyou.com/13123.jpg"));
        // 接收对象集合，实现批量新增
        itemRepository.saveAll(list);
    }

    //------------------------------高级查询-------------------------------
    @Test
    public  void testMach(){
        //使用工具类:通过工具类QueryBuilders可以构建高级查询
        MatchQueryBuilder matchQuery = QueryBuilders.matchQuery("title", "手机");
        Iterable<Item> items = this.itemRepository.search(matchQuery);
        items.forEach(System.out::println);
    }

    @Test
    public void testNative(){
        //创建一个自定义查询构建器
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        //给构建器添加参数查询的条件
        queryBuilder.withQuery(QueryBuilders.matchQuery("category", "手机"));
        //给构建器添加分页查询条件  页码从0开始(下标从0开始)
        queryBuilder.withPageable(PageRequest.of(0,5));
        //添加排列方式  根据xx排列
        queryBuilder.withSort(SortBuilders.fieldSort("id").order(SortOrder.ASC));
        //执行搜索  把获取数据放入集合 返回结果集
        Page<Item> items = this.itemRepository.search(queryBuilder.build());
        //打印输出总条数
        System.out.println(items.getTotalPages());
        //打印输入总页数
        System.out.println(items.getTotalElements());
        //打印当前页的记录
        items.forEach(System.out::println);
    }


   //测试聚合
    @Test
    public void testAgg(){
        //创建一个自定义构建器
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        //创建一个根据那个字段来进行聚合查询（分桶查询）
        //添加聚合查询 使用工具类AggregationBuilders构建聚合
        queryBuilder.addAggregation(AggregationBuilders.terms("brands").field("brand"));
        //添加过滤结果集
        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{}, null));
        //查询,需要把结果强转为AggregatedPage类型
        AggregatedPage<Item> itemPage = (AggregatedPage)this.itemRepository.search(queryBuilder.build());
        StringTerms brands = (StringTerms)itemPage.getAggregation("brands");
        brands.getBuckets().forEach(bucket -> {
            System.out.println(bucket.getKeyAsString());
        });

    }
}
