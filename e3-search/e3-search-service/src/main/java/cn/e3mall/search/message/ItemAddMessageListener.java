package cn.e3mall.search.message;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import cn.e3mall.common.pojo.SearchItem;
import cn.e3mall.search.mapper.ItemMapper;
/*
 * 监听商品添加消息,接受消息后,将对应的商品信息同步到索引库
 */
public class ItemAddMessageListener implements MessageListener{
	@Autowired
	private ItemMapper itemMapper;
	@Autowired
	private SolrServer solrServer;
	@Override
	public void onMessage(Message message) {
		try {
			//从消息中取商品id
			TextMessage textMessage = (TextMessage)message;
			//根据商品id查询商品信息
			String text = textMessage.getText();
			Long itemId = new Long(text);
			//等待事务提交,另一种方式,在manager-web中发送
			Thread.sleep(1000);
			SearchItem searchItem = itemMapper.getItemById(itemId);
			solrServer.addBean(searchItem);
			//提交
			solrServer.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
