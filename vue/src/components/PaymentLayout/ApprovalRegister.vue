<template>
  <div class="content">
    <div class="container-fluid">
      <div class="row">
          <div class='col-8'>
              <div class="card left-card">
                <!-- head -->
                  <div class="d-flex justify-content-between align-items-center">
                      <div>
                          <h4 class="card-title me-auto" >{{title}}</h4>
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
                            <input type="text" class="form-control" v-model='docKind' readonly/>
                          </div>
                          <div class="col-3">
                            <input type="text" class="form-control" v-model='formType' readonly/>
                          </div>
                          <div class="col-3">
                            <input type="text" class="form-control" v-model='deptNm' readonly/>
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
                          <input type="file" @change="addFileList($event)" multiple/>
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
                          <button class="btn btn-sm btn-danger" @click="removeFile(index)">삭제</button>
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
                <div  class="approval-box">
                  <div v-for="(approver, index) in approvers" :key="index" class="approval-item">
                      <span class='badge bg-info text-dark'>{{ getApprovalStatusName(approver.signName) }}</span>
                      [{{ approver.deptNm }}] {{ approver.mberNm }} {{ approver.gradeNm }}
                  </div>
                </div>

                <div style="height: 15px;"></div>
                <span>수신</span>
                  <div class="approval-box">
                  <div v-for="(receiver, index) in receivers" :key="index" class="approval-item">
                    <span class="badge bg-warning text-dark">수신</span>
                    <span v-if="receiver.mberNm">[{{ receiver.gradeNm }}] {{ receiver.mberNm }}</span> <!-- 사원 -->
                    <span v-else>[{{ receiver.deptNm }}]</span> <!-- 부서 -->
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
        <ApprovalLine ref="approvalLineRef"
        :approvers="approvers"
        :receivers="receivers"/>
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
    <Modal :isShowModal="isShowModal" :modalTitle="'양식 선택'" @click.self="modalClose">
    <template v-slot:body>
      <card>
        <p class="card-title mb-2">양식 목록</p>
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
                  <div class="list-nodata">등록된 양식이 없습니다.</div>
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
import Card from '../Cards/Card.vue';
import axios from '../../assets/js/customAxios.js';
import { useUserInfoStore } from '../../store/userStore.js';
import { useRouter } from 'vue-router'; 
import { applyEditorStyles } from '@/assets/js/editorStyle.js'


const router = useRouter();

const userInfoStore = useUserInfoStore();
let loginUser = userInfoStore.user ? userInfoStore.user.mberId : ""; // 로그인한 사용자 정보 가져오기
let editor ;
const route = useRoute();

//에디터객체
const editorElement = ref('');

//데이터받기
const docCd = ref("");
const docKind = ref("I01");
const formType = ref("");
const deptNm = ref("");
const docTitle = ref("");
const docCnEditor = ref("");
const fileList = ref([]);
const formFile = ref("");
const formCd = ref("");

//  ApprovalLine을 참조할 ref 생성
const approvalLineRef = ref(null);

//결재자목록 수신자목록 가져오기
const approvers = ref([]);
const receivers = ref([]);

//양식유형 가져오기
const formList =ref([]);
const formCount = ref(0);

//  Bootstrap 모달 이벤트 리스너 추가
onMounted(() => {
  const modalElement = document.getElementById('approvalRegiModal');
  if (modalElement) {
    modalElement.addEventListener('shown.bs.modal', handleModalOpen);
  }
  // 앞전 페이지에서 정보 받아옴
  docCd.value = route.query.docCd || "";
  docKind.value = route.query.docKind || "일반결재";
  formType.value = route.query.formType || "";
  deptNm.value = userInfoStore.user.deptNm;
  docTitle.value = route.query.docTitle || "";
  docCnEditor.value = route.query.docCnEditor || "";
  formCd.value = route.query.formCd || "";
  
  initEditor();//바로 에디터생성

  if(docCd.value){
    approvalList();
    receiverList();
  } 
  //param값 제거
  window.history.replaceState({}, '', route.path);
});

//에디터
const initEditor = () => {
  applyEditorStyles();
  //새로운 에디터 생성
  editor = new Editor({
    el: editorElement.value,
    height: '500px',
    initialEditType: 'wysiwyg',
    previewStyle: 'vertical',
    usageStatistics: false,
    plugins: [tableMergedCell],
    initialValue :'양식을 선택후 작성해주세요', 
  });
  
};


defineProps({
  headButtons: { type: Array, required: true },
  ApprovalButtons: { type: Boolean, default: true },
  showFile: { type: Boolean, default: true },
  title: { type: String, required: true },
});

//  모달이 열릴 때 Toast Grid를 초기화하는 함수
const handleModalOpen = () => {
  if (approvalLineRef.value) {
    approvalLineRef.value.onModalOpen();  // ApprovalLine의 onModalOpen() 실행
  }
};
 // 파일 삭제 기능
const removeFile = (index) => {
  fileList.value.splice(index, 1);
  const fileInput = document.querySelector("input[type='file']");
  if (fileInput) {
    fileInput.value = "";
  }
};

const registerApprovals = () => {
  if (approvalLineRef.value) {
    approvers.value = approvalLineRef.value.approvers;//이값으로 디비에넘기기
    receivers.value = approvalLineRef.value.receivers;
  }
};

/////////////////////수신자 가져오기/////////////////////
const receiverList = async () => {
  if (!route.query.docCd) return; // docCd가 없으면 실행하지 않음
  try {
    const response = await axios.get("/api/document/receiverList", {
      params: { docCd: route.query.docCd }
    });
    if (response.data) {
      // 수신자 목록 설정
      receivers.value = response.data.map((approver) => ({
        mberId: approver.mberId,
        mberNm: approver.mberNm,
        deptNm: approver.deptNm,
        deptCd: approver.deptCd,
        signName: approver.signName, // 상태 코드 변환
        gradeNm : approver.gradeNm
      }));
    }
  } catch (error) {
    console.error("결재선 정보 불러오기 실패:", error);
  }
};

/////////////////////결재선 가져오기/////////////////////
const approvalList = async () => {
  if (!docCd.value) return; // docCd가 없으면 실행하지 않음
  try {
    const response = await axios.get("/api/document/approvalList", {
      params: { docCd: docCd.value }
    });
    if (response.data) {
      approvers.value = response.data.map((approver) => ({
        mberId: approver.mberId,
        mberNm: approver.mberNm,
        deptNm: approver.deptNm,
        deptCd: approver.deptCd,
        signName: approver.signName, // 상태 코드 변환
      }));
    }
  } catch (error) {
    console.error("결재선 정보 불러오기 실패:", error);
  }
};

/////////////////////첨부파일/////////////////////////
// const addFileList = (target) => {
//   const newFile = Array.from(target.files);

//   fileList.value.push(...newFile);
// }
// 파일 추가 함수 수정 (event.target.files 체크 추가)
const addFileList = (event) => {
  // 이벤트가 없거나 files가 없으면 return
  if (!event || !event.target || !event.target.files) {
    console.error("파일 선택 오류: event 또는 files가 존재하지 않습니다.");
    return;
  }

  const newFiles = Array.from(event.target.files); //배열로

  newFiles.forEach((newFile) => { //중복체크
    const isDuplicate = fileList.value.some(
      (file) => file.name == newFile.name && file.size == newFile.size
    );
    if (!isDuplicate) {
      fileList.value.push(newFile);
    }
  });

  // input 필드 초기화 (같은 파일 다시 추가 가능)
  event.target.value = "";
};

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

const formGetList = async () =>{
  const formType = await axios.get('/api/document/form')
  formList.value = formType.data;
  formCount.value = formType.data.length;
}

//양신석택 모달
const formSelect = (params) => {
  formType.value = params.formType;
  formFile.value = params.formFile;
  formCd.value = params.formCd;
  editor.setHTML(formFile.value);
  isShowModal.value = false;

  console.log(editor.getHTML(formFile.value));

}

////////////////////리셋버튼/////////////////////////

const conditionReset = () => { // 정보 리셋
  formType.value = "";
  docTitle.value = "";
  docCnEditor.value = "";
  approvers.value = [];
  receivers.value = [];
  fileList.value=[];
}

//문서제목 검사
const inputCheckDoc = () => {
  if (docTitle.value.trim() == '') {
    Swal.fire({
      icon: 'warning',
      title: '필수 항목 누락',
      text: '문서 제목이 비어있습니다.'
    });
    return false;
  }
  return true;
}
//양식 검사
const inputCheckForm = () => {
  if ( formCd.value.trim() == '') {
    Swal.fire({
      icon: 'warning',
      title: '필수 항목 누락',
      text: '양식 선택 후 작성해주세요'
    });
    return false;
  }
  return true;
}


// 결재선 포멧
const approvalStatusMap = {
  "K01": "기안",
  "K02": "결재",
  "K03": "전결",
  "K04": "결정"
};

// 상태 코드 변환 함수
const getApprovalStatusName = (code) => {
  return approvalStatusMap[code] || code; // 코드가 없으면 기존 코드 그대로 표시
};

///////////////////상신버튼////////////////////////////
const approvalInfo = async() => {
  if (!inputCheckDoc()) return;
  if (!inputCheckForm()) return;
  const approvalCheck1 = approvers.value.some(a => a.signName == "K01");
  const approvalCheck4 = approvers.value.some(a => a.signName == "K04");

  if(!approvalCheck1){
    Swal.fire({
      icon: "warning",
      title: "결재선 오류",
      text: "기안자가 반드시 필요"
    });
    return;
  }
  if (!approvalCheck4) {
    Swal.fire({
      icon: "warning",
      title: "결재선 오류",
      text: "최소 한 명의 결정이 필요."
    });
    return;
  }

  //데이터담기
  const formData = new FormData();
  formData.append(
    "document", JSON.stringify({
      docTitle : docTitle.value,
      docCnEditor : editor.getHTML(),
      mberId : loginUser,
      docKind : docKind.value =="수신결재" ? "I02" : "I01",
      formCd : formCd.value,
      deptNm : deptNm.value,
      formType : formType.value
    })
  )
  //결재쟈
  formData.append("approvalLine", JSON.stringify([...approvers.value].reverse()));
  //수신자
  formData.append("reception", JSON.stringify(receivers.value));
  //첨부파일  
  fileList.value.forEach((file) => {
    if (file instanceof File) {
      formData.append("files[]", file);
    }
  });

  console.log(editor.getHTML());
  try {
    const response = await axios.post('/api/document/register', formData,
      {headers: { "Content-Type": "multipart/form-data" }});
      if(response.status == 200) {
          Swal.fire({
            icon: "success",
            title: "등록 성공",
          });
          return true;
      }else if(formCd.value == ''){
        Swal.fire({
          icon: "error",
          title: "등록 실패",
          text:  "Error : "
      });
      return false;
      }
    } catch (err) {
      Swal.fire({
          icon: "error",
          title: "등록 실패",
          text:  "Error : " + err.response.data.error
      });
      return false;
    }
    router.push({path:'/approval/pendingList'}); 
  }
defineExpose({  // modalOpen expose
  modalOpen,
  conditionReset,
  approvalInfo,
  receiverList,
  approvalList
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
    height: 95%;
    padding: 10px;
  }

</style>
