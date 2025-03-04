package egovframework.com.uss.olp.mgt.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.egovframe.rte.fdl.idgnr.EgovIdGnrService;
import org.egovframe.rte.psl.dataaccess.util.EgovMap;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import egovframework.com.cmm.ComDefaultVO;
import egovframework.com.uss.olp.mgt.service.EgovMeetingManageService;
import egovframework.com.uss.olp.mgt.service.MeetingManageVO;
import egovframework.com.uss.olp.mgt.service.MeetingMemberVO;
/**
 * 회의관리를 처리하기 위한 ServiceImpl 구현 Class
 * @author 공통서비스 장동한
 * @since 2009.03.20
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2009.03.20  장동한          최초 생성
 *
 * </pre>
 */
@Service("egovMeetingManageService")
public class EgovMeetingManageServiceImpl extends EgovAbstractServiceImpl implements EgovMeetingManageService{

	//final private Log log = LogFactory.getLog(this.getClass());

	@Resource(name="meetingManageDao")
	private MeetingManageDao dao;

	@Resource(name="egovMgtIdGnrService")
	private EgovIdGnrService idgenService;

    /**
	 * 부서 목록을 조회한다.
	 * @param searchVO - 조회할 정보가 담긴 VO
	 * @return List
	 * @throws Exception
	 */
	@Override
	public List<EgovMap> egovMeetingManageLisAuthorGroupPopup(ComDefaultVO searchVO){
		return dao.egovMeetingManageLisAuthorGroupPopup(searchVO);
	}

    /**
	 * 아이디 목록을 조회한다.
	 * @param searchVO - 조회할 정보가 담긴 VO
	 * @return List
	 * @throws Exception
	 */
	@Override
	public List<EgovMap> egovMeetingManageLisEmpLyrPopup(ComDefaultVO searchVO){
		return dao.egovMeetingManageLisEmpLyrPopup(searchVO);
	}

    /**
	 * 회의정보 목록을 조회한다.
	 * @param searchVO - 조회할 정보가 담긴 VO
	 * @return List
	 * @throws Exception
	 */
	@Override
	public List<EgovMap> selectMeetingManageList(ComDefaultVO searchVO) throws Exception{
		return dao.selectMeetingManageList(searchVO);
	}

    /**
	 * 회의정보를 상세조회 한다.
	 * @param MeetingManageVO - 회정정보가 담김 VO
	 * @return List
	 * @throws Exception
	 */
	@Override
	public Map<String, Object> selectMeetingManageDetail(MeetingManageVO meetingManageVO) throws Exception {
	    Map<String, Object> resultMap = new HashMap<>();
	    
	    // 회의 정보 조회
	    List<EgovMap> meetingInfo = dao.selectMeetingManageDetail(meetingManageVO);
	    resultMap.put("result", meetingInfo);
	    
	    // 회의 참석자 명단 조회
	    List<EgovMap> attendeeList = dao.selectMeetingMember(meetingManageVO.getMtgId());
	    resultMap.put("list", attendeeList);
	    
	    return resultMap;
	}

    /**
	 * 회의정보를 목록 전체 건수를 조회한다.
	 * @param searchVO - 조회할 정보가 담긴 VO
	 * @return int
	 * @throws Exception
	 */
	@Override
	public int selectMeetingManageListCnt(ComDefaultVO searchVO) throws Exception{
		return dao.selectMeetingManageListCnt(searchVO);
	}

    /**
	 * 회의정보를 등록한다.
	 * @param searchVO - 조회할 정보가 담긴 VO
	 * @throws Exception
	 */
	@Override
	public void insertMeetingManage(MeetingManageVO meetingManageVO) throws Exception {
		String sMakeId = idgenService.getNextStringId();

		meetingManageVO.setMtgId(sMakeId);

		dao.insertMeetingManage(meetingManageVO);
	}
	
	//회의 정보/참여자 등록
	@Transactional
    public boolean saveMeeting(MeetingManageVO meetingManageVO) throws Exception {
		 try {
			 String sMakeId = idgenService.getNextStringId();

			meetingManageVO.setMtgId(sMakeId);

			dao.insertMeetingManage(meetingManageVO);

	        // 작업 리스트가 존재할 경우
	        if (meetingManageVO.getMemberArr() != null && !meetingManageVO.getMemberArr().isEmpty()) {
	            
	            for (MeetingMemberVO mem : meetingManageVO.getMemberArr()) {	            	
	            	mem.setMtgId(sMakeId);  // 각 작업 항목에 mtgId 설정
	            	
	                dao.insertMeetingMember(mem);  // 개별 INSERT 실행
	            }
	        }
	        return true; 
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
    }
	

    /**
	 * 회의정보를 수정한다.
	 * @param searchVO - 조회할 정보가 담긴 VO
	 * @throws Exception
	 */
	@Override
	public void updateMeetingManage(MeetingManageVO meetingManageVO){
		dao.updateMeetingManage(meetingManageVO);
	}

    /**
	 * 회의정보를 삭제한다.
	 * @param searchVO - 조회할 정보가 담긴 VO
	 * @throws Exception
	 */
	@Override
	public void deleteMeetingManage(MeetingManageVO meetingManageVO){
		dao.deleteMeetingManage(meetingManageVO);
	}

}
