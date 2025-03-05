package egovframework.com.approval.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchDTO {
	String keyword;
	String status; // 현재결재상태
	String status1; // 현재결재상태
	String delYn;
	String formCd;
	String docKind;
	String deptNm;
	String startDate;
	String endDate;
	String deptCd;
	String docCd;
	String myDocYn;
	String mberId;
	
	int pageUnit = 5; // 한페이지 출력할 레코드 건수
	int pageSize = 10; // 페이지번호 수 (5) 1~ 5까지
	int lastPage; // 마지막 페이지번호
	int totalRecord; // 전체 레코드건수
	int page; // 현재 페이지
	int startPage; // 페이지그룹내에서 시작페이지번호
	int endPage; // 페이지그룹내에서 마지막페이지번호
	int first;
	int last;

	public int getFirst() {
		first = (getPage() - 1) * getPageUnit() + 1;
		return first;
	}

	public int getLast() {
		last = getPage() * getPageUnit();
		return last;
	}

	public int getPageUnit() {
		return pageUnit;
	}

	public void setPageUnit(int pageUnit) {
		this.pageUnit = pageUnit;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getLastPage() {
		lastPage = totalRecord / pageUnit + (totalRecord % pageUnit > 0 ? 1 : 0);
		return lastPage;
	}

	public void setLastPage(int lastPage) {
		this.lastPage = lastPage;
	}

	public int getTotalRecord() {
		return totalRecord;
	}

	public void setTotalRecord(int totalRecord) {
		this.totalRecord = totalRecord;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getStartPage() {
		startPage = (page - 1) / pageSize * pageSize + 1;
		return startPage;
	}

	public void setStartPage(int startPage) {
		this.startPage = startPage;
	}

	public int getEndPage() {
		endPage = (page - 1) / pageSize * pageSize + pageSize;
		if (endPage > getLastPage())
			endPage = getLastPage();
		return endPage;
	}

	public void setEndPage(int endPage) {
		this.endPage = endPage;
	}

	public int getStart() {
		return (page - 1) * pageUnit + 1; // 시작 번호
	}

	public int getEnd() {
		return page * pageUnit; // 끝 번호
	}

}
