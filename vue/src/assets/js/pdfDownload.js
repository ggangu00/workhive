import jsPDF from "jspdf";
import html2canvas from "html2canvas";

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
  document.body.appendChild(content); // 임시 추가

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
};
