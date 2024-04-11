package in.am.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import in.am.model.DataModel;

@Repository
public class DataRepo {
	
	@Autowired
	private Connection conn;
	


	public List<DataModel> fetchdatafilerfno(String filerfno) {
		List<DataModel> dataList = new ArrayList<>();
		//DataModel details = null;
		try {
			String q = "select * from public_service_req_details where filerfno=?";
			PreparedStatement stmt = conn.prepareStatement(q);
			stmt.setString(1, filerfno);
			ResultSet resultSet = stmt.executeQuery();
			 while (resultSet.next()) {
	                DataModel dataModel = new DataModel();
	                dataModel.setFilerfno(resultSet.getString("filerfno"));
	                dataModel.setService_id(resultSet.getString("service_id"));
	                dataModel.setField_indx(resultSet.getString("field_indx"));
	                dataModel.setField_id(resultSet.getString("field_id"));
	                dataModel.setField_value(resultSet.getString("field_value"));;
	                dataList.add(dataModel);
	            }
			
			return dataList;
		} catch (Exception e) {
		 e.printStackTrace();
		}
		return null;
	}
	

}
