package egovframework.com.common.util;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import egovframework.com.cmm.service.EgovFileMngService;
import egovframework.com.cmm.service.EgovFileMngUtil;
import egovframework.com.cmm.service.FileVO;

/**
 * 공통 파일 관리 유틸리티 클래스 eGovFramework의 파일 관리 서비스와 유틸리티를 활용하여 파일을 저장하고, 첨부파일 ID를
 * 반환하는 역할을 수행
 */
@Service
public class CommonFileManage {

	// eGov 파일관리 서비스 빈 주입 (파일 마스터/상세 등록용)
	@Resource(name = "EgovFileMngService")
	private EgovFileMngService fileMngService;

	// eGov 파일 유틸리티 (파일 파싱 및 저장 처리)
	@Resource(name = "EgovFileMngUtil")
	private EgovFileMngUtil fileMngUtil;

	/**
	 * 파일 리스트를 받아 eGov에서 관리하는 형식으로 저장 후, 첨부파일 ID 반환
	 * 
	 * @param files 업로드할 MultipartFile 리스트
	 * @return atchFileId (저장된 파일의 첨부파일 ID, 오류 발생 시 빈 문자열 반환)
	 */
	public String fileManage(List<MultipartFile> files) {
        List<FileVO> fileVOList = null; // 변환된 파일 정보 리스트
        String atchFileId = "";         // 저장된 첨부파일 ID (파일 그룹 키 역할)

        // 파일 리스트가 비어있지 않으면 처리 시작
		if (files != null && !files.isEmpty()) {
			try {
                // 파일을 eGov 파일 정보 객체로 변환
                // 파라미터 설명
                // 1. files : 업로드된 파일 리스트
                // 2. "DSCH_" : 파일 prefix (커스텀 가능)
                // 3. 0 : 파일 순번 (일괄 업로드 시 보통 0부터 시작)
                // 4. "" : 기존 atchFileId (신규 저장 시 공백)
                // 5. "" : 저장경로 (특정 경로 지정 필요 시 입력)
				fileVOList = fileMngUtil.parseFileInf(files, "DSCH_", 0, "", "");

				// 변환된 파일 정보 리스트를 DB에 저장하고 첨부파일 ID 반환
				atchFileId = fileMngService.insertFileInfs(fileVOList);
				
			} catch (Exception e) {
				e.printStackTrace();
				return ""; // 파일 저장 중 오류 발생 시 중단
			}
		}
		
		// 저장된 첨부파일 ID 반환 (정상 처리된 경우)
		return atchFileId;
	}
}
