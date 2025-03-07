// editorStyle.js

// 공통 스타일 적용 함수
export const applyEditorStyles = () => {
    // 기존 스타일이 추가되어 있는지 확인 후 중복 방지
    if (document.getElementById("custom-editor-styles")) return;
  
    const styleElement = document.createElement("style");
    styleElement.id = "custom-editor-styles";
    styleElement.innerHTML = `
      .toastui-editor-contents {
        margin-top: 30px;
        border: 2px solid #ddd;
        padding: 30px;
        background-color: #fff;
        font-family: 'Arial', sans-serif;
        color: #333;
        border-radius: 8px;
        box-shadow: 2px 2px 10px rgba(0, 0, 0, 0.1);
      }
      
      .toastui-editor-contents table {
        width: 100%;
        border-collapse: collapse;
        margin-top: 10px;
        border: 1.5px solid #bbb;
      }
  
      .toastui-editor-contents thead td {
        background-color:rgb(163, 163, 163);
        color:  #f8f9fa;
        font-weight: bold;
        text-align: center;
        padding: 12px;
        border: 1.5px solid #bbb;
      }
  
      .toastui-editor-contents td {
        border: 1.5px solid #bbb;
        padding: 12px;
        text-align: left;
        font-size: 14px;
      }
  
      .toastui-editor-contents strong {
        font-weight: bold;
        font-size: 18px;
        color: #222;
        display: block;
        margin-bottom: 10px;
        text-align: center;
      }
  
      .toastui-editor-contents p {
        margin: 5px 0;
        font-size: 14px;
      }
  
      .toastui-editor-contents tbody tr:nth-child(even) {
        background-color: #f8f9fa;
      }
  
      .toastui-editor-contents tbody tr:hover {
        background-color: #ddd;
      }
  
      .toastui-editor-contents td, .toastui-editor-contents th {
        line-height: 1.5;
      }
  
      .toastui-editor-contents .section-header {
        background-color: #f1f1f1;
        font-weight: bold;
        text-align: center;
        padding: 12px;
        border: 1.5px solid #bbb;
      }
    `;
    document.head.appendChild(styleElement);
  };
  