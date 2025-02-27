package egovframework.com.securing.service;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.Email;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class UserDTO {

    private Long id;
    //private String fullName;
    //private String deptName;

    private List<String> roles;

    private String mberId;
    private String password;
    private String password_hint;
    private String password_cnsr;
    private String ihidnum;
    private String mberNm;
    private String zip;
    private String adres;
    private String areaNo;
    private String mberSttus;
    private String detailAdres;
    private String endTelno;
    private String mbtlnum;
    private String groupId;
    private String mberFxnum;
    private String mberEmailAdres;
    private String meddleTelno;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date sbscrbDe;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date quitDt;
    private String sexdstnCode;
    private String esntlId;
    private String lockAt;
    private int lockCnt;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private String lockLastPnttm;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createDt;
    private String createId;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date update_dt;
    private String updateId;
    private String delYn;
    private String respCd;
    private String respNm;
    private String gradeCd;
    private String deptCd;
    private String deptNm;

    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String userEmail;
    private String userAdres;
    private String userZip;
    private String userNm;
    private String userId;
    
}