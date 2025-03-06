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
                            <div class="alert alert-primary">
                                <span>
                                    <i class="fa-solid fa-circle-info" aria-hidden="true"></i>
                                    추가할 구성원을 선택하여 배정할 프로젝트로 드래그해주세요
                                </span>
                            </div>
                            <div class="col-3 depth treeview"><!-- 조직 트리 -->
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
                                <div class="d-flex p-2 mb-1">
                                    <div class="d-flex" style="align-items: center;">
                                        <select name="searchSel" id="searchSel" class="form-select w50">
                                            <option value="name">이름</option>
                                            <option value="auth">사번</option>
                                        </select>
                                        <input type="text" class="form-control" placeholder="검색어 입력" />
                                        <button class="btn btn-info btn-fill w30">검색</button>
                                    </div>
                                </div>

                                <table class="table m-table">
                                    <colgroup>
                                        <col width="10%">
                                        <col width="20%">
                                        <col width="20%">
                                        <col width="30%">
                                    </colgroup>
                                    <thead class="table-light">
                                        <tr>
                                            <th>번호</th>
                                            <th>이름</th>
                                            <th>직급</th>
                                            <th>진행 중인 프로젝트 개수</th>
                                        </tr>
                                    </thead>

                                    <tbody>
                                        <tr v-for="(member, i) in members" :key="i">
                                            <td>{{ i + 1 }}</td>
                                            <td>
                                                <div class="profile-text" align="left">
                                                    <span class="team-label" style="text-align:left">{{ member.deptNm
                                                        }}</span>
                                                    <span class="user-name">{{ member.mberNm }} ({{ member.mberId
                                                        }})</span>
                                                </div>
                                            </td>
                                            <td>{{ member.gradeNm }}</td>
                                            <td>{{ member.projectCnt }}건</td>
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
                            <div class="d-flex justify-content-between align-items-center p-2 mb-1">
                                <div class="d-flex" style="align-items: center;">
                                    <label class="w50">프로젝트명</label>
                                    <input type="text" class="form-control" placeholder="검색어 입력" />
                                </div>
                            </div>

                            <div class="project treeview">
                                <!-- 프로젝트 트리 -->
                                <ul class="list-unstyled" v-for="project in projectList" :key="project"
                                    @click="project.memberArr.length > 0 ? toggleMemMenu(project) : ''"
                                    style="cursor: pointer;">
                                    <span>
                                        <i :class="['folder', project.isHidden ? 'bi bi-folder-minus' : 'bi bi-folder-plus',
                                            project.memberArr.length < 1 ? 'bi bi-folder' : '']"></i>
                                        {{ project.prNm }} ({{ project.memberArr.length }})
                                    </span>

                                    <li v-for="member in project.memberArr" :key="member" class="ms-3"
                                        v-show="project.isHidden">
                                        <span>
                                            <i class="bi bi-dot"></i>
                                            {{ member.deptNm != null ? '[' + member.deptNm + ']' : '' }}
                                            {{ member.mberNm || '' }}
                                            {{ member.gradeNm || '' }}
                                            <i class="bi bi-x" @click="btnProjectMemRemove(member)"></i>
                                            <i class="fa-solid fa-crown master mlp5" aria-hidden="true"
                                                v-show="member.mgrSt == 'A01'"></i>
                                        </span>
                                    </li>
                                </ul>
                            </div>
                        </div>
                    </card>
                </div>
            </div>
        </div>
    </div>

    <div class="container">
        <!-- 원본 리스트 -->
        <div class="list">
            <h3>원본 리스트</h3>
            <VueDraggableNext v-model="sourceItems" group="shared" item-key="id" @start="dragging = true"
                @end="dragging = false" @choose="onChoose" @unchoose="onUnchoose" :move="onMove"
                class="draggable-container">
                <template #item="{ element }">
                    <div :class="['draggable-item', { selected: selectedItems.includes(element.id) }]"
                        @click="toggleSelection(element.id, $event)">
                        {{ element.name }}
                    </div>
                </template>
            </VueDraggableNext>
        </div>

        <!-- 타겟 리스트 -->
        <div class="list">
            <h3>타겟 리스트</h3>
            <VueDraggableNext v-model="targetItems" group="shared" item-key="id" class="draggable-container">
                <template #item="{ element }">
                    <div class="draggable-item">
                        {{ element.name }}
                    </div>
                </template>
            </VueDraggableNext>
        </div>
    </div>

</template>

<script setup>
import { onBeforeMount, ref, defineComponent } from 'vue';
import axios from "../../assets/js/customAxios.js";

//========================== 컴포넌트 ==========================
import Swal from 'sweetalert2';
import { VueDraggableNext } from 'vue-draggable-next'
import Card from '../../components/Cards/Card.vue'

defineComponent({
    components: { VueDraggableNext }
});

onBeforeMount(() => {
    memberGetList();
    projectGetList();
});

const toggleMemMenu = (memberMenu) => {
    memberMenu.isHidden = !memberMenu.isHidden;
};

//========================== drag ==========================
const sourceItems = ref([
    { id: 1, name: "🍎 사과" },
    { id: 2, name: "🍌 바나나" },
    { id: 3, name: "🍇 포도" },
    { id: 4, name: "🍊 오렌지" },
    { id: 5, name: "🍉 수박" }
]);

const targetItems = ref([]);
const selectedItems = ref([]);
const dragging = ref(false);

// 아이템 선택/해제 (Ctrl/Command 키 체크)
const toggleSelection = (id, event) => {
    if (event.ctrlKey || event.metaKey) {
        if (selectedItems.value.includes(id)) {
            selectedItems.value = selectedItems.value.filter(item => item !== id);
        } else {
            selectedItems.value.push(id);
        }
    } else {
        selectedItems.value = [id]; // 단일 선택
    }
};

// 드래그 시 선택된 아이템만 이동하도록 필터
const onMove = (event) => {
    if (selectedItems.value.length > 0) {
        return selectedItems.value.includes(event.draggedContext.element.id);
    }
    return true;
};

// 드래그 시작 시 선택된 요소 유지
const onChoose = (event) => {
    if (!selectedItems.value.includes(event.item.__draggable_context.element.id)) {
        selectedItems.value = [event.item.__draggable_context.element.id];
    }
};

// 드래그가 끝난 후 선택 목록 초기화
const onUnchoose = () => {
    selectedItems.value = [];
};


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

        menuBuildTree(projectList);
    } catch (err) {
        projectList.value = [];

        Swal.fire({
            icon: "error",
            title: "API 조회 오류",
            text: "Error : " + err
        });
    }
}

// 프로젝트 트리 구조 생성
const menuBuildTree = (projectList) => {

    let projectArr = new Map();
    let memberArr = new Map();

    projectList.value.forEach(project => {

        if (project.parent == 1) {
            projectArr.set(project.prCd, {
                prNm: project.prNm,
                parent: project.parent,
                iconClass: 'bi bi-folder-plus',
                memberArr: [],
            });
        }
    });


    projectList.value.forEach(project => {

        if (project.parent == 0 && project.mberId) {
            const parentMenu = projectArr.get(project.prCd); // mainMenus의 key값으로 value를 가져옴

            if (parentMenu) {
                // parentMenu의 memberArr에 배열 넣기
                parentMenu.memberArr.push({
                    prCd: project.prCd,
                    prNm: project.prNm,
                    mberId: project.mberId,
                    mberNm: project.mberNm,
                    deptNm: project.deptNm,
                    gradeNm: project.gradeNm,
                    mgrSt: project.mgrSt,
                    iconClass: 'fa-solid fa-angle-right'
                });
                memberArr.set(project.prCd, parentMenu.memberArr[parentMenu.memberArr.length - 1]); // 저장
            }
        }
    });

    projectList.value = Array.from(projectArr, ([key, value]) => ({
        prCd: key,
        ...value,
    }));
}

// 프로젝트 참여자 삭제 버튼
const btnProjectMemRemove = (param) => {
    console.log(param);
    Swal.fire({
        title: "해당 참여자를 프로젝트에서 제외하시겠습니까?",
        icon: "question",
        showCancelButton: true,
        customClass: {
            confirmButton: "btn btn-secondary btn-fill",
            cancelButton: "btn btn-danger btn-fill"
        },
        confirmButtonText: "닫기",
        cancelButtonText: "삭제",
    }).then((result) => {
        if (result.dismiss == Swal.DismissReason.cancel) {
            ProjectMemRemove(param); //삭제처리 함수
        }
    });
}

//프로젝트 참여자 삭제
const ProjectMemRemove = async (param) => {

    try {
        const response = await axios.delete(`/api/project/tree/delete`, {
            params: {
                prCd: param.prCd,
                mberId: param.mberId
            }
        });

        if (response.data === true) {
            Swal.fire({
                icon: "success",
                title: "삭제완료",
                text: "선택한 참여자를 삭제하였습니다",
            })
        }
    } catch (err) {
        Swal.fire({
            icon: "error",
            title: "삭제 실패",
            text: "Error : " + err
        });
    }
}

</script>