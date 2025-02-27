package egovframework.com.common.util;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

public class GridUtil {
	public static Map<String, Object> responseData(int page, int total, List list) {
		String str = """
				{
				  "result": true,
				  "data": {
				    "contents": [],
				    "pagination": {
				      "page": 1,
				      "totalCount": 100
				    }
				  }
				}
								""";
		ObjectMapper objectMapper = new ObjectMapper();

		Map<String, Object> map = null;
		try {
			map = objectMapper.readValue(str, Map.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		Map<String, Object> data = (Map) map.get("data");
		Map<String, Object> pagination = (Map) data.get("pagination");

		// 페이징처리
		pagination.put("page", page);
		pagination.put("totalCount", total);

		data.put("contents", list);
		return map;
	}
}
