package egovframework.com.authority.service;

import java.util.Date;

import javax.validation.constraints.NotBlank;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthorityDTO {
	
	private String authorityCd;
	
	@NotBlank
	private String authorityNm;
	private String description;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date createDt;
	
	@NotBlank
	private String createId;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date updateDt;
	
	private String updateId;
	
	private String delYn;
}
