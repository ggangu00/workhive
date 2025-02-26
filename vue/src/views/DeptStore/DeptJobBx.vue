<template>
  <li>
    <div class="flex-container">
      <!-- 부서 열기/닫기 아이콘 -->
      <i :class="isOpen ? 'fa-regular fa-folder-open' : 'fa-solid fa-folder'" @click="toggle"></i>
      <span @click="toggle">{{ dept.deptNm }}</span>
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
      <li v-for="job in deptJobBoxes" :key="job.deptJobBxId" class="job-box">
        <i class="fa-solid fa-file" @click="jobBoxClicked(job)"></i>
        <span @click="jobBoxClicked(job)">{{ job.deptJobBxNm }}</span>
        <i class="fa-solid fa-pen" @click.stop="jobBxManage('modify', job)"></i>
        <i class="fa-solid fa-x" @click.stop="jobBxManage('remove', job)"></i>
      </li>
    </ul>
  </li>
</template>

<script setup>
import { ref, computed } from 'vue';
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

// 업무함 클릭시 vuex 정보 변경
const store = useStore();
const jobBoxClicked = (job) => {
  const relatedJobBoxes = props.jobBoxes.filter(j => j.deptCd === job.deptCd);

  console.log("jobbx click : ", job);
  store.dispatch('jobBxSelectedUpdate', { 
    searchDeptCd: job.deptCd, 
    searchDeptNm: job.deptNm,
    searchDeptjobBxId: job.deptJobBxId,
    searchDeptJobBxNm: job.deptJobBxNm,
  });
  
  store.dispatch('jobBxListUpdate', relatedJobBoxes);
};

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
