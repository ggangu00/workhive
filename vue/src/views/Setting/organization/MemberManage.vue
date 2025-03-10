<template>
   <div class="content">
      <div class="container-fluid" @keydown.esc="modalClose">
         <div class="card">
            <div class="card-header d-flex justify-content-between align-items-center">
               <h4 class="card-title">부서 관리</h4>
            </div>
         </div>

         <div class="card" @keydown.esc="modalClose">
            <div class="card-body">
               <div class="row m-0">
                  <!-- 트리 뷰 (왼쪽) -->
                  <div class="col-3 m-group">
                     <div class="bottom-line">
                        <div class="d-flex justify-content-start align-items-center p-2">
                           <button class="btn btn-primary btn-fill " @click="modalOpen">부서 등록</button>
                        </div>
                     </div>
                     <!-- 데이터 로딩 끝난 후에만 자식 렌더링 -->
                     <DepartmentComponent
                        v-if="isTreeLoaded"
                        :departmentTree="departmentTree"
                        @btnDepartmentRemove="btnDepartmentRemove"
                        @btnDepartmentModify="btnDepartmentModify"
                        @btnDepartmentAdd="btnDepartmentAdd"
                     />
                  </div>

                  <!-- 구성원 테이블 (오른쪽) -->
                  <div class="col-9 m-group">
                     <div class="bottom-line">
                        <div class="d-flex justify-content-between align-items-center p-2">
                           <div class="button-group justify-content-between">
                              <button class="btn btn-secondary btn-fill">이동</button>
                              <button class="btn btn-primary btn-fill">저장</button>
                              <button class="btn btn-danger btn-fill">삭제</button>
                           </div>

                           <div class="d-flex justify-content-between align-items-center w50">
                              <select name="searchSel" id="searchSel" class="form-select w25">
                                 <option value="name">이름</option>
                                 <option value="auth">권한</option>
                              </select>
                              <input type="text" class="form-control w50" placeholder="검색어 입력"/>
                              <button class="btn btn-info btn-fill w20">검색</button>
                           </div>
                        </div>
                     </div>

                     <table class="table m-table">
                        <thead class="table-light">
                           <tr>
                              <th>
                                 <div class="form-check">
                                    <input class="form-check-input" type="checkbox">
                                 </div>
                              </th>
                              <th>사번</th>
                              <th>이름</th>
                              <th>직급</th>
                              <th>접근 권한</th>
                              <th>메뉴 권한</th>
                           </tr>
                        </thead>

                        <tbody>
                           <tr v-for="member in members" :key="member.id">
                              <td>
                                 <div class="form-check">
                                    <input class="form-check-input" type="checkbox" >
                                 </div>
                              </td>
                              <td>{{ member.id }}</td>
                              <td>
                                 <div class="profile-text">
                                    <span class="team-label">개발팀</span>
                                    <span class="user-name">{{ member.name }}</span>
                                 </div>
                              </td>
                              <td>{{ member.rank }}</td>
                              <td>
                                 <select class="form-select w-70">
                                    <option value="">선택하세요</option>
                                    <option value="">최고관리자</option>
                                    <option value="">부관리자</option>
                                    <option value="">운영관리자</option>
                                 </select>
                              </td>
                              <td>
                                 <select class="form-select w-70">
                                    <option value="">선택하세요</option>
                                    <option value="">메뉴1</option>
                                    <option value="">메뉴2</option>
                                 </select>
                              </td>
                           </tr>
                        </tbody>
                     </table>

                  </div>
               </div>
            </div>
         </div>

         <!-- [s]-->
         <Modal :isShowModal="isShowModal" :modalTitle="modalTitle" @click.self="modalClose">
            <!-- 모달 바디 -->
            <template v-slot:body>
               <card class="mb-0">
                  <!-- 부서 코드 -->
                  <div class="mb-3" v-show="isEditMode">
                     <label class="form-label">부서코드 <i class="fa-solid fa-asterisk point-red"></i></label>
                     <input type="text" v-model="deptCd" readonly class="form-control w30">
                  </div>

                  <!-- 부모 코드 (하위 부서 추가 시 표시) -->
                  <div class="mb-3" v-show="isSubDeptMode">
                     <label class="form-label">부모 부서 코드</label>
                     <input type="text" v-model="parentCd" readonly class="form-control w30">
                  </div>

                  <!-- DEPTH (하위 부서 추가 시 표시) -->
                  <div class="mb-3" v-show="isSubDeptMode">
                     <label class="form-label">DEPTH</label>
                     <input type="text" v-model="depth" readonly class="form-control w30">
                  </div>

                  <!-- 부서명 -->
                  <div class="mb-3">
                     <label class="form-label">부서명 <i class="fa-solid fa-asterisk point-red"></i></label>
                     <input type="text" v-model="deptNm" ref="deptNmInput" class="form-control w30">
                  </div>

                  <!-- 부서 설명 -->
                  <div class="mb-3">
                     <div class="form-group has-label">
                        <label>부서 설명</label>
                     </div>
                     <textarea type="text" v-model="description" class="form-control" placeholder="부서에 대한 설명을 입력하세요." style="height: 130px;"></textarea>
                  </div>
               </card>
            </template>

            <!-- 모달 푸터 -->
            <template v-slot:footer>
               <button type="button" class="btn btn-secondary btn-fill" @click.self="modalClose">닫기</button>
               <button v-if="isEditMode" type="button" class="btn btn-success btn-fill" @click="btnDeptModify">수정</button>
               <button v-else type="button" class="btn btn-primary btn-fill" @click="btnDeptAdd">등록</button>
            </template>
         </Modal>
         <!--부서등록 모달[e]-->
      </div>
   </div>
</template>

<script setup>
   import { ref, onBeforeMount, onMounted, onBeforeUnmount, computed } from "vue";
   import Swal from 'sweetalert2';
   import axios from "../../../assets/js/customAxios"; // 공통 Axios 설정 파일
   import Modal from '../../../components/Modal.vue';
   import Card from '../../../components/Cards/Card.vue';
   import DepartmentComponent from "../../../components/Department/DepartmentComponent.vue";

   const isShowModal = ref(false);  // 모달 체크
   const isEditMode = ref(false);   // 수정, 등록 모달 여부 체크
   const deptCd = ref("");          // 부서 코드
   const deptNm = ref("");          // 부서명
   const description = ref("");     // 부서 설명
   const isSubDeptMode = ref(false); // 하위 부서 추가 모드 여부
   const parentCd = ref(""); // 부모 부서 코드 저장
   const depth = ref(0); // DEPTH 저장 (기본값 0)
// ================================================== 생명주기 함수 ==================================================
   // 컴포넌트가 마운트되기 전에 권한 및 메뉴 목록 조회 실행
   onBeforeMount(async () => {
      await departmentGetList();
      isTreeLoaded.value = true;
   });

      // 컴포넌트 마운트 시 이벤트 리스너 등록
   onMounted(() => {
      document.addEventListener('keydown', modalClose);        // ESC 키 감지 이벤트 등록
   });

   // 컴포넌트 언마운트 시 이벤트 리스너 해제
   onBeforeUnmount(() => {
      document.removeEventListener('keydown', modalClose);     // ESC 이벤트 해제
   });

// ================================================== 모달 이벤트 ==================================================
   // 모달 타이틀 동적 변경
   const modalTitle = computed(() => (isEditMode.value ? "부서 수정" : "부서 등록"));

   const modalOpen = () => {
      isShowModal.value = true;
   }

   const modalClose = (e) => {
      if (!e || (e.type === "click")) {  // ✅ 클릭 이벤트도 모달을 닫도록 처리
         isShowModal.value = false;
         return;
      }

      if (e.key === "Escape") {
         if(isShowModal.value) {
            isShowModal.value = !isShowModal.value
         }
      }
   }
// ================================================== 버튼 이벤트 ==================================================
   // 모달 등록 버튼
   const btnDeptAdd = () => {
      if (!validationCheck()) {
         return;
      }

      departmentAdd(); // ✅ 부서 추가 실행
   };

   // 하위부서 추가 모드
   const btnDepartmentAdd = (node) => {

      isEditMode.value = false; // 등록 모드
      isSubDeptMode.value = true; // 하위 부서 모드 활성화

      deptCd.value = ""; // 신규 부서 코드 (자동 생성될 것이므로 빈 값)
      deptNm.value = "";
      description.value = "";
      parentCd.value = node.key; // ✅ 부모 부서 코드 지정
      depth.value = node.depth + 1; // ✅ 부모 DEPTH + 1 (같은 계층 유지)

      isShowModal.value = true; // ✅ 모달 열기
   };

   // 모달 저장 버튼
   const btnDeptModify = () => {

      if(!validationCheck()) {
         return;
      }

      const requestData = {
         deptCd: deptCd.value,
         deptNm: deptNm.value,
         description: description.value,
      }

      Swal.fire({
         title: `부서를 수정 하시겠습니까 ?`,
         icon: "question",
         showCancelButton: true,
         reverseButtons: true,
         customClass: {
            cancelButton: "btn btn-secondary btn-fill",  // 아니오 버튼
            confirmButton: "btn btn-danger btn-fill",   // 예 버튼
         },
         confirmButtonText: "예",
         cancelButtonText: "아니오"
      }).then((result) => {

         if (result.isConfirmed) {
            departmentModify(requestData);
            Swal.fire({
               title: "수정 완료",
               icon: "success"
            });
            modalClose();
         }
      });
   }

   // 자식 컴포넌트한테서 값을 받아서 모달 열기
   const btnDepartmentModify = (node) => {
      // 수정 모드 활성화
      isEditMode.value = true;

      // 선택한 부서 정보 필드에 채우기
      deptCd.value = node.key;
      deptNm.value = node.label;
      description.value = node.description || "";

      // 모달 열기
      isShowModal.value = true;
   };

   // 부서 삭제
   const btnDepartmentRemove = (node) => {

      // 하위 부서 존재 여부 체크
      if (node.children && node.children.length > 0) {
         Swal.fire({
            icon: "warning",
            title: "삭제 불가",
            text: "하위 부서가 존재하는 경우 삭제할 수 없습니다."
         });
         return;
      }

      Swal.fire({
         title: `부서를 삭제하시겠습니까 ?`,
         icon: "question",
         showCancelButton: true,
         reverseButtons: true,
         customClass: {
            cancelButton: "btn btn-secondary btn-fill",  // 아니오 버튼
            confirmButton: "btn btn-danger btn-fill",   // 예 버튼
         },
         confirmButtonText: "예",
         cancelButtonText: "아니오"
      }).then((result) => {
         if (result.isConfirmed) {
            departmentRemove(node.key);
            Swal.fire({
               title: "삭제 완료",
               icon: "success"
            });
         }
      });
      // 부서 삭제 로직 구현
   };

// ================================================== 부서관련 axios ==================================================
   const departmentTree = ref([]);
   // 데이터 로드 완료 플래그
   const isTreeLoaded = ref(false)
   const departmentGetList = async () => {
      try {
         const response = await axios.get('/api/department');

         const tree = buildPrimeVueTree(response.data);
         departmentTree.value = tree;

      } catch (err) {
         departmentTree.value = []
         Swal.fire({
            icon: "error",
            title: "API 조회 실패",
            text: `Error: ${err.response?.data?.error || err.message}`
         })
      }
   }

   // 부서명 유효성 체크
   const deptNmInput = ref(null); // 부서명 input 요소 참조
   const validationCheck = () => {
      if(!deptNm.value) {
         Swal.fire({
            icon: "info",
            title: "부서명을 입력하세요.",
         }).then(() => {
            deptNmInput.value.focus(); // 포커스 설정
         });
         return false;
      }
      return true;
   }

   // 부서 추가
   const departmentAdd = async () => {
      if (!validationCheck()) {
         return;
      }

      const requestData = {
         deptNm: deptNm.value,
         description: description.value,
         parentCd: isSubDeptMode.value ? parentCd.value : null, // 하위 부서라면 부모 코드 설정
         depth: isSubDeptMode.value ? depth.value : 0 // DEPTH 자동 설정 (최상위는 0)
      };

      try {
         const response = await axios.post('/api/department', requestData);

         if (response.data.result === true) {
            Swal.fire({
               icon: "success",
               title: `"${deptNm.value}" 부서가 추가되었습니다.`,
            }).then(() => {
               const updatedTree = buildPrimeVueTree(response.data.deptList);
                departmentTree.value = updatedTree; // ✅ UI 업데이트

                isSubDeptMode.value = false; // ✅ 하위 부서 모드 초기화
                modalClose(); // ✅ 모달 닫기
            });
         }
      } catch (err) {
         Swal.fire({
            icon: "error",
            title: "등록 실패",
            text: `Error: ${err.response?.data?.error || err.message}`
         });
      }
   };


   // 부서 수정
   const departmentModify = async (requestData) => {
      try {
         const response = await axios.put(`/api/department`, requestData);

         if (response.data.result === true) {
            const modifyTree = buildPrimeVueTree(response.data.deptList);
            departmentTree.value = modifyTree; // 삭제 후 업데이트
         }

      } catch (err) {

         Swal.fire({
            icon: "error",
            title: "삭제 실패",
            text: `Error: ${err.response?.data?.error || err.message}`
         })
      }
   }

   // 부서 삭제
   const departmentRemove = async (code) => {
      try {
         const response = await axios.delete(`/api/department`, { data : [code]});

         if (response.data.result > 0) {
            const deleteTree = buildPrimeVueTree(response.data.deptList);
            departmentTree.value = deleteTree; // 삭제 후 업데이트
         }

      } catch (err) {

         Swal.fire({
            icon: "error",
            title: "삭제 실패",
            text: `Error: ${err.response?.data?.error || err.message}`
         })
      }
   }

   // 트리 형태로 만들어주는 함수
   const buildPrimeVueTree = (flatList) => {
      const map = new Map();

      // ✅ 전체 데이터 Map에 먼저 등록
      flatList.forEach(item => {
         if (!item.deptCd) {
            console.warn("🚨 deptCd가 없는 데이터 발견!", item);
            return;
         }
         map.set(item.deptCd, {
            key: item.deptCd,
            label: item.deptNm,
            depth: item.depth, // ✅ DEPTH 값 저장
            parentCd: item.parentCd, // ✅ 부모 코드 저장
            description: item.description || "", // ✅ 설명 값 저장 (화면에 노출되지 않음)
            children: []
         });
      });

      const tree = [];

      // ✅ 부모-자식 연결
      flatList.forEach(item => {
         const parent = map.get(item.parentCd);
         const child = map.get(item.deptCd);

         if (!child) {
            console.warn("🚨 deptCd가 없는 노드 발견!", item);
            return;
         }

         if (parent) {
            parent.children.push(child);
         } else {
            tree.push(child);
         }
      });

      return tree;
   };


   const members = ref([
      { id: "2025001", name: "김민진", rank: "부장" },
      { id: "2025002", name: "박주현", rank: "부장" },
   ]);

</script>

<style lang="scss" scoped>
   .form-control[readonly] {
      background-color: #f5f5f5; /* 🔹 연한 회색 배경 */
      border: 1px solid #dcdcdc; /* 🔹 흐린 테두리 */
      color: #999 !important; /* 🔹 글씨 색 연하게 */
      cursor: not-allowed !important; /* 🔹 입력 불가 커서 */
      pointer-events: auto !important; /* 🔹 클릭 가능하도록 변경 */
   }
   /* .menu-container {
      display: flex;
      justify-content: space-between;
      align-items: center;
      width: 100%;
   }
   .menu-button {
      border: none;
      background: none;
      font-size: 16px;
      cursor: pointer;
   } */
   /* .dropdown-menu {
      position: absolute;
      background: white;
      box-shadow: 0px 0px 5px rgba(0, 0, 0, 0.2);
      border-radius: 5px;
      padding: 5px;
      z-index: 1000;
   }
   .dropdown-menu button {
      display: block;
      width: 100%;
      border: none;
      background: none;
      padding: 5px;
      text-align: left;
   } */
</style>
