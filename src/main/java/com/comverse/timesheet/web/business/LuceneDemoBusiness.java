package com.comverse.timesheet.web.business;

import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.store.Directory;

import com.comverse.timesheet.web.bean.Item;

public interface LuceneDemoBusiness {
	boolean buildIndexer(Analyzer analyzer, Directory directory, List<Item> items);
	public List<Item> searchIndexer(Analyzer analyzer, Directory directory, String keyword);
}
