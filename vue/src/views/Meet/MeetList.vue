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
                <div id="nowGrid" class="toastui project"></div>
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

                <div id="allGrid" class="toastui project"></div>
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
                                        <td class="text-start">{{ dateFormat(meetInfo.mtgDe) }}({{
                                            dateGetDay(meetInfo.mtgDe) }}) {{ meetInfo.mtgBeginTm }} ~ {{
                                                meetInfo.mtgEndTm }}
                                        </td>
                                        <th class="table-secondary">회의실</th>
                                        <td class="text-start">{{ meetInfo.mtgPlace }}</td>
                                    </tr>
                                    <tr>
                                        <th class="table-secondary">참여자</th>
                                        <td colspan="3" class="text-start">김민진, 박주현, 신강현</td>
                                    </tr>
                                    <tr>
                                        <th class="table-secondary">회의안건</th>
                                        <td colspan="3" class="text-start">{{ meetInfo.mtgMtrCn }}</td>
                                    </tr>
                                    <tr>
                                        <th class="table-secondary">회의결과</th>
                                        <td colspan="3" class="text-start">{{ meetInfo.mtgResultCn }}</td>
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
import { useRouter } from "vue-router";
import { onBeforeMount, ref, watch, onMounted } from 'vue';

//---------------컴포넌트-------------- 
import Swal from 'sweetalert2';
import Grid from 'tui-grid';
import Card from '../../components/Cards/Card.vue'
import Modal from '../../components/Modal.vue';

//---------------js-------------- 
import { dateFormat, numberFormat } from '../../assets/js/common'
import { dateGetDay } from '../../assets/js/project'

//---------------데이터-------------- 

onBeforeMount(() => {
    meetGetNowList();
    meetGetList();
});

//회의 상세보기 모달
class BtnRendererModal {
    constructor(props) {
        const rowKey = props.row?.rowKey ?? props.grid.getRow(props.rowKey)?.rowKey;
        const rowData = props.grid.getRow(rowKey);

        const el = document.createElement("div");
        el.className = "subject";

        el.innerHTML = `
           <a href="#">${rowData.mtgNm}</a>
        `;

        el.addEventListener("click", () => {
            modalOpen(rowData.mtgId)
        });

        this.el = el;
    }

    getElement() {
        return this.el;
    }
}

//회의 수정/삭제 버튼
class BtnRendererSetting {
    constructor(props) {

        const el = document.createElement("div");

        el.innerHTML = `
            <button class="btn btn-success btn-fill btn-sm mr-1" data-type="edit">수정</button>
            <button class="btn btn-danger btn-fill btn-sm mr-1" data-type="del">삭제</button>
            `;

        el.addEventListener("click", (event) => {
            const type = event.target.dataset.type;
            const rowKey = props.row?.rowKey ?? props.grid.getRow(props.rowKey)?.rowKey;

            const rowData = props.grid.getRow(rowKey);

            if (type === "edit") {
                btnPageMove("add", rowData.mtgId);
            } else if (type === "del") {
                btnMeetRemove(rowData.mtgId);
            }
        });

        this.el = el;
    }

    getElement() {
        return this.el;
    }
}

// 그리드 컬럼 데이터
let nowCol = [
    { header: "구분", name: "typeCd", align: "center", formatter: ({ row }) => `${row.typeCd == 'A04' ? '완료' : '진행중'}` },
    { header: "회의주제", name: "mtgNm", align: "center", renderer: BtnRendererModal },
    { header: "회의일시", name: "mtgDe", align: "center", formatter: ({ row }) => `${dateFormat(row.mtgDe)} (${dateGetDay(row.mtgDe)}) ${row.mtgBeginTm} ~ ${row.mtgEndTm}` },
    { header: "회의실", name: "mtgPlace", align: "center" },
    { header: "참여자", name: "", align: "center" },
    { header: "관리", align: "center", renderer: BtnRendererSetting },
];
let allCol = [
    { header: "구분", name: "typeCd", align: "center", formatter: ({ row }) => `${row.typeCd == 'A04' ? '완료' : '진행중'}` },
    { header: "회의주제", name: "mtgNm", align: "center", renderer: BtnRendererModal },
    { header: "회의일시", name: "mtgDe", align: "center", formatter: ({ row }) => `${dateFormat(row.mtgDe)} (${dateGetDay(row.mtgDe)}) ${row.mtgBeginTm} ~ ${row.mtgEndTm}` },
    { header: "회의실", name: "mtgPlace", align: "center" },
    { header: "참여자", name: "", align: "center" },
    { header: "관리", align: "center", renderer: BtnRendererSetting },
];

//검색조건
const searchData = ref({
    searchCondition: "",
    searchKeyword: ''
});

watch(() => searchData, () => {
    meetGetList();
}, { deep: true });

const initGrid = (gridInstance, gridDiv, rowData, colData) => {
    gridInstance.value = new Grid({
        el: document.getElementById(gridDiv),
        data: rowData.value,
        scrollX: false,
        scrollY: true,
        rowHeaders: ['checkbox'],
        columns: colData,
    });
};

let nowGridInstance = ref();
let allGridInstance = ref();
onMounted(() => {
    initGrid(nowGridInstance, 'nowGrid', meetNowList, nowCol);
    initGrid(allGridInstance, 'allGrid', meetList, allCol);
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

//-------------버튼이벤트------------

const router = useRouter();
const btnPageMove = (mode, code) => { //수정/일정관리 페이지로
    router.push({ path: `/project/${mode}`, query: { prCd: code } });
}

// 회의 삭제 버튼
const btnMeetRemove = () => {
    Swal.fire({
        title: "해당 프로젝트를 삭제 하시겠습니까?",
        icon: "question",
        showCancelButton: true,
        customClass: {
            confirmButton: "btn btn-danger btn-fill",
            cancelButton: "btn btn-secondary btn-fill"
        },
        cancelButtonText: "닫기",
        confirmButtonText: "삭제",
    }).then((result) => {
        if (result.isConfirmed) {
            // 프로젝트 삭제
            //meetRemove(code);
        }
    });
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
    nowGridInstance.value.resetData(meetNowList.value);
}

const meetList = ref([]);
const meetCount = ref(0);
const meetGetList = async () => { //진행 예정 회의 전체출력
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
    allGridInstance.value.resetData(meetList.value);
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
