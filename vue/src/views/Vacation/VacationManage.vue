<template>
  <!-- 휴가 신청 등록/수정 -->
  <div class="row mt-5">
    <div class="col">

      <div class="row">
        <div class="col header">
          <h4 v-if="!isUpdate">  [ 휴가 신청 등록 ]</h4>
          <h4 v-else>  [ 휴가 신청 수정 ]</h4>
          <hr>
        </div>
      </div>

      <!-- 테이블 -->
      <div class="table-responsive">
        <table class="table" style="table-layout: fixed;">
          <colgroup>
            <col style="width: 16.6%;">
            <col style="width: 16.6%;">
            <col style="width: 16.6%;">
            <col style="width: 16.6%;">
            <col style="width: 16.6%;">
            <col style="width: 16.6%;">
          </colgroup>
          <tbody>
            <tr>
              <th>휴가 시작일</th>
              <td colspan="2"><input type="date" id="startDate" class="form-control" v-model="vcData.vcStartDt"></td>
              <th>휴가 종료일</th>
              <td colspan="2">
                <input type="date" id="startDate" class="form-control"
                       v-model="vcData.vcEndDt" :readonly="vcData.vcType === 'E02' || vcData.vcType === 'E03'">
              </td>
            </tr>
            <tr>
              <th>휴가 종류</th>
              <td>
                <select class="form-select" aria-label="Default select example" v-model="vcData.vcType">
                  <option value="E01" selected>연차</option>
                  <option value="E02">오전반차</option>
                  <option value="E03">오후반차</option>
                  <option value="E04">공가</option>
                </select>
              </td>
              <th>사용 일수</th>
              <td><input type="text" class="form-control" v-model="vcData.useDays"></td>
              <th>예상 잔여 일수</th>
              <td><input type="text" class="form-control" v-model="vcData.remainDays" readonly></td>
            </tr>
            <tr>
              <th>휴가 사유</th>
              <td colspan="5"><textarea class="form-control" v-model="vcData.vcReason"></textarea></td>
            </tr>
            <tr>
              <th>파일 첨부</th>
              <td colspan="5">
                <div class="form-control custom-file-div">
                  <label class="btn btn-fill cell-btn-custom" for="inputFile">파일선택</label>
                  <a>{{ (fileList.length == 0) ? "선택된 파일 없음" : `파일 ${fileList.length}개` }}</a>
                  <p class="file-info">개별 파일 기준 최대 30MB까지 첨부할 수 있습니다.</p>
                  <input type="file" id="inputFile" style="display: none;" @change="addFileList($event.target)" multiple>
                  <hr>
                  <div class="row file-list">
                    <div class="col-4" v-for="(file, index) in fileList" :key="index">
                    {{ file.name }}
                    <button class="btn btn-sm btn-danger cell-btn-custom" @click="removeFile(index)">삭제</button>
                    </div>
                    <div class="col" v-if="fileList.length == 0">첨부된 파일이 없습니다.</div>
                  </div>
                </div>
              </td>
            </tr>
            <tr>
              <th>결재자</th>
              <td colspan="5"><input type="text" class="form-control" v-model="vcData.signNm" @click="modalOpen" readonly></td>

              <SignerModal
                :isShowModal="isShowModal"
                @modalClose="modalClose"
                @modalConfirm="modalConfirm"
              />
            </tr>

          </tbody>
        </table>
      </div>

      <div class="row justify-content-center">
        <button class="btn btn-secondary btn-fill" @click="btnVcManageCancle">취소</button>
        <button class="btn btn-success btn-fill" @click="btnVcManage">저장</button>
      </div>
    </div>
  </div>

</template>

<script setup>
import { ref, onMounted, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import axios from '../../assets/js/customAxios';
import { dateTimeFormat } from '../../assets/js/common';
import * as vacation from '../../assets/js/vacation';
import SignerModal from '../components/Modal/SignerModal.vue';
import Swal from 'sweetalert2';

// query에서 isUpdate 받아오기
const route = useRoute();
const router = useRouter();
const isUpdate = ref(route.query.isUpdate === 'true');
const vcCd = ref(route.query.vcCd);

// 라우터 파라미터가 변경 감지
watch(() => route.query.isUpdate, (newVal) => {
  isUpdate.value = newVal === 'true';
});

const props = defineProps({
  vcInfo: Object,
});

// 받아온 vcCd가 있는 경우 단건조회
onMounted(() => {
  if(vcCd.value != undefined) {
    vcGetInfo();
  }
});

const vcGetInfo = async () => {
  let result;
  try {
    result = await axios.get(`/api/vacation/vcInfo?vcCd=${vcCd.value}`);
  } catch (err) {
    Swal.fire({ icon: "error", title: "휴가 신청 정보 조회에 실패하였습니다.", text: "Error : " + err });
  }
  vcData.value = result.data;

  vcData.value.vcStartDt = dateTimeFormat(vcData.value.vcStartDt, 'yyyy-MM-dd');
  vcData.value.vcEndDt = dateTimeFormat(vcData.value.vcEndDt, 'yyyy-MM-dd');
  vcData.value.vcType = result.data.vcType;
  vcData.value.useDays = result.data.useDays;
  vcData.value.vcReason = result.data.vcReason;
  vcData.value.atchFileId = result.data.atchFileId;
  vcData.value.signId = result.data.signId;
  vcData.value.createId = result.data.createId;

  vcData.value.remainDays = '';
  files();
}

/////////////////////첨부파일 가져오기/////////////////////
const files = async () => {
  if (!vcData.value.atchFileId) return;

  try{
    const response = await axios.get("/api/cmm/fms/selectFileInfs.do",{
      params: {
        param_atchFileId: vcData.value.atchFileId
      },
    });

    console.log("파일내용=> ",response.data);
    fileList.value = response.data; // 결과 저장
    fileList.value.forEach(i => {
      i.name = i.orignlFileNm;
    })
  } catch (error) {
    console.error("파일 목록 불러오기 실패:", error);
  }
}

// 입력 데이터
let vcData = ref({
  vcStartDt: '',
  vcEndDt: '',
  vcType: 'E01',
  useDays: '',
  vcReason: '',
  atchFileId: '',
  signId: '',
  signNm: '',
  remainDays: '',
})

// 첨부파일
const fileList = ref([]);
const addFileList = (target) => {
  fileList.value = [];
  const newFile = Array.from(target.files);
  fileList.value.push(...newFile);
}
const removeFile = (index) => {
  fileList.value.splice(index, 1);
};

// 날짜 입력 감지 - 사용일수 계산
watch(() => [vcData.value.vcStartDt, vcData.value.vcEndDt, vcData.value.vcType], async (newVal) => {

  // 연차 정보 없는 경우 - 연차 생성
  let startYear;
  let startCheck;
  if(newVal[0] != '') {
    startYear = dateTimeFormat(newVal[0], 'yyyy');
    startCheck = await vacation.yearVcGetInfo(vcData.value.createId, startYear);
    if(startCheck == '') await vacation.yearVcAdd(vcData.value.createId, startYear);
  }


  switch(newVal[2]) { // 연차 종류에 따른 동작
    case 'E01': // 연차
      vcData.value.useDays = vcDateCalc(newVal[0], newVal[1]);
      break;
    case 'E04': // 공가
      vcData.value.useDays = 0;
      break;
    default:
      vcData.value.useDays = 0.5;
      vcData.value.vcEndDt = vcData.value.vcStartDt;
  }
})

// 사용일수 감지 - 예상 잔여 일수 입력
let isFirstLoad = 0;
let firstRemainDays;
watch(() => vcData.value.useDays, (newVal) => {
  if(isUpdate.value) {
    console.log("useDays 감지 : ", vcData.value.useDays);

    // 처음 실행에만 실행되어야 하는 코드 시작
    if (isFirstLoad < 2) {
      vcData.value.remainDays = props.vcInfo.requestDays;
      firstRemainDays = vcData.value.useDays;
      isFirstLoad++; // 첫 로딩 이후 감지 시작

      return;
    }

    vcData.value.remainDays = (props.vcInfo.requestDays + firstRemainDays - newVal).toFixed(1);
  } else {
    vcData.value.remainDays = (props.vcInfo.requestDays - newVal).toFixed(1);
  }
})

// 휴가 일수 계산
const vcDateCalc = (startDt, endDt) => {
  let startDate = new Date(startDt);
  let endDate = new Date(endDt);

  const diff = endDate - startDate;
  let diffDay = Math.floor(diff / (1000 * 60 * 60 * 24)) + 1;

  let weekendDays = 0;

  // 시작 날짜부터 종료 날짜까지 반복
  for (let date = new Date(startDate); date <= endDate; date.setDate(date.getDate() + 1)) {
    // 토요일(6) 또는 일요일(0)인 경우 weekendDays 증가
    if (date.getDay() === 6 || date.getDay() === 0) {
      weekendDays++;
    }
  }

  // 주말 제외한 일수 반환
  return diffDay - weekendDays;
}

// 등록 / 수정
const emit = defineEmits(['manageClick']);
const btnVcManage = async () => {
  let formData = new FormData();
  formData.append("vcStartDt", vcData.value.vcStartDt);
  formData.append("vcEndDt", vcData.value.vcEndDt);
  formData.append("vcType", vcData.value.vcType);
  formData.append("useDays", vcData.value.useDays);
  formData.append("vcReason", vcData.value.vcReason);
  formData.append("signId", vcData.value.signId);

  fileList.value.forEach((file) => {
    formData.append("files[]", file);
  });

  if(!validCheck()) return;
  
  if(!isUpdate.value) {
    try {
      await axios.post('/api/vacation/vcAdd', formData);
    } catch (err) {
      Swal.fire({ icon: "error", title: "휴가 신청 등록에 실패하였습니다.", text: "Error : " + err });
    }
  } else {
    formData.append("vcCd", vcCd.value);
    try {
      await axios.post('/api/vacation/vcModify', formData);
    } catch (err) {
      Swal.fire({ icon: "error", title: "휴가 신청 수정에 실패하였습니다.", text: "Error : " + err });
    }
  }

  emit('manageClick'); // 저장 동작 확인
  router.push({name:"VcList"});
}
// 취소
const btnVcManageCancle = () => {
  router.push({name:"VcList"});

}

// 유효성 체크
const validCheck = () => {
  if(!vcData.value.vcStartDt?.trim()) {
    Swal.fire({
      icon: "info",
      title: "휴가 시작일을 입력하세요.",
    });
    return false;
  }
  if(!vcData.value.vcEndDt?.trim()) {
    Swal.fire({
      icon: "info",
      title: "휴가 종료일을 입력하세요.",
    });
    return false;
  }
  if(!vcData.value.vcReason?.trim()) {
    Swal.fire({
      icon: "info",
      title: "휴가 사유를 입력하세요.",
    });
    return false;
  }
  if(!vcData.value.signNm?.trim()) {
    Swal.fire({
      icon: "info",
      title: "결재자를 선택하세요.",
    });
    return false;
  }

  return true;
}

// 결재자 모달
let isShowModal = ref(false);
const modalOpen = () => {
  isShowModal.value = true;
}
const modalClose = () => {
  isShowModal.value = false;
}
const modalConfirm = (row) => {
  isShowModal.value = false;
  console.log("결재자 : ", row);
  vcData.value.signId = row.mberId;
  vcData.value.signNm = row.mberNm;
}
</script>


<style scoped>
th {
  background-color: #d2d6da !important;
  color: black !important;
  font-size: 16px;
}

button {
  width: 80px;
  padding: 5px 10px !important;
}
</style>

