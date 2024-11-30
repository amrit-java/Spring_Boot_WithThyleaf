package in.am.controller;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import in.am.entity.Image;
import in.am.service.ImageService;

@Controller
@RequestMapping("/images")
public class ImageController {

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }


    @GetMapping
    public String showUploadPage() {
        return "upload";
    }

    // Handle image upload
    @PostMapping("/upload")
    public String uploadImage(@RequestParam("file") MultipartFile file, Model model) {
        try {
            Image savedImage = imageService.saveImage(file);
            model.addAttribute("image", savedImage);
            return "crop";  // Navigate to cropping page
        } catch (IOException e) {
            model.addAttribute("message", "Failed to upload image: " + e.getMessage());
            return "upload";
        }
    }

    // View the image
    @GetMapping("/view/{id}")
    public ResponseEntity<byte[]> viewImage(@PathVariable Long id) {
        Image image = imageService.getImage(id).orElseThrow(() -> new RuntimeException("Image not found"));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, image.getType())
                .body(image.getData());
    }

    @PostMapping("/save")
    public String saveCroppedImage(@RequestParam Long id, @RequestParam("croppedData") MultipartFile croppedData, RedirectAttributes redirectAttributes) {
        try {
            // Save the cropped image data to the database
            byte[] croppedImageData = croppedData.getBytes();
            imageService.saveCroppedImage(id, croppedImageData);
            redirectAttributes.addFlashAttribute("message", "Image cropped and saved successfully.");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("message", "Failed to save cropped image.");
        }
        return "redirect:/images";
    }
}
