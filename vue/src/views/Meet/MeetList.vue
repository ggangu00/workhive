<template>
    <div class="content" @keydown.esc="modalClose">
        <div class="container-fluid">
            <div class="card">
                <div class="card-body">
                    <h4 class="card-title float-left mt-1">회의 조회</h4>
                    <button class="btn btn-primary btn-sm btn-fill float-right" onclick="location.href ='/meet/add'">회의
                        등록</button>
                </div>
            </div>
            <card>
                <h5>금일 예정 회의 ({{ numberFormat(meetNowCount) }}건)</h5>
                <div class="table-responsive">
                    <table class="table table-hover project">
                        <thead class="table-light">
                            <tr>
                                <th>번호</th>
                                <th>구분</th>
                                <th>회의주제</th>
                                <th>회의일시</th>
                                <th>회의실</th>
                                <th>참여자</th>
                                <th>관리</th>
                            </tr>
                        </thead>
                        <tbody>
                            <template v-if="meetNowCount > 0">
                                <tr :key="i" v-for="(meet, i) in meetNowList">
                                    <td>{{ i + 1 }}</td>
                                    <td>{{ meet.typeCd }}</td>
                                    <td>
                                        <div class="subject"><a href="#" @click="modalOpen(meet.mtgId)">{{ meet.mtgNm
                                        }}</a></div>
                                    </td>
                                    <td>{{ dateFormat(meet.mtgDe) }}({{ dateGetDay(meet.mtgDe) }}) {{ meet.mtgBeginTm }}
                                        ~ {{
                                            meet.mtgEndTm }}</td>
                                    <td>{{ meet.mtgPlace }}</td>
                                    <td>김민진, 신강현, 박주현 외 3명</td>
                                    <td>
                                        <button class="btn btn-success btn-fill btn-sm mr-1">수정</button>
                                        <button class="btn btn-danger btn-fill btn-sm mr-1">삭제</button>
                                    </td>
                                </tr>
                            </template>
                            <tr v-else class="list-nodata">
                                <td colspan="8">
                                    <div>등록된 프로젝트가 없습니다.</div>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </card>
            <card>
                <div class="d-flex">
                    <div class="w80">
                        <h5>전체 회의 ({{ numberFormat(meetCount) }}건)</h5>
                    </div>
                    <div class="col-auto">
                        <input type="text" class="form-control form-control-sm" placeholder="제목을 입력해주세요">
                    </div>
                    <div class="col-auto mlp10">
                        <button class="btn btn-info btn-fill btn-sm">검색</button>
                    </div>
                </div>

                <div class="table-responsive">
                    <table class="table table-hover project">
                        <thead class="table-light">
                            <tr>
                                <th>번호</th>
                                <th>상태</th>
                                <th>구분</th>
                                <th>회의주제</th>
                                <th>회의일시</th>
                                <th>회의실</th>
                                <th>참여자</th>
                                <th>관리</th>
                            </tr>
                        </thead>
                        <tbody>
                            <template v-if="meetCount > 0">
                                <tr :key="i" v-for="(meet, i) in meetList">
                                    <td>{{ i + 1 }}</td>
                                    <td>진행예정</td>
                                    <td>{{ meet.typeCd }}</td>
                                    <td>
                                        <div class="subject"><a href="#" @click="modalOpen(meet.mtgId)">{{ meet.mtgNm
                                        }}</a></div>
                                    </td>
                                    <td>{{ dateFormat(meet.mtgDe) }}({{ dateGetDay(meet.mtgDe) }}) {{ meet.mtgBeginTm }}
                                        ~ {{
                                            meet.mtgEndTm }}</td>
                                    <td>{{ meet.mtgPlace }}</td>
                                    <td>김민진, 신강현, 박주현 외 3명</td>
                                    <td>
                                        <button class="btn btn-success btn-fill btn-sm mr-1">수정</button>
                                        <button class="btn btn-danger btn-fill btn-sm mr-1">삭제</button>
                                    </td>
                                </tr>
                            </template>
                            <tr v-else class="list-nodata">
                                <td colspan="8">
                                    <div>등록된 프로젝트가 없습니다.</div>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </card>

            <!--회의 상세보기 모달[s]-->
            <Modal :isShowModal="isShowModal" :modalTitle="'회의 상세보기'" @click.self="modalClose">
                <template v-slot:body>
                    <card>
                        <p class="card-title mb-2">프로젝트 정보</p>

                        <div class="table-responsive">
                            <table class="table table-hover project">
                                <tbody>
                                    <tr>
                                        <th class="table-secondary">회의주제</th>
                                        <td class="text-start">{{ meetInfo.mtgNm }}</td>
                                        <th class="table-secondary">구분</th>
                                        <td class="text-start">{{ meetInfo.typeCd }}</td>
                                    </tr>
                                    <tr>
                                        <th class="table-secondary">회의일시</th>
                                        <td class="text-start">{{ dateFormat(meetInfo.mtgDe) }}({{ dateGetDay(meetInfo.mtgDe) }}) {{ meetInfo.mtgBeginTm }}
                                        ~ {{
                                            meetInfo.mtgEndTm }}
                                        </td>
                                        <th class="table-secondary">회의실</th>
                                        <td class="text-start">{{ meetInfo.mtgPlace}}</td>
                                    </tr>
                                    <tr>
                                        <th class="table-secondary">참여자</th>
                                        <td colspan="3" class="text-start">김민진, 박주현, 신강현</td>
                                    </tr>
                                    <tr>
                                        <th class="table-secondary">회의안건</th>
                                        <td colspan="3" class="text-start">{{ meetInfo.mtgMtrCn}}</td>
                                    </tr>
                                    <tr>
                                        <th class="table-secondary">회의결과</th>
                                        <td colspan="3" class="text-start">{{ meetInfo.mtgResultCn}}</td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                    </card>
                </template>

                <template v-slot:footer>
                    <button type="button" class="btn btn-secondary btn-fill" @click="modalClose">닫기</button>
                </template>
            </Modal>
            <!--회의 상세보기 모달[e]-->

        </div>
    </div>
</template>

<script setup>
import axios from "axios";
import Swal from 'sweetalert2';
import { onBeforeMount, ref } from 'vue';
import Card from '../../components/Cards/Card.vue'
import Modal from '../../components/Modal.vue';
import { dateFormat, dateGetDay, numberFormat } from '../../assets/js/common.js'

//---------------데이터-------------- 

onBeforeMount(() => {
    meetGetNowList();
    meetGetList();
});

//---------------모달--------------

const isShowModal = ref(false);
const modalOpen = (code) => { //회의 정보 모달 열기
  isShowModal.value = true;
  meetGetInfo(code);
}

const modalClose = (e) => { //회의 정보 모달 닫기
  if (e.key === "Escape") {
    if (isShowModal.value) {
      isShowModal.value = !isShowModal.value
    }
  } else {
    isShowModal.value = false;
  }
}


//---------------axios--------------

const meetNowList = ref([]);
const meetNowCount = ref(0);
const meetGetNowList = async () => { //금일 예정 회의 조회
    try {
        const result = await axios.get('/api/meet/list?state=now');

        meetNowList.value = result.data;
        meetNowCount.value = result.data.length;
    } catch (err) {
        meetNowList.value = [];

        Swal.fire({
            icon: "error",
            title: "API 조회 오류",
            text: "Error : " + err
        });
    }
}

const meetList = ref([]);
const meetCount = ref(0);
const meetGetList = async () => { //회의 최신 3건 조회
    try {
        const result = await axios.get('/api/meet/list?state=ing');

        meetList.value = result.data;
        meetCount.value = result.data.length;
    } catch (err) {
        meetList.value = [];

        Swal.fire({
            icon: "error",
            title: "API 조회 오류",
            text: "Error : " + err
        });
    }
}

const meetInfo = ref([]);
const meetGetInfo = async (mtgId) => { //회의 단건조회
  try {
    const result = await axios.get(`/api/meet/info/${mtgId}`);
    console.log(result.data[0]);
    meetInfo.value = result.data[0];
  } catch (err) {
    meetInfo.value = [];

    Swal.fire({
      icon: "error",
      title: "API 조회 오류",
      text: "Error : " + err
    });
  }
}

</script>
