package cn.itcast.bos.dao.qp;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.util.Version;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.wltea.analyzer.lucene.IKAnalyzer;

import cn.itcast.bos.domain.qp.WorkOrderManage;

//  spring  data jpa  如何自定义目标接口的实现类 
//  WorkOrderManageDaoImpl  该类 WorkOrderManageDao接口的实现类
public class WorkOrderManageDaoImpl {
	@PersistenceContext
	private EntityManager entityManager;

	// WorkOrderManageDao 自定义实现类的方法
	public Page<WorkOrderManage> pageQuery(PageRequest pageRequest, String name, String value) {
		// hibernate-search 实现 全文检索 分页 条件 官方文档 ... dao 代码 要求 看懂 了解
		// Page total row List<WorkOrderManage> 目标 使用hibernate-search 进行索引查询 将查询的分页数据List 和 total总记录数 查询到即可
		try {
			// 官方文档 2 第三方文档
			// EntityManager em = entityManagerFactory.createEntityManager();
			// 2: 获取 全文检索 FullTextEntiyManager --->获取 全文检索Query
			FullTextEntityManager fullTextEntityManager = org.hibernate.search.jpa.Search.getFullTextEntityManager(entityManager);
			// 3: 准备luncenQuery 对象 luceneQuery 封装 查询条件 没有模糊匹配
			// 2: 解析词条
			QueryParser queryParser = new QueryParser(Version.LUCENE_31, name, new IKAnalyzer());
			Query query1 = queryParser.parse(value);// 完全匹配查询
			WildcardQuery query2 = new WildcardQuery(new Term(name, "*" + value + "*"));// 扩展查询 * 通配符
			// // // 3: 两个查询 合并成一个查询 BooleanQuery
			BooleanQuery booleanQuery = new BooleanQuery();
			booleanQuery.add(query1, Occur.SHOULD);// should 表示 或者关系 or
			booleanQuery.add(query2, Occur.SHOULD);// should 表示 或者关系 or
			// 4 获取 全文检索query FullTextQuery 导入 org.hibernate.search.jpa.FullTextQuery
			// javax.persistence.Query fullTextQuery = fullTextEntityManager.createFullTextQuery(booleanQuery);
			FullTextQuery fullTextQuery = fullTextEntityManager.createFullTextQuery(booleanQuery, WorkOrderManage.class);
			// 5
			int totalCounts = fullTextQuery.getResultSize();// 总记录数 total
			// 6: 分页查询
			fullTextQuery.setFirstResult(pageRequest.getOffset());// 起始记录数
			fullTextQuery.setMaxResults(pageRequest.getPageSize());// 每页显示记录数
			List<WorkOrderManage> resultList = fullTextQuery.getResultList();// rows
			// 7: 获取接口 Page 接口实现类
			Page<WorkOrderManage> data = new PageImpl<WorkOrderManage>(resultList, pageRequest, totalCounts);
			return data;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("索引查询失败" + e);
		}
	}

}
