package com.comverse.timesheet.web.business.impl;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;


import org.kie.api.io.ResourceType;
import org.kie.internal.KnowledgeBase;
import org.kie.internal.KnowledgeBaseFactory;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.io.ResourceFactory;
import org.kie.internal.runtime.StatelessKnowledgeSession;

import com.comverse.timesheet.web.bean.Rule;
import com.comverse.timesheet.web.business.IRule;


public class RuleImpl implements IRule {
	private static Logger log = Logger.getLogger(RuleImpl.class);

	/**
	 * 设置kbuilder缓存
	 */
	private Map<String, KnowledgeBuilder> kbMap = new HashMap<String, KnowledgeBuilder>();

	private KnowledgeBuilder getKnowledgeBuilder(String rule) {
		KnowledgeBuilder kbuilder=kbMap.get(rule);
		if(kbuilder==null){
			StringReader ruleReader = new StringReader(rule);
			kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
			kbuilder.add(ResourceFactory.newReaderResource(ruleReader), ResourceType.DRL);
			kbMap.put(rule, kbuilder);
		}
		return kbuilder;
	}

	public int addService() {
		Rule rule = new Rule();
		try {
			
				StringBuilder sb=new StringBuilder();
				sb.append("package tnmc.sms.test.drools\r\n");
				sb.append("import com.comverse.timesheet.web.bean.Rule;\r\n");
				sb.append("rule \"Rule1\"	"+
							"when  "+
								"rule:Rule(montch>3); "+
							"then  "+
								"rule.setMoney(123)	;"+
						"end ");

				KnowledgeBuilder kbuilder = getKnowledgeBuilder(sb.toString());
				if (kbuilder.hasErrors()) {
					log.error(kbuilder.getErrors());
				}

				KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
				kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
				StatelessKnowledgeSession ksession = kbase.newStatelessKnowledgeSession();
				
				rule.setMontch(4);
				ksession.execute(Arrays.asList(new Object[] { rule }));
			
			
		} catch (Exception e) {
			log.error(e, e);
		}

		return rule.getMoney(); 
	}

}
