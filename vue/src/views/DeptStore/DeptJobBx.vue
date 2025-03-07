<template>
  <!-- <li class="ms-3"> -->
  <li :class="['ms-3', 'my-1', `depth-${dept.depth}`]" :style="{ fontSize: `${18 - dept.depth}px`, width: `${100 - dept.depth * 5}%` }">
    <div class="flex-container">
      <!-- 부서 열기/닫기 아이콘 -->
      <i :class="isOpen ? 'fa-solid fa-angle-down' : 'fa-solid fa-angle-right'" @click="toggle"></i>
      <div @click="deptClicked(dept)"><span @click="toggle">{{ dept.deptNm }}</span></div>
      <i class="fa-solid fa-plus" @click.stop="jobBxManage('add', dept)"></i>
    </div>

    <!-- 하위 부서 및 업무함을 같은 위치에 배치 -->
    <ul v-if="isOpen">
      <!-- 하위 부서 렌더링 -->
      <li v-for="child in children" :key="child.deptCd">
        <DeptJobBx
          :dept="child"
          :departments="departments"
          :jobBoxes="jobBoxes"
        />
      </li>

      <!-- 업무함 렌더링 -->
      <li v-for="job in deptJobBoxes" :key="job.deptJobBxId" class="job-box ms-3 my-1">
        <div class="flex-container">
          <i class="folder bi bi-folder-plus bi bi-folder" @click="jobBoxClicked(job)"></i>
          <span @click="jobBoxClicked(job)">{{ job.deptJobBxNm }}</span>
          <i class="bi bi-pencil-square" @click.stop="jobBxManage('modify', job)"></i>
          <i class="fa-solid fa-xmark align-self-center" @click.stop="jobBxManage('remove', job)"
             :style="{ fontSize: `${23 - dept.depth}px`}"></i>
        </div>
      </li>
    </ul>
  </li>
</template>

<script setup>
import { ref, computed, watch } from 'vue';
import { useStore } from 'vuex';
import { inject } from 'vue'; // 부모에게 받아오기

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

// 부모로부터 jobBxManage 함수 받아오기
const jobBxCheck = inject('jobBxCheck');

// 자식에서 발생한 이벤트 처리
const jobBxManage = (type, data) => {
  jobBxCheck(type, data); // 부모로 이벤트 전달
};

// 업무함 관리 기능 감시
watch(() => props.jobBoxes, () => {
  if(selectedJobBx != undefined) jobBoxClicked(selectedJobBx);
});

const store = useStore();
let selectedJobBx;

// 부서 선택
const deptClicked = (dept) => {
  const relatedJobBoxes = props.jobBoxes.filter(j => j.deptCd === dept.deptCd);

  store.dispatch('jobBxSelectedUpdate', { 
    searchDeptCd: dept.deptCd, 
    searchDeptNm: dept.deptNm,
    searchDeptJobBxId: '',
    searchDeptJobBxNm: '',
  });

  store.dispatch('jobBxListUpdate', relatedJobBoxes);
}

// 업무함 클릭시 vuex 정보 변경
const jobBoxClicked = (job) => {
  selectedJobBx = job;
  const relatedJobBoxes = props.jobBoxes.filter(j => j.deptCd === job.deptCd);

  store.dispatch('jobBxSelectedUpdate', { 
    searchDeptCd: job.deptCd, 
    searchDeptNm: job.deptNm,
    searchDeptJobBxId: job.deptJobBxId,
    searchDeptJobBxNm: job.deptJobBxNm,
  });
  
  store.dispatch('jobBxListUpdate', relatedJobBoxes);
};

</script>

<style scoped>
.flex-container {
  display: flex;
  align-items: center;
}

span {
  font-weight: bold;
  cursor: pointer;
  margin-right: 8px;
}

i {
  margin-right: 8px;
  cursor: pointer;
}

</style>
