<template>
  <div class="content">
    <div class="container-fluid">     
      <!-- 부서 선택-->
      <div class="row">
        <!-- 부서 트리 구조 -->
        <div class="col-5">
          <div class="box">
            <h5>부서 선택</h5>
            <div class="tree-container">
              <ul>
                <li v-for="dept in departmentTree" :key="dept.deptCd">
                  <span @click="toggleDept(dept), selectDept(dept)" class="tree-node">
                    <i :class="dept.expanded ? 'fa-solid fa-folder-open' : 'fa-solid fa-folder'"></i>
                    {{ dept.name }}
                  </span>

                  <!-- 하위 부서 렌더링 -->
                  <ul v-if="dept.expanded && (dept.children || []).length">
                    <li v-for="subDept in dept.children || []" :key="subDept.deptCd">
                      <span @click="toggleDept(subDept), selectDept(subDept)" class="tree-node">
                        <i :class="subDept.expanded ? 'fa-solid fa-folder-open' : 'fa-solid fa-folder'"></i>
                        {{ subDept.name }}
                      </span>

                      <!-- 하위-하위 부서도 렌더링  -->
                      <ul v-if="subDept.expanded && (subDept.children || []).length">
                        <li v-for="subSubDept in subDept.children || []" :key="subSubDept.deptCd">
                          <span @click="selectDept(subSubDept)">
                            <i class="fa-solid fa-folder" ></i>
                            {{ subSubDept.name }}
                          </span>
                        </li>
                      </ul>
                    </li>
                  </ul>
                </li>
              </ul>
            </div>
          </div>
        </div>

        <!-- 화살표 버튼 -->
        <div class="col-1 d-flex align-items-center justify-content-center">
          <button class="btn btn-warning mt-2 w-100" @click="addReceiver">수신 추가 ➡</button>
        </div>

        <!-- 수신 목록 -->
        <div class="col-5">
          <div class="box">
            <h5>수신 목록</h5>
            <span>수신</span>
            <div class="approval-box">
              <div v-for="(receiver, index) in receivers" :key="index" class="approval-item">
                <span class="badge bg-warning text-dark">수신</span>
                <span v-if="receiver.name">[{{ receiver.title }}] {{ receiver.name }}<button @click="removeReceiver(index)" class="btn btn-sm btn-danger">삭제</button></span> <!-- [직책]사원 -->
                <span v-else>[{{ receiver.dept }}]<button @click="removeReceiver(index)" class="btn btn-sm btn-danger">삭제</button></span> <!-- 부서 -->                
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 결재 추가 -->
      <div class="row mt-4">
        <!-- 직원 목록 -->
        <div class="col-5">
          <div class="box">
            <h5>직원 목록</h5>
            <div id="employeeGrid"></div>
          </div>
        </div>

        <!-- 결재 버튼 -->
        <div class="col-1 d-flex align-items-center justify-content-center">
          <button class="btn btn-primary" @click="addApproval">결재 ➡</button>
        </div>

        <!-- 결재 목록 -->
        <div class="col-5">
          <div class="box">
            <h5>결재 목록</h5>
            <div class="approval-box">
              <div v-for="(approver, index) in reversedApprovers" :key="index" class="approval-item">
                <select v-model="approver.status" class="form-select form-select-sm">
                  <option value = "결정" name="K04">결정</option>
                  <option value = "결재" name="K02">결재</option>
                  <option value = "기안" name="K01">기안</option>
                </select>
                [{{ approver.dept }}] {{ approver.name }} {{ approver.title }}
                <button @click="removeApproval(index)" class="btn btn-sm btn-danger">삭제</button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch, computed} from 'vue';
import "tui-grid/dist/tui-grid.css";
import Grid from "tui-grid";
import axios from 'axios';

  let gridInstance = null; 

    //역으로표현
    const reversedApprovers = computed(() => [...approvers.value].reverse());

    const selectedDept = ref(''); //UI가 자동 업데이트
    const receivers = ref([]);
    const approvers = ref([]);
    const employees = ref([]); // 직원 목록
    const departmentTree = ref([]);

    //부서정보가져오기
    const deptList = async () => {
      try {
        const response = await axios.get('/api/department');
        const deptData = response.data;

        const departmentsTree = (deptList, parentCd = null, depth = 0) => {
          if (depth > 10) return [];

          return deptList
            .filter(dept => dept.parentCd == parentCd)
            .map(dept => ({
              deptCd: dept.deptCd,
              name: dept.deptNm,
              expanded: false,
              children: departmentsTree(deptList, dept.deptCd, depth + 1) || []
            }));
        };

        departmentTree.value = departmentsTree(deptData);
        console.log(deptData);
      } catch (error) {
        console.error("부서 목록 불러오기 실패:", error);
      }
    };
    
    //사원목로가져오기
    const getDeptMembers = async (deptCd) => {
      try {
          const response = await axios.get(`/api/document/deptMember`, 
          { params: { deptCd : deptCd } 
        });
        employees.value =response.data;
        console.log(response.data)
        updateGridData();
      } catch (error) {
        console.error("사원 목록 불러오기 실패:", error);
      }
    };

    const toggleDept = (dept) => {
      if (!dept.expanded) {
        dept.expanded = false; 
      }
      dept.expanded = !dept.expanded;
    };

    const selectDept = (dept) => {
      selectedDept.value = dept.name;
      getDeptMembers(dept.deptCd) ;

      updateGridData() ;
    };

    //수신자추가기능
    const addReceiver = () => {
      console.log(selectedDept);
      //부서추가
      if (selectedDept.value && !receivers.value.some(receiver => receiver.dept === selectedDept.value)) {
        receivers.value.push({
          dept: selectedDept.value, 
          name: "", 
          title: "", 
          status: "수신" 
        });
      }
      //사원추가
      const selectedData = gridInstance.getCheckedRows();
      console.log(selectedData)

      selectedData.forEach(emp => {
        const empName = emp.mberNm;  
        const empTitle = emp.respCd

        if (!receivers.value.some(receiver => receiver.name == empName)) {
            receivers.value.push({
              name: empName,
              dept: emp.deptNm,  // 부서명도 같이 저장
              title: empTitle,
              status: "수신",

            });
          }
        });
    };
    // 수신자 삭제 기능
    const removeReceiver = (index) => {
      receivers.value.splice(index, 1);
    };

    //결재추가기능
    const addApproval = () => {
      if (!gridInstance) return;
      const selectedData = gridInstance.getCheckedRows();
      console.log(selectedData)
      selectedData.forEach(emp => {
        if (!approvers.value.find(appr => appr.mberNm == emp.mberNm)) {
          approvers.value.push({ 
            name: emp.mberNm || emp.name,  
            dept: emp.deptNm || emp.dept, 
            title: emp.respCd, 
            status: '결재',
          mberId: emp.mberId});
        }
      });
    };

    // 결재자 삭제 기능
    const removeApproval = (index) => {
      approvers.value.splice(index, 1);
      if (approvers.value[index].status == "기안") {
        alert("기안자는 삭제불가");
        return;
      }
    };
    //토스트그리드 불러오기
    const initGrid = () => {
      gridInstance = new Grid({
        el: document.getElementById('employeeGrid'),
        data: employees.value,
        scrollX: false,
        scrollY: true,
        rowHeaders: ['checkbox'],
        columns: [
          { header: '이름', name: 'mberNm' },
          { header: '직책', name: 'respCd' },
          { header: '부서', name: 'deptNm' },
        ]
      });
    };

    // const filteredEmployees = computed(() => { //자동계산
    //   return selectedDept.value ? employees.value.filter(emp => emp.dept == selectedDept.value) : [];
    // });

    //  부서 선택 시 직원 목록 갱신
    const updateGridData = () => {
      if (gridInstance) {
      gridInstance.resetData(employees.value); //토스트유아이 내장메서드 데이터 업데이트나 새로 교체
      }
    };
    watch(selectedDept, () => {
      console.log('ads');
      updateGridData();
    });

    //로그인정보(임시)
    const login = ref({
      name: "신강현", 
      dept: "총무팀",
      title: "대리",
      mberId: "admin3"
    });
    //  모달이 열릴 때 Toast UI Grid를 다시 초기화
    const onModalOpen = () => {
      setTimeout(() => {
        if (!gridInstance) {
          initGrid();
        } else {
          gridInstance.resetData(employees.value);
        }
        
        if (selectedDept.value) {
          gridInstance.resetData(employees.value);
        }
      },300);
      //로그인 기안자
      if (!approvers.value.find(a => a.status === "기안")) {
      approvers.value.push({
        name: login.value.name,
        dept: login.value.dept,
        title: login.value.title,
        status: "기안",
        mberId: login.value.mberId
      });
  }
    };

    onMounted(() => {
      deptList();

    });

    defineExpose({onModalOpen, approvers, receivers, reversedApprovers});
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
  height: 400px;
  background-color: #f8f9fa;
}

/* 결재 및 수신 박스 */
.approval-box {
  height: 80%;
  overflow-y: auto;
  border: 1px solid #ddd;
  padding: 5px;
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
  height: 100%;
}
</style>
