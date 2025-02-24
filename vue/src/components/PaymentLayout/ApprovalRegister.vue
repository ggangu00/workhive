<template>
  <div class="content">
    <div class="container-fluid">
      <div class="row">
          <div class='col-8'>
              <div class="card left-card">
                <!-- head -->
                  <div class="d-flex justify-content-between align-items-center">
                      <div>
                          <h4 class="card-title me-auto" >문서기안</h4>
                      </div>
                      <div>
                        <button 
                          v-for="(btn, index) in headButtons" 
                          :key="index"
                          :class="['btn', btn.class, 'btn-fill']"
                          @click="$emit('button-click', btn.label)">
                          <!-- 자식컴포넌트 클릭이벤트 -->
                          {{ btn.label }}
                        </button>
                      </div>
                  </div>
                  <!-- 제목구분선 --> 
                  <div class="divid_line"></div>
                  <!-- body -->
                  <div class="document-input-section">
                    <div class=''> 
                      <div class="d-flex justify-content-between mb-1">
                          <div class="col-3 title-box"><strong>결재종류</strong></div>
                          <div class="col-3 title-box"><strong>양식</strong></div>
                          <div class="col-3 title-box"><strong>기안부서</strong></div>
                      </div>
                      <div class="d-flex justify-content-between mb-1">
                          <div class="col-3">
                            <input type="text" class="form-control" v-model='docKind' readonly>
                          </div>
                          <div class="col-3">
                            <input type="text" class="form-control" v-model='formNm' readonly/>
                          </div>
                          <div class="col-3">
                            <input type="text" class="form-control" v-model='deptNm'>
                          </div>
                      </div>
                    </div>
                    <div class='col-12' style='margin-bottom: 20px;'>
                      <div class="d-flex justify-content-between mb-1">
                          <div class="col-12" style='padding-left: 0;'><strong>제목</strong></div>
                      </div>
                      <div class="d-flex justify-content-between mb-1">
                          <div class="col-12">
                            <input type="text" class="form-control" v-model='docTitle'>
                          </div>
                      </div>
                    </div>
                    <!-- 에디터 -->
                    <div class='col-12'>
                        <div id="editor" ref='editorElement'></div>
                    </div>
                    <div class="d-flex" style='margin-top: 20px;'>
                      <!-- 왼쪽 -->
                      <div class="col-5" v-if="showFile">
                          <strong>첨부파일</strong>
                          <br>
                          <input type="file" @change="addFileList($event.target)" multiple/>
                          <p class="file-info">개별 파일 기준 최대 30MB까지 첨부할 수 있습니다.</p>
                      </div>

                      <!-- 구분선 -->
                      <div class="col-2 d-flex justify-content-center align-items-center">
                        <div class=' file_line'></div>
                      </div>

                      <!-- 첨부된 파일 목록 -->
                      <div class="col-5 file-upload-right">
                      <strong>첨부된 파일 목록</strong>
                      <ul class="file-list">
                          <li v-for="(file, index) in fileList" :key="index">
                          {{ file.name }}
                          <button class="btn btn-sm btn-danger">삭제</button>
                          </li>
                          <li v-if="fileList.length == 0">첨부된 파일이 없습니다.</li>
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
                  <button v-if="ApprovalButtons" class="btn btn-primary btn-fill float-right" data-bs-toggle="modal"
                  data-bs-target="#approvalRegiModal">결재선지정</button>
                </div>
              </div>

              <div class="divid_line"></div>
              <div class="d-flex flex-column flex-grow-1">
               <span>결재</span>    
                <!-- 결재 목록 -->
                <div class="approval-box">
                  <div v-for="(approver, index) in reversedApprovers" :key="index" class="approval-item">
                    <span class='badge bg-info text-dark'>{{ approver.status }}</span> 
                    [{{ approver.dept }}] {{ approver.name }} {{ approver.title }}
                  </div>
                </div>
                <span>수신</span>
                  <div class="approval-box">
                  <div v-for="(receiver, index) in receivers" :key="index" class="approval-item">
                    <span class="badge bg-warning text-dark">수신</span>
                    <span v-if="receiver.name">[{{ receiver.title }}] {{ receiver.name }}</span> <!-- 사원 -->
                    <span v-else>[{{ receiver.dept }}]</span> <!-- 부서 -->
                  </div>
                </div>
              </div>
          </div>
        </div>  
      </div>
    </div>
  </div>

  <!-- 모달 시작 (결재선)  -->
<div class="modal fade" id="approvalRegiModal" tabindex="-1" aria-labelledby="approvalRegiModalLabel"
  aria-hidden="true">
  <div class="modal-dialog modal-xl modal-dialog-centered">
    <div class="modal-content">
      <!-- 모달 헤더 -->
      <div class="modal-header">
        <h5 class="modal-title" id="approvalRegiModalLabel">결재선</h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>

      <!-- 모달 바디 -->
      <div class="modal-body">
        <ApprovalLine ref="approvalLineRef" />
      </div>

      <!-- 모달 푸터 -->
      <div class="modal-footer">
        <button type="button" class="btn btn-primary btn-fill" data-bs-dismiss="modal" @click='registerApprovals'>등록</button>
        <button type="button" class="btn btn-secondary btn-fill" data-bs-dismiss="modal">닫기</button>
      </div>
    </div>
  </div>
</div>
<!-- 모달 끝 -->

    <!--양식 상세보기 모달[s]-->
    <Modal :isShowModal="isShowModal" :modalTitle="'거래처 선택'" @click.self="modalClose">
    <template v-slot:body>
      <card>
        <p class="card-title mb-2">거래처 목록</p>
        <div class="table-responsive">
          <table class="table table-hover project">
            <thead class="table-light">
              <tr>
                <th>번호</th>
                <th>양식명</th>
                <th></th>
              </tr>
            </thead>
            <tbody>
              <template v-if="formCount > 0">
                <tr :key="i" v-for="(form, i) in formList">
                  <td>{{ i + 1 }}</td>
                  <td>{{ form.formType }}</td>
                  <td>
                    <button class="btn btn-info btn-fill btn-sm mr-1" @click="formSelect(form)">선택</button>
                  </td>
                </tr>
              </template>
              <tr v-else>
                <td colspan="10">
                  <div class="list-nodata">등록된 거래처가 없습니다.</div>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </card>
    </template>

    <template v-slot:footer>
      <button type="button" class="btn btn-secondary btn-fill" @click="modalClose">닫기</button>
    </template>
  </Modal>
  <!--프로젝트 상세보기 모달[e]-->
</template>

<script setup>
import { ref, onMounted, onUnmounted, watch} from 'vue';
import '@toast-ui/editor/dist/toastui-editor.css';
import Editor from '@toast-ui/editor';
import tableMergedCell from '@toast-ui/editor-plugin-table-merged-cell';
import ApprovalLine from '../../components/PaymentLayout/ApprovalLine.vue';
import { useRoute } from 'vue-router';
import Swal from 'sweetalert2';
import Modal from '../../components/Modal.vue';
import axios from 'axios';

let editor ;
const route = useRoute();

//에디터객체

const editorElement = ref('');

//데이터받기
const docCd = ref("");
const docKind = ref("I01");
const formNm = ref("");
const deptNm = ref("");
const docTitle = ref("");
const docCnEditor = ref("");
const fileList = ref([]);
const formFile = ref("");
const formCd = ref("");

//  Bootstrap 모달 이벤트 리스너 추가
onMounted(() => {
  const modalElement = document.getElementById('approvalRegiModal');
  if (modalElement) {
    modalElement.addEventListener('shown.bs.modal', handleModalOpen);
  }

  //query값 가져오기
  // 앞전 페이지에서 정보 받아옴
  docCd.value = route.query.docCd || "";
  docKind.value = route.query.docKind || "";
  formNm.value = route.query.formNm || "";
  deptNm.value = route.query.deptNm || "";
  docTitle.value = route.query.docTitle || "";
  docCnEditor.value = route.query.docCnEditor || "";
  formCd.value = route.query.formCd || "";

  initEditor();

  //param값 제거
  window.history.replaceState({}, '', route.path);
});

//에디터
const initEditor = () => {
  //새로운 에디터 생성
  editor = new Editor({
    el: editorElement.value,
    height: '500px',
    initialEditType: 'wysiwyg',
    previewStyle: 'vertical',
    usageStatistics: false,
    plugins: [tableMergedCell]
  });

};

/////////////////////////////////
defineProps({
  headButtons: { type: Array, required: true },
  ApprovalButtons: { type: Boolean, default: true },
  showFile: { type: Boolean, default: true }
});

//  ApprovalLine을 참조할 ref 생성
const approvalLineRef = ref(null);

//  모달이 열릴 때 Toast Grid를 초기화하는 함수
const handleModalOpen = () => {
  if (approvalLineRef.value) {
    approvalLineRef.value.onModalOpen();  // ApprovalLine의 onModalOpen() 실행
  }
};

const reversedApprovers = ref([]);
//결재자목록 수신자목록 가져오기
const approvers = ref([]);
const receivers = ref([]);
const registerApprovals = () => {
  if (approvalLineRef.value) {
    reversedApprovers.value = approvalLineRef.value.reversedApprovers;  // ApprovalLine의 approvers 데이터를 가져오기(화면에 보여주는)
    approvers.value = approvalLineRef.value.approvers;
    console.log("결재자 => ", approvalLineRef.value.approvers);//이값으로 디비에넘기기
    console.log("결재자 => ", approvers.value);
    receivers.value = approvalLineRef.value.receivers;
    console.log("수신자 => ", approvalLineRef.value.receivers);
    console.log("수신자 => ", receivers.value);
  }
};

/////////////////////첨부파일/////////////////////////
const addFileList = (target) => {
  const newFile = Array.from(target.files);
  fileList.value.push(...newFile);
  console.log(fileList.value);
}



//  컴포넌트 언마운트 시 이벤트 리스너 제거 (페이지이동)
onUnmounted(() => {
    const modalElement = document.getElementById('approvalRegiModal');
    if (modalElement) {
      modalElement.removeEventListener('shown.bs.modal', handleModalOpen);
    }

    if (editor) {
    editor.destroy();
    editor = null;
  }
});

//--------------양식 모달--------------

const isShowModal = ref(false);
const modalOpen = () => { //양식 정보 모달 열기
  isShowModal.value = true;
  formGetList();
}

const modalClose = (e) => { //양식 정보 모달 닫기
  if (e.key == "Escape") {
    if (isShowModal.value) {
      isShowModal.value = !isShowModal.value
    }
  } else {
    isShowModal.value = false;
  }
}

//양식유형 가져오기
const formList =ref([]);
const formCount = ref(0);
const formGetList = async () =>{
  const formType = await axios.get('/api/document/form')

  console.log(formType.data.formCd)
  formList.value = formType.data;
  formCount.value = formType.data.length;
}

//양신석택 모달
const formSelect = (params) => {
  formNm.value = params.formType;
  formFile.value = params.formFile;
  formCd.value = params.formCd;
  editor.setHTML(formFile.value);
  isShowModal.value = false;
}

////////////////////리셋버튼/////////////////////////

const conditionReset = () => { // 정보 리셋
  formNm.value = "";
  docTitle.value = "";
  docCnEditor.value = "";

}

//const sessionId = this.$session.get('user_id');
///////////////////상신버튼////////////////////////////
const approvalInfo = async() => {
  console.log(editor.getHTML()); // 내용
  console.log(reversedApprovers.value);// 결재자정보
  console.log('결재자정보 =>',approvers.value);// 결재자정보
  console.log(receivers.value);// 수신자정보
  console.log(fileList.value);

  //결재자정보포멧
  const formatApprovalLine = approvers.value.map((approver, index)=>({
    mberId: approver.mberId,
    signSeq: index + 1,
    signName: approver.status, 
  }))

  const requestData = { 
  document:{// 서버로 보낼 데이터
      docTitle : docTitle.value,
      docCnEditor : editor.getHTML(),
      mberId : '신강현',
      docKind : 'I01',
      formCd : formCd.value,
      deptNm : deptNm.value,
      formNm : formNm.value
      },
      approvalLine: formatApprovalLine,
      reception: receivers.value,
      file: fileList.value
    };

      console.log(requestData);
      try {
         const response = await axios.post('/api/document/register', requestData);
         if(response.status == 200) {
            Swal.fire({
               icon: "success",
               title: "등록 성공",
            });

         }
      } catch (err) {
         Swal.fire({
            icon: "error",
            title: "등록 실패",
            text:  "Error : " + err.response.data.error
         });
      }

}

defineExpose({  // modalOpen expose
  modalOpen,
  conditionReset,
  approvalInfo
});

watch(()=> docCnEditor.value, async()=>{
    editor.setHTML(docCnEditor.value)
  }
);

</script>
<style scoped>
  .modal-xl {
    max-width: 70vw !important; /* 모달 가로 크기 확장 */
  }
  .modal-content {
    max-height: 80vh; /* 모달 세로 크기 제한 */
    overflow-y: auto; /* 내부 스크롤 적용 */
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
    padding: 10px;
  }
</style>
