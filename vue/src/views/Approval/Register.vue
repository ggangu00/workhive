<template>
    <div class="content">
      <div class="container-fluid">
        <div class="row">
            <div class='col-8'>
                <div class="card left-card">
                  <!-- head -->
                    <div class="button-collection d-flex justify-content-between align-items-center flex-wrap" style='padding: 5px ;'>
                        <div class='d-flex'>
                            <h4 class="card-title">문서기안</h4>
                        </div>
                        <div class='selectbox d-flex'>
                            <button class="btn btn-primary btn-fill">양식선택</button>
                            <button class="btn btn-warning btn-fill">초기화</button>
                            <button class="btn btn-success">결재문서첨부</button>
                            <button class="btn btn-success">상신</button>
                        </div>
                    </div>
                    <!-- 제목구분선 -->
                    <div class="divid_line"></div>
                    <!-- body -->
                    <div class="document-input-section">
                      <div class='col-6'>
                        <div class="d-flex justify-content-between mb-1">
                            <div class="col-3 title-box"><strong>결재종류</strong></div>
                            <div class="col-3 title-box"><strong>양식</strong></div>
                            <div class="col-3 title-box"><strong>기안부서</strong></div>
                        </div>
                        <div class="d-flex justify-content-between mb-1">
                            <div class="col-3 input-box">일반결재</div>
                            <div class="col-3 input-box">지출 결의서</div>
                            <div class="col-3 input-box">총무팀</div>
                        </div>
                      </div>
                      <div class='col-12' style='margin-bottom: 20px;'>
                        <div class="d-flex justify-content-between mb-1">
                            <div class="col-12" style='padding-left: 0;'><strong>제목</strong></div>
                        </div>
                        <div class="d-flex justify-content-between mb-1">
                            <div class="col-12 input-box">test20250208</div>
                        </div>
                      </div>
                      <!-- 에디터 -->
                      <div class='col-12'>
                          <div id="editor"></div>
                      </div>
                      <div class="d-flex" style='margin-top: 20px;'>
                        <!-- 왼쪽 -->
                        <div class="col-5">
                            <strong>첨부파일</strong>
                            <button class="btn btn-primary file-upload-btn d-flex">파일선택</button>
                            <p class="file-info">개별 파일 기준 최대 30MB까지 첨부할 수 있습니다.</p>
                        </div>

                        <!-- 구분선 -->
                        <div class="col-2 d-flex justify-content-center align-items-center">
                          <div class=' file_line'></div>
                        </div>

                        <!-- 첨부된 파일 목록 -->
                        <div class="col-5 file-upload-right">
                            <ul class="file-list">
                                <li> 첨부2.JPG (60KB)</li>
                                <li> 첨부2.JPG (60KB)</li>
                            </ul>
                        </div>
                    </div>
                  </div>
                </div>
            </div>
          <!-- 우측 결재선 -->
          <div class="col-4">
            <div class='card right-card'>
                <div class="button-collection d-flex justify-content-between align-items-center flex-wrap" style='padding: 5px ;'>
                  <div class='d-flex'>
                    <h4 class="card-title">결재선</h4>
                  </div>
                  <div class='selectbox d-flex'>
                    <button class="btn btn-primary btn-fill float-right" data-bs-toggle="modal"
                    data-bs-target="#organizationModal">결재선지정</button>
                  </div>
                </div>

                <div class="divid_line"></div>
                <div class="d-flex flex-column flex-grow-1">
                 <span>결재</span>    
                    <div class="approval-box flex-grow-1">                    
                      <div class="approval-item"><span class='badge bg-info text-dark'>결정</span> [총무팀] 홍길동 팀장</div>
                      <div class="approval-item"><span class='badge bg-info text-dark'>결정</span> [인사팀] 홍길순 과장</div>
                      <div class="approval-item"><span class='badge bg-info text-dark'>기안</span> [영업팀] 홍길권 대리</div>
                    </div>
                  <span>수신</span>
                    <div class="approval-box flex-grow-1">                      
                      <div class="approval-item"><span class="badge bg-warning text-dark">수신</span> [총무팀]</div>
                      <div class="approval-item"><span class="badge bg-warning text-dark">수신</span> [인사팀]</div>
                      <div class="approval-item"><span class="badge bg-warning text-dark">수신</span> [영업팀]</div>
                    </div>
                </div>
            </div>
          </div>  
        </div>
      </div>
    </div>

    <!-- 모달 시작 -->
  <div class="modal fade" id="organizationModal" tabindex="-1" aria-labelledby="organizationModalLabel"
    aria-hidden="true">
    <div class="modal-dialog modal-xl modal-dialog-centered">
      <div class="modal-content">
        <!-- 모달 헤더 -->
        <div class="modal-header">
          <h5 class="modal-title" id="organizationModalLabel">결재선</h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>

        <!-- 모달 바디 -->
        <div class="modal-body">
          <ApprovalLine/>
        </div>

        <!-- 모달 푸터 -->
        <div class="modal-footer">
          <button type="button" class="btn btn-primary btn-fill" data-bs-dismiss="modal">등록</button>
          <button type="button" class="btn btn-secondary btn-fill" data-bs-dismiss="modal">닫기</button>
        </div>
      </div>
    </div>
  </div>
  <!-- 모달 끝 -->
  </template>
  <script>
    import '@toast-ui/editor/dist/toastui-editor.css';
    import { Editor } from '@toast-ui/editor';
    import ApprovalLine from '../../components/PaymentLayout/ApprovalLine.vue';
    export default {
      components: {
        ApprovalLine
      },
      data () {
        return {
        
        }
      },
      mounted() {
       this.initEditor();
      },
      methods: {
          initEditor() {
            this.editor = new Editor({
              el: document.querySelector('#editor'),
              height: '500px',
              initialEditType: 'WYSIWYG',
              previewStyle: 'vertical'
            });
          }
        }
      };
    
    
  </script>
  <style>
    .modal-xl {
      max-width: 70vw !important; 
    }
    .modal-content {
      max-height: 80vh; 
      overflow-y: auto; 
    }
    .button-collection button{
        margin-right: 10px;
    }
    .selectbox select{
        margin-right: 10px;
    }
    .divid_line{
      width: 99%;
      height: 2px;
      background-color: #e2e2e2;
      margin: 10px auto;
    }
    .input-box{
      background-color: #e2e2e2;
      padding: 0px;
    }
    .title-box{
      padding: 0px;
    }
    .file_line{
      width: 2px;
      height: 90%;
      background-color: #ccc;
      margin-top: 5px;
      margin-bottom: 5px;
    }
    .approval-box{
      border: 1px solid #ccc;
      padding: 10px;
      background-color: #fafafa;
    }
    .left-card, .right-card {
    height: 100%;
}
  </style>
  