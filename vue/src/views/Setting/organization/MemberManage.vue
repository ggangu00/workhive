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
                  <div class="mb-3" v-show="isEditMode">
                     <label class="form-label">부서코드 <i class="fa-solid fa-asterisk point-red"></i></label>
                     <input type="text" v-model="deptCd" class="form-control w30">
                  </div>
                  <div class="mb-3">
                     <label class="form-label">부서명 <i class="fa-solid fa-asterisk point-red"></i></label>
                     <input type="text" v-model="deptNm" class="form-control w30">
                  </div>
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
               <button type="button" class="btn btn-secondary btn-fill" @click="modalClose">닫기</button>
               <button v-if="isEditMode" type="button" class="btn btn-success btn-fill" @click="btnDeptModify">수정</button>
               <button v-else type="button" class="btn btn-primary btn-fill" @click="btnDeptAdd">등록</button>
            </template>
         </Modal>
         <!--업무등록 모달[e]-->
      </div>
   </div>
</template>

<script setup>
   import { ref, onBeforeMount, computed } from "vue";
   import Swal from 'sweetalert2';
   import axios from "../../../assets/js/customAxios"; // 공통 Axios 설정 파일
   import Modal from '../../../components/Modal.vue';
   import Card from '../../../components/Cards/Card.vue';
   import DepartmentComponent from "../../../components/Department/DepartmentComponent.vue";

   const isShowModal = ref(false);
   const isEditMode = ref(false);
   const deptCd = ref("");
   const deptNm = ref("");
   const description = ref("");
// ================================================== 생명주기 함수 ==================================================
   // 컴포넌트가 마운트되기 전에 권한 및 메뉴 목록 조회 실행
   onBeforeMount(async () => {
      await departmentGetList();
      isTreeLoaded.value = true;
   });

// ================================================== 모달 이벤트 ==================================================
   // 모달 타이틀 동적 변경
   const modalTitle = computed(() => (isEditMode.value ? "부서 수정" : "부서 등록"));

   const modalOpen = () => {
      isShowModal.value = true;
   }

   const modalClose = (e) => {
      if (!e) {
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
   const btnDeptAdd = () => {
      departmentAdd();

   }

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

   // 젤 상위 부서 추가
   const departmentAdd = async () => {
      const requestData = {
         deptNm: deptNm.value,
         description: description.value
      }

      try {
         const response = await axios.post('/api/department', requestData);

         if (response.data.result === true) {

            const updatedTree = buildPrimeVueTree(response.data.deptList);
            departmentTree.value = updatedTree; // ✅ UI 업데이트

            modalClose(); // 모달 닫기
         }
      } catch (err) {
         Swal.fire({
            icon: "error",
            title: "API 조회 실패",
            text: `Error: ${err.response?.data?.error || err.message}`
         });
      }
   }

   const buildPrimeVueTree = (flatList) => {
      const map = new Map();

      // 전체 데이터 Map에 먼저 등록
      flatList.forEach(item => {
         if (!item.deptCd) {
            console.warn("🚨 deptCd가 없는 데이터 발견!", item);
            return; // deptCd가 없으면 등록하지 않음
         }
         map.set(item.deptCd, {
            key: item.deptCd,
            label: item.deptNm,
            children: []
         });
      });

      const tree = [];

      // 부모-자식 연결
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

<style scoped>
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
