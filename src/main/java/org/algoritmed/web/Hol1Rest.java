package org.algoritmed.web;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.algoritmed.web.util.ReadJsonFromFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class Hol1Rest {
	private static final Logger logger = LoggerFactory.getLogger(Hol1Rest.class);

	@Autowired	ReadJsonFromFile readJsonFromFile;
	
	@GetMapping("/r/principal")
	public @ResponseBody Map<String, Object> principal(Principal principal) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("principal", principal);
		logger.info(" --------- \n"
				+ "/v/testUUI \n" + map);
		return map;
	}

	@GetMapping("/r/testUUID")
	public @ResponseBody Map<String, Object> testUUI(Principal principal) {
		Map<String, Object> map = new HashMap<String, Object>();
		UUID uuid = addUuid(map);
		map.put("version", uuid.version());
		map.put("variant", uuid.variant());
		map.put("length", uuid.toString().length());
		map.put("principal", principal);
		logger.info(" --------- \n"
				+ "/v/testUUI \n" + map);
		return map;
	}

	/**
	 * Генерує випадковий universally unique identifier (UUID), 
	 * додає його до map
	 * @param map об'єкт для довавання новоствореного UUID
	 * @return новостворений UUID
	 */
	protected UUID addUuid(Map<String, Object> map) {
		UUID uuid = UUID.randomUUID();
		map.put("uuid", uuid);
		return uuid;
	}

	@GetMapping("/v/{page1}/{page2}")
	public String readPage1Page2(
		@PathVariable String page1
		,@PathVariable String page2
		,Model model
		) {
		model.addAttribute("page01", page1);
		String th_template = viewPage1(page2, model);
		return th_template;
	}

	@GetMapping("/v/department/{department}/{part}")
	public String viewDepartmentPage(
			@PathVariable String department
			,@PathVariable String part
			, Model model) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("department", department);
		map.put("part", part);
		setModelAtribute(model, "department", "ng_template");
		logger.info(" --------- \n"
				+ "/v/department/{department}/{part}"
				+ "\n" + map
				+ "\n" + model
				);
		return "hol.in.ua2";
	}

	@GetMapping("/v/department/{department}")
	public String viewDepartmentPage(@PathVariable String department, Model model) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("department", department);
		setModelAtribute(model, "department", "ng_template");
		logger.info(" --------- \n"
				+ "/v/department/{department}"
				+ "\n" + map
				+ "\n" + model
				);
		return "hol.in.ua2";
	}

	@GetMapping("/v/{page1}")
	public String viewPage1(@PathVariable String page1, Model model) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("page1", page1);
		logger.info(" --------- \n"
				+ "/v/{page1}"
				+ "\n" + map
				+ "\n" + model
				);
		setModelAtribute(model, page1, "ng_app");
		setModelAtribute(model, page1, "ng_template");
		setModelAtribute(model, page1, "am_include_js");
		setModelAtribute(model, page1, "ng_controller");
		logger.info(" --------- \n"
				+ "/v/{page1}"
				+ "\n" + map
				+ "\n" + model
				);
		String th_template = (String) getModelAttribute(page1, "th_template");
		return th_template;
	}

	private void setModelAtribute(Model model, String page1, String attribute) {
		Object ngController = getModelAttribute(page1, attribute);
		model.addAttribute(attribute, ngController);
	}

	private Object getModelAttribute(String page1, String attribute) {
		Map<String, Object> configWebSite = readJsonFromFile.readConfigWebSite();
		Map<String, Object> pageConfig = (Map<String, Object>) configWebSite.get(page1);
		Object ngController = null;
		if(pageConfig != null){
			if(pageConfig.containsKey(attribute))
				ngController = pageConfig.get(attribute);
			else
				ngController = configWebSite.get(attribute);
		}
//		logger.info(" --------- "
//				+"\n"+ "page1="+page1
//				+"\n"+ attribute
//				+"\n"+ ngController
//				+"\n"+ pageConfig
//				+"\n"+ pageConfig.containsKey(attribute)
//				+"\n"+ configWebSite.containsKey(attribute)
//				);
		return ngController;
	}

	@GetMapping("/r/testReadConfigFile")
	public @ResponseBody Map<String, Object> testReadConfigFile() {
		Map<String, Object> map = new HashMap<String, Object>();
		logger.info(" --------- \n"
				+ "/r/testReadConfigFile"
				+ "\n" + map);
		Map<String, Object> readJsonFromFullFileName = readJsonFromFile.readConfigWebSite();
		logger.info(" --------- \n"
				+ "/r/testReadConfigFile"
				+ "\n" + readJsonFromFullFileName);
		map.put("readJsonFromFullFileName", readJsonFromFullFileName);
		return map;
	}

}
