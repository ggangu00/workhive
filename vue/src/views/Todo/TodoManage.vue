<template>
  <div class="content" @keydown.esc="modalClose">
    <div class="container-fluid">
      <div class="card">
        <div class="card-body">
          <h4 class="card-title float-left">업무일지 관리</h4>
        </div>
      </div>

      <div class="card todo-container">
        <!--
        <div class="todo-header">
          <button @click="toggleSubMenu">캘린더 펼침
            <i :class="['fa-solid', 'arrow-icon', isHidden ? 'fa-chevron-up' : 'fa-chevron-down']"></i>
          </button>
        </div>
        -->
        <div class="month mt-3">
          <button @click="btnMonthMove('prev')"><i class="fa-solid fa-angle-left" style="float: left;"></i></button>
          {{ year }}.{{ month }}
          <button @click="btnMonthMove('next')"><i class="fa-solid fa-angle-right" style="float: left;"></i></button>
        </div>
        <div class="days mt-3 mb-3" :class="'day' + dateArr.length">
          <button class="day" :key="i" v-for="i in dateArr" :class="{
            'point-red': i.dateDay === '일',
            'point-blue': i.dateDay === '토',
            'highlight': todoListCnt.includes(dateFormat(i.thisDate)),
            'today': dateFormat(i.thisDate) == todoDt
          }" @click=" todoGetDate(i.thisDate)">{{ i.date }}</button>
        </div>
      </div>

      <div class="card">
        <div class="card-body">
          <div class="row">
            <div class="col-2">
              <select class="form-select float-right" :name="typeCd" v-model="typeCd">
                <option :key="type" v-for="type in typeCdArr" :value="type.commDtlCd">{{ type.commDtlNm }}</option>
              </select>
            </div>
            <div class="col-8">
              <input type="text" :name="title" v-model="title" class="form-control float-right"
                placeholder="업무내용을 입력해주세요" @keyup.enter="todoAdd">
            </div>
            <div class="col-2">
              <button class="btn btn-primary btn-fill float-left mt-0" @click="todoAdd">등록</button>
              <button class="btn btn-secondary btn-fill loat-left mt-0" @click="openModal">상세등록</button>
            </div>
          </div>
          <div class="table-responsive">
            <button class="btn btn-secondary btn-fill btn-sm float-left" @click="btnTodoListChange('A02')">선택미완료</button>
            <button class="btn btn-secondary btn-fill btn-sm float-left" @click="btnTodoListChange('A01')">선택완료</button>
            <button class="btn btn-danger btn-fill btn-sm float-left" @click="btnTodoListChange('del')">선택삭제</button>
            <div id="tableGrid" class="toastui project"></div>
          </div>
        </div>
      </div>
    </div>
  </div>

  <!--업무등록 모달[s]-->
  <Modal :isShowModal="isShowModal" :modalTitle="'일지등록'" @click.self="closeModal">
    <!-- 모달 바디 -->
    <template v-slot:body>
      <card>

        <div class="mb-3">
          <label class="form-label">업무일 <em class="point-red">*</em></label>
          <input type="date" class="form-control w30" :name="todoDt" v-model="todoDt">
        </div>
        <div class="mb-3">
          <label class="form-label">업무분류 <em class="point-red">*</em></label>
          <div class="row">
            <div class="col-auto">
              <select class="form-select float-right" :name="typeCd" v-model="typeCd">
                <option :key="type" v-for="type in typeCdArr" :value="type.commDtlCd">{{ type.commDtlNm }}</option>
              </select>
            </div>
          </div>
        </div>
        <div class="mb-3">
          <div class="form-group has-label">
            <label>일지제목 <em class="point-red">*</em></label>
          </div>
          <input type="text" class="form-control" placeholder="일지제목을 입력해주세요" :name="title" v-model="title">
        </div>
        <div class="mb-3">
          <div class="form-group has-label">
            <label>업무내용 <em class="point-red">*</em></label>
          </div>
          <textarea type="text" class="form-control" placeholder="업무내용을 입력해주세요" style="min-height: 150px;"
            :name="content" v-model="content"></textarea>
        </div>
        <div class="mb-3">
          <label class="form-label">상태 <em class="point-red">*</em></label>
          <div class="row">
            <div class="col-auto">
              <select class="form-select" :name="state" v-model="state">
                <option value="A02">미완료</option>
                <option value="A01">완료</option>
              </select>
            </div>
          </div>
        </div>
      </card>
    </template>

    <!-- 모달 푸터 -->
    <template v-slot:footer>
      <button type="button" class="btn btn-secondary btn-fill" @click="closeModal">닫기</button>
      <button type="button" class="btn btn-primary btn-fill" @click="todoAdd; closeModal">등록</button>
    </template>
  </Modal>
  <!--업무등록 모달[e]-->

</template>

<script setup>
import axios from "../../assets/js/customAxios";
import { ref, computed, watch, watchEffect, onBeforeMount, onMounted, nextTick } from 'vue';

//========================== 컴포넌트 ==========================
import Grid from 'tui-grid';
import Swal from 'sweetalert2';
import Modal from '../../components/Modal.vue';
import Card from '../../components/Cards/Card.vue'

//============================= js =============================
import { dateGetDay } from '../../assets/js/project'
import { getComm, dateFormat } from '../../assets/js/common'

//========================= 데이터 초기화 =========================

const year = ref(new Date().getFullYear()); // 현재 연도 기본값
const month = ref(new Date().getMonth() + 1); // 현재 월 기본값 (1~12 범위)

const typeCd = ref('B01');        //등록 시 업무구분
const typeCdArr = ref([]);        //업무구분 공통코드 목록
const title = ref('');            //업무제목
const content = ref('');          //업무내용
const state = ref('A02');         //업무 완료여부
const mberId = ref('admin');      //등록자 아이디
const todoDt = ref(dateFormat()); // 현재 선택된 날짜 (디폴트 : 오늘)

onBeforeMount(() => {
  getStatus();      //업무구분
  nowDateInfo();    //해당 월의 일수 표출
  todoGetListAll(); //업무일지 전체조회 (금일)
});

//========================= Toast grid =========================

//리스트 초기화
const grid = ref([]);
onMounted(() => {

  grid.value = new Grid({
    el: document.getElementById("tableGrid"),
    scrollX: true,
    scrollY: true,
    columns: [
      { header: "업무구분", name: "state", align: "center", width: 80, formatter: ({ row }) => row.state == 'A01' ? '완료' : '미완료' },
      { header: "완료여부", name: "typeNm", align: "center", width: 100 },
      { header: "업무내용", name: "title", align: "left", renderer: subjectRenderer },
      { header: "관리", name: "managementSetting", align: "center", width: 150, renderer: BtnRendererSetting }
    ],
    pageOptions: {
      useClient: false,
      perPage: 5,
    },
    rowHeight: 50,
    data: todoList.value,
    rowHeaders: ["checkbox"],
  });
});

//===================== Toast Grid Rendere =====================

//일지 제목
class subjectRenderer {
  constructor(props) {
    const rowKey = props.row?.rowKey ?? props.grid.getRow(props.rowKey)?.rowKey;
    const rowData = props.grid.getRow(rowKey);

    const el = document.createElement("div");

    el.innerHTML = `${rowData.title}`;  //제목

    if (rowData.dateTerm > 0) {
      el.innerHTML += ` <span class="badge badge-danger"> D+${rowData.dateTerm} </span>`; //작성일자로부터 초과된 일수
    }

    if (rowData.content) {
      el.setAttribute("title", rowData.content); //툴팁
      el.innerHTML += ` ( + )`;
    }

    this.el = el;
  }

  getElement() {
    return this.el;
  }
}

//수정/삭제 버튼
class BtnRendererSetting {
  constructor(props) {
    const rowKey = props.row?.rowKey ?? props.grid.getRow(props.rowKey)?.rowKey;
    const rowData = props.grid.getRow(rowKey);

    const el = document.createElement("div");

    el.innerHTML = `
      <button class="btn btn-success btn-fill btn-sm mr-1" data-type="edit">수정</button>
      <button class="btn btn-danger btn-fill btn-sm mr-1" data-type="del">삭제</button>
    `;

    el.addEventListener("click", (event) => {
      const type = event.target.dataset.type;

      if (type === "edit") { //수정버튼 클릭 시 수정모달 표출
        btnSelectUpdate(rowData.todoCd)
      } else if (type === "del") { //삭제버튼 클릭 시 삭제처리
        btnTodoRemove(rowData.todoCd)
      }
    });

    this.el = el;
  }

  getElement() {
    return this.el;
  }
}

//========================= 모달 =========================

const isShowModal = ref(false);

//업무일지 등록 모달 열기
const openModal = () => {
  isShowModal.value = true;
}

//업무일지 등록 모달 닫기
const closeModal = () => {
  formReset();
  isShowModal.value = false;
}

//======================== 공통함수 ========================

// 현재 월의 총 일수
const dateArr = ref([]);
const dayCnt = computed(() => new Date(year.value, month.value, 0).getDate());
const nowDateInfo = () => {
  dateArr.value = [];
  for (let i = 1; i <= dayCnt.value; i++) {
    const formattedDate = `${year.value}-${String(month.value).padStart(2, '0')}-${String(i).padStart(2, '0')}`;
    dateArr.value.push({
      date: i,
      thisDate: formattedDate,            //현재일수
      dateDay: dateGetDay(formattedDate)  //현재요일 (토,일 구분용)
    });
  }
}

// todoDt가 변경되면 watchEffect가 자동 실행됨
const todoGetDate = (date) => {
  todoDt.value = date;
};

//입력정보 초기화
const formReset = () => {
  typeCd.value = 'B01';
  title.value = '';
  content.value = '';
}

//======================= 버튼이벤트 =======================

/*
//캘린더 펼침/닫힘 (시간나면 작업예정)
const isHidden = ref(false);
const toggleSubMenu = () => {
  isHidden.value = !isHidden.value;
};
*/

//월 이동
const btnMonthMove = (mode) => {

  if (mode == "prev") { //이전달
    if (month.value === 1) {
      year.value--;
      month.value = 12;
    } else {
      month.value--;
    }
  }

  if (mode == "next") { //다음달
    if (month.value === 12) {
      year.value++;
      month.value = 1;
    } else {
      month.value++;
    }
  }
}

//수정버튼 클릭 시 수정모달 표출
const btnSelectUpdate = (code) => {
  openModal();
  todoGetList(code);
}

//업무일지 단건삭제
const btnTodoRemove = (code) => {
  Swal.fire({
    title: "해당 업무일지를 삭제 하시겠습니까?",
    icon: "question",
    showCancelButton: true,
    customClass: {
      confirmButton: "btn btn-secondary btn-fill",
      cancelButton: "btn btn-danger btn-fill"
    },
    confirmButtonText: "아니요",
    cancelButtonText: "네",
  }).then((result) => {
    if (result.dismiss == Swal.DismissReason.cancel) {
      todoListUpdate(code);
    }
  });
};

//업무일지 다중 상태변경/삭제 처리 (버튼)
const btnTodoListChange = (mode) => {
  const checkedData = grid.value.getCheckedRows();
  const modeText = ref('상태변경');
  if (mode == 'del') {
    modeText.value = '삭제';
  }

  if (checkedData.length === 0) {
    alert("선택된 항목이 없습니다.");
    return;
  } else {
    Swal.fire({
      title: "해당 업무일지를 " + modeText.value + " 하시겠습니까?",
      icon: "question",
      showCancelButton: true,
      customClass: {
        confirmButton: "btn btn-secondary btn-fill",
        cancelButton: "btn btn-danger btn-fill"
      },
      confirmButtonText: "아니요",
      cancelButtonText: "네",
    }).then((result) => {
      if (result.dismiss == Swal.DismissReason.cancel) {
        todoListUpdate(mode);
      }
    });

  }
}

//======================= axios =======================

//업무구분 목록 호출
const getStatus = async () => {
  let arr = await getComm("WT");
  let arrAdd = { comm_dtl_cd: '', comm_dtl_nm: '전체' };
  typeCdArr.value.unshift(arrAdd);
  typeCdArr.value = arr;
}

//업무일지 전체조회
const todoList = ref([]);
const todoCount = ref(0);
const todoGetListAll = async () => {

  const date = dateFormat(todoDt.value).replaceAll("-", ""); //선택된 날짜 없을 때 디폴트 금일

  try {
    const result = await axios.get(`/api/todo/list/${date}`);
    todoList.value = result.data;
    todoCount.value = result.data.length;

    grid.value.resetData(todoList.value); //toast grid로 데이터 전달

    await nextTick(); //toast grid 로드될때까지 기다림
    todoList.value.forEach((row, index) => { //toast grid 로드완료 후 상태별 rowClass 부여
      if (row.state == "A01") {
        grid.value.addRowClassName(index, "table-end");
      } else {
        grid.value.addRowClassName(index, "table-white");
      }
    });

  } catch (err) {
    todoList.value = [];

    Swal.fire({
      icon: "error",
      title: "API 조회 오류",
      text: "Error : " + err
    });
  }
}

watchEffect(() => {
  todoGetListAll();
});

//일지 단건조회
const todoInfo = ref([]);
const todoGetList = async (todoCd) => {

  try {
    const result = await axios.get(`/api/todo/info/${todoCd}`);

    todoInfo.value = result.data.info;
    todoDt.value = todoInfo.value.todoDt;
    typeCd.value = todoInfo.value.typeCd;
    title.value = todoInfo.value.title;
    content.value = todoInfo.value.content;
    state.value = todoInfo.value.state;

  } catch (err) {
    todoInfo.value = [];

    Swal.fire({
      icon: "error",
      title: "API 조회 오류",
      text: "Error : " + err
    });
  }
};

//해당 일자 건수조회
const todoListCnt = ref([]);
watch(month, async () => {
  try {
    const result = await axios.get(`/api/todo/list/cnt?year=${year.value}&month=${month.value}`);

    //기존 데이터를 지우고 새로 할당
    todoListCnt.value = result.data.map(item => dateFormat(item.todoDt));

  } catch (err) {
    todoListCnt.value = [];

    Swal.fire({
      icon: "error",
      title: "API 조회 오류",
      text: "Error : " + err
    });
  }
}, { immediate: true });

//업무일지 등록
const todoAdd = async () => {

  if (!title.value) {
    Swal.fire({
      icon: "info",
      title: "업무내용을 입력하세요"
    });
    return;
  }

  const formData = new FormData();
  formData.append("typeCd", typeCd.value);
  formData.append("mberId", mberId.value);
  formData.append("title", title.value);
  formData.append("content", content.value);
  formData.append("state", state.value);
  formData.append("todoDt", todoDt.value);

  try {
    const response = await axios.post('/api/todo', formData);

    if (response.data.result === true) {
      Swal.fire({
        icon: "success",
        title: "등록완료",
        text: "일지등록을 완료하였습니다",
      })
      todoGetListAll(); //업무일지 리스트 리로드
      formReset(); //모달 닫힐 때 등록폼 초기화
    }
  } catch (err) {
    Swal.fire({
      icon: "error",
      title: "등록실패",
      text: "일지 등록 실패",
    })
  }
}

//업무일지 다중 상태변경/삭제 처리
const todoListUpdate = async (mode) => {
  const checkedData = grid.value.getCheckedRows();

  const modeText = ref('상태변경');
  if (mode == 'del') {
    modeText.value = '삭제';
  }

  try {
    const response = ref([]);
    if (mode == 'del') { //삭제
      response.value = await axios.delete(`/api/todo/delete`, {
        data: { todoArr: checkedData.map(row => row.todoCd) }
      });

    } else { //상태변경
      response.value = await axios.put(`/api/todo/state`, {
        todoArr: checkedData.map(row => row.todoCd),
        state: mode
      });
    }

    if (response.value.statusText == "OK") {
      Swal.fire({
        icon: "success",
        title: modeText.value + "완료",
        text: "선택한 일지를 " + modeText.value + "하였습니다",
      })
    }
    todoGetListAll();
  } catch (err) {
    Swal.fire({
      icon: "error",
      title: modeText.value + "실패",
      text: "Error : " + err
    });
  }
}
</script>