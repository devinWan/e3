package cn.e3mall.search.dao;

import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.e3mall.common.pojo.SearchItem;
import cn.e3mall.common.pojo.SearchResult;

/**
 * 
 商品搜索dao
 */
@Repository
public class SearchDao {
	@Autowired
	private SolrServer solrServer;
	/*
	 * 根据查询条件查询索引哭
	 */
	public SearchResult search(SolrQuery query) throws Exception{
		//根据query查询索引库
		QueryResponse queryResponse = solrServer.query(query);
		//取查询结果
		List<SearchItem> list = queryResponse.getBeans(SearchItem.class);
		SolrDocumentList solrDocumentList = queryResponse.getResults();
		
		//取查询结果总记录数
		long numFound = solrDocumentList.getNumFound();
		SearchResult result = new SearchResult();
		result.setRecordCount(numFound);
		//取商品列表,需要取高亮显示
		Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
		for(SearchItem searchItem:list){
			List<String> list2 = highlighting.get(searchItem.getId()).get("item_title");
			String title="";
			if (list2!=null&&list.size()>0) {
				title = list2.get(0);
			} else {
				title = searchItem.getTitle();
			}
			searchItem.setTitle(title);
		}
		result.setItemList(list);
		
		//返回结果
		return result;
	}
}
