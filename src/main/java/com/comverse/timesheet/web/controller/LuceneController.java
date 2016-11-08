package com.comverse.timesheet.web.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.comverse.timesheet.web.bean.Item;
import com.comverse.timesheet.web.business.ISystemBusiness;
import com.comverse.timesheet.web.business.ILuceneBusiness;
import com.comverse.timesheet.web.business.Memberbusiness;
import com.comverse.timesheet.web.business.impl.LucenusinessImpl;


@Controller   
public class LuceneController {
	@Autowired
	private ILuceneBusiness luceneDemoBusiness; 
	@Autowired
	private ISystemBusiness systemBusiness; 
	private static Logger log = Logger.getLogger(LuceneController.class);  
	@RequestMapping("/testLucene") 
	public String testLucene() {
		        Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_44);
		
//		         List<Item> items = new ArrayList<Item>();
//		         items.add(new Item("1", "first", "This is the text to be greatly indexed."));
//		         items.add(new Item("2", "second", "This is great"));
//		         items.add(new Item("3", "third", "I love apple and pear. "));
//		         items.add(new Item("4", "four", "我是中国人"));
//		         items.add(new Item("5", "five", "我叫何瑞"));
		         
		         // 索引存到内存中的目录
		         //Directory directory = new RAMDirectory();
		         // 索引存储到硬盘
		         File file = new File("d:/lucene");
		         String bookFile =  "d:/book/20161107/1478502028125_linux.txt";
		         Directory directory = null;
		         try {
		        	 directory = FSDirectory.open(new File(systemBusiness.getSysConfigure("LUCENE_FILE_ADDRESS").getValue()));
		         }catch(Exception e) {
		        	 log.error("FSDirectory.open产生异常e:"+e);
		         }
//		         luceneDemoBusiness.buildIndexer(analyzer,directory,bookFile);
//		         List<Item> result = luceneDemoBusiness.searchIndexer(analyzer, directory, "复制");
		         
//		        for (Item item : result) {
//		             System.out.println(item.toString());
//		         }
		     return "result";
	}
}