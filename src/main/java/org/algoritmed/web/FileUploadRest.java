package org.algoritmed.web;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class FileUploadRest {

	private @Value("${config.uploadedFilesDirectory}") String uploadedFilesDirectory;

	@PostMapping(value = "/uploadFile")
	public @ResponseBody ResponseEntity<?> uploadFile(
			@RequestParam("uploadfile") MultipartFile uploadfile) {

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
