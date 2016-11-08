package com.comverse.timesheet.web.business.impl;

import java.io.File;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.springframework.transaction.annotation.Transactional;

import com.comverse.timesheet.web.BookEnum;
import com.comverse.timesheet.web.SystemEnum;
import com.comverse.timesheet.web.bean.Item;
import com.comverse.timesheet.web.business.CheckBookIsLegitimateBusiness;
import com.comverse.timesheet.web.business.IBookBusiness;
import com.comverse.timesheet.web.business.ILuceneBusiness;
import com.comverse.timesheet.web.business.ISystemBusiness;
import com.comverse.timesheet.web.dto.BookTemporaryDTO;

@Transactional
public class CheckBookIsLegitimateBusinessImpl implements CheckBookIsLegitimateBusiness{
	private static final Logger log = Logger.getLogger(CheckBookIsLegitimateBusinessImpl.class);
	@Resource
	private IBookBusiness bookBusiness;
	@Resource
	private ILuceneBusiness luceneBusiness;
	@Resource
	private ISystemBusiness systemBusiness;
	public void CheckBookIsLegitimate() {
		log.debug("后台线程定时检查书籍是否合法");
		Analyzer analyzer = null;
        Directory directory = null;
        try {
	       	int count = SystemEnum.CHECK_BOOKCOUNT.getLogFlag();
			List<BookTemporaryDTO> bookTemporaryDTOList = bookBusiness.findTemporaryBook(new BookTemporaryDTO(count, BookEnum.IS_CHECK_NO.getSexFlag()));
			for (BookTemporaryDTO bookTemporaryDTO : bookTemporaryDTOList) {
				if((null != bookTemporaryDTO)&&(null!=bookTemporaryDTO.getBookFile())) {
					analyzer = new StandardAnalyzer(Version.LUCENE_44);
		       	 	directory = FSDirectory.open(new File(systemBusiness.getSysConfigure("LUCENE_FILE_ADDRESS").getValue()));
					luceneBusiness.buildIndexer(analyzer,directory,bookTemporaryDTO.getBookFile());
			        int result = luceneBusiness.searchIndexer(analyzer, directory, BookEnum.ILLEGAL_CHARACTER.getValue().split(","));
			        bookTemporaryDTO.setIsCheck(BookEnum.IS_CHECK_ONGOING.getSexFlag());
			        bookTemporaryDTO.setIllegalCharacter(result);
			        bookBusiness.updateTemporaryBook(bookTemporaryDTO);
				}
			}
        }catch(Exception e) {
       	 log.error("FSDirectory.open产生异常e:"+e);
        }
		
	}

}
