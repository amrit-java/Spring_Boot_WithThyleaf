package in.am.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import in.am.entity.FileUpload;


@Repository
public interface FileUploadRepository extends JpaRepository<FileUpload, Long> {
}
