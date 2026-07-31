package book.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dbconnection.ProjectDBConnection;

public class CategoryDAO_imple implements CategoryDAO {

	private Connection conn = ProjectDBConnection.getConn(); 
	private PreparedStatement pstmt;   
	private ResultSet rs;
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	// 카테고리 목록 가져오기
	@Override
	public List<Map<String, String>> getCategories() {
		List<Map<String, String>> resultList = new ArrayList<>();
		
		String sql = " SELECT CATEGORY_ID, CATEGORY_NAME FROM TBL_CATEGORY ";
		
		try {
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				Map<String, String> map = new HashMap<>();
				
				map.put("CATEGORY_ID", rs.getString("CATEGORY_ID"));
				map.put("CATEGORY_NAME", rs.getString("CATEGORY_NAME"));
				
				resultList.add(map);
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return resultList;
	}

}
