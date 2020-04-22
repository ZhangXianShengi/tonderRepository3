package com.leyou.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.pojo.PageResult;
import com.leyou.itme.pojo.*;
import com.leyou.itme.bo.SpuBo;
import com.leyou.mapper.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GoodsService {

    @Autowired
    private SpuMapper spuMapper;

    @Autowired
    private SpuDetailMapper spuDetailMapper;

    @Autowired
    private BrandMapper brandMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private AmqpTemplate amqpTemplate;

    /**
     * 根据条件分页查询spu
     * @param key
     * @param saleable
     * @param page
     * @param rows
     * @return
     */
    public PageResult<SpuBo> querySpuByPage(String key, Boolean saleable, Integer page, Integer rows) {

        /*Example example = new Example(Spu.class);
        //用来拼接查询条件
        Example.Criteria criteria = example.createCriteria();

        //添加查询条件
        //如果用户传的查询key不为空，再拼接sql
        if (StringUtils.isNotBlank(key)){
            criteria.andLike("title","%"+key+"%");
        }

        //添加上下架的过滤条件
        if(saleable != null)

        //添加分页
        PageHelper.startPage(page,rows);

        //执行查询,获取spu集合
        List<Spu> spus = this.spuMapper.selectByExample(example);

        //将包装的SpuBo装进PageInfo中
        PageInfo<Spu> pageInfo = new PageInfo<>(spus);

        //将spu集合转化为SpuBo
        //把查询结果包装成SpuBo
        List<SpuBo> spuBos = spus.stream().map(spu -> {
            SpuBo spuBo = new SpuBo();
            //将spu对象的所有属性拷贝给spuBo对象
            BeanUtils.copyProperties(spus,spuBo);
            //查询品牌名称
            Brand brand = this.brandMapper.selectByPrimaryKey(spu.getBrandId());
            spuBo.setBname(brand.getName());
            //查询商品3级分类获取name属性
            List<Long> cids = Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3());
            List<Category> categories = categoryMaapper.selectByIdList(cids);
            // 笨蛋式获取
            *//*List<String> names = new ArrayList<>();
            for (Category category : categories) {
                names.add(category.getName());
            }*//*
            // stream表达式
            List<String> names = categories
                    .stream()
                    //过滤
                    .filter(category -> StringUtils.isNotBlank(category.getName()))
                    .map(Category::getName)
                    .collect(Collectors.toList());
            spuBo.setCname(StringUtils.join(names, " | "));
            return spuBo;
        }).collect(Collectors.toList());

        //返回PageResult<SpuBo>
        return new PageResult<>(pageInfo.getTotal(),spuBos);
    }*/
    Example example = new Example(Spu.class);
    Example.Criteria criteria = example.createCriteria();

    //根据条件查询
        if(StringUtils.isNotBlank(key)){
        criteria.andLike("title","%"+key+"%");
    }
    //是否上下架
        if(saleable != null){
        criteria.andEqualTo("saleable",saleable);
    }
    //分页
        PageHelper.startPage(page,rows);
    List<Spu> spus = this.spuMapper.selectByExample(example);
    //将包装的SpuBo装进PageInfo中
    PageInfo<Spu> pageInfo = new PageInfo<>(spus);
    //把查询结果包装成SpuBo
    List<SpuBo> spuBoList = spus.stream().map(spu -> {
        //把spu变为spuBo
        SpuBo spuBo = new SpuBo();
        //将spu拷贝到spuBo
        BeanUtils.copyProperties(spu,spuBo);
        //查询品牌名称
        Brand brand =  brandMapper.selectByPrimaryKey(spu.getBrandId());
        spuBo.setBname(brand.getName());
        //查询商品3级分类获取name属性
        List<Long> cids = Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3());
        List<Category> categories = categoryMapper.selectByIdList(cids);
        // 笨蛋式获取
            /*List<String> names = new ArrayList<>();
            for (Category category : categories) {
                names.add(category.getName());
            }*/
        // stream表达式
        List<String> names = categories
                .stream()
                //过滤
                .filter(category -> StringUtils.isNotBlank(category.getName()))
                .map(Category::getName)
                .collect(Collectors.toList());
        spuBo.setCname(StringUtils.join(names, " | "));
        return spuBo;
    }).collect(Collectors.toList());


        return new PageResult<>(pageInfo.getTotal(),spuBoList);
}
    /**
     * 新增商品
     * @param spuBo
     * @return
     */
    @Transactional
    public void saveGoods(SpuBo spuBo) {
        //先新增spu
        spuBo.setId(null);
        spuBo.setSaleable(true);
        spuBo.setValid(true);
        spuBo.setCreateTime(new Date());
        spuBo.setLastUpdateTime(spuBo.getCreateTime());
        this.spuMapper.insertSelective(spuBo);

        //再新增spuDetail
        SpuDetail spuDetail = spuBo.getSpuDetail();
        spuDetail.setSpuId(spuBo.getId());
        this.spuDetailMapper.insertSelective(spuDetail);

        //再新增sku
        //先将游览器传过来的sku集合遍历成sku，然后新增
        saveSkuAndStock(spuBo);

        //调用同步数据的方法
        sendMassage(spuBo.getId(), "insert");

    }

    //抽取同步数据方法
    public void sendMassage(Long spuId,String type) {
        //如果失败不能影响其它，try/catch
        try {
            this.amqpTemplate.convertAndSend("item."+type, spuId);
        } catch (AmqpException e) {
            e.printStackTrace();
        }
    }

    //新增sku和stock
    public void saveSkuAndStock(SpuBo spuBo){
        spuBo.getSkus().forEach(sku -> {
            //新增sku
            sku.setId(null);
            sku.setSpuId(spuBo.getId());
            sku.setCreateTime(new Date());
            sku.setLastUpdateTime(sku.getCreateTime());
            this.skuMapper.insertSelective(sku);

            //新增库存
            Stock stock = new Stock();
            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock());
            this.stockMapper.insertSelective(stock);
        });
    }

    /**
     * 根据spuId查询SpuDetail
     * @param spuId
     * @return
     */
    public SpuDetail querySpuDetailSpuId(Long spuId) {
        return this.spuDetailMapper.selectByPrimaryKey(spuId);
    }

    /**
     * 根据spuId查询Sku集合
     * @param spuId
     * @return
     */
    public List<Sku> querySkusBySpuId(Long spuId) {
        Sku record = new Sku();
        record.setSpuId(spuId);
        List<Sku> skus = this.skuMapper.select(record);
        skus.forEach(sku -> {
            Stock stock = this.stockMapper.selectByPrimaryKey(sku.getId());
            sku.setStock(stock.getStock());
        });
        return skus;
    }

    /**
     * 更新商品信息
     * @param spuBo
     * @return
     */
    @Transactional
    public void updateGoods(SpuBo spuBo) {
        Sku record = new Sku();
        record.setSpuId(spuBo.getId());
        //根据spuId查询要删除的sku
        List<Sku> skus = this.skuMapper.select(record);
        skus.forEach(sku -> {
            //先删除stock
            this.stockMapper.deleteByPrimaryKey(sku.getId());
        });

        //再删除sku
        Sku sku = new Sku();
        sku.setSpuId(spuBo.getId());
        this.skuMapper.delete(sku);

        //然后再先新增sku和stock
        this.saveSkuAndStock(spuBo);

        //更新spu和spuDetail
        spuBo.setCreateTime(null);
        spuBo.setLastUpdateTime(new Date());
        spuBo.setSaleable(null);
        spuBo.setValid(null);
        this.spuMapper.updateByPrimaryKeySelective(spuBo);
        this.spuDetailMapper.updateByPrimaryKeySelective(spuBo.getSpuDetail());

        //向mq发送消息，同步数据
        sendMassage(spuBo.getId(),"update");
    }

    /**
     * 根据spuid查询spu
     * @param id
     * @return
     */
    public Spu querySpuById(Long id) {
        return this.spuMapper.selectByPrimaryKey(id);
    }

    /**
     * 根据skuId查询sku
     * @param skuId
     * @return
     */
    public Sku querSkuById(Long skuId) {
        return this.skuMapper.selectByPrimaryKey(skuId);
}
}



























































































