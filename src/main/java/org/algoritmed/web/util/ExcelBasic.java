package org.algoritmed.web.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("excelService")
public class ExcelBasic {
	private static final Logger logger = LoggerFactory.getLogger(ExcelBasic.class);
	
	private @Value("${config.uploadedFilesDirectory}") String uploadedFilesDirectory;

	public HSSFWorkbook readExcel(String fileName) {
		Path resolve = Paths.get(uploadedFilesDirectory).resolve(fileName);
		return readExcelFullName(resolve.toString());
	}

	protected HSSFWorkbook readExcelFullName(String fullFileName) {
		try {
			logger.debug(fullFileName);
			InputStream inputStream = new FileInputStream(fullFileName);
			return new HSSFWorkbook(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
