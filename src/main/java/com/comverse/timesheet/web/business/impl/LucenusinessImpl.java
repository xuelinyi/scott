package com.comverse.timesheet.web.business.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
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
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.springframework.transaction.annotation.Transactional;

import com.comverse.timesheet.web.bean.Item;
import com.comverse.timesheet.web.business.ISystemBusiness;
import com.comverse.timesheet.web.business.ILuceneBusiness;

@Transactional
public class LucenusinessImpl implements ILuceneBusiness{
	private static final Logger log = Logger.getLogger(LucenusinessImpl.class);
	@Resource
	private ISystemBusiness systemBusiness;
	@SuppressWarnings("resource")
	public boolean buildIndexer(Analyzer analyzer, Directory directory, String bookFile) {
		IndexWriter iwriter = null;
		BufferedReader br = null; 
	    try {
	         
	    	  iwriter = new IndexWriter(directory, new IndexWriterConfig(Version.LUCENE_44, analyzer));
	          // 删除所有document
	          iwriter.deleteAll();
	          // 将文档信息存入索引
	          br = new BufferedReader(new FileReader(bookFile)); 
	          int x = 0;   // 用于统计行数，从0开始
	          String tempString = null;
	          List<Item> items = new ArrayList<Item>();
	          while ((tempString = br.readLine()) != null) {
	        	  items.add(new Item(x+"", "第"+x+"行", tempString));
	        	  x++;
	          }
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
	          log.error("生成lucence错误。e:"+e);
	          return false;
	      } finally {
	          try {
	        	  if(null != br) {
	        		  br.close();
	        		  br = null;
	        	  }
	          } catch (IOException e) {
	        	  log.error("关闭br错误。e:"+e);
	          }
	          try {
	        	  if(null != iwriter) {
	        		  iwriter.close();
	        		  iwriter = null;
	        	  }
	          } catch (IOException e) {
	        	  log.error("关闭iwriter错误。e:"+e);
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
      public int searchIndexer(Analyzer analyzer, Directory directory, String[] keywords) {
          DirectoryReader ireader = null;
          int sum = 0;
          try {
        	  for (String keyword : keywords) {
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
                  //ScoreDoc[] hits = isearcher.search(query, null, 10).scoreDocs.length;
                  sum += isearcher.search(query, null, 10).scoreDocs.length;
        	  }
//             for (int i = 0; i < hits.length; i++) {
//                Document hitDoc = isearcher.doc(hits[i].doc);
//                 Item item = new Item();
//                 for (String field : multiFields) {
//                     String setMethodName = "set"+toFirstLetterUpperCase(field);
//                     item.getClass().getMethod(setMethodName, String.class).invoke(item, hitDoc.get(field));
//                 }
//                result.add(item);
//            }
         } catch (Exception e) {
             e.printStackTrace();
            return sum;
         } finally {
            try {
            	if(null != ireader) {
            		ireader.close();
            		ireader = null;
            	}
             } catch (IOException e) {
            	 log.error("关闭ireader连接产生异常e:"+e);
             }
             try {
            	if(null != directory) {
            		directory.close();
            		directory = null;
            	}
             } catch (IOException e) {
            	 log.error("关闭directory连接产生异常e:"+e);
             }
         }
         return sum;
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
