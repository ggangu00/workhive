<template>
   <div class="content">
      <div class="container-fluid">
         <div class="card">
            <div class="card-header d-flex justify-content-between align-items-center">
               <h4 class="card-title">개인정보 설정</h4>
               <div class="button-group">
                  <button class="btn btn-secondary btn-fill mr-1" @click="btnMemberReset">초기화</button>
                  <button class="btn btn-primary btn-fill mr-1" @click="btnMemberSave">저장</button>
               </div>
            </div>
         </div>

         <div class="row">
            <div class="col-12">
               <div class="card">
                  <div class="card-body">
                     <div class="section">
                        <div class="section-header">[ 회사정보 ]</div>
                        <div class="row p-1">
                           <div class="col-md-3">
                              <label>회사명 <i class="fa-solid fa-asterisk"></i></label>
                              <input type="text" v-model="viewData.compNm" class="form-control readonly" />
                           </div>
                           <div class="col-md-3">
                              <label>사업자등록번호 <i class="fa-solid fa-asterisk"></i></label>
                              <input type="text" v-model="viewData.businessNo" class="form-control readonly" />
                           </div>
                           <div class="col-md-3">
                              <label>대표자</label>
                              <input type="text" v-model="viewData.ceoNm" class="form-control readonly" />
                           </div>
                        </div>
                     </div>

                     <div class="section">
                        <div class="section-header">[ 기본정보 ]</div>
                        <div class="row p-1">
                           <div class="col-md-3">
                              <label>대표전화 <i class="fa-solid fa-asterisk"></i></label>
                              <input type="text" v-model="viewData.mberId" class="form-control readonly" />
                           </div>
                           <div class="col-md-3">
                              <label>이메일 <i class="fa-solid fa-asterisk"></i></label>
                              <input type="text" v-model="viewData.esntlId" class="form-control readonly" />
                           </div>
                           <div class="col-md-3">
                              <label>팩스 <i class="fa-solid fa-asterisk"></i></label>
                              <input type="text" v-model="formData.mberNm" class="form-control editable" />
                           </div>
                        </div>

                     </div>

                     <div class="section">
                        <div class="section-header">[ 설정 ]</div>
                        <div class="row p-1">
                           <div class="col-md-1">
                              <label>우편번호 <i class="fa-solid fa-asterisk"></i></label>
                              <input type="text" v-model="viewData.mberId" class="form-control editable" />
                           </div>
                           <div class="col-md-5">
                              <label>주소 <i class="fa-solid fa-asterisk"></i></label>
                              <input type="text" v-model="viewData.esntlId" class="form-control editable" />
                           </div>
                           <div class="col-md-5">
                              <label>상세주소 <i class="fa-solid fa-asterisk"></i></label>
                              <input type="text" v-model="formData.mberNm" class="form-control editable" />
                           </div>
                        </div>

                     </div>

                     <div class="section">
                        <div class="section-header">[ 설정 ]</div>
                        <div class="row p-1">
                           <div class="col-md-6">
                              <label class="align-items-center">메뉴사용 <i class="fa-solid fa-asterisk"></i></label>
                              <input type="email" v-model="formData.mberEmailAdres" class="editable" />
                           </div>
                           <div class="col-md-3">
                              <label class="align-items-center">근무 시작시간 <i class="fa-solid fa-asterisk"></i></label>
                              <input type="email" v-model="formData.mberEmailAdres" class="editable" />
                           </div>
                           <div class="col-md-3">
                              <label class="align-items-center">근무 종료시간 <i class="fa-solid fa-asterisk"></i></label>
                              <input type="email" v-model="formData.mberEmailAdres" class="editable" />
                           </div>
                        </div>
                     </div>

                  </div>
               </div>
            </div>
         </div>
      </div>
   </div>

</template>

<script setup>
   import { onBeforeMount, ref } from 'vue';
   import axios from '../../../assets/js/customAxios';
   import Swal from 'sweetalert2';

   //============================= 공통코드 함수 =============================
   import { getComm } from '../../../assets/js/common'

   // 📌 데이터 저장용
   const formData = ref({
      mberNm: "",
      mberEmailAdres: "",
      areaNo: "",
      middleTelno: "",
      endTelno: "",
      mbtlnum: ""
   });

   const originalData = ref({});  // 원본 데이터 저장 (수정 비교용)

   // 📌 읽기 전용 데이터 (화면 표시용)
   const viewData = ref({
      deptNm: "",
      gradeNm: "",
      respNm: "",
      mberId: "",
      esntlId: "",
   });

   // 📌 공통코드 목록 (지역번호)
   const commCodeList = ref([]);

   // ============================================= Lifecycle =============================================
   // 컴포넌트 마운트 시 데이터 조회
   onBeforeMount(async () => {
      await memberGet();       // 회원 정보 조회
      await commonCodeList();  // 공통코드 조회
      setSelectedAreaNo();     // 지역번호 디폴트 세팅
   });

   // ============================================= Btn Event =============================================
   // 초기화 버튼 이벤트
   const btnMemberReset = () => {
      formData.value = JSON.parse(JSON.stringify(originalData.value)); // 깊은 복사로 초기화
      Swal.fire({
         icon: "info",
         title: "초기화 완료",
      });
   };

   // 저장 버튼 이벤트
   const btnMemberSave = () => {
      const modifiedData = getModifiedFields(originalData.value, formData.value);

      if (!validateFormData()) {
        return;  // 유효성 검사 실패 시 중단
      }

      if (Object.keys(modifiedData).length === 0) {
         Swal.fire({
            icon: "info",
            title: "수정된 정보가 없습니다."
         });
         return;
      }

      Swal.fire({
         title: `${formData.value.mberNm}님의 정보를 수정하시겠습니까?`,
         icon: "question",
         showCancelButton: true,
         customClass: {
            confirmButton: "btn btn-secondary btn-fill",
            cancelButton: "btn btn-primary btn-fill",
         },
         confirmButtonText: "취소",
         cancelButtonText: "확인"
      }).then((result) => {
         if (result.dismiss == Swal.DismissReason.cancel) {
            // 회원정보 수정
            memberModify(formData.value);

            Swal.fire({
               icon: "success",
               title: "수정 완료",
               text: "회원 정보가 수정되었습니다."
            });
         }
      });
   };

   // ============================================= Axios Event =============================================
   // 회원 정보 조회
   const memberGet = async () => {
      try {
         const result = await axios.get('/api/member/info');
         const data = result.data;

         // 읽기전용 데이터
         viewData.value = {
            deptNm: data.deptNm || "",
            gradeNm: data.gradeNm || "",
            respNm: data.respNm || "",
            mberId: data.mberId || "",
            esntlId: data.esntlId || "",
         };

         // 수정 가능 데이터
         formData.value = {
            mberNm: data.mberNm || "",
            mberEmailAdres: data.mberEmailAdres || "",
            areaNo: data.areaNo || "",
            middleTelno: data.middleTelno || "",
            endTelno: data.endTelno || "",
            mbtlnum: data.mbtlnum?.trim() || ""
         };

         // 원본 데이터 저장 (수정 비교용)
         originalData.value = JSON.parse(JSON.stringify(formData.value));
      } catch (err) {
         Swal.fire({
            icon: "error",
            title: "회원 정보 조회 실패",
            text: err.response?.data?.error || err.message
         });
      }
   };

   // 회원 정보 수정 (이름/이메일/전화/휴대폰만 전송)
   const memberModify = async (modifiedData) => {
      try {

         await axios.put('/api/member', modifiedData, {
               headers: { 'Content-Type': 'application/json' }
         });

         // 저장 성공 후 원본 데이터 갱신
         originalData.value = JSON.parse(JSON.stringify(formData.value));
      } catch (err) {
         Swal.fire({
            icon: "error",
            title: "수정 실패",
            text: err.response?.data?.error || err.message
         });
      }
   };

   // 공통코드 조회 함수 (지역번호용)
   const commonCodeList = async () => {
      commCodeList.value = await getComm("LC");
   };

   // 공통코드와 DB 값 일치 처리
   const setSelectedAreaNo = () => {
      const selectedArea = commCodeList.value.find(comm => comm.commDtlCd === formData.value.areaNo);
      if (selectedArea) {
         formData.value.areaNo = selectedArea.commDtlCd;
      }
   };

   // 변경된 필드만 추출하는 유틸 함수
   const getModifiedFields = (original, current) => {
      const modified = {};
      Object.keys(current).forEach(key => {
         if (original[key] !== current[key]) {
            modified[key] = current[key];
         }
      });
      return modified;
   };

   const validateFormData = () => {
      const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;   // 이메일 형식 체크
      const phonePattern = /^[0-9]*$/;                     // 숫자만 허용 (전화번호)

      if (!formData.value.mberNm?.trim()) {
         Swal.fire({
            icon: "info",
            title: "이름을 입력하세요.",
         });
         return false;
      }

      if (!formData.value.mberEmailAdres?.trim()) {
         Swal.fire({
            icon: "info",
            title: "이메일을 입력하세요.",
         });
         return false;
      }

      if (!emailPattern.test(formData.value.mberEmailAdres)) {
         Swal.fire({
            icon: "info",
            title: "이메일 형식을 확인하세요.",
         });
         return false;
      }

      if (!formData.value.middleTelno || formData.value.middleTelno.length > 5) {
         Swal.fire({
            icon: "info",
            title: "전화번호는 4자리로 이하로 입력하세요.",
         });
         return false;
      }

      if (!formData.value.endTelno || formData.value.endTelno.length > 5) {
         Swal.fire({
            icon: "info",
            title: "전화번호는 4자리 이하로 입력하세요.",
         });
         return false;
      }

      if (formData.value.middleTelNo && !phonePattern.test(formData.value.middleTelNo)) {
         Swal.fire({
            icon: "info",
            title: "전화번호는 숫자만 입력 가능합니다.",
         });
         return false;
      }

      if (formData.value.endTelno && !phonePattern.test(formData.value.endTelno)) {
         Swal.fire({
            icon: "info",
            title: "전화번호는 숫자만 입력 가능합니다.",
         });
         return false;
      }

      return true; // 통과
   };
</script>

<style lang="scss" scoped>
   .section {
      margin-bottom: 70px;
   }

   .section-header {
      font-weight: bold;
      font-size: 15px;
      margin-bottom: 20px;
      border-bottom: 1.5px solid #002966;
      padding-bottom: 10px;
      color: #636363;
   }

   .form-group:last-child {
      margin-right: 0;
   }

   input, select {
      width: 100%;
      padding: 8px;
      border: none;
      border-bottom: 0.5px solid #7c7c7c;
      border-radius: 3px;
   }

   .readonly {
      background-color: #fff;
      color: #7c7c7c;
      cursor: not-allowed;  /* 마우스 올리면 금지 표시 */
      pointer-events: none; /* 클릭, 포커스 막기 */
      user-select: none;    /* 드래그로 텍스트 선택 막기 */
   }


   .editable {
      background: #eee;
   }

   .button-group {
      display: flex;
      justify-content: center;
      align-items: center;
   }

   .phone-group {
      display: flex;
      align-items: center;
      gap: 5px; /* 각 입력칸 사이 여백 */

      select:nth-of-type(1) {
         width: 20%;
      }

      input:nth-of-type(1),
      input:nth-of-type(2){
         -webkit-appearance: none;
         width: 30%;
      }

      input[type="number"]::-webkit-outer-spin-button,
      input[type="number"]::-webkit-inner-spin-button {
         -webkit-appearance: none;
         margin: 0;
      }
   }

   .phone-group span {
      font-weight: bold;
   }
</style>
