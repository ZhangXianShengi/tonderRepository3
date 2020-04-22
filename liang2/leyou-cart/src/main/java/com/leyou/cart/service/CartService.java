package com.leyou.cart.service;

import com.leyou.auth.pojo.UserInfo;
import com.leyou.cart.client.GoodsClient;
import com.leyou.cart.interceptor.LoginInterceptor;
import com.leyou.cart.pojo.Cart;
import com.leyou.common.utils.JsonUtils;
import com.leyou.itme.pojo.Sku;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private GoodsClient goodsClient;

    public void addCart(Cart cart) {
        UserInfo userInfo = LoginInterceptor.get();
        //先查询redis中是否已有要添加的数据
        BoundHashOperations<String, Object, Object> hashOperations = redisTemplate.boundHashOps(userInfo.getId().toString());
        String skuId = cart.getSkuId().toString();
        Integer num = cart.getNum();
        //判断是否有
        if ( hashOperations.hasKey(skuId)){

            String cartJosn = hashOperations.get(skuId).toString();
            cart = JsonUtils.parse(cartJosn, Cart.class);
            //更新数量
            cart.setNum(num+cart.getNum());//加上redis中的数量

        }else {
            //没有就新增
            cart.setUserId(userInfo.getId());
            //查询商品信息
            Sku sku = this.goodsClient.querSkuById(cart.getSkuId());
            cart.setPrice(sku.getPrice());
            cart.setImage(StringUtils.isBlank(sku.getImages()) ? "" : StringUtils.split(sku.getImages(),",")[0]);
            cart.setOwnSpec(sku.getOwnSpec());
            cart.setTitle(sku.getTitle());
        }
        //保存到redis
        hashOperations.put(skuId,JsonUtils.serialize(cart));
    }

    /**
     * 查询redis购物车
     * @return
     */
    public List<Cart> queryCarts() {
        //获取用户信息
        UserInfo userInfo = LoginInterceptor.get();
        //判断redis是否存在该用户的购物车数据
        if (!this.redisTemplate.hasKey(userInfo.getId().toString())){
            return null;
        }
        //查询redis中的数据
        BoundHashOperations<String, Object, Object> hashOperations = redisTemplate.boundHashOps(userInfo.getId().toString());
        List<Object> cartJosns = hashOperations.values();
        return  cartJosns.stream().map(cartJosn ->
            JsonUtils.parse(cartJosn.toString(),Cart.class)
        ).collect(Collectors.toList());
    }

    /**
     * 修改redis中购物车的数据
     * @param cart
     * @return
     */
    public void updateNum(Cart cart) {
        UserInfo userInfo = LoginInterceptor.get();
        //先查询redis中是否已有要添加的数据
        BoundHashOperations<String, Object, Object> hashOperations = redisTemplate.boundHashOps(userInfo.getId().toString());
        String skuId = cart.getSkuId().toString();
        Integer num = cart.getNum();
        String cartJosn = hashOperations.get(skuId).toString();
        Cart cart1 = JsonUtils.parse(cartJosn, Cart.class);
        //更新数量
        cart1.setNum(cart.getNum());//加上redis中的数量
        hashOperations.put(skuId,JsonUtils.serialize(cart1));
    }

    /**
     * 根据skuid删除购物车商品
     * @param skuId
     * @return
     */
    public void deleteCart(String skuId) {
        UserInfo userInfo = LoginInterceptor.get();
        //先查询redis中是否已有要添加的数据
        BoundHashOperations<String, Object, Object> hashOperations = redisTemplate.boundHashOps(userInfo.getId().toString());
        hashOperations.delete(skuId);
    }
}





























