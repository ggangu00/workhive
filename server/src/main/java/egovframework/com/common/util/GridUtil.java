package egovframework.com.common.util;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Grid 형태의 응답 데이터를 구성하는 유틸 클래스 
 * - 페이지네이션 정보 및 조회 결과 데이터를 포함한 응답 구조 반환
 */
public class GridUtil {

	/**
	 * Grid 형태의 응답 데이터를 구성하여 반환
	 * 
	 * @param page  현재 페이지 번호
	 * @param total 전체 데이터 개수
	 * @param list  실제 조회된 데이터 리스트
	 * @return 응답 데이터 Map (페이지네이션 정보와 데이터 리스트 포함)
	 */
	public static Map<String, Object> responseData(int page, int total, List list) {
		
		// 기본 응답 템플릿 (JSON 문자열 형태)
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
		
		// JSON 파싱을 위한 ObjectMapper 객체 생성
		ObjectMapper objectMapper = new ObjectMapper();

		Map<String, Object> map = null;
		
		try {
			// 기본 응답 템플릿을 Map 형태로 변환
			map = objectMapper.readValue(str, Map.class);
		} catch (Exception e) {
			e.printStackTrace(); // 파싱 중 예외 발생 시 스택 트레이스 출력
		}
		
		// data 영역과 pagination 영역 추출
		Map<String, Object> data = (Map<String, Object>) map.get("data");
		Map<String, Object> pagination = (Map<String, Object>) data.get("pagination");

        // 실제 페이징 정보 설정
        pagination.put("page", page);          // 현재 페이지 번호
        pagination.put("totalCount", total);    // 전체 데이터 개수

        // 조회된 데이터 리스트 설정
        data.put("contents", list);

        // 구성된 최종 응답 데이터 반환
        return map;
	}
}
