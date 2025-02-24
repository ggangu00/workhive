<template>
   <div class="content">
      <div class="container-fluid">
         <div class="card">
            <div class="card-header d-flex justify-content-between align-items-center">
               <h4 class="card-title">권한 관리</h4>
            </div>
         </div>

         <div class="card">
            <div class="card-body">
               <div class="row m-0">

                  <div class="col-3 treeview">
                     <div class="bottom-line p-2">
                        <button class="btn btn-success btn-sm" @click="btnAuthorityAdd"><i class="fa-solid fa-plus"></i> 권한 추가</button>
                     </div>
                     <div class="p-2">
                        <div v-for="(role, idx) in roles" :key="idx">
                           <div class="d-flex justify-content-between align-items-center">
                              <span class="role-item">{{ role.authorityNm }}</span>
                              <div class="options pe-2" @click.stop="optionsToggle(idx, role.authorityCd)">
                                 <i class="fa-solid fa-ellipsis-vertical font-13"></i>
                              </div>
                           </div>

                           <!-- 드롭다운 메뉴 -->
                           <div v-show="selectedRole === idx" class="editing-dropdown-menu">
                              <div class="font-14 p-2" @click="btnAuthorityModify(role.authorityCd)">권한 수정</div>
                              <div class="font-14 p-2" @click="btnAuthorityRemove(role.authorityNm, role.authorityCd)">권한 삭제</div>
                           </div>
                        </div>
                     </div>
                  </div>

                  <!-- 오른쪽 패널 -->
                  <div class="col-9 treeview">
                     <div class="d-flex justify-content-between align-items-center bottom-line p-2">
                        <span>권한명</span>
                        <div class="button-group">
                           <button class="btn btn-secondary btn-sm btn-fill">취소</button>
                           <button class="btn btn-success btn-sm btn-fill">저장</button>
                        </div>
                     </div>

                     <div v-for="(menu, index) in menus" :key="index" class="bottom-line">
                        <div class="menu-title">
                           <div class="">
                              <div class="form-check form-check-inline">
                                 <input class="form-check-input" type="checkbox" v-model="menu.selected" />
                              </div>
                              <span class="me-2 font-15">{{ menu.title }}</span>
                              <span class="font-14">({{ submenusSelected(menu) }}/{{ menu.submenus.length }})</span>
                           </div>
                           <di @click="toggleMenu(index)">
                              <i class="fa-solid fa-angle-down" :class="{ 'rotated': menu.open }"></i>
                           </di>
                        </div>
                        <div v-if="menu.open" class="submenu px-4 py-2">
                           <div v-for="(sub, i) in menu.submenus" :key="i" class="px-4 py-2">
                              <div class="form-check form-check-inline">
                                 <input class="form-check-input" type="checkbox" v-model="menu.submenus[i].selected" />
                              </div>
                              <span class="font-13">{{ sub.title }}</span>
                           </div>
                        </div>
                     </div>

                  </div>

               </div>

               <!-- [s]-->
               <Modal :isShowModal="isShowModal" :modalTitle="modalTitle" @click.self="modalClose">
                  <!-- 모달 바디 -->
                  <template v-slot:body>
                     <card class="mb-0">
                        <div class="mb-3" v-show="isEditMode">
                           <label class="form-label">권한코드 <i class="fa-solid fa-asterisk point-red"></i></label>
                           <input type="text" v-model="authorityCd" class="form-control w30" readonly disabled>
                        </div>
                        <div class="mb-3">
                           <label class="form-label">권한명 <i class="fa-solid fa-asterisk point-red"></i></label>
                           <input type="text" v-model="authorityNm" class="form-control w30">
                        </div>
                        <div class="mb-3">
                           <div class="form-group has-label">
                              <label>권한에 대한 설명</label>
                           </div>
                           <textarea type="text" v-model="description" class="form-control" placeholder="부서에 대한 설명을 입력하세요." style="height: 130px;"></textarea>
                        </div>
                     </card>
                  </template>

                  <!-- 모달 푸터 -->
                  <template v-slot:footer>
                     <button type="button" class="btn btn-secondary btn-fill" @click="modalClose">닫기</button>
                     <!-- 등록 모드 -->
                     <button v-if="!isEditMode" type="button" class="btn btn-primary btn-fill" @click="authorityAdd">등록</button>
                     <!-- 수정 모드 -->
                     <button v-else type="button" class="btn btn-success btn-fill" @click="authorityModify(authorityCd)">저장</button>
                  </template>
               </Modal>

            </div>
         </div>
      </div>
   </div>
</template>

<script setup>
   import { ref, onMounted, onBeforeMount } from 'vue';
   import axios from "axios";
   import Swal from 'sweetalert2';
   import Card from '../../components/Cards/Card.vue'
   import Modal from '../../components/Modal.vue';

   onBeforeMount(() => {
      authorityGetList();  // 권한 목록 조회
   });

   onMounted(() => {
      document.addEventListener("click", outsideClickHandler);
   });
// ================================================== Modal ==================================================
   const isShowModal = ref(false);
   let modalTitle = ref("");
   let isEditMode = ref(false);

   // 모달 열기
   const modalOpen = (mode, title) => {
      isEditMode.value = mode;
      modalTitle.value = title;

      isShowModal.value = true;
   };

   // 모달 닫기
   const modalClose = (e) => {
      if (e.key === "Escape") {
         if(isShowModal.value) {
            isShowModal.value = !isShowModal.value
         }
      } else {
         modalReset();
         isShowModal.value = false;
      }
   };

   // 모달 input 초기화
   const modalReset = () => {
      authorityCd.value = '';
      authorityNm.value = '';
      description.value = '';
   };

   const roles = ref([]);
   const selectedRole = ref(null);

// ============================================= 버튼이벤트 =============================================
   // 권한추가 버튼
   const btnAuthorityAdd = () => {
      modalOpen(false, "권한 등록")
   };

   // 권한수정 버튼
   const btnAuthorityModify = (role) => {
      modalOpen(true, "권한 수정");
      authorityGet(role);
   };

   // 권한삭제 버튼
   const btnAuthorityRemove = (name, code) => {
      Swal.fire({
         title: `"${name}" 권한을 삭제 하시겠습니까?`,
         icon: "question",
         showCancelButton: true,
         customClass: {
            confirmButton: "btn btn-secondary btn-fill",
            cancelButton: "btn btn-danger btn-fill"
         },
         confirmButtonText: "닫기", // "삭제" → "닫기"
         cancelButtonText: "삭제", // "닫기" → "삭제"
      }).then((result) => {
         if (result.dismiss == Swal.DismissReason.cancel) {
            // 권한 삭제
            authorityRemove(code);

            Swal.fire({
               title: "삭제 완료",
               icon: "success"
            });
         }
      });
   };

// ============================================= Axios 통신 =============================================
   // 권한 전체 조회
   const authorityGetList = async () => {
      try {
         const result = await axios.get('/api/authority');
         roles.value = result.data;
      } catch (err) {
         roles.value = [];

         Swal.fire({
            icon: "error",
            title: "API 조회 오류",
            text:  "Error : " + err.response.data.error
         });
      }
   };

   let authorityCd = ref(""); // 권한코드
   // 권한 단건조회
   const authorityGet = async (role) => {
      try {
         const response = await axios.get(`/api/authority/${role}`);
         if(response.data.result === true) {
            authorityCd.value = response.data.info.authorityCd;
            authorityNm.value = response.data.info.authorityNm;
            description.value = response.data.info.description;
         }
      } catch (err) {
         Swal.fire({
            icon: "error",
            title: "API 조회 오류",
            text:  "Error : " + err.response.data.error
         });
      }
   };

   let authorityNm = ref(''); // 모달 권한명
   let description = ref(''); // 모달 권한설명
   // 권한 등록
   const authorityAdd = async () => {
      const requestData = { // 서버로 보낼 데이터
         authorityNm: authorityNm.value,
         description : description.value,
         createId : "admin"
      };

      try {
         const response = await axios.post('/api/authority', requestData);

         if(response.data.result === true) {
            Swal.fire({
               icon: "success",
               title: "등록 성공",
            });
            roles.value = response.data.list;
            modalReset();
            isShowModal.value = !isShowModal.value;
         }
      } catch (err) {
         roles.value = [];

         Swal.fire({
            icon: "error",
            title: "등록 실패",
            text:  "Error : " + err.response.data.error
         });
      }
   };

   //권한 수정
   const authorityModify = async (code) => {
      const requestData = { // 요청 본문에 보낼 데이터
         authorityCd: code,
         authorityNm: authorityNm.value,
         description : description.value,
         createId : "admin"
      };

      try {
         const response = await axios.put(`/api/authority`, requestData);

         if(response.data.result === true) {
            roles.value = response.data.list;
            modalReset();
            isShowModal.value = !isShowModal.value;
         }
      } catch (err) {
         roles.value = [];

         Swal.fire({
            icon: "error",
            title: "수정 실패",
            text:  "Error : " + err.response.data.error
         });
      }
   };

   // 권한 삭제
   const authorityRemove = async (code) => {
      try {
         const response = await axios.delete(`/api/authority/${code}`);

         if(response.data.result === true) {
            roles.value = response.data.list;
         }
      } catch (err) {
         roles.value = [];

         Swal.fire({
            icon: "error",
            title: "삭제 실패",
            text:  "Error : " + err.response.data.error
         });
      }
   };


// ======================================== Menu ========================================
   const menus = ref([
      {
         title: '대메뉴 1',
         open: false,
         selected: false,
         submenus: [
            { title: '소메뉴 1', selected: false },
            { title: '소메뉴 2', selected: false },
            { title: '소메뉴 3', selected: false },
            { title: '소메뉴 4', selected: false }
         ]
      },
      {
         title: '대메뉴 2',
         open: false,
         selected: false,
         submenus: [
            { title: '소메뉴 1', selected: false },
            { title: '소메뉴 2', selected: false },
            { title: '소메뉴 3', selected: false },
            { title: '소메뉴 4', selected: false }
         ]
      },
   ]);

   const submenusSelected = (menu) => {
      return menu.submenus.filter(sub => sub.selected).length;
   };

// ======================================== editing, delete Toggle Dropdown Menu ========================================
   const toggleMenu = (index) => {
      menus.value[index].open = !menus.value[index].open;
   };


   const optionsToggle = (role) => {
      selectedRole.value = selectedRole.value === role ? null : role;
   };

   // 바깥 클릭 시 dropdown 닫기
   const outsideClickHandler = (event) => {
      if (!event.target.closest(".editing-dropdown-menu") && !event.target.closest(".options")) {
         selectedRole.value = null;
      }
   };

</script>