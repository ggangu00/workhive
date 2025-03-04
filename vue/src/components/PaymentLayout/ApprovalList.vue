<template>
  <div class="content">
    <div class="container-fluid">
      <div class="row">
        <div class="col-12">
          <div class="card">
            <!-- 버튼 & 필터 -->
            <div class="button-collection d-flex justify-content-between align-items-center flex-wrap" style="padding: 15px;">
              <div class="d-flex">
                <button
                  v-for="(btn, index) in buttons"
                  :key="index"
                  :class="['btn', btn.class]"
                  @click="$emit('button-click', btn.label)">
                  {{ btn.label }}
                </button>
              </div>

              <div class="selectbox d-flex">
                <select class="form-select w10" name="doc_kind" v-model="docKind">
                  <option v-for="(data, idx) in selectedData"
                  :key="idx"
                  :value="data.commDtlCd">
                  {{ data.commDtlNm }}
                  </option>
                </select>
                <select class="form-select w10" name="dept_nm" v-model="deptNm">
                  <option v-for="(data, idx) in selectedDeptData"
                  :key="idx"
                  :value="data.deptNm">
                  {{ data.deptNm }}
                  </option>
                </select>
                <select class="form-select w10" name="form_cd" v-model="formType">
                  <option v-for="(data, idx) in selectedFormData"
                  :key="idx"
                  :value="data.formCd">
                  {{ data.formType }}
                  </option>
                </select>
                <div class="d-flex">
                  <div class="input-group">
                    <span class="input-group-text fw-bold">기안일(시작)</span>
                    <input type="date" class="form-control" v-model="startDate">
                  </div>
                  <span class="fw-bold">~</span>
                  <div class="input-group">
                    <span class="input-group-text fw-bold">기안일(종료)</span>
                    <input type="date" class="form-control" v-model="endDate">
                  </div>
                </div>

                <button class="btn btn-secondary btn-fill" @click="resetBtn">초기화</button>
              </div>
            </div>
          </div>
        </div>

        <!-- Toast UI Grid 영역 -->
        <div class="card">
          <div class="card-body">
            <div class="col-12">
              <div id="tableGrid" class="toastui"></div>
              <!-- <div id="pagination" class="tui-pagination"></div> -->
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from "vue";
import { useRouter } from "vue-router";
import "tui-grid/dist/tui-grid.css";
import axios from "../../assets/js/customAxios.js";
import Swal from 'sweetalert2';
import { useUserInfoStore } from '../../store/userStore.js';


const userInfoStore = useUserInfoStore();
let loginUser = userInfoStore.user ? userInfoStore.user.mberId : ""; // 로그인한 사용자 정보 가져오기

// Props 정의
const props = defineProps({
  buttons: { type: Array, required: true },
  columnDefs: { type: Array, required: true }, // Column 정의
  status: { type: String, required: true }, // 현재 진행 상태
  status1: { type: String, required: true }, // 현재 진행 상태
});

// Vue Router 사용
const router = useRouter();
const page = ref(1);

// Grid 및 필터 설정
const grid = ref(null);
const docKind = ref('');
const deptNm = ref('전체');
const formType = ref('');
const rowData = ref([]);
const startDate = ref('');
const endDate = ref('');

//셀렉트박스
const selectedData = ref([]); //문서종류 셀렉트박스
const selectedDeptData =ref([]); //부서종류 셀렉트박스
const selectedFormData =ref([]); //문서양식종류 셀렉트박스

//공통코드 가져오기
const commonDtlList = async () =>{
  const docKind = await axios.get(`/api/comm/codeList`, {
    params: {cd:'DK'}
  });
  selectedData.value = [{ commDtlCd: "", commDtlNm: "전체" } ,...docKind.data]
}
//부서명 가져오기
const deptList = async () =>{
   try {
      const deptNm = await axios.get('/api/department')
      selectedDeptData.value=[{ deptNm: "전체" } , ...deptNm.data]
   } catch (err) {
      Swal.fire({
         icon: "error",
         title: "조회 실패",
         text:  "Error : " + err.response.data.error
      });
   }


}
//양식유형 가져오기
const formList = async () =>{
   const formType = await axios.get('/api/document/form')
   selectedFormData.value=[{ formCd:'', formType: "전체"}, ...formType.data]
}
//초기화 버튼
const resetBtn = () =>{
  deptNm.value = "전체";  // 부서 선택 초기화
  docKind.value = "";       // 문서 종류 초기화
  startDate.value = "";     // 날짜 초기화
  endDate.value = "";       // 날짜 초기화
  formType.value = "";
}

//아이디정보 불러오서 밑에 있는 getparams에 아이디값 뿌려주기

// API 요청 파라미터
const getParams = ({
  status: props.status,
  status1: props.status1,
  deptNm: '',
  docKind: '',
  formCd: '',
  startDate: '',
  endDate: '',
  mberId:loginUser,
});

const token = localStorage.getItem("token");
const dataSource = {
  api: {
    readData: {
      url: "/api/document/list",
      method: "GET",
      initParams: getParams, // 페이지, 상태코드(미결, 반려, 진행완료)
      headers: {
      'Authorization': `Bearer ${token}`  // 백틱 사용
      },
    },
  },
}

//승인 버튼 기능(다중)
const btnSelectChange = () => {
  const checkedData = grid.value.getCheckedRows();

  Swal.fire({
    title: "결재 진행",
    text: "승인하시겠습니까? 아니면 반려하시겠습니까?",
    showDenyButton: true,
    showCancelButton: true,
    confirmButtonText: "승인",
    denyButtonText: "반려"
  }).then(async (result) => {
    let modeText = '';
    let newSignStat = '';
    if (result.isConfirmed) {
      modeText = "승인";
      newSignStat = "D02";
    } else if (result.isDenied) {
      modeText = "반려";
      newSignStat = "D04";
    } else {
      return; // 취소 시 함수 종료
    }

    try {
      const response = await axios.put(`/api/document/state`, {
        approvalArr: checkedData.map(row => row.docCd),
        mberId: loginUser, // 실제 로그인 아이디로 변경
        signStat: newSignStat
      });

      if (response.statusText == "OK") {
        Swal.fire({
          icon: "success",
          title: modeText + " 완료",
          text: "선택한 문서를 " + modeText + "하였습니다",
        });
      }
    } catch (err) {
      Swal.fire({
        icon: "error",
        title: modeText + " 실패",
        text: "Error : " + err,
      });
    }
  });
}
/////////////////////////다중회수///////////////////////////
const reteriveBtn = () => {
  const checkedData = grid.value.getCheckedRows();

  Swal.fire({
    title: "회수 진행",
    text: "회수 하시겠습니까?",
    showDenyButton: false,
    showCancelButton: true,
    confirmButtonText: "회수",
  }).then(async (result) => {
    let modeText = '';
    if (result.isConfirmed) {
      modeText = "회수";
      try {
      const response = await axios.put(`/api/document/reteriveState`, {
        retrieveArr: checkedData.map(row => row.docCd),
      });

      if (response.statusText == "OK") {
        Swal.fire({
          icon: "success",
          title: modeText + " 완료",
          text: "선택한 문서를 " + modeText + "하였습니다",
        });
      }
    } catch (err) {
      Swal.fire({
        icon: "error",
        title: modeText + " 실패",
        text: "Error : " + err,
      });
    }
    } else {
      return; // 취소 시 함수 종료
    }
  });
}
///////////////////////////////////////////////////////////
const TueGrid = () => {
  grid.value = new window.tui.Grid({
    el: document.getElementById("tableGrid"),
    scrollX: true,
    scrollY: true,
    columns: props.columnDefs,
    rowHeaders: ["checkbox"],
    pageOptions: {
      useClient: false, // 서버 사이드 페이지네이션 사용
      perPage: 10,
    },
    data: dataSource,
  });

  // 행 클릭 이벤트 추가
  grid.value.on("click", handleRowClick);
};

// 행 클릭 이벤트 핸들러
const handleRowClick = (e) => {
  if (!grid.value || e.rowKey == null || e.rowKey == undefined ) return;
  const dataRow = grid.value.getRow(e.rowKey);
  if (e.nativeEvent.target.type == "checkbox") {
    console.log("체크박스 클릭 감지, 행 클릭 이벤트 무시");
    return;
  }

  let routePath ='';

  // 특정 조건일 때 페이지 이동
  if (dataRow?.crntSignStat == "반려") {
    routePath = "/approval/rejectedInfo"
  }else if (dataRow?.crntSignStat == "완료") {
    routePath = "/approval/completedInfo";
  }else if (dataRow?.crntSignStat == "미결") {
    routePath = "/approval/pendingInfo"
  }else if (dataRow?.crntSignStat == "진행중") {
    routePath = "/approval/proceedInfo"
  }else if (dataRow?.crntSignStat == "회수"){
    routePath = "/approval/restartDraft"
  }

  console.log(dataRow)
  router.push({
    path: routePath,
    query :{
      docCd : dataRow.docCd,
      docKind : dataRow.docKind,
      formType : dataRow.formType,
      formCd : dataRow.formCd,
      deptNm : dataRow.deptNm,
      docTitle : dataRow.docTitle,
      docCnEditor : dataRow.docCnEditor,
      atchFileId : dataRow.atchFileId,
    }
  });
};

onMounted(() => {
  TueGrid(); // Grid 초기화 실행
  commonDtlList(); //공통코드
  deptList(); //부서코드
  formList(); //문서유형
});

//문서 유형 셀렉트박스 변경시 필터 감지하여 재로딩
watch([docKind, deptNm, formType, startDate, endDate], async ([newDodKind, newDeptNm, newFormType, newStartDate, newEndDate]) => {
    const response = await axios.get("/api/document/list", { params: {
      docKind : newDodKind,
      deptNm : newDeptNm == "전체" ? "" : newDeptNm,
      formCd : newFormType,
      startDate : newStartDate,
      endDate : newEndDate,
      perPage: 15,
      page: page.value,
      status : props.status,
      status1 : props.status1,
      mberId : loginUser
    }
  });

    rowData.value = [...response.data?.data?.contents ?? [], {...response.data?.data?.pagination ?? []}];
    if (grid.value) {
      grid.value.resetData(rowData.value);
    }
});

defineExpose({btnSelectChange, reteriveBtn})
</script>

<style scoped>
.button-collection button {
  margin-right: 10px;
}
.selectbox select {
  margin-right: 10px;
}
.toastui {
  width: 100%;
  height: 95%;
}
</style>
