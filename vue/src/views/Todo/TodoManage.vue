<template>
  <div class="content" @keydown.esc="modalCloseFunc">
    <div class="container-fluid">
      <div class="card">
        <div class="card-body">
          <h4 class="card-title float-left">업무일지 관리</h4>
        </div>
      </div>

      <div class="card todo-container">
        <div class="todo-header">
          <button>캘린더 펼침 ▼</button>
        </div>
        <div class="month">
          <button>&lt;</button>
          2025.01
          <button>&gt;</button>
        </div>
        <div class="days">
          <button class="day red">1</button>
          <button class="day">2</button>
          <button class="day">3</button>
          <button class="day blue">4</button>
          <button class="day red">5</button>
          <button class="day">6</button>
          <button class="day">7</button>
          <button class="day">8</button>
          <button class="day">9</button>
          <button class="day">10</button>
          <button class="day blue">11</button>
          <button class="day red">12</button>
          <button class="day">13</button>
          <button class="day">14</button>
          <button class="day">15</button>
          <button class="day">16</button>
          <button class="day">17</button>
          <button class="day blue">18</button>
          <button class="day red">19</button>
          <button class="day">20</button>
          <button class="day">21</button>
          <button class="day">22</button>
          <button class="day">23</button>
          <button class="day">24</button>
          <button class="day blue">25</button>
          <button class="day red">26</button>
          <button class="day">27</button>
          <button class="day">28</button>
          <button class="day">29</button>
          <button class="day">30</button>
          <button class="day">31</button>
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
                placeholder="업무내용을 입력해주세요">
            </div>
            <div class="col-2">
              <button class="btn btn-secondary btn-fill float-left mt-0" @click="todoAdd">등록</button>
              <button class="btn btn-primary btn-fill loat-left mt-0" @click="openModal">상세등록</button>
            </div>
          </div>
          <div class="table-responsive">
            <table class="table table-hover project" style="margin-bottom: 0;">
              <thead>
                <tr>
                  <th width="50px">
                    <div class="form-check">
                      <input class="form-check-input" type="checkbox" id="inlineCheckbox1" value="option1">
                    </div>
                  </th>
                  <th>
                    <button class="btn btn-secondary btn-fill btn-sm float-left">선택미완료</button>
                    <button class="btn btn-secondary btn-fill btn-sm float-left">선택완료</button>
                    <button class="btn btn-danger btn-fill btn-sm float-left">선택삭제</button>
                  </th>
                  <th></th>
                  <th></th>
                  <th></th>
                </tr>
              </thead>
            </table>
            <table class="table table-hover project">
              <colgroup>
                <col width="50px">
                <col width="10%">
                <col width="10%">
                <col>
                <col width="13%">
              </colgroup>
              <tbody>
                <template v-if="todoCount > 0">
                  <tr :key="i" v-for="(todo, i) in todoList"
                    :class="todo.state == 'A01' ? 'table-secondary' : ''">
                    <td>
                      <div class="form-check">
                        <input class="form-check-input" type="checkbox" id="inlineCheckbox1" value="option1">
                      </div>
                    </td>
                    <td>{{todo.state == 'A01' ? '완료' : '미완료'}}</td>
                    <td>{{todo.typeCd}}</td>
                    <td align="left">{{todo.title}}</td>
                    <td>
                      <button class="btn btn-success btn-fill btn-sm mr-1">수정</button>
                      <button class="btn btn-danger btn-fill btn-sm mr-1">삭제</button>
                    </td>
                  </tr>
                </template>
                <tr v-else class="list-nodata">
                  <td colspan="10">
                    <div>등록된 일지가 없습니다.</div>
                  </td>
                </tr>
              </tbody>
            </table>
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
          <input type="date" name="project_nm" class="form-control w30">
        </div>
        <div class="mb-3">
          <label class="form-label">업무분류 <em class="point-red">*</em></label>
          <div class="row">
            <div class="col-auto">
              <select class="form-select">
                <option value="1">내부업무</option>
                <option value="2">외부업무</option>
                <option value="3">프로젝트</option>
              </select>
            </div>
          </div>
        </div>
        <div class="mb-3">
          <div class="form-group has-label">
            <label>일지제목 <em class="point-red">*</em></label>
          </div>
          <input type="text" class="form-control" placeholder="일지제목을 입력해주세요">
        </div>
        <div class="mb-3">
          <div class="form-group has-label">
            <label>업무내용 <em class="point-red">*</em></label>
          </div>
          <textarea type="text" class="form-control" placeholder="업무내용을 입력해주세요"></textarea>
        </div>
        <div class="mb-3">
          <label class="form-label">상태 <em class="point-red">*</em></label>
          <div class="row">
            <div class="col-auto">
              <select class="form-select" name="state">
                <option value="1">미완료</option>
                <option value="2">완료</option>
              </select>
            </div>
          </div>
        </div>
      </card>
    </template>

    <!-- 모달 푸터 -->
    <template v-slot:footer>
      <button type="button" class="btn btn-secondary btn-fill" @click="closeModal">닫기</button>
      <button type="button" class="btn btn-primary btn-fill" @click="todoAdd; confirm">등록</button>
    </template>
  </Modal>
  <!--업무등록 모달[e]-->

</template>

<script setup>
import axios from "axios";
import Swal from 'sweetalert2';
import Modal from '../../components/Modal.vue';
import { ref, onBeforeMount } from 'vue';
import Card from '../../components/Cards/Card.vue'
import { getComm, dateFormat } from '../../assets/js/common.js'

//---------------데이터-------------- 

const typeCd = ref('B01');
const typeCdArr = ref([]);
const title = ref('');
const content = ref('');
const state = ref('A02');

onBeforeMount(() => {
  todoGetList();
  getStatus();
});

//---------------공통함수-------------- 

const getStatus = async () => { //업무구분 목록 호출
  let arr = await getComm("WT");
  let arrAdd = { comm_dtl_cd: '', comm_dtl_nm: '전체' };
  typeCdArr.value.unshift(arrAdd);
  typeCdArr.value = arr;
}

const isShowModal = ref(false);
const modalCloseFunc = (e) => {
  if (e.key === "Escape") {
    if (isShowModal.value) {
      isShowModal.value = !isShowModal.value
    }
  }
}
const openModal = () => {
  isShowModal.value = true;
}
const closeModal = () => {
  isShowModal.value = false;
}
const confirm = () => {
  isShowModal.value = false;
}

//---------------axios--------------

const todoList = ref([]);
const todoCount = ref(0);
const todoGetList = async () => { //일지 전체조회
  try {
    const result = await axios.get(`/api/todo/list/${dateFormat()}`);

    todoCount.value = result.data.length;
    todoList.value = result.data;

  } catch (err) {
    todoList.value = [];

    Swal.fire({
      icon: "error",
      title: "API 조회 오류",
      text: "Error : " + err
    });
  }
}

const todoAdd = async () => { //일지 등록

  const formData = new FormData();
  formData.append("typeCd", typeCd.value);
  formData.append("memCd", '2025001');
  formData.append("title", title.value);
  formData.append("content", content.value);
  formData.append("state", state.value);
  console.log(formData);

  try {
    const response = await axios.post('/api/todo', formData);

    if (response.data.result === true) {
      Swal.fire({
        icon: "success",
        title: "등록완료",
        text: "일지등록을 완료하였습니다",
      })
      todoList.value = response.data.list;
    }
  } catch (err) {
    Swal.fire({
      icon: "error",
      title: "등록실패",
      text: "일지 등록 실패",
    })
  }
}
</script>