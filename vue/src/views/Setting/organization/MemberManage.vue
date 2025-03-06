<template>
   <div class="content">
      <div class="container-fluid" @keydown.esc="modalClose">
         <div class="card">
            <div class="card-header d-flex justify-content-between align-items-center">
               <h4 class="card-title">부서 관리</h4>
            </div>
         </div>

         <div class="card">
            <div class="card-body">
               <div class="row m-0">
                  <!-- 트리 뷰 (왼쪽) -->
                  <div class="col-3 depth treeview">
                     <button @click="modalOpen">모달 오픈</button>
                     <DepartmentComponent
                        v-if="Array.isArray(departmentTree.value) && departmentTree.value.length > 0"
                        :treeData="departmentTree.value"
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
         <Modal :isShowModal="isShowModal" :modalTitle="'부서 등록'" @click.self="modalClose">
            <!-- 모달 바디 -->
            <template v-slot:body>
               <card class="mb-0">
                  <div class="mb-3">
                     <label class="form-label">부서명 <i class="fa-solid fa-asterisk point-red"></i></label>
                     <input type="text" name="dept_name" class="form-control w30">
                  </div>
                  <div class="mb-3">
                     <div class="form-group has-label">
                        <label>부서 설명</label>
                     </div>
                     <textarea type="text" name="project_nm" class="form-control" placeholder="부서에 대한 설명을 입력하세요." style="height: 130px;"></textarea>
                  </div>
               </card>
            </template>

            <!-- 모달 푸터 -->
            <template v-slot:footer>
               <button type="button" class="btn btn-secondary btn-fill" @click="modalClose">닫기</button>
               <button type="button" class="btn btn-primary btn-fill" @click="confirm">등록</button>
            </template>
         </Modal>
         <!--업무등록 모달[e]-->
      </div>
   </div>
</template>

<script setup>
   import { ref, onBeforeMount } from "vue";
   import Swal from 'sweetalert2';
   import axios from "../../../assets/js/customAxios"; // 공통 Axios 설정 파일
   import Modal from '../../../components/Modal.vue';
   import Card from '../../../components/Cards/Card.vue';
   import DepartmentComponent from "../../../components/Department/DepartmentComponent.vue";

   const isShowModal = ref(false);
// ================================================== 생명주기 함수 ==================================================
   // 컴포넌트가 마운트되기 전에 권한 및 메뉴 목록 조회 실행
   onBeforeMount(async () => {
      await departmentGetList() // 비동기 완전 끝나야 데이터 있음
   });

// ================================================== 부서관련 axios ==================================================
   const departmentTree = ref([]);

   const departmentGetList = async () => {
      try {
         const response = await axios.get('/api/department')
         console.log('API 응답 데이터:', response.data)

         const tree = convertToTreeFormat(response.data)
         console.log('트리 변환 결과:', tree)

         departmentTree.value = tree
         console.log("departmentTree.value.length => ", departmentTree.value.length)

      } catch (err) {
         departmentTree.value = []  // 실패 시 안전장치

         Swal.fire({
            icon: "error",
            title: "API 조회 실패",
            text: `Error: ${err.response?.data?.error || err.message}`
         })

      }
   }

   const convertToTreeFormat = (flatList = []) => {
      console.log("flatList => ", flatList)
      const map = {}
      const tree = []

      flatList.forEach(dept => {
         const node = {
               id: dept.deptCd,
               label: dept.deptNm,
               children: []   // ✅ 이거 반드시 children
         }
         map[dept.deptCd] = node  // 자기 자신 등록

         if (dept.parentCd) {
               if (!map[dept.parentCd]) {
                  map[dept.parentCd] = { id: dept.parentCd, label: '(알 수 없음)', children: [] }
               }
               map[dept.parentCd].children.push(node)  // ✅ 여기도 children
         } else {
               tree.push(node)
         }
      })

      return tree
   }



   const members = ref([
      { id: "2025001", name: "김민진", rank: "부장" },
      { id: "2025002", name: "박주현", rank: "부장" },
   ]);

   const modalClose = (e) => {
      if (e.key === "Escape") {
         if(isShowModal.value) {
            isShowModal.value = !isShowModal.value
         }
      } else {
         isShowModal.value = false;
      }
   }
   const modalOpen = () => {
      isShowModal.value = true;
   }

</script>

<style scoped>
   .menu-container {
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
   }
   .dropdown-menu {
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
   }
</style>
