package com.dome.elasticsearch;

import com.dome.elasticsearch.pojo.Item;
import com.dome.elasticsearch.repository.ItemRepositroy;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.metrics.avg.InternalAvg;
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
import java.util.Map;
import java.util.Optional;

@SpringBootTest
@RunWith(SpringRunner.class)
public class EsTest {

    @Autowired
    private ItemRepositroy itemRepositroy;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    //创建索引库和映射
    @Test
    public void testIndex(){
        //根据实体类创建索引库
        this.elasticsearchTemplate.createIndex(Item.class);
        //根据实体类创建映射
        this.elasticsearchTemplate.putMapping(Item.class);
    }

    //增删改查数据
    //增加
    @Test
    public void testSave(){
        Item item = new Item(1L, "小米手机7", " 手机",
                "小米", 3499.00, "http://image.leyou.com/13123.jpg");
        this.itemRepositroy.save(item);
    }

    //批量增加
    @Test
    public void ListSave(){
        List<Item> list = new ArrayList<>();
        list.add(new Item(1L, "小米手机7", "手机", "小米", 3299.00, "http://image.leyou.com/13123.jpg"));
        list.add(new Item(2L, "坚果手机R1", "手机", "锤子", 3699.00, "http://image.leyou.com/13123.jpg"));
        list.add(new Item(3L, "华为META10", "手机", "华为", 4499.00, "http://image.leyou.com/13123.jpg"));
        list.add(new Item(4L, "小米Mix2S", "手机", "小米", 4299.00, "http://image.leyou.com/13123.jpg"));
        list.add(new Item(5L, "荣耀V10", "手机", "华为", 2799.00, "http://image.leyou.com/13123.jpg"));
        // 接收对象集合，实现批量新增
        itemRepositroy.saveAll(list);
    }

    //删除
    @Test
    public void ItemDelete(){
        this.itemRepositroy.deleteById(1L);
    }

    //根据id查询
    @Test
    public void TestFind(){
        Optional<Item> item = this.itemRepositroy.findById(3L);
        if (item.isPresent()){
            System.out.println(item.get());
        }
    }

    //查询全部根据价格排序
    @Test
    public void TestFindAll(){
        Iterable<Item> price = this.itemRepositroy.findAll(Sort.by(Sort.Direction.DESC, "price"));
        price.forEach(item -> {
            System.out.println(item);
        });
    }

    //调用自定义方法
    @Test
    public void TestFindByTitle(){
        List<Item> title = this.itemRepositroy.findByTitle("手机");
        title.forEach(item -> {
            System.out.println(title);
        });
    }
    @Test
    public void TestFindByPriceBetween(){
        List<Item> title = this.itemRepositroy.findByPriceBetween(3000,3500);
        title.forEach(item -> {
            System.out.println(title);
        });
    }

    @Test
    public void testQuery(){
        //工具类：通过工具类可以构建高级查询
        MatchQueryBuilder queryBuilder = QueryBuilders.matchQuery("title","手机");
        Iterable<Item> search = this.itemRepositroy.search(queryBuilder);
        search.forEach(item -> {
            System.out.println(item);
        });
    }

    @Test
    public void testNativeQuery(){
        //构建一个自定义方查询构建器
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        //给构建起添加查询条件
        queryBuilder.withQuery(QueryBuilders.matchQuery("category","手机"));
        //给构建器添加分页查询条件
        queryBuilder.withPageable(PageRequest.of(0,3));
        //排序
        queryBuilder.withSort(SortBuilders.fieldSort("price").order(SortOrder.DESC));

        //执行查询，返回分页结果集
        Page<Item> items = this.itemRepositroy.search(queryBuilder.build());
        System.out.println(items.getTotalPages());//总页数
        System.out.println(items.getTotalElements());//总的记录条数
        //打印当前页的记录
        items.forEach(item -> {
            System.out.println(item);
        });
    }

    @Test
    public void testAgg_avg(){
        //创建一个自定义查询构建器
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        //添加聚合查询，使用工具类AggregationBuilders构建聚合
        queryBuilder.addAggregation(AggregationBuilders.terms("brands").field("brand")
                .subAggregation(AggregationBuilders.avg("price_avg").field("price")));
        //添加结果集过滤，不包含任何字段，也就是不需要普通的结果集
        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{""}, null));
        //执行聚合查询，需要强转成子类，因为子类中才有想用的方法
        AggregatedPage<Item> items = (AggregatedPage)this.itemRepositroy.search(queryBuilder.build());
        //根据聚合名称获取结果集中的聚合对象，强转成子类
        StringTerms brands = (StringTerms)items.getAggregation("brands");
        //获取聚合中的桶
        brands.getBuckets().forEach(bucket -> {
            //输出桶中的key
            System.out.println(bucket.getKey());
            //输出桶中的docCount记录数
            System.out.println(bucket.getDocCount());
            //map:解析子聚合，子聚合结果集转成map结构，key:聚合名称，value:聚合内容 map<price_avg,聚合>
            Map<String, Aggregation> map = bucket.getAggregations().asMap();
            InternalAvg price_avg = (InternalAvg)map.get("price_avg");
            System.out.println(price_avg.getValue());
        });

    }
}
































































