<template>
   <div class="content">
      <div class="container-fluid">
         <div class="card">
            <div class="card-header d-flex justify-content-between align-items-center">
               <h4 class="card-title">권한 관리</h4>
            </div>
         </div>

         <div class="card" @keydown.esc="modalClose">
            <div class="card-body">
               <div class="row m-0">
                  <!-- 왼쪽 레이아웃 : 권한 목록  -->
                  <div class="col-3 treeview">
                     <div class="bottom-line p-2">
                        <button class="btn btn-success btn-sm" @click="btnAuthorityAdd"><i class="fa-solid fa-plus"></i> 권한 추가</button>
                     </div>

                     <div class="p-2">
                        <div v-for="(role, idx) in roles" :key="idx">
                           <div class="d-flex justify-content-between align-items-center">
                              <span class="role-item">{{ role.authorityNm }}</span>
                              <div class="options pe-2" @click.stop="optionsToggle(idx)">
                                 <i class="fa-solid fa-ellipsis-vertical font-13"></i>
                              </div>
                           </div>

                           <!-- 드롭다운 메뉴 -->
                           <di v-show="selectedRole == idx && isEditMenu == true" class="editing-dropdown-menu">
                              <div class="font-14 p-2" @click="btnAuthorityModify(role.authorityCd)">권한 수정</div>
                              <div class="font-14 p-2" @click="btnAuthorityRemove(role.authorityNm, role.authorityCd)">권한 삭제</div>
                           </di>
                        </div>
                     </div>
                  </div>

                  <!-- 오른쪽 레이아웃 : 메뉴목록 조회 -->
                  <div class="col-9 treeview">
                     <div class="d-flex justify-content-between align-items-center bottom-line p-2">
                        <span>권한명</span>
                        <div class="button-group">
                           <button class="btn btn-secondary btn-sm btn-fill">취소</button>
                           <button class="btn btn-success btn-sm btn-fill">저장</button>
                        </div>
                     </div>
                     <div class="bottom-line">
                        <menuTree v-for="(item, idx) in menuData" :key="idx" :item="item" ></menuTree>
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
                     <button type="button" class="btn btn-secondary btn-fill" @click="btnModalClose">닫기</button>
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
   import { ref, onMounted, onBeforeMount, onBeforeUnmount } from 'vue';
   import axios from "axios";
   import Swal from 'sweetalert2';
   import Card from '../../components/Cards/Card.vue'
   import Modal from '../../components/Modal.vue';
   import menuTree from './components/menuComponent.vue'


   onBeforeMount(() => {
      authorityGetList();  // 권한 목록 조회
      menuGetList();   // 메뉴 목록 조회
   });

   onMounted(() => {
      document.addEventListener("click", outsideClickHandler); // 바깥 클릭 시 수정, 삭제 드롭다운 메뉴 꺼짐
      document.addEventListener('keydown', modalClose);        // esc 클릭 시 모달 꺼짐
   });

   onBeforeUnmount(() => {
      document.removeEventListener('keydown', modalClose);
   });

   const menuData = ref([]);
   /**
    * @description 메뉴 목록 조회 API
    */
   const menuGetList = async () => {
      try {
         const response = await axios.get('/api/menu');

         menuData.value = treeBuild(response.data);
         console.log("menuData.value => ", menuData.value);
      } catch (err) {
         Swal.fire({
            icon: "error",
            title: "API 조회 오류",
            text: "Error : " + err.response.data
         });
      }
   }

   /**
    * @description 서버에서 가져온 메뉴 리스트를 트리 형태로 변경해주는 함수
    * @param {Array} menuArray - DB에서 불러온 메뉴 리스트
    * @returns {Array} 트리 형태의 메뉴 배열
    */
   function treeBuild(menuArray) {
      const map = new Map();
      const tree = [];

      // 1. 모든 항목을 맵에 저장 (하위 메뉴 배열, open, selected 속성 추가)
      menuArray.forEach(item => {
         map.set(item.menuCd, { ...item, subMenus: [], open: false, selected: false });
      });

      // 2. 부모-자식 관계를 설정
      menuArray.forEach(item => {
         if (item.parentMenuCd === null) {
            tree.push(map.get(item.menuCd));  // 루트 노드
         } else {
            const parent = map.get(item.parentMenuCd);
            if (parent) {
               parent.subMenus.push(map.get(item.menuCd));  // 부모의 subMenus에 추가
            }
         }
      });

      return tree;
   }
// ================================================== Modal ==================================================
   const isShowModal = ref(false);
   const isEditMenu = ref(false);
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
         if(isEditMenu.value) {
            isEditMenu.value = !isEditMenu.value
         }
      }
      if(e === "remove") {
         if(isShowModal.value) {
            isShowModal.value = !isShowModal.value
         }
      }
   };

   // 모달 input 초기화
   const modalReset = () => {
      authorityCd.value = '';
      authorityNm.value = '';
      description.value = '';
   };

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

   // 모달 닫기 버튼
   const btnModalClose = () => {
      modalClose("remove")
   }

// ======================================== 권한 관리 Axios 통신 ========================================
   // 권한 전체 조회
   const roles = ref([]);  // 권한 목록 배열
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

// ======================================== edit, delete Toggle DropDown Menu ========================================
   const selectedRole = ref(null); // 선택한 권한코드
   /**
    * @description 선택한 권한에 대한 수정 삭제 메뉴 토글
    * @param role 토글할 권한명 index
    */
   const optionsToggle = (role) => {
      if(isEditMenu.value == false) {
         isEditMenu.value = true;
      }
      selectedRole.value = selectedRole.value === role ? null : role;
   };

   /**
    * @description 드롭다운 메뉴가 나타났을 때 외부 클릭 시 드롭다운 메뉴 닫힘
    */
   const outsideClickHandler = () => {
      selectedRole.value = null;
   };

</script>