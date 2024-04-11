package in.am.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import in.am.model.DataModel;
import in.am.service.DataService;

@Controller
public class DownloadController {
	
	 @Autowired
	    private DataService dataService;
	
	@GetMapping("/")
	public String getdownloadByFilerfno(Model model) {

		return "DownloadByFilerfno";

	}
	@PostMapping("/DownloadByFilerfno")
	public ResponseEntity<List<DataModel>> fetchDataByFilerfno(@RequestParam("filerfno") String filerfno) {
	    try {
	        List<DataModel> dataList = dataService.fetchDataFilerfno(filerfno);
	        return ResponseEntity.ok().body(dataList);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	    }
	}

}
