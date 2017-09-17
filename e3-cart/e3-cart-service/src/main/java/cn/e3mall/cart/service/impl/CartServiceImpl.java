package cn.e3mall.cart.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItem;
/*
 * 购物车处理服务
 */
@Service
public class CartServiceImpl implements CartService{
	@Autowired
	private JedisClient jedisClient;
	@Value("${REDIS_CART_PRE}")
	private String REDIS_CART_PRE;
	@Autowired
	private TbItemMapper itemMapper;
	@Override
	public E3Result addCart(Long userId, Long itemId,Integer num) {
		//向redis中添加购物车
		//数据类型是hash key:用户id field:商品id  value:商品信息
		//判断商品是否存在
		Boolean hexists = jedisClient.hexists(REDIS_CART_PRE+":"+userId, itemId.toString());
		//如果存在数量相加
		if (hexists) {
			String json = jedisClient.hget(REDIS_CART_PRE+":"+userId, itemId.toString());
			//把json转换称TbItem
			TbItem item = JsonUtils.jsonToPojo(json, TbItem.class);
			item.setNum(item.getNum()+num);
			//写成redis
			jedisClient.hset(REDIS_CART_PRE+":"+userId, itemId.toString(), JsonUtils.objectToJson(item));
			return E3Result.ok();
		}
		//如果不存在,根据商品id取商品信息
		TbItem item = itemMapper.selectByPrimaryKey(itemId);
		//设置购物车数量
		item.setNum(num);
		//取一张图片
		String image = item.getImage();
		if (StringUtils.isNotBlank(image)) {
			item.setImage(image.split(",")[0]); 
		}
		//添加到购物车列表
		jedisClient.hset(REDIS_CART_PRE+":"+userId, itemId.toString(), JsonUtils.objectToJson(item));
		//返回成功
		return E3Result.ok();
	}
	@Override
	public E3Result mergeCart(Long userId, List<TbItem> itemList) {
		// 遍历商品列表
		//把列表添加到购物车
		//判断购物车中是否有此商品
		//如果没有添加新的商品
		for (TbItem tbItem : itemList) {
			addCart(userId, tbItem.getId(), tbItem.getNum());
		}
		//返回成功
		return E3Result.ok();
	}
	@Override
	public List<TbItem> getCartList(Long userId) {
		// 根据用户id查询购物车列表
		List<String> jsonList = jedisClient.hvals(REDIS_CART_PRE+":"+userId);
		List<TbItem> itemList = new ArrayList();
		for (String string : jsonList) {
			//创建一个TbItem对象
			TbItem item = JsonUtils.jsonToPojo(string, TbItem.class);
			//添加到列表
			itemList.add(item);
		}
		return itemList;
	}
	@Override
	public E3Result updateCartNum(Long userId, Long itemId, Integer num) {
		//从redis中取商品信息
		String json = jedisClient.hget(REDIS_CART_PRE+":"+userId, itemId.toString());
		//更新商品数量
		TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
		tbItem.setNum(num);
		//写入redis
		jedisClient.hset(REDIS_CART_PRE+":"+userId, itemId.toString(), JsonUtils.objectToJson(tbItem));
		
		return E3Result.ok();
	}
	@Override
	public E3Result deleteCartItem(Long userId, Long itemId) {
		//删除购物车商品
		jedisClient.hdel(REDIS_CART_PRE+":"+userId, itemId.toString());
		return E3Result.ok();
	}
	@Override
	public E3Result clearCartItem(Long userId) {
		// 删除购物车信息
		jedisClient.del(REDIS_CART_PRE+":"+userId);
		return E3Result.ok();
	}

}
