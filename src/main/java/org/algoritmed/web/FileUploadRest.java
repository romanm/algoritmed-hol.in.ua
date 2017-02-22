package org.algoritmed.web;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	@PostMapping("/uploadFile2")
	public String handleFileUpload(@RequestParam("file") MultipartFile file,
			RedirectAttributes redirectAttributes) {

		logger.info("--------------------\n"
				+ "/uploadFile2 \n"
				+ uploadedFilesDirectory
				+ "\n"
				+ file);

/*
		try {
			// Get the filename and build the local file path
			String filename = file.getOriginalFilename();
			String filepath = Paths.get(uploadedFilesDirectory, filename).toString();

			// Save the file locally
			BufferedOutputStream stream =
					new BufferedOutputStream(new FileOutputStream(new File(filepath)));
			stream.write(file.getBytes());
			stream.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
 * */

		Path path = Paths.get(uploadedFilesDirectory);
System.out.println(path);

		try {
			Files.copy(file.getInputStream(), path.resolve(file.getOriginalFilename()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		/*

		storageService.store(file);
		redirectAttributes.addFlashAttribute("message",
				"You successfully uploaded " + file.getOriginalFilename() + "!");
 * */

		return "redirect:/";
	}
/*
	@ExceptionHandler(StorageFileNotFoundException.class)
	public ResponseEntity handleStorageFileNotFound(StorageFileNotFoundException exc) {
		return ResponseEntity.notFound().build();
	}
 * */

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
