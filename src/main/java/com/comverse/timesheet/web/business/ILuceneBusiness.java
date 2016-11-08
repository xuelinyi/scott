package com.comverse.timesheet.web.business;

import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.store.Directory;

import com.comverse.timesheet.web.bean.Item;

public interface ILuceneBusiness {
	boolean buildIndexer(Analyzer analyzer, Directory directory, String bookFile);
	public int searchIndexer(Analyzer analyzer, Directory directory, String[] keywords);
}
