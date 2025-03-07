import jsPDF from "jspdf";
import html2canvas from "html2canvas";

// HTML 요소에 인라인 스타일 적용 함수
const applyInlineStyles = (element) => {
  element.style.marginTop = "30px";
  element.style.border = "2px solid #ddd";
  element.style.padding = "30px";
  element.style.backgroundColor = "#fff";
  element.style.fontFamily = "Arial, sans-serif";
  element.style.color = "#333";
  element.style.borderRadius = "8px";
  element.style.boxShadow = "2px 2px 10px rgba(0, 0, 0, 0.1)";

  // 테이블 스타일 적용
  const tables = element.querySelectorAll("table");
  tables.forEach((table) => {
    table.style.width = "100%";
    table.style.borderCollapse = "collapse";
    table.style.marginTop = "10px";
    table.style.border = "1.5px solid #bbb";
  });

  // 테이블 헤더 스타일
  const theadCells = element.querySelectorAll("thead td");
  theadCells.forEach((td) => {
    td.style.backgroundColor = "#575757";
    td.style.color = "white";
    td.style.fontWeight = "bold";
    td.style.textAlign = "center";
    td.style.padding = "12px";
    td.style.border = "1.5px solid #bbb";
  });

  // 테이블 본문 스타일
  const tds = element.querySelectorAll("td");
  tds.forEach((td) => {
    td.style.border = "1.5px solid #bbb";
    td.style.padding = "12px";
    td.style.textAlign = "left";
    td.style.fontSize = "14px";
  });

  // 강조 표시
  const strongs = element.querySelectorAll("strong");
  strongs.forEach((strong) => {
    strong.style.fontWeight = "bold";
    strong.style.fontSize = "18px";
    strong.style.color = "#222";
    strong.style.display = "block";
    strong.style.marginBottom = "10px";
    strong.style.textAlign = "center";
  });

  // 본문 텍스트 스타일
  const paragraphs = element.querySelectorAll("p");
  paragraphs.forEach((p) => {
    p.style.margin = "5px 0";
    p.style.fontSize = "14px";
  });

  // 배경색 구분 (짝수 행)
  const evenRows = element.querySelectorAll("tbody tr:nth-child(even)");
  evenRows.forEach((tr) => {
    tr.style.backgroundColor = "#f8f9fa";
  });

  // 마우스 오버 효과
  const rows = element.querySelectorAll("tbody tr");
  rows.forEach((tr) => {
    tr.addEventListener("mouseenter", () => {
      tr.style.backgroundColor = "#ddd";
    });
    tr.addEventListener("mouseleave", () => {
      tr.style.backgroundColor = "";
    });
  });
};

// PDF 생성 함수
export const generatePDF = async (htmlContent, fileName = "document") => {
  if (!htmlContent || htmlContent.trim() === "") {
    alert("다운로드할 내용이 없습니다.");
    return;
  }

  // HTML 내용을 동적으로 div에 삽입
  const content = document.createElement("div");
  content.innerHTML = htmlContent;
  content.style.width = "100%";
  content.style.maxWidth = "800px";
  content.style.padding = "20px";
  content.style.background = "white";
  content.classList.add("pdf-content"); // PDF 전용 스타일 클래스 추가

  // 인라인 스타일 적용
  applyInlineStyles(content);

  document.body.appendChild(content); // 임시 추가

  // 스타일 반영을 위해 약간의 딜레이를 줌
  setTimeout(async () => {
    try {
      // HTML을 이미지로 변환
      const canvas = await html2canvas(content, {
        scale: 3, // 해상도 높이기
        useCORS: true, // CORS 이슈 방지
      });

      const imgData = canvas.toDataURL("image/png");
      const pdf = new jsPDF("p", "mm", "a4");

      // PDF 크기 설정
      const pdfWidth = pdf.internal.pageSize.getWidth();
      const pdfHeight = (canvas.height * pdfWidth) / canvas.width;

      pdf.addImage(imgData, "PNG", 0, 0, pdfWidth, pdfHeight);
      pdf.save(`${fileName}.pdf`);
    } catch (error) {
      console.error("PDF 변환 오류:", error);
      alert("PDF 변환 중 오류가 발생했습니다.");
    } finally {
      document.body.removeChild(content); // 임시 추가한 div 제거
    }
  }, 300); // 0.3초 딜레이 후 실행
};
