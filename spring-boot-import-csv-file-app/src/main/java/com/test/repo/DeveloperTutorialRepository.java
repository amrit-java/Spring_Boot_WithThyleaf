package com.test.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.test.entity.DeveloperTutorial;

@Repository
public interface DeveloperTutorialRepository extends JpaRepository<DeveloperTutorial, Integer>{

}
