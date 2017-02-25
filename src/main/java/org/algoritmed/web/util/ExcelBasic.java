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
		String[] split = fromDateString.split("\\.");
		DateTime dateTime = new DateTime(Integer.parseInt("20"+split[2]), Integer.parseInt(split[1]), Integer.parseInt(split[0]), 9, 0);
		System.out.println(dateTime);
		for (int rowNum = 5; rowNum < lastRowNum; rowNum++) {
			XSSFRow row = sheetAt.getRow(rowNum);
			XSSFCell medicamentId = row.getCell(1);
			CellType cellTypeEnum = medicamentId.getCellTypeEnum();
			int c1Id = 0;
			if(medicamentId.getCellTypeEnum() == CellType.STRING){
				c1Id=Integer.parseInt(medicamentId.getStringCellValue());
			}
			Map<String,Object> map = new HashMap<>();
			map.put("medicament_id", c1Id);
			List<Map<String, Object>> queryForList = db1ParamJdbcTemplate.queryForList(medicamentFromId, map);
			if(!queryForList.isEmpty()){
				System.out.println(queryForList);
			}else{
				String medicamentName = row.getCell(2).getStringCellValue();
				map.put("medicament_name", medicamentName);
//				System.out.println(medicamentName);
				db1ParamJdbcTemplate.update(medicamentInsert, map);
			}
			if(rowNum>11111)
				break;
		}
	}


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
