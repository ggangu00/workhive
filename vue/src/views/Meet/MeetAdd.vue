<template>
    <div class="content">
        <div class="container-fluid">
            <card>
                <h4 class="card-title float-left">회의 등록</h4>
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
                        <select class="form-select w30" aria-label="Default select example" :name="mtgPlace"
                            v-model="mtgPlace">
                            <option value="A03">회의실A</option>
                            <option value="A04">회의실B</option>
                            <option value="A04">회의실C</option>
                        </select>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">회의 구분 <em class="point-red">*</em></label>
                        <select class="form-select w30" aria-label="Default select example" :name="typeCd"
                            v-model="typeCd">
                            <option value="A03">내부</option>
                            <option value="A04">외부</option>
                            <option value="A04">프로젝트</option>
                            <option value="A04">기타</option>
                        </select>
                    </div>
                </div>
            </div>

            <div class="card">
                <div class="card-header">
                    <h5 class="card-title">2. 회의 안건 내용</h5>
                </div>
                <div class="card-body">
                    <div class="mb-3">
                        <div class="form-group has-label">
                            <label>회의 안건 내용 <em class="point-red">*</em></label>
                        </div>
                        <textarea type="text" :name="mtgMtrCn" v-model="mtgMtrCn" class="form-control"
                            placeholder="회의안건내용을 입력해주세요"></textarea>
                    </div>
                </div>
            </div>

            <div class="card">
                <div class="card-header">
                    <h5 class="card-title">3. 회의 결과 내용</h5>
                </div>
                <div class="card-body">
                    <div class="mb-3">
                        <div class="form-group has-label">
                            <label>회의 결과 내용 <em class="point-red">*</em></label>
                        </div>
                        <textarea type="text" :name="mtgResultCn" v-model="mtgResultCn" class="form-control"
                            placeholder="회의결과내용을 입력해주세요"></textarea>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import axios from "axios";
import Swal from 'sweetalert2';
import { ref } from 'vue';
import Card from '../../components/Cards/Card.vue'
import { useRouter } from 'vue-router';

//---------------데이터-------------- 

const typeCd = ref('A03');
const mtgNm = ref('');
const mtgMtrCn = ref('');
const mtgResultCn = ref('');
const mtgDe = ref('');
const mtgBeginTm = ref('');
const mtgEndTm = ref('');
const mtgPlace = ref('A03');

const router = useRouter()

const meetAdd = async () => { //회의 등록

    const formData = new FormData();
    formData.append("cmd", "save");
    formData.append("typeCd", typeCd.value);
    formData.append("mtgNm", mtgNm.value);
    formData.append("mtgMtrCn", mtgMtrCn.value);
    formData.append("mtgResultCn", mtgResultCn.value);
    formData.append("mtgDe", mtgDe.value);
    formData.append("mtgBeginTm", mtgBeginTm.value);
    formData.append("mtgEndTm", mtgEndTm.value);
    formData.append("mtgPlace", mtgPlace.value);
    formData.append("createId", 'admin');

    try {
        const response = await axios.post('/api/meet', formData);

        if (response.data.result === true) {
            Swal.fire({
                icon: "success",
                title: "등록완료",
                text: "등록한 회의는 목록에서 확인할 수 있습니다",
            }).then(() => {
                router.replace({ name: 'ProjectList' }) //프로젝트 조회페이지로 이동
            });
        }
    } catch (err) {
        Swal.fire({
            icon: "error",
            title: "등록실패",
            text: "회의 등록 실패",
        })
    }
}

</script>