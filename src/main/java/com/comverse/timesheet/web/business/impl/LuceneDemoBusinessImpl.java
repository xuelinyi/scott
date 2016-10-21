package com.comverse.timesheet.web.business.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.Version;
import org.springframework.transaction.annotation.Transactional;

import com.comverse.timesheet.web.bean.Item;
import com.comverse.timesheet.web.business.LuceneDemoBusiness;

@Transactional
public class LuceneDemoBusinessImpl implements LuceneDemoBusiness{

	public boolean buildIndexer(Analyzer analyzer, Directory directory,
			List<Item> items) {

	IndexWriter iwriter = null;
	         try {
	              // 配置索引
	              iwriter = new IndexWriter(directory, new IndexWriterConfig(
	                      Version.LUCENE_44, analyzer));
	              // 删除所有document
	              iwriter.deleteAll();
	              // 将文档信息存入索引
	              Document doc[] = new Document[items.size()];
	              for (int i = 0; i < items.size(); i++) {
	                  doc[i] = new Document();
	                  
	                  Item item = items.get(i);
	                  java.lang.reflect.Field[] fields = item.getClass().getDeclaredFields();
	                  for (java.lang.reflect.Field field : fields) {
	                      String fieldName = field.getName();
	                      String getMethodName = "get"+toFirstLetterUpperCase(fieldName);
	                      Object obj = item.getClass().getMethod(getMethodName).invoke(item);
	                      doc[i].add(new Field(fieldName, (String)obj, TextField.TYPE_STORED));
	                  }
	                  
	                  iwriter.addDocument(doc[i]);
	              }
	          } catch (Exception e) {
	              e.printStackTrace();
	              return false;
	          } finally {
	              try {
	                  iwriter.close();
	              } catch (IOException e) {
	              }
	          }
	          return true;
	      }
	     
	      /**
	       * 根据keyword搜索索引
	       * @param analyzer
	       * @param directory
	       * @param keyword
	       * @return
	       */
	      public List<Item> searchIndexer(Analyzer analyzer, Directory directory, String keyword) {
	          DirectoryReader ireader = null;
	          List<Item> result = new ArrayList<Item>();
	          try {
	              // 设定搜索目录
	              ireader = DirectoryReader.open(directory);
	             IndexSearcher isearcher = new IndexSearcher(ireader);
	  
	              // 对多field进行搜索
	             java.lang.reflect.Field[] fields = Item.class.getDeclaredFields();
	              int length = fields.length;
	              String[] multiFields = new String[length];
	              for (int i = 0; i < length; i++) {
	                 multiFields[i] = fields[i].getName();
	              }
	              MultiFieldQueryParser parser = new MultiFieldQueryParser(
	                      Version.LUCENE_30, multiFields, analyzer);
	  
	            // 设定具体的搜索词
	           Query query = parser.parse(keyword);
	            ScoreDoc[] hits = isearcher.search(query, null, 10).scoreDocs;
	 
	             for (int i = 0; i < hits.length; i++) {
	                Document hitDoc = isearcher.doc(hits[i].doc);
	                 Item item = new Item();
	                 for (String field : multiFields) {
	                     String setMethodName = "set"+toFirstLetterUpperCase(field);
	                     item.getClass().getMethod(setMethodName, String.class).invoke(item, hitDoc.get(field));
	                 }
	                result.add(item);
	            }
	         } catch (Exception e) {
	             e.printStackTrace();
	            return null;
	         } finally {
	            try {
	                 ireader.close();
	                 directory.close();
	             } catch (IOException e) {
            }
	         }
	         return result;
	     }
	     
	     /**
	      * 首字母转大写
	      * @param str
	      * @return
	      */
	    public static String toFirstLetterUpperCase(String str) {  
	         if(str == null || str.length() < 2){  
	             return str;  
	         }  
	        return str.substring(0, 1).toUpperCase() + str.substring(1, str.length());  
	      }  
}
