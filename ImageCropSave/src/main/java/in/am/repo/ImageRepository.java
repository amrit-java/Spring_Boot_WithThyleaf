package in.am.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import in.am.entity.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
