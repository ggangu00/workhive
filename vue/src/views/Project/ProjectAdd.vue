<template>
  <div class="content">
    <div class="container-fluid">
      <card>
        <h4 class="card-title float-left mt-1">프로젝트 등록</h4>
        <button class="btn btn-primary btn-sm btn-fill float-right" @click="projectAdd">등록</button>
        <button class="btn btn-secondary btn-sm btn-fill float-right" @click="formReset">초기화</button>
      </card>
      <input type="hidden" :name="createId" v-model="createId">
      <div class="card">
        <div class="card-header">
          <h5 class="card-title">1. 프로젝트 정보</h5>
        </div>
        <div class="card-body">
          <div class="mb-3">
            <div class="form-group has-label">
              <label>프로젝트명 <em class="point-red">*</em></label>
            </div>
            <input type="text" :name="prNm" v-model="prNm" class="form-control" placeholder="프로젝트명을 입력해주세요">
          </div>
          <div class="mb-3">
            <label class="form-label">프로젝트 구분 <em class="point-red">*</em></label>
            <select class="form-select w30" :name="typeCd" v-model="typeCd" aria-label="Default select example">
              <option value="A03">내부프로젝트</option>
              <option value="A04">외부프로젝트</option>
            </select>
          </div>
          <div class="mb-3">
            <label class="form-label">프로젝트 기간 <em class="point-red">*</em></label>
            <div class="row">
              <div class="col-auto">
                <input type="date" :name="startDt" v-model="startDt" class="form-control">
              </div>
              <div class="col-auto p-none">~</div>
              <div class="col-auto">
                <input type="date" :ame="endDt" v-model="endDt" class="form-control">
              </div>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-label">프로젝트 금액</label>
            <div class="row">
              <div class="col-auto">
                <input type="number" :name="price" v-model="price" class="form-control" placeholder="0"
                  style="text-align: right;">
              </div>
              <div class="col-auto p-none">
                <span class="form-text">
                  원
                </span>
              </div>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-label">내부완료일</label>
            <input type="date" :name="aheadDt" v-model="aheadDt" class="form-control w30">
          </div>
          <div class="mb-3">
            <label class="form-label">거래처</label>
            <div class="row">
              <div class="col-auto">
                <input type="text" :name="entrprsMberId" v-model="entrprsMberId" class="form-control"
                  placeholder="제목을 입력해주세요">
              </div>
              <div class="col-auto p-none">
                <button class="btn btn-info btn-fill">검색</button>
              </div>
            </div>
          </div>
        </div>

      </div>
      <!--
          <div class="card">
            <div class="card-body">
            <h5 class="card-title float-left mt-1">2. 프로젝트 과업</h5>
          </div>
            <div class="card-body">
              <button class="btn btn-secondary btn-sm btn-fill float-right"
              onclick="location.href ='#/admin/project/add'">과업추가</button>
              <table class="table">
                <colgroup>
                  <col width="10%">
                  <col>
                  <col width="15%">
                  <col width="10%">
                  <col width="5%">

                </colgroup>
                <thead>
                  <tr>
                    <th></th>
                    <th>과업내용</th>
                    <th>진행률</th>
                    <th>완료상태</th>
                    <th></th>
                  </tr>
                </thead>
                <tbody>
                  <tr>
                    <td><i class="bi bi-arrows-vertical"></i></td>
                    <td><input type="text" name="PR_WORK_NM" class="form-control"></td>
                    <td><input type="number" name="PROGRESS" class="form-control"></td>
                    <td>
                      <select class="form-select" aria-label="Default select example">
                        <option value="A01">완료</option>
                        <option value="A02" selected>미완료</option>
                      </select>
                    </td>
                    <td><a><i class="bi bi-x-lg"></i></a></td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        -->
    </div>
  </div>
</template>

<script setup>
import axios from "axios";
import Swal from 'sweetalert2';
import { ref } from 'vue';
import Card from '../../components/Cards/Card.vue'
import { useRouter } from 'vue-router';
import { getComm } from '../../assets/js/common.js'
console.log(getComm('YN'));  

//---------------데이터-------------- 

const router = useRouter()

const prNm = ref('');
const typeCd = ref('');
const startDt = ref('');
const endDt = ref('');
const price = ref('');
const aheadDt = ref('');
const entrprsMberId = ref('');
const createId = ref('admin');

//--------------공통함수------------- 





//---------------axios--------------

const projectAdd = async () => { //프로젝트 등록
  if (!prNm.value) {
    Swal.fire({
      icon: "error",
      title: "프로젝트명을 입력하세요"
    });
    return;
  } else if (!typeCd.value) {
    Swal.fire({
      icon: "error",
      title: "프로젝트 구분을 선택하세요"
    });
    return;
  } else if (!startDt.value) {
    Swal.fire({
      icon: "error",
      title: "프로젝트 시작일을 선택하세요"
    });
    return;
  } else if (!endDt.value) {
    Swal.fire({
      icon: "error",
      title: "프로젝트 종료일을 선택하세요"
    });
    return;
  }

  const formData = new FormData();
  formData.append("prNm", prNm.value);
  formData.append("typeCd", typeCd.value);
  formData.append("startDt", startDt.value);
  formData.append("endDt", endDt.value);
  formData.append("price", price.value);
  formData.append("aheadDt", aheadDt.value);
  formData.append("entrprsMberId", entrprsMberId.value);
  formData.append("createId", createId.value);

  try {
    const result = await axios.post('/api/project/add', formData);
    console.log(result);
    if (result.data == 'success') {
      Swal.fire({
        icon: "success",
        title: "등록완료",
        text: "등록한 프로젝트는 목록에서 확인할 수 있습니다",
      }).then(() => {
        router.replace({ name: 'ProjectList' }) //프로젝트 조회페이지로 이동
      });
    } else {
      Swal.fire({
        icon: "error",
        title: "등록실패",
        text: "프로젝트 등록 실패",
      })
    }
  } catch (err) {
    Swal.fire({
      icon: "error",
      title: "등록실패",
      text: "프로젝트 등록 실패",
    })
  }
}

const formReset = () => { //프로젝트 정보 모달 열기
  Swal.fire({
    icon: "question",
    title: "작성내용을 초기화하시겠습니까?",
    showCancelButton: true,
    confirmButtonColor: "#83a2e9",
    cancelButtonColor: "#ff9097",
    confirmButtonText: "예",
    cancelButtonText: "아니요"
  }).then(result => {
    if (result.isConfirmed) {
      prNm.value = '';
      typeCd.value = '';
      startDt.value = '';
      endDt.value = '';
      price.value = '';
      aheadDt.value = '';
      entrprsMberId.value = '';
    }
  })
}
</script>