<template>
    <div class="content">
        <div class="container-fluid">
            <card>
                <h4 class="card-title float-left">프로젝트 참여자관리</h4>
            </card>

            <div class="row">
                <div class="col-7">
                    <card>
                        <div class="row m-0">
                            <!-- 트리 뷰 (왼쪽) -->
                            <div class="col-3 treeview p-2"><!-- 조직 트리 -->
                                <ul class="list-unstyled">
                                    <li><i class="fa-solid fa-angle-down"></i> 개발팀 (137)</li>
                                    <li class="ms-3"><i class="fa-solid fa-angle-right" aria-hidden="true"></i> 개발 1팀
                                        (10)
                                    </li>
                                    <li class="ms-3"><i class="fa-solid fa-angle-right" aria-hidden="true"></i> 개발 2팀
                                        (12)
                                    </li>
                                    <li class="ms-3 mb-1"><i class="fa-solid fa-angle-right" aria-hidden="true"></i> 개발
                                        3팀
                                        (15)</li>
                                    <li><i class="fa-solid fa-angle-down" aria-hidden="true"></i> 디자인팀 (13)</li>
                                    <li class="ms-3"><i class="fa-solid fa-angle-right" aria-hidden="true"></i> 디자인 1팀
                                        (5)
                                    </li>
                                    <li class="ms-3"><i class="fa-solid fa-angle-right" aria-hidden="true"></i> 디자인 2팀
                                        (6)
                                    </li>
                                    <li class="ms-3 mb-1"><i class="fa-solid fa-angle-right" aria-hidden="true"></i> 디자인
                                        3팀
                                        (2)</li>
                                    <li class="mb-1"><i class="fa-solid fa-angle-right" aria-hidden="true"></i> 인사팀 (6)
                                    </li>
                                    <li><i class="fa-solid fa-angle-right" aria-hidden="true"></i> 기획팀 (9)</li>
                                </ul>
                            </div>

                            <!-- 구성원 테이블 (오른쪽) -->
                            <div class="col-9 m-group">
                                <div class="bottom-line">
                                    <div class="d-flex justify-content-between align-items-center p-2">
                                        <div class="d-flex justify-content-between align-items-center w50">
                                            <select name="searchSel" id="searchSel" class="form-select w25">
                                                <option value="name">이름</option>
                                                <option value="auth">권한</option>
                                            </select>
                                            <input type="text" class="form-control w50" placeholder="검색어 입력" />
                                            <button class="btn btn-info btn-fill w20">검색</button>
                                        </div>
                                    </div>
                                </div>

                                <table class="table m-table">
                                    <thead class="table-light">
                                        <tr>
                                            <th>사번</th>
                                            <th>이름</th>
                                            <th>직급</th>
                                        </tr>
                                    </thead>

                                    <tbody>
                                        <tr v-for="member in members" :key="member.mberId">
                                            <td>{{ member.mberId }}</td>
                                            <td>
                                                <div class="profile-text">
                                                    <span class="team-label">개발팀</span>
                                                    <span class="user-name">{{ member.mberNm }}</span>
                                                </div>
                                            </td>
                                            <td>{{ member.gradeNm }}</td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>

                        </div>
                    </card>
                </div>
                <div class="col-5">
                    <card>
                        <div class="m-0">
                            <!-- 트리 뷰 (왼쪽) -->
                            <div class="treeview p-2">
                                <!-- 조직 트리 -->
                                <ul class="list-unstyled">
                                    <li v-for="project in projectList" :class="project.parent == 0 ? 'ms-3' : ''" :key="project">{{ project.parent == 1 ? '📂 '+project.prNm : '• '+project.mberNm }}</li>
                                </ul>
                            </div>
                        </div>
                    </card>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import axios from "../../assets/js/customAxios.js";
import { onBeforeMount, ref } from 'vue';

//========================== 컴포넌트 ==========================
import Swal from 'sweetalert2';
import Card from '../../components/Cards/Card.vue'

onBeforeMount(() => {
    memberGetList();
    projectGetList();
});

//======================= axios =======================

//구성원 전체조회
const members = ref([]);
const memberGetList = async () => {

    try {
        const result = await axios.get(`/api/member`);
        members.value = result.data;

    } catch (err) {
        members.value = [];

        Swal.fire({
            icon: "error",
            title: "API 조회 오류",
            text: "Error : " + err
        });
    }
}

//진행중인 프로젝트 전체조회
const projectList = ref([]);
const projectGetList = async () => {

  try {
    const result = await axios.get('/api/project/tree');
    projectList.value = result.data;
    console.log(projectList.value);

  } catch (err) {
    projectList.value = [];

    Swal.fire({
      icon: "error",
      title: "API 조회 오류",
      text: "Error : " + err
    });
  }
}

</script>