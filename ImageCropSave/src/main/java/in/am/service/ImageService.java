package in.am.service;

import java.io.IOException;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import in.am.entity.Image;
import in.am.repo.ImageRepository;

@Service
public class ImageService {

    private final ImageRepository imageRepository;

    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    // Save uploaded image
    public Image saveImage(MultipartFile file) throws IOException {
        Image image = new Image();
        image.setName(file.getOriginalFilename());
        image.setType(file.getContentType());
        image.setData(file.getBytes());
        return imageRepository.save(image);
    }

    // Get image by ID
    public Optional<Image> getImage(Long id) {
        return imageRepository.findById(id);
    }

    // Save cropped image
    public void saveCroppedImage(Long id, byte[] croppedData) {
        Image image = imageRepository.findById(id).orElseThrow(() -> new RuntimeException("Image not found"));
        image.setData(croppedData);
        imageRepository.save(image);
    }
}
