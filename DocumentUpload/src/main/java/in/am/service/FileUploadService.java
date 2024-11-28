package in.am.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.am.entity.FileUpload;
import in.am.repo.FileUploadRepository;

import java.util.Optional;

@Service
public class FileUploadService {

    @Autowired
    private FileUploadRepository fileUploadRepository;

    public FileUpload saveFileDetails(FileUpload fileUpload) {
        return fileUploadRepository.save(fileUpload);
    }

    public Optional<FileUpload> getFileById(Long id) {
        return fileUploadRepository.findById(id);
    }
}
