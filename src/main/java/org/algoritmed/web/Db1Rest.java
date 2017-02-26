package org.algoritmed.web;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.LocalDate;
import org.joda.time.format.ISODateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class Db1Rest {
	private static final Logger logger = LoggerFactory.getLogger(Db1Rest.class);

	@Autowired JdbcTemplate db1JdbcTemplate;
	@Autowired NamedParameterJdbcTemplate db1ParamJdbcTemplate;

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

	private @Value("${sql.db1.testDb1Medicamenten}") String sqlDb1TestDb1Medicamenten;
	private @Value("${sql.db1.allDateDb1Medicamenten}") String sqlDb1AllDateDb1Medicamenten;
	@GetMapping("/r/readMedicamentRest/{medicament_rest_dateStr}")
	public @ResponseBody Map<String, Object> readMedicamentRest(
			@PathVariable String medicament_rest_dateStr
		) {
		Map<String,Object> map = new HashMap<>();
		map.put("url", "/r/readMedicamentRest");
		logger.info(" --------- \n"
				+ medicament_rest_dateStr
				+ "\n"
				+ "/r/readMedicamentRest"
				+ "\n"
				+ sqlDb1TestDb1Medicamenten
				);
		LocalDate parseLocalDate = ISODateTimeFormat.date().parseLocalDate(medicament_rest_dateStr);
		logger.info(" --------- \n"
				+ parseLocalDate
				);
		java.util.Date date = parseLocalDate.toDate();
		long time = date.getTime();
		Date date2 = new Date(time);
		map.put("medicament_rest_date", date2);
		readMedicamentRest(map);
		return map;
	}

	private void readMedicamentRest(Map<String, Object> map) {
		List<Map<String, Object>> db1Test1Medicamenten = db1ParamJdbcTemplate.queryForList(sqlDb1TestDb1Medicamenten, map);
		map.put("db1Test1Medicamenten", db1Test1Medicamenten);
	}

	@GetMapping("/r/testDb1Medicamenten")
	public  @ResponseBody Map<String, Object> testDb1Medicamenten() {
		Map<String,Object> map = new HashMap<>();
		map.put("url", "/r/testDb1Medicamenten");
		List<Map<String, Object>> allDateDb1MedicamentenList = db1JdbcTemplate.queryForList(sqlDb1AllDateDb1Medicamenten);
		map.put("allDateDb1MedicamentenList", allDateDb1MedicamentenList);
		Map<String, Object> map2 = allDateDb1MedicamentenList.get(0);
		Date medicament_rest_date = (Date) map2.get("medicament_rest_date");
		map.put("medicament_rest_date", medicament_rest_date);
		logger.info(" --------- \n"
				+ medicament_rest_date
				+ "\n"
				+ "/r/testDb1Medicamenten"
				+ "\n"
				+ sqlDb1TestDb1Medicamenten
				);
		logger.info(" --------- \n"
				+ "/r/testDb1" + map);
		return map;
	}


}
