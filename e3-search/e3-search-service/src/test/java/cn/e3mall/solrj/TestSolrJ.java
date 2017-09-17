package cn.e3mall.solrj;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

import cn.e3mall.common.pojo.SearchItem;

public class TestSolrJ {
	@Test
	public void addDocument() throws Exception{
		//创建一个SolrServer对象,创建一个连接.参数solr服务的url
		SolrServer solrServer = new HttpSolrServer("http://192.168.136.128:8080/solr/collection1");
		//创建一个文档对象SolrInputDocument
		SolrInputDocument document = new SolrInputDocument();
		//向文档中添加域,文档中必须包含一个id域,所有的域的名称必须在schema.xml中定义
		document.addField("id", "doc01");
		document.addField("item_title", "测试商品01");
		document.addField("item_price", 1000);
			
		//把文档写入索引库
		solrServer.add(document);
		//提交
		solrServer.commit();
	}
	@Test
	public void deleteDocument() throws Exception{
		SolrServer solrServer = new HttpSolrServer("http://192.168.136.128:8080/solr/collection1");
		//删除文档
		solrServer.deleteById("doc01");
		//solrServer.deleteByQuery("id:doc01");
		//提交
		solrServer.commit();
	}
	@Test
	public void queryIndex() throws Exception{
		//创建一个SolrServer对象
		SolrServer solrServer = new HttpSolrServer("http://192.168.136.128:8080/solr/collection1");
		//创建一个SolrQuery对象
		SolrQuery query = new SolrQuery();
		//设置查询条件
		query.setQuery("*:*");
		
		//执行查询QueryResponse
		QueryResponse queryResponse = solrServer.query(query);
		//取文档列表,取查询结果的总记录数
		List<SearchItem> list = queryResponse.getBeans(SearchItem.class);
		System.out.println("查询结果总记录数:"+list.size());
		//遍历文档列表,取域的内容
		for (SearchItem searchItem : list) {
			System.out.println(searchItem.getId());
			System.out.println(searchItem.getTitle());
			System.out.println(searchItem.getImage());
			System.out.println(searchItem.getSell_point());
			System.out.println(searchItem.getPrice());
			System.out.println(searchItem.getCategory_name());
		}
	}
	@Test
	public void queryIndexFuza() throws Exception{
		//创建一个SolrServer对象
		SolrServer solrServer = new HttpSolrServer("http://192.168.136.128:8080/solr/collection1");
		//创建一个SolrQuery对象
		SolrQuery query = new SolrQuery();
		//设置查询条件
		query.setQuery("手机");
		query.setStart(0);
		query.setRows(20);
		query.set("df","item_title");
		query.setHighlight(true);
		query.addHighlightField("item_title");
		query.setHighlightSimplePre("<em>");
		query.setHighlightSimplePost("</em>");
		
		//执行查询QueryResponse
		QueryResponse queryResponse = solrServer.query(query);
		//取文档列表,取查询结果的总记录数
		List<SearchItem> list = queryResponse.getBeans(SearchItem.class);
		System.out.println("查询结果总记录数:"+list.size());
		Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
		//遍历文档列表,取域的内容
		for (SearchItem searchItem : list) {
			System.out.println(searchItem.getId());
			//取高亮显示
			List<String> list2 = highlighting.get(searchItem.getId()).get("item_title");
			String title="";
			if (list2!=null&&list2.size()>0) {
				title = list2.get(0);
			} else {
				title = searchItem.getTitle().toString();
			}
			System.out.println(title);
			System.out.println(searchItem.getImage());
			System.out.println(searchItem.getSell_point());
			System.out.println(searchItem.getPrice());
			System.out.println(searchItem.getCategory_name());
		}
	}
}
