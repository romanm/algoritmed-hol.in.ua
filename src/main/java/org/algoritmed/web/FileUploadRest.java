package org.algoritmed.web;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.algoritmed.web.util.ExcelBasic;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class FileUploadRest {
	private static final Logger logger = LoggerFactory.getLogger(FileUploadRest.class);
	private @Value("${config.uploadedFilesDirectory}") String uploadedFilesDirectory;

	@Autowired private ExcelBasic excelService;

	@PostMapping("/uploadFile2")
	public String handleFileUpload(@RequestParam("file") MultipartFile file,
			RedirectAttributes redirectAttributes) {

		logger.info("--------------------\n"
				+ "/uploadFile2 \n"
				+ file);

		String originalFilename = file.getOriginalFilename();
		Path resolve = Paths.get(uploadedFilesDirectory).resolve(originalFilename);
		try {
			Files.copy(file.getInputStream(), resolve, REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}

		HSSFWorkbook readExcel = excelService.readExcel(originalFilename);

		logger.info("--------------------\n"
				+ "/uploadFile2 \n"
				+ readExcel
				);
		
		return "redirect:/";
	}

	@PostMapping(value = "/uploadFile")
	public @ResponseBody ResponseEntity<?> uploadFile(
			@RequestParam("uploadfile") MultipartFile uploadfile) {

		logger.info("--------------------\n"
				+ "/uploadFile\n"
				+ uploadfile);

		try {
			// Get the filename and build the local file path
			String filename = uploadfile.getOriginalFilename();
			String filepath = Paths.get(uploadedFilesDirectory, filename).toString();

			// Save the file locally
			BufferedOutputStream stream =
					new BufferedOutputStream(new FileOutputStream(new File(filepath)));
			stream.write(uploadfile.getBytes());
			stream.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(HttpStatus.OK);
	} // method uploadFile

}
