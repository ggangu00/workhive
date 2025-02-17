<template>
  <ul class="dept-list">
    <li v-for="dept in departments" :key="dept.id">
      <div @click="toggleDept(dept)" class="flex-container">
        <i :class="dept.open ? 'fa-regular fa-folder-open' : 'fa-solid fa-folder'"></i>
        <span :data-id="dept.id">{{ dept.name }}</span>
        <i class="fa-solid fa-plus" @click.stop="addDept(dept)"></i>
      </div>
      <ul v-if="dept.open">
        <li v-for="sub in dept.subDepartments" :key="sub.id">
          <div @click="toggleDept(sub)" class="flex-container">
            <i :class="sub.open ? 'fa-regular fa-folder-open' : 'fa-solid fa-folder'"></i>
            <span :data-id="dept.id">{{ sub.name }}</span>
            <i class="fa-solid fa-plus" @click.stop="addDept(sub)"></i>
          </div>
          <ul v-if="sub.open">
            <li v-for="job in sub.jobs" :key="job.id" class="flex-container">
              <i class="fa-solid fa-file"></i>
              <span :data-id="job.id">{{ job.name }}</span>
              <i class="fa-solid fa-pen" @click.stop="editJob(job)"></i>
              <i class="fa-solid fa-x" @click.stop="removeJob(job)"></i>
            </li>
          </ul>
        </li>
      </ul>
    </li>
  </ul>

  <Modal
    :isShowModal="isShowModal"
    :modalTitle="'업무함 관리'"
    @click.self="modalClose"
  >
    <template v-slot:body>
      <div class="content">
        <div class="container-fluid">
          <div class="card">
            <div class="card-body">

              <div class="col mb-3">
                <div class="row">
                  <div class="col">
                    <label>부서명</label>
                  </div>
                  <div class="col">
                    <label>표시순서<em class="point-red">*</em></label>
                  </div>
                </div>
                <div class="row align-items-center">
                  <div class="col-6">
                    <input type="text" class="form-control" placeholder="부서명" readonly>
                  </div>
                  <div class="col-6">
                    <input type="number" class="form-control" placeholder="0">
                  </div>
                </div>
              </div>

            </div>
          </div>
        </div>
      </div>
    </template>

    <template v-slot:footer>
      <button class="btn btn-secondary btn-fill mx-2" @click="modalClose">닫기</button>
      <button class="btn btn-success btn-fill mx-2" @click="modalConfirm">저장</button>
    </template>
  </Modal>

  <button @click="datareturn">임시</button>
</template>
  
<script setup>
  import { ref, onMounted } from 'vue';
  import axios from 'axios';

  import Modal from '../../components/Modal.vue';


  let deptList = [];
  let jobbxList = [];
  // 부서 목록 조회  
  const deptGetList = async () => {
    let result = await axios.get('/api/deptstore/deptlist')
                            .catch(error => console.error("에러 :", error));
    
    deptList = result.data.resultList;
  };
  // 업무함 목록 조회
  const jobbxGetList = async () => {
    let result = await axios.get('/api/deptstore/jobbxlist')
                            .catch(error => console.error("에러 :", error));
    
    jobbxList = result.data;
  };

  const departments = ref([
    {
      id: 1, name: "개발팀", open: false,
      subDepartments: [
        { id: 2, name: "서버팀", open: false, jobs: [{ id: 101, name: "서버 유지보수" }] }
      ]
    },
    {
      id: 3, name: "디자인팀", open: false,
      subDepartments: [
        { id: 4, name: "UI 디자인팀", open: false, jobs: [{ id: 102, name: "앱 UI 개선" }] }
      ]
    }
  ]);

  const toggleDept = (dept) => {
    dept.open = !dept.open;
  };
  const addDept = (parent) => {
    console.log("부서 추가", parent);
  };
  const editJob = (job) => {
    console.log("업무 수정", job);
  };
  const removeJob = (job) => {
    console.log("업무 삭제", job);
  };

  // 모달
  const isShowModal = ref(false);
  const modalOpen = () => {
    isShowModal.value = true;
  }
  const modalClose = () => {
    isShowModal.value = false;
  }
  const modalConfirm = () => {
    isShowModal.value = false;
  }

  const deptName = ref();
  onMounted(() => {
    document.querySelector('.dept-list').addEventListener('click', (e) => {
      if (e.target.classList.contains('fa-plus')) {
        deptName.value = e.target.previousElementSibling.dataset.id;
        
        modalOpen();
      }
    })

    deptGetList();
    jobbxGetList();
  })

  // 업무함 클릭시 부서/업무함 정보 전달
  const emit = defineEmits(['datareturn']);
  const datareturn = () => {
    emit('datareturn', { deptId: deptList[0].orgnztId, jobbxId: jobbxList[0].deptJobBxId, jobbxList: jobbxList });
  };

</script>
  
<style>
  ul {
    list-style-type: none;
    padding: 5px 8px !important;
  }
  .flex-container {
    display: flex;
    align-items: center;
    gap: 5px;
  }
</style>
  
  
  