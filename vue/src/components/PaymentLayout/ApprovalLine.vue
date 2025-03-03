<template>
  <div class="content">
    <div class="container-fluid">
      <!-- 부서 선택-->
      <div class="row">
        <!-- 부서 트리 구조 -->
        <div class="col-5">
          <div class="box">
            <h6>부서 선택</h6>
            <div class="tree-container">
              <ul>
                <li v-for="dept in departmentTree" :key="dept.deptCd">
                  <span @click="toggleDept(dept), selectDept(dept)" class="tree-node">
                    <i :class="dept.expanded ? 'fa-solid fa-folder-open' : 'fa-solid fa-folder'"></i>
                    {{ dept.deptNm }}
                  </span>

                  <!-- 하위 부서 렌더링 -->
                  <ul v-if="dept.expanded && (dept.children || []).length">
                    <li v-for="subDept in dept.children || []" :key="subDept.deptCd">
                      <span @click="toggleDept(subDept), selectDept(subDept)" class="tree-node">
                        <i :class="subDept.expanded ? 'fa-solid fa-folder-open' : 'fa-solid fa-folder'"></i>
                        {{ subDept.deptNm }}
                      </span>

                      <!-- 하위-하위 부서도 렌더링  -->
                      <ul v-if="subDept.expanded && (subDept.children || []).length">
                        <li v-for="subSubDept in subDept.children || []" :key="subSubDept.deptCd">
                          <span @click="selectDept(subSubDept)">
                            <i class="fa-solid fa-folder" ></i>
                            {{ subSubDept.deptNm }}
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
        <div class="col-2 d-flex align-items-center justify-content-center">
          <button class="btn btn-warning mt-2 w-60" @click="addReceiver">수신 추가 ➡</button>
        </div>

        <!-- 수신 목록 -->
        <div class="col-5">
          <div class="box">
            <h6>수신 목록</h6>
            <div class="recept-box">
              <div v-for="(receiver, index) in receivers" :key="index" class="approval-item">
                <span class="badge bg-warning text-dark">수신</span>
                <span v-if="receiver.mberNm">[{{ receiver.gradeNm }}] {{ receiver.mberNm }}<button @click="removeReceiver(index)" class="btn btn-sm btn-danger">삭제</button></span> <!-- [직책]사원 -->
                <span v-else>[{{ receiver.deptNm }}]<button @click="removeReceiver(index)" class="btn btn-sm btn-danger">삭제</button></span> <!-- 부서 -->
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
            <h6>직원 목록</h6>
            <div id="employeeGrid"></div>
          </div>
        </div>

        <!-- 결재 버튼 -->
        <div class="col-2 d-flex align-items-center justify-content-center">
          <button class="btn btn-primary" @click="addApproval">결재 ➡</button>
        </div>

        <!-- 결재 목록 -->
        <div class="col-5">
          <div class="box">
            <h6>결재 목록</h6>
            <div class="approval-box">
              <div
                v-for="(approver, index) in approvers"
                :key="approver.mberId"
                class="approval-item d-flex align-items-center"
                draggable="true"
                @dragstart="startDrag(index, $event)"
                @dragover.prevent
                @drop="onDrop(index)" style="height: 30%">
                <i class="fa-solid fa-grip-vertical"></i>
                <select v-model="approver.signName" @change='signNameChange(index)' class="form-select form-select-sm mx-2" style='width: 100px;'>
                  <option v-for="(data, idx) in selectedData" :key="idx" :value="data.commDtlCd" >
                    {{ data.commDtlNm }}
                  </option>
                </select>
                [{{ approver.deptNm }}] {{ approver.mberNm }} {{ approver.gradeNm }}
                <button @click="removeApproval(index)" class="btn btn-sm btn-danger ms-2">삭제</button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch, toRefs } from 'vue';
import "tui-grid/dist/tui-grid.css";
import Grid from "tui-grid";
import axios from '../../assets/js/customAxios.js';
import { useUserInfoStore } from '../../store/userStore.js';
import Swal from 'sweetalert2';

const userInfoStore = useUserInfoStore();
let loginUser = userInfoStore.user ? userInfoStore.user.mberId : ""; // 로그인한 사용자 정보 가져오기
let gridInstance = null;

const selectedDept = ref(''); //UI가 자동 업데이트
const employees = ref([]); // 직원 목록
const departmentTree = ref([]);
const selectedData = ref([]); //결재상태(signname) 셀렉트박스

const props = defineProps({
  approvers: { type: Array, required: true },
  receivers: { type: Array, required: true },
});

const { approvers, receivers } = toRefs(props);

//공통코드 가져오기
const commonDtlList = async () =>{
  const docKind = await axios.get(`/api/comm/codeList`, {
    params: {cd:'AN'}
  });
  selectedData.value = [...docKind.data]
}

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
        deptNm: dept.deptNm,
        expanded: false,
        children: departmentsTree(deptList, dept.deptCd, depth + 1) || []
      }));
    };
  departmentTree.value = departmentsTree(deptData);
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
  selectedDept.value = dept;
  getDeptMembers(dept.deptCd) ;

  updateGridData() ;
};

//수신자추가기능
const addReceiver = () => {
  //부서추가
  if (selectedDept.value && !receivers.value.some(receiver => receiver.deptCd == selectedDept.value.deptCd)) {
    receivers.value.push({
      deptNm: selectedDept.value.deptNm,
      deptCd: selectedDept.value.deptCd,
      status: "수신"
    });
  }
  //사원추가
  const selectedData = gridInstance.getCheckedRows();
  selectedData.forEach(emp => {
    if (!receivers.value.some(receiver => receiver.mberId == emp.mberId)) {
        receivers.value.push({
          mberNm: emp.mberNm,
          mberId: emp.mberId,
          deptNm: emp.deptNm,  // 부서명도 같이 저장
          deptCd: emp.deptCd,
          gradeNm: emp.gradeNm,
          gradeCd: emp.gradeCd,
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
  selectedData.forEach(emp => {
    if (!approvers.value.find(appr => appr.mberNm == emp.mberNm)) {
      approvers.value.unshift({
        mberNm: emp.mberNm,
        deptNm: emp.deptNm,
        deptCd: emp.deptCd,
        gradeNm: emp.gradeNm,
        gradeCd: emp.gradeCd,
        signName: 'K02',
        mberId: emp.mberId});
    }
  });
};

//-------------------드래그--------------------------
// 드래그 시작 이벤트 (드래그한 요소의 인덱스를 저장)
const dragIndex = ref(null);

const startDrag = (index, event) => {
dragIndex.value = index; //현재 드래그 인덱스 저장
event.dataTransfer.effectAllowed = "move";   // 드래그 효과 설정
};

// 드롭 이벤트 (드래그한 아이템을 새로운 위치로 이동)
const onDrop = (index) => {
  if (dragIndex.value == null || dragIndex.value == index) return;

  if (approvers.value[dragIndex.value].signName == "K01") return; //기안자는 이동불가
  if(approvers.value[index].signName == "K01") return;

  const movedItem = approvers.value.splice(dragIndex.value, 1)[0]; // 기존 위치에서 제거 기존위치 데이터
  approvers.value.splice(index, 0, movedItem); // 새로운 위치에 삽입

  const data = approvers.value.find((a)=> a.signName =="K04");
  if(index==0){
    if(data){
      data.signName = 'K02' //기존데이터 결재로
    }
    movedItem.signName = 'K04' //움직인 데이터 결정으로
  }

  if (approvers.value[0] != movedItem) {
    //결정 중간으로 내려가는 걸 방지
    if (movedItem.signName == "K04") {
      movedItem.signName = "K02"; // 내려가면 자동으로 결재(K02)로 변경
    }
    if (data) {
      approvers.value[0].signName = "K04"; // 맨 위 요소를 자동으로 결정(K04)로 변경
    }
  }

};
//---------------------------------------------------

//------------------결재자 결정기안 개수제한---------------------------
const signNameChange = (idx) => {
const signNameValue = approvers.value[idx];

// 기안(K01)으로 변경할 때, 이미 기안이 있으면 불가능
if (signNameValue.signName == "K01" && approvers.value.some(a => a.signName == "K01" && a !== signNameValue)) {
Swal.fire({
  icon: "warning",
  title: "결재선 오류",
  text: "기안자는 한명만 가능"
});
signNameValue.signName = "K02"; // 원래대로 되돌림
return;
}

// 결정(K04)으로 변경할 때, 이미 결정이 있으면 불가능
if (signNameValue.signName == "K04" && approvers.value.some(a => a.signName == "K04" && a !== signNameValue)) {
Swal.fire({
  icon: "warning",
  title: "결재선 오류",
  text: "결정자는 한명만 가능"
});
signNameValue.signName = "K02"; // 원래대로 되돌림
return;
}

// 기안(K01)은 항상 맨 아래로 이동
if (signNameValue.signName == "K01") {
approvers.value.splice(idx, 1); // 기존 위치에서 제거
approvers.value.push(signNameValue); // 맨 아래 추가
}

// 결정(K04)은 항상 맨 위로 이동
if (signNameValue.signName == "K04") {
approvers.value.splice(idx, 1); // 기존 위치에서 제거
approvers.value.unshift(signNameValue); // 맨 위 추가
}
};

//---------------------------------------------------------
// 결재자 삭제 기능
const removeApproval = (index) => {
  if (approvers.value[index] && approvers.value[index].signName == "K01") {
    alert("기안자는 삭제할 수 없습니다.");
    return;
  }
  approvers.value.splice(index, 1);
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
      { header: '직책', name: 'gradeNm' },
      { header: '부서', name: 'deptNm' },
    ]
  });
};

//  부서 선택 시 직원 목록 갱신
const updateGridData = () => {
  if (gridInstance) {
  gridInstance.resetData(employees.value); //토스트유아이 내장메서드 데이터 업데이트나 새로 교체
  }
};
watch(selectedDept, () => {
  updateGridData();
});

//로그인정보
const login = ref({
  mberNm: userInfoStore.user.mberNm,
  deptNm: userInfoStore.user.deptNm,
  gradeNm: userInfoStore.user.gradeNm,
  mberId: loginUser
});
//  모달이 열릴 때 Toast UI Grid를 다시 초기화
const onModalOpen = async() => {
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
  if (!approvers.value.find(a => a.signName == "K01")) {
    approvers.value.push({
      mberNm: login.value.mberNm,
      deptNm: login.value.deptNm,
      gradeNm: login.value.gradeNm,
      signName: "K01",
      mberId: login.value.mberId
    });
  }
};
onMounted(() => {
  deptList();
  commonDtlList();
});

defineExpose({onModalOpen,addApproval,removeApproval,addReceiver,removeReceiver,approvers, receivers});
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
  height: 250px;
  background-color: #f8f9fa;
}

/* 결재 및 수신 박스 */
.recept-box {
  height: 80%;
  overflow-y: auto;
  border: 1px solid #ddd;
  padding: 5px;
}

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
  height: 80%;
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

.approval-item {
  padding: 10px;
  border-bottom: 1px solid #ccc;
  background-color: #f8f9fa;
  cursor: grab;
}

.approval-item:active {
  background-color: #e9ecef;
}

</style>
