package org.algoritmed.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class Db1Rest {
	private static final Logger logger = LoggerFactory.getLogger(Db1Rest.class);

	@Autowired
	JdbcTemplate db1JdbcTemplate;
	
	private @Value("${sql.db1.testDb1}") String sqlDb1TestDb1;

	@GetMapping("/r/testDb1")
	public  @ResponseBody Map<String, Object> testDb1() {
		Map<String,Object> map = new HashMap<>();
		map.put("url", "/r/testDb1");
		List<Map<String, Object>> db1Test1 = db1JdbcTemplate.queryForList(sqlDb1TestDb1);
		map.put("db1Test1", db1Test1);
		logger.info(" --------- \n"
				+ "/r/testDb1" + map);
		return map;
	}

}
