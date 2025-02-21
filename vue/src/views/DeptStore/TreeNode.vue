<template>
  <li>
    <div class="flex-container">
      <!-- 부서 열기/닫기 아이콘 -->
      <i :class="isOpen ? 'fa-regular fa-folder-open' : 'fa-solid fa-folder'" @click="toggle"></i>
      <span @click="toggle">{{ dept.deptNm }}</span>
      <i class="fa-solid fa-plus" @click.stop="addJobBx(dept)"></i>
    </div>

    <!-- 하위 부서 및 업무함을 같은 위치에 배치 -->
    <ul v-if="isOpen">
      <!-- 하위 부서 렌더링 -->
      <li v-for="child in children" :key="child.deptCd">
        <TreeNode
          :dept="child"
          :departments="departments"
          :jobBoxes="jobBoxes"
        />
      </li>

      <!-- 업무함 렌더링 -->
      <li v-for="job in deptJobBoxes" :key="job.deptJobBxId" class="job-box">
        <i class="fa-solid fa-file" @click="handlerDeptJobBx(job)"></i>
        <span @click="handlerDeptJobBx(job)">{{ job.deptJobBxNm }}</span>
        <i class="fa-solid fa-pen" @click.stop="editJobBx(job)"></i>
        <i class="fa-solid fa-x" @click.stop="removeJobBx(job)"></i>
      </li>
    </ul>
  </li>
</template>

<script setup>
import { ref, computed } from 'vue';

const props = defineProps({
  dept: Object,
  departments: Array,
  jobBoxes: Array,
});

// 상태: 트리 노드 열기/닫기
const isOpen = ref(false);
const toggle = () => {
  isOpen.value = !isOpen.value;
};

// 현재 부서의 하위 부서 필터링
const children = computed(() =>
  props.departments.filter(d => d.parentCd === props.dept.deptCd)
);

// 현재 부서의 업무함 필터링
const deptJobBoxes = computed(() =>
  props.jobBoxes.filter(j => j.deptCd === props.dept.deptCd)
);

const addJobBx = (parent) => {
  console.log("업무함 추가", parent);
};
const editJobBx = (job) => {
  console.log("업무함 수정", job);
};
const removeJobBx = (job) => {
  console.log("업무함 삭제", job);
};

const handlerDeptJobBx = (job) => {
  console.log("업무함 정보 : ", job);
}
</script>

<style scoped>
.flex-container {
  display: flex;
  align-items: center;
  gap: 8px;
}

span {
  font-weight: bold;
  cursor: pointer;
}

i {
  margin-right: 8px;
}

/* 아이콘 스타일 */
.job-box {
  color: blue;
  font-style: italic;
}

ul {
  list-style-type: none;
  padding-left: 15px;
}

li {
  margin: 5px 0;
}
</style>


