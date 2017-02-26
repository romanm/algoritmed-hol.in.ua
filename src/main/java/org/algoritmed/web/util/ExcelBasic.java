package org.algoritmed.web.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

@Component("excelService")
public class ExcelBasic {
	private static final Logger logger = LoggerFactory.getLogger(ExcelBasic.class);

	@Autowired NamedParameterJdbcTemplate db1ParamJdbcTemplate;
	
	private @Value("${config.uploadedFilesDirectory}") String uploadedFilesDirectory;
	private @Value("${sql.db1.medicament.fromId}") String medicamentFromId;
	private @Value("${sql.db1.medicament.insert}") String medicamentInsert;
	DateTimeFormatter shortDate = ISODateTimeFormat.date();

	int medicamentIdCellNum = 1;
	int medicamentNameCellNum = 2;
	int medicamentRestCellNum = 16;
	public void pharmaC1ToDb(XSSFWorkbook xssfWorkbook){
		logger.info("-----------\n"
				+ "-------\n"
				+ xssfWorkbook
				);
		int activeSheetIndex = xssfWorkbook.getActiveSheetIndex();
		XSSFSheet sheetAt = xssfWorkbook.getSheetAt(activeSheetIndex);
		short topRow = sheetAt.getTopRow();
		int firstRowNum = sheetAt.getFirstRowNum();
		int lastRowNum = sheetAt.getLastRowNum();
		System.out.println(topRow+"/"+firstRowNum+"/"+lastRowNum);
		XSSFCell cell = sheetAt.getRow(1).getCell(2);
		String fromDateString = cell.getStringCellValue().split(" по ")[1].split(" ")[0];
		String[] ddmmyyStr = fromDateString.split("\\.");
		DateTime dateTime = new DateTime(Integer.parseInt("20"+ddmmyyStr[2]), Integer.parseInt(ddmmyyStr[1]), Integer.parseInt(ddmmyyStr[0]), 9, 0);
		System.out.println(dateTime);
		String medicament_rest_date = shortDate.print(dateTime);
		System.out.println( medicament_rest_date );
		Map<String,Object> map = new HashMap<>();
		map.put("medicament_rest_date", medicament_rest_date);
		for (int rowNum = 5; rowNum < lastRowNum; rowNum++) {
			XSSFRow row = sheetAt.getRow(rowNum);
			XSSFCell medicamentId = row.getCell(medicamentIdCellNum);
			CellType cellTypeEnum = medicamentId.getCellTypeEnum();
			int medicament_id = 0;
			if(cellTypeEnum == CellType.STRING){
				medicament_id=Integer.parseInt(medicamentId.getStringCellValue());
			}
			map.put("medicament_id", medicament_id);
			List<Map<String, Object>> medicamentFromIdList = db1ParamJdbcTemplate.queryForList(medicamentFromId, map);
			if(medicamentFromIdList.isEmpty()){
				String medicament_name = row.getCell(medicamentNameCellNum).getStringCellValue();
				map.put("medicament_name", medicament_name);
				db1ParamJdbcTemplate.update(medicamentInsert, map);
			}else{
//				System.out.println(queryForList);
			}
			Double numericCellValue = row.getCell(medicamentRestCellNum).getNumericCellValue();
			int medicament_rest = numericCellValue.intValue();
			map.put("medicament_rest", medicament_rest);
			List<Map<String, Object>> medicamentRestFromDateList = db1ParamJdbcTemplate.queryForList(medicamentRestFromDate, map);
			if(medicamentRestFromDateList.isEmpty()){
//				logger.info("-----------\n" + medicamentRestInsert + "\n" + map);
				db1ParamJdbcTemplate.update(medicamentRestInsert, map);
			}else{
//				logger.info("-----------\n" + medicamentRestUpdate + "\n" + map);
				db1ParamJdbcTemplate.update(medicamentRestUpdate, map);
			}
		}
	}
	private @Value("${sql.db1.medicament_rest.fromDate}") String medicamentRestFromDate;
	private @Value("${sql.db1.medicament_rest.insert}") String medicamentRestInsert;
	private @Value("${sql.db1.medicament_rest.update}") String medicamentRestUpdate;


//	public HSSFWorkbook readExcel(String fileName) {
	public XSSFWorkbook readExcel(String fileName) {
		Path resolve = Paths.get(uploadedFilesDirectory).resolve(fileName);
		return readExcelFullName(resolve.toString());
	}

//	protected HSSFWorkbook readExcelFullName(String fullFileName) {
	protected XSSFWorkbook readExcelFullName(String fullFileName) {
		try {
			logger.debug(fullFileName);
			InputStream inputStream = new FileInputStream(fullFileName);
			XSSFWorkbook xssfWorkbook = new XSSFWorkbook(inputStream);
			return xssfWorkbook;
//			HSSFWorkbook hssfWorkbook = new HSSFWorkbook(inputStream);
//			return hssfWorkbook;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
