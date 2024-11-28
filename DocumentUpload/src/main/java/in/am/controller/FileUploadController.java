package in.am.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import in.am.entity.FileUpload;
import in.am.service.FileUploadService;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
public class FileUploadController {

    @Autowired
    private FileUploadService fileUploadService;
    private static final String FILE_DIRECTORY = "D:/uploads";
    @GetMapping("/")
    public String showUploadForm() {
        return "upload";
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file, Model model) {
        if (file.isEmpty()) {
            model.addAttribute("message", "Please select a file to upload.");
            model.addAttribute("messageType", "error");
            return "upload";
        }

        File uploadDir = new File(FILE_DIRECTORY);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf('.'));
        String newFilename = originalFilename + "_" +
                             LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) + extension;

        try {
            File destinationFile = new File(uploadDir, newFilename);
            file.transferTo(destinationFile);

            // Save file metadata to the database
            FileUpload fileUpload = new FileUpload();
            fileUpload.setOriginalFilename(originalFilename);
            fileUpload.setNewFilename(newFilename);
            fileUpload.setFilePath(destinationFile.getAbsolutePath());
            fileUpload.setUploadTime(LocalDateTime.now());

            fileUploadService.saveFileDetails(fileUpload);

            model.addAttribute("message", "File uploaded successfully: " + newFilename);
            model.addAttribute("messageType", "success");
        } catch (IOException e) {
            model.addAttribute("message", "Error uploading file: " + e.getMessage());
            model.addAttribute("messageType", "error");
        }

        return "upload";
    }
}
