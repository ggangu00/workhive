package egovframework.com.member.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.com.member.mapper.MemberMapper;
import egovframework.com.member.service.MemberService;
import egovframework.com.securing.service.UserDTO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("MemberService")
public class MemberServiceImpl implements MemberService{
	
	@Resource
	private MemberMapper memberMapper;
	
	//사원 전체조회
	@Override
	public List<UserDTO> memberSelectAll() {
		return memberMapper.memberSelectAll();
	}

}
