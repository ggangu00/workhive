package egovframework.com.common.util;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import egovframework.com.cmm.service.EgovFileMngService;
import egovframework.com.cmm.service.EgovFileMngUtil;
import egovframework.com.cmm.service.FileVO;

@Service
public class CommonFileManage {
	
	// eGov 파일관리 서비스 빈 주입 (파일 마스터/상세 등록용)
	@Resource(name = "EgovFileMngService")
	private EgovFileMngService fileMngService;
	
	// eGov 파일 관리 유틸리티
	@Resource(name = "EgovFileMngUtil")
	private EgovFileMngUtil fileMngUtil;
	
	public String fileManage(List<MultipartFile> files) {
        List<FileVO> fileVOList = null;
        String atchFileId = "";
        
        if (files != null && !files.isEmpty()) {
            try {
                // 파일을 변환하여 저장할 준비
                fileVOList = fileMngUtil.parseFileInf(files, "DSCH_", 0, "", "");

                // 파일을 DB에 저장하고 생성된 첨부파일 ID를 가져옴
                atchFileId = fileMngService.insertFileInfs(fileVOList);
                System.out.println("생성된 첨부파일 ID: " + atchFileId);
            } catch (Exception e) {
                e.printStackTrace();
                return ""; // 파일 저장 중 오류 발생 시 중단
            }
        }
        
        return atchFileId;
	}
}
