<template>
    <div class="content">
        <div class="container-fluid">
            <card>
                <h4 class="card-title float-left mt-1">회의 등록</h4>
                <button class="btn btn-sm btn-fill float-right" :class="isUpdated ? 'btn-success' : 'btn-primary'"
                    @click="meetAdd">{{ isUpdated ? '수정' : '등록' }}</button>
                <button class="btn btn-secondary btn-sm btn-fill float-right" @click="formReset">초기화</button>
            </card>
            <div class="card">
                <div class="card-header">
                    <h5 class="card-title">1. 회의 정보</h5>
                </div>
                <div class="card-body">
                    <div class="mb-3">
                        <div class="form-group has-label">
                            <label>회의주제 <em class="point-red">*</em></label>
                        </div>
                        <input type="text" :name="mtgNm" v-model="mtgNm" class="form-control"
                            placeholder="회의주제를 입력해주세요">
                    </div>
                    <div class="mb-3">
                        <label class="form-label">회의일시 <em class="point-red">*</em></label>
                        <div class="row">
                            <div class="col-auto">
                                <input type="date" :name="mtgDe" v-model="mtgDe" class="form-control">
                            </div>
                            <div class="col-auto">
                                <input type="time" :name="mtgBeginTm" v-model="mtgBeginTm" class="form-control">
                            </div>
                            <div class="col-auto">~</div>
                            <div class="col-auto">
                                <input type="time" :name="mtgEndTm" v-model="mtgEndTm" class="form-control">
                            </div>
                        </div>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">회의실 구분 <em class="point-red">*</em></label>
                        <select class="form-select w30" :name="mtgPlace" v-model="mtgPlace">
                            <option :key="place" v-for="place in mtgPlaceArr" :value="place.commDtlCd">
                                {{ place.commDtlNm }}</option>
                        </select>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">회의 구분 <em class="point-red">*</em></label>
                        <select class="form-select w30" aria-label="Default select example" :name="typeCd"
                            v-model="typeCd">
                            <option :key="type" v-for="type in typeCdArr" :value="type.commDtlCd">{{ type.commDtlNm }}
                            </option>
                        </select>
                    </div>

                    <div class="mb-3">
                        <label class="form-label">회의 참여자 <em class="point-red">*</em></label>
                        <Multiselect class="w50" v-model="selected" :options="options" label="label" track-by="value"
                            :multiple="true" placeholder="참여자를 검색해주세요" />
                    </div>
                </div>
            </div>

            <div class="card">
                <div class="card-header">
                    <h5 class="card-title">2. 회의 안건 내용</h5>
                </div>
                <div class="card-body">
                    <div class="form-group has-label">
                        <label>회의 안건 내용 <em class="point-red">*</em></label>
                    </div>
                    <textarea type="text" :name="mtgMtrCn" v-model="mtgMtrCn" class="form-control"
                        style="min-height: 150px;" placeholder="회의안건내용을 입력해주세요"></textarea>
                </div>
            </div>

            <div class="card">
                <div class="card-header">
                    <h5 class="card-title">3. 회의 결과 내용</h5>
                </div>
                <div class="card-body">
                    <div class="form-group has-label">
                        <label>회의 결과 내용</label>
                    </div>
                    <textarea type="text" :name="mtgResultCn" v-model="mtgResultCn" class="form-control"
                        style="min-height: 150px;" placeholder="회의결과내용을 입력해주세요"></textarea>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import axios from "../../assets/js/customAxios.js";
import { useRoute, useRouter } from 'vue-router';
import { onBeforeMount, ref } from 'vue';

//========================== 컴포넌트 ==========================
import Swal from 'sweetalert2';
import Multiselect from "vue-multiselect";
import "vue-multiselect/dist/vue-multiselect.css";
import Card from '../../components/Cards/Card.vue'

//============================= js =============================
import { getComm } from '../../assets/js/common.js'

//========================= 데이터 초기화 =========================

const route = useRoute();
const router = useRouter();

const selected = ref([]);   //선택된 참여자 목록
const options = ref([]);    //참여자 목록 데이터
const typeCd = ref('B01');  //회의실 구분
const typeCdArr = ref([]);  //회의실 공통함수 목록
const mtgId = ref('');      //회의주제
const mtgNm = ref('');      //회의주제
const mtgMtrCn = ref('');   //회의 안건
const mtgResultCn = ref('');//회의 결과
const mtgDe = ref('');      //회의 일시
const mtgBeginTm = ref(''); //회의 시작시간
const mtgEndTm = ref('');   //회의 종료시간
const mtgPlace = ref('M01');//회의 장소
const mtgPlaceArr = ref([]);//회의 장소 공통함수 목록
const isUpdated = ref(false);
let txt = '';

if (route.query.mtgId) { //수정일 경우
    mtgId.value = route.query.mtgId;
    isUpdated.value = true;
    txt = '수정';
} else {
    txt = '등록';
}

//======================== 공통함수 ========================

onBeforeMount(() => {
    getStatus();
    memberGetList();
    if (isUpdated.value == true) {
        meetGetInfo();
    }    
});

//입력정보 초기화
const formReset = () => {
    Swal.fire({
        title: "작성내용을 초기화하시겠습니까?",
        icon: "question",
        showCancelButton: true,
        customClass: {
            confirmButton: "btn btn-secondary btn-fill",
            cancelButton: "btn btn-danger btn-fill"
        },
        confirmButtonText: "아니요",
        cancelButtonText: "네",
    }).then(result => {
        if (result.dismiss == Swal.DismissReason.cancel) {
            mtgNm.value = '';
            typeCd.value = 'B01';
            mtgPlace.value = 'M01';
            mtgDe.value = '';
            mtgBeginTm.value = '';
            mtgEndTm.value = '';
            selected.value = '';
            mtgMtrCn.value = '';
            mtgResultCn.value = '';
        }
    })
}

//======================= axios =======================

//회의 단건조회
const meetInfo = ref([]);
const meetGetInfo = async () => { //회의 단건조회
    try {
        const result = await axios.get(`/api/meet/info/${mtgId.value}`);
        meetInfo.value = result.data.result[0];
        mtgNm.value = meetInfo.value.mtgNm;
        typeCd.value = meetInfo.value.typeCd;
        mtgPlace.value = meetInfo.value.mtgPlace;
        mtgDe.value = meetInfo.value.mtgDe;
        mtgBeginTm.value = meetInfo.value.mtgBeginTm;
        mtgEndTm.value = meetInfo.value.mtgEndTm;
        mtgMtrCn.value = meetInfo.value.mtgMtrCn;
        mtgResultCn.value = meetInfo.value.mtgResultCn;
    } catch (err) {
        meetInfo.value = [];

        Swal.fire({
            icon: "error",
            title: "API 조회 오류",
            text: "Error : " + err
        });
    }
}

//회의실 목록 호출
const getStatus = async () => {
    let arr = await getComm("MP");
    let arrAdd = { comm_dtl_cd: '', comm_dtl_nm: '전체' };
    mtgPlaceArr.value.unshift(arrAdd);
    mtgPlaceArr.value = arr;

    let arr2 = await getComm("WT");
    typeCdArr.value.unshift(arrAdd);
    typeCdArr.value = arr2;
}

//구성원 전체조회
const memList = ref([]);
const memberGetList = async () => {

    try {
        const result = await axios.get(`/api/member`);
        memList.value = result.data;
        options.value = result.data.map((row) => ({
            label: `${row.deptNm ? '['+row.deptNm+']' : ''} ${row.mberNm || ''} ${row.gradeNm || ''}`, // 드롭다운에 표시될 값
            value: row // 선택 시 바인딩할 값 (사원코드)
        }));

    } catch (err) {
        memList.value = [];

        Swal.fire({
            icon: "error",
            title: "API 조회 오류",
            text: "Error : " + err
        });
    }
}

//회의 등록
const meetAdd = async () => {


    if (!mtgNm.value) {
        Swal.fire({
        icon: "info",
        title: "회의주제를 입력하세요"
        });
        return;
    } else if (!mtgDe.value) {
        Swal.fire({
        icon: "info",
        title: "회의일시를 입력하세요"
        });
        return;
    } else if (selected.value.length < 1) {
        Swal.fire({
        icon: "info",
        title: "회의 참여자를 등록하세요"
        });
        return;
    } else if (!mtgMtrCn.value) {
        Swal.fire({
        icon: "info",
        title: "회의안건을 등록하세요"
        });
        return;
    }

    const requestData = {
        memberArr: selected.value.map(row => ({
            mberId: row.value.mberId,
            gradeCd: row.value.gradeCd,
            deptCd: row.value.deptCd
        })),
        typeCd: typeCd.value,
        mtgNm: mtgNm.value,
        mtgDe: mtgDe.value,
        mtgBeginTm: mtgBeginTm.value,
        mtgEndTm: mtgEndTm.value,
        mtgPlace: mtgPlace.value,
        mtgMtrCn: mtgMtrCn.value,
        mtgResultCn: mtgResultCn.value
    };

    try {
        const response = ref([]);
        if(isUpdated.value) response.value = await axios.put(`/api/meet`, requestData); //수정
        else response.value = await axios.post("/api/meet", requestData); //등록

        if (response.value.data === true) {
            Swal.fire({
                icon: "success",
                title: txt+"완료",
                text: txt+"한 회의는 목록에서 확인할 수 있습니다",
            }).then(() => {
                router.replace({ name: 'MeetList' }) //프로젝트 조회페이지로 이동
            });
        }
    } catch (err) {
        Swal.fire({
            icon: "error",
            title: txt+"실패",
            text: "회의 "+txt+" 실패",
        })
    }
}

</script>