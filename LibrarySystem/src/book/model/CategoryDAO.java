package book.model;

import java.util.List;
import java.util.Map;

public interface CategoryDAO {

	// 카테고리 목록 가져오기
	List<Map<String, String>> getCategories();

}
