package in.am.service;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.am.model.DataModel;
import in.am.repository.DataRepo;

@Service
public class DataService {
	
	@Autowired
    private  DataRepo dataRepo;
	
	 public List<DataModel> fetchDataFilerfno(String filerfno) {
	        List<DataModel> dataList = new ArrayList<>();
	        try {
	            dataList = dataRepo.fetchdatafilerfno(filerfno);
	        } catch (Exception e) {
	            e.printStackTrace(); // Log or handle the exception appropriately
	        }
	        return dataList;
	    }

}
