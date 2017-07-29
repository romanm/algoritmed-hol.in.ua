package org.algoritmed.web;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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

	@GetMapping("/r/lastELiky")
	public  @ResponseBody Map<String, Object> lastELiky() {
		Map<String,Object> map = new HashMap<>();
		Map<String, Object> testDb1Medicamenten = testDb1Medicamenten();
		List<Map<String, Object>> allRestLikyDate = allRestLikyDate();
		Map<String, Object> map2 = allRestLikyDate.get(0);
		logger.info(" --------- \n"
				+ "/r/lastELiky" + map);
		Object object = map2.get("medicament_rest_date");
		Date d = (Date) object;
		map.put("date", d);
		String medicament_rest_date = DATE_FORMAT.format(d);
		Map<String, Object> readMedicamentRest = readMedicamentRest(medicament_rest_date);
		List<Map<String, Object>> db1Test1Medicamenten 
			= (List<Map<String, Object>>) readMedicamentRest.get("db1Test1Medicamenten");
//		map.put("db1Test1Medicamenten", db1Test1Medicamenten);
		List<Map<String, Object>> dbToEliky = dbToEliky(db1Test1Medicamenten);
		map.put("dbToEliky", dbToEliky);
		return map;
	}

	private List<Map<String, Object>> dbToEliky(List<Map<String, Object>> db1Test1Medicamenten) {
		List<Map<String, Object>> list = new ArrayList<>();
		for (Map<String, Object> map : db1Test1Medicamenten) {
			HashMap<String, Object> map2 = new HashMap<>();
			map2.put("number", (Integer) map.get("medicament_rest"));
			String medicament_name = (String) map.get("medicament_name");
			String unit = "уп";
			if(medicament_name.contains("фл")){
				unit = "фл";
			}else
			if(medicament_name.contains("амп")){
				unit = "амп";
			}else
			if(medicament_name.matches(".* \\d{3}\\s*мл.*")){
				unit = "фл";
			}else
			if(medicament_name.matches(".* \\d{1,2}\\s*мл.*")){
				unit = "амп";
			}
			map2.put("unit", unit);
			String[] split = medicament_name.split(" ");
			String name = split[0];
			split = Arrays.copyOfRange(split, 1, split.length);
			/**/
			if(split.length>0){
				byte[] bytes = split[0].getBytes();
				if(bytes.length>0){
					if(bytes[0] < 0){
						name += " " + split[0];
						split = Arrays.copyOfRange(split, 1, split.length);
					}
				}
			}
			map2.put("name", name);
			String pack = arrayToStrin(split);
			map2.put("pack", pack);
//			map2.put("pack", Arrays.toString(split));
			list.add(map2);
		}
		return list;
	}
	private String arrayToStrin(String[] split) {
		StringBuilder builder = new StringBuilder();
		for(String s : split) {
		    builder.append(s).append(" ");
		}
		String string = builder.toString().trim();
		return string;
	}
	SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

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
		List<Map<String, Object>> allDateDb1MedicamentenList = allRestLikyDate();
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
	protected List<Map<String, Object>> allRestLikyDate() {
		List<Map<String, Object>> allDateDb1MedicamentenList = db1JdbcTemplate.queryForList(sqlDb1AllDateDb1Medicamenten);
		return allDateDb1MedicamentenList;
	}


}
