package com.test.service;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.test.entity.DeveloperTutorial;
import com.test.helper.CSVHelper;
import com.test.repo.DeveloperTutorialRepository;

@Service
public class CSVService {
  @Autowired
  DeveloperTutorialRepository repository;

  public void save(MultipartFile file) {
    try {
      List<DeveloperTutorial> tutorials = CSVHelper.csvToTutorials(file.getInputStream());
      repository.saveAll(tutorials);
    } catch (IOException e) {
      throw new RuntimeException("fail to store csv data: " + e.getMessage());
    }
  }

  public ByteArrayInputStream load() {
    List<DeveloperTutorial> tutorials = repository.findAll();

    ByteArrayInputStream in = CSVHelper.tutorialsToCSV(tutorials);
    return in;
  }

  public List<DeveloperTutorial> getAllTutorials() {
    return repository.findAll();
  }
}
