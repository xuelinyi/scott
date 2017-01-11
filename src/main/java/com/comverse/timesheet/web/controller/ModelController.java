package com.comverse.timesheet.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Model;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value="model")
public class ModelController {
	private static final Logger log = Logger.getLogger(ModelController.class);
	@Autowired
	private RepositoryService repositoryService;
	
	/**
	 * 模型列表
	 */
	@RequestMapping(value="list")
	public String modelList(org.springframework.ui.Model model) {
		log.debug("查询所有的模型列表");
		List<Model> modelList = repositoryService.createModelQuery().list();
		model.addAttribute("modelList", modelList);
		return "workflow/model-list";
	}
	/**
	 * 创建模型
	 * @author 12440
	 */
	@RequestMapping(value="create", method = RequestMethod.POST)
	public void create (@RequestParam("name")String name, @RequestParam("key")String key,@RequestParam("description")String description
			,HttpServletResponse resp) {
		log.debug("创建模型。name："+name+"<<key:"+key+"<<description:"+description);
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			ObjectNode editorNode = objectMapper.createObjectNode();
			editorNode.put("id", "canvas");;
			editorNode.put("resourceId", "canvas");
			ObjectNode stencilSetNode = objectMapper.createObjectNode();
			stencilSetNode.put("stencilset", stencilSetNode);
			Model modelData = repositoryService.newModel();
			ObjectNode modelObjectNode = objectMapper.createObjectNode();
			modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, name);
			modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, 1);
			description = StringUtils.defaultString(description);
			modelObjectNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, description);
			modelData.setMetaInfo(modelObjectNode.toString());
			modelData.setName(name);
			modelData.setKey(StringUtils.defaultString(key));
			repositoryService.saveModel(modelData);
			repositoryService.addModelEditorSource(modelData.getId(), editorNode.toString().getBytes("utf-8"));
			resp.sendRedirect("/service/editor?id=" + modelData.getId());
		}catch(Exception e) {
			log.error("创建模型失败。"+e);
		}
		
	}
}
