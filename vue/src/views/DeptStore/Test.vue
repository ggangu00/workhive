<template>
  <div class="sidebar">
    <ul>
      <TreeNode
        v-for="dept in rootDepartments"
        :key="dept.deptCd"
        :dept="dept"
        :departments="departments"
        :jobBoxes="jobBoxes"
      />
    </ul>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import axios from '../../assets/js/customAxios';
import Swal from 'sweetalert2';
import TreeNode from './TreeNode.vue';

// 부서 및 업무함 데이터 상태
const departments = ref([]);
const jobBoxes = ref([]);

// API 요청 함수
const fetchDepartments = async () => {
  try {
    const response = await axios.get('/api/department');
    departments.value = response.data;
    console.log("부서 : ", response.data);
  } catch (error) {
    Swal.fire({ icon: "error", title: "부서 조회 실패", text: error.message });
  }
};

const fetchJobBoxes = async () => {
  try {
    const response = await axios.get('/api/deptstore/jobBxList');
    jobBoxes.value = response.data.map(job => ({
      ...job,
      deptCd: job.deptId  // deptId를 deptCd로 통일
    }));
    console.log("업무함 : ", response.data);
  } catch (error) {
    Swal.fire({ icon: "error", title: "업무함 조회 실패", text: error.message });
  }
};

// 최상위 부서 찾기 (depth === 0)
const rootDepartments = computed(() =>
  departments.value.filter(dept => dept.depth === 0)
);

// API 호출
onMounted(() => {
  fetchDepartments();
  fetchJobBoxes();
});
</script>

<style scoped>
.sidebar {
  width: 250px;
  background: #f8f9fa;
  padding: 10px;
  border-right: 1px solid #ddd;
}
ul {
  list-style: none;
  padding-left: 15px;
}
li {
  margin: 5px 0;
}
span {
  font-weight: bold;
  cursor: pointer;
}
.job-box {
  color: blue;
  font-style: italic;
  margin-left: 15px;
}

/* 아이콘 스타일 */
i {
  margin-right: 8px;
}
</style>