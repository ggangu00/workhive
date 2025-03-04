package egovframework.com.vacation.service;

public interface YearVcService {
	public boolean yearVcInsert(YearVcDTO yearVcDTO);
	public boolean yearVcUpdate(YearVcDTO yearVcDTO);
	public YearVcDTO yearVcSelect(YearVcDTO yearVcDTO);
}
