package egovframework.com.vacation.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class YearVcDTO {
	private String yearVcCd;
	private String mberId;
	private String targetYear;
	private int giveDays;
	private double useDays;
}
