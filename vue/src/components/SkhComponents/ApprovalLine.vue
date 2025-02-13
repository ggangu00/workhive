<template>
  <div class="content">
    <div class="container-fluid">
      
      <!-- 🔹 1행: 부서 선택 & 수신 목록 -->
      <div class="row">
        
        <!-- 왼쪽: 부서 트리 구조 -->
        <div class="col-5">
          <div class="box">
            <h5>부서 선택</h5>
            <div class="tree-container">
              <ul>
                <li v-for="dept in departmentTree" :key="dept.name">
                  <span @click="toggleDept(dept)" class="tree-node">{{ dept.name }}</span>
                  <ul v-if="dept.expanded">
                    <li v-for="subDept in dept.children" :key="subDept">
                      <span @click="selectDept(subDept)">{{ subDept }}</span>
                    </li>
                  </ul>
                </li>
              </ul>
            </div>
          </div>
        </div>

        <!-- 가운데: 화살표 버튼 -->
        <div class="col-1 d-flex align-items-center justify-content-center">
          <button class="btn btn-warning mt-2 w-100" @click="addReceiver">수신 추가 ➡</button>
        </div>

        <!-- 오른쪽: 수신 목록 -->
        <div class="col-5">
          <div class="box">
            <h5>수신 목록</h5>
            <div class="approval-box">
              <div v-for="receiver in receivers" :key="receiver" class="approval-item">
                <span class="badge bg-warning text-dark">수신</span> [{{ receiver }}]
              </div>
            </div>
          </div>
        </div>

      </div>

      <!-- 🔹 2행: 직원 목록 & 결재 추가 -->
      <div class="row mt-4">
        <!-- 왼쪽: 직원 목록 (Toast UI Grid 적용) -->
        <div class="col-5">
          <div class="box">
            <h5>직원 목록</h5>
            <div id="employeeGrid"></div> <!-- ✅ Toast UI Grid -->
          </div>
        </div>

        <!-- 가운데: 결재 버튼 -->
        <div class="col-1 d-flex align-items-center justify-content-center">
          <button class="btn btn-primary" @click="addApproval">결재 ➡</button>
        </div>

        <!-- 오른쪽: 결재 목록 -->
        <div class="col-5">
          <div class="box">
            <h5>결재 목록</h5>
            <div class="approval-box">
              <div v-for="(approver, index) in approvers" :key="index" class="approval-item">
                <select v-model="approver.status" class="form-select form-select-sm">
                  <option value="결정">결정</option>
                  <option value="결재">결재</option>
                  <option value="기안">기안</option>
                </select>
                [{{ approver.dept }}] {{ approver.name }} {{ approver.title }}
              </div>
            </div>
          </div>
        </div>

      </div>

    </div>
  </div>
</template>

<script>
import { ref, computed, onMounted } from 'vue';
import "tui-grid/dist/tui-grid.css";
import Grid from "tui-grid";

export default {
  setup() {
    const selectedDept = ref('');
    const receivers = ref([]);
    const approvers = ref([]);
    let gridInstance = null; // ✅ Toast UI Grid 인스턴스

    const departmentTree = ref([
      { name: '총무팀', children: ['총무 1팀', '총무 2팀'], expanded: false },
      { name: '영업팀', children: ['영업 1팀', '영업 2팀'], expanded: false },
      { name: '기획팀', children: ['기획 1팀', '기획 2팀'], expanded: false },
      { name: '인사팀', children: ['인사 1팀', '인사 2팀'], expanded: false }
    ]);

    const employees = ref([
      { name: '김팀장', title: '팀장', dept: '인사 1팀' },
      { name: '김대리', title: '대리', dept: '인사 2팀' },
      { name: '박사원', title: '사원', dept: '영업 1팀' },
      { name: '이과장', title: '과장', dept: '총무 2팀' }
    ]);

    const filteredEmployees = computed(() => {
      return selectedDept.value ? employees.value.filter(emp => emp.dept === selectedDept.value) : [];
    });

    const toggleDept = (dept) => {
      dept.expanded = !dept.expanded;
    };

    const selectDept = (subDept) => {
      selectedDept.value = subDept;
      updateGridData();
    };

    const addReceiver = () => {
      if (selectedDept.value && !receivers.value.includes(selectedDept.value)) {
        receivers.value.push(selectedDept.value);
      }
    };

    const addApproval = () => {
      if (!gridInstance) return;
      const selectedData = gridInstance.getCheckedRows();
      selectedData.forEach(emp => {
        if (!approvers.value.find(appr => appr.name === emp.name)) {
          approvers.value.push({ ...emp, status: '결재' });
        }
      });
    };

    // ✅ Toast UI Grid 초기화
    onMounted(() => {
      gridInstance = new Grid({
        el: document.getElementById('employeeGrid'),
        data: employees.value, // 초기 데이터
        scrollX: false,
        scrollY: true,
        columns: [
          { header: '이름', name: 'name', sortable: true, align: 'center' },
          { header: '직책', name: 'title', sortable: true, align: 'center' },
          { header: '부서', name: 'dept', sortable: true, align: 'center' },
          { header: '선택', name: 'select', align: 'center', renderer: { type: 'checkbox' } }
        ]
      });
    });

    // ✅ 부서 선택 시 직원 목록 갱신
    const updateGridData = () => {
      if (gridInstance) {
        gridInstance.resetData(filteredEmployees.value);
      }
    };

    return {
      selectedDept,
      receivers,
      approvers,
      departmentTree,
      toggleDept,
      selectDept,
      employees,
      filteredEmployees,
      addReceiver,
      addApproval
    };
  }
};
</script>

<style scoped>
/* 전체 컨테이너 스타일 */
.content {
  padding: 20px;
}

/* 공통 박스 스타일 */
.box {
  border: 1px solid #ddd;
  padding: 15px;
  border-radius: 5px;
  background-color: #f8f9fa;
}

/* 결재 및 수신 박스 */
.approval-box {
  height: 300px;
  overflow-y: auto;
  border: 1px solid #ddd;
  padding: 10px;
}

/* 트리 구조 스타일 */
.tree-container {
  border: 1px solid #ddd;
  padding: 10px;
  max-height: 200px;
  overflow-y: auto;
}

.tree-node {
  cursor: pointer;
  font-weight: bold;
}

/* Toast UI Grid 스타일 */
#employeeGrid {
  width: 100%;
  height: 300px;
}
</style>
