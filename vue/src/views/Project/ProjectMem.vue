<template>
    <div class="content">
        <div class="container-fluid">
            <card>
                <h4 class="card-title float-left">프로젝트 참여자관리</h4>
            </card>

            <div class="row">
                <!-- 좌측 영역 : 사원정보 -->
                <div class="col-8">
                    <card>
                        <div class="row m-0">
                            <!-- 트리뷰(부서목록) -->
                            <div class="alert alert-primary">
                                <span>
                                    <i class="fa-solid fa-circle-info" aria-hidden="true"></i>
                                    추가할 구성원을 선택하여 배정할 프로젝트로 드래그해주세요
                                </span>
                            </div>
                            <div class="col-4 m-group"><!-- 조직 트리 -->
                                <DepartmentComponent v-if="isTreeLoaded" :departmentTree="departmentTree" />
                            </div>

                            <!-- 리스트(사원목록) -->
                            <div class="col-8 m-group">
                                <div class="d-flex p-2" style="justify-content: end;">
                                    <div class="d-flex" style="align-items: center;">
                                        <select name="searchSel" id="searchSel" class="form-select w50">
                                            <option value="name">이름</option>
                                            <option value="auth">사번</option>
                                        </select>
                                        <input type="text" class="form-control mlp10" placeholder="검색어 입력" />
                                        <button class="btn btn-info btn-fill mlp10 w30">검색</button>
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

                                    <VueDraggableNext 
                                        v-if="members.length > 0" 
                                        tag="tbody" 
                                        :list="members"
                                        :multiDrag="true" 
                                        :selected-class="'selected'" 
                                        @start="onDragStart"
                                        @end="onDrop" 
                                        animation="300">
                                        <tr v-for="(member, i) in members" :key="i">
                                            <td>{{ i + 1 }}</td>
                                            <td>
                                                <div class="profile-text" align="left">
                                                    <span class="team-label" style="text-align:left">{{
                                                        member.deptNm
                                                        }}</span>
                                                    <span class="user-name">{{ member.mberNm }} ({{ member.mberId
                                                        }})</span>
                                                </div>
                                            </td>
                                            <td>{{ member.gradeNm || '-' }}</td>
                                            <td>{{ member.projectCnt }}건</td>
                                        </tr>
                                    </VueDraggableNext>
                                </table>
                            </div>
                        </div>
                    </card>
                </div>

                <!-- 우측 영역 : 트리 뷰 (프로젝트 목록) -->
                <div class="col-4">
                    <card>
                        <div class="m-0">
                            <div class="d-flex mb-1" style="justify-content: end;">
                                <div class="d-flex" style="align-items: center;">
                                    <input type="text" class="form-control" placeholder="프로젝트명 입력" />
                                    <button class="btn btn-info btn-fill mlp10 w30">검색</button>
                                </div>
                            </div>

                            <div class="project treeview">
                                <!-- 프로젝트 트리 -->
                                <draggable
                                    :list="projectList"
                                    group="project"
                                    @add="onDrop"
                                >
                                    <ul class="list-unstyled" v-for="project in projectList" :key="project"
                                        style="cursor: pointer;">
                                        <span @click="project.memberArr.length > 0 ? toggleMemMenu(project) : ''">
                                            <i :class="['folder', project.isHidden ? 'bi bi-folder-minus' : 'bi bi-folder-plus',
                                                project.memberArr.length < 1 ? 'bi bi-folder' : '']"></i>
                                            {{ project.prNm }} ({{ project.memberArr.length }})
                                        </span>

                                        <li v-for="member in project.memberArr" :key="member" class="ms-3"
                                            v-show="project.isHidden">

                                            <div class="form-check form-switch d-flex"
                                                style="justify-content: start; align-items: center;">
                                                <input class="form-check-input" type="checkbox" role="switch"
                                                    id="flexSwitchCheckChecked" checked
                                                    v-model="member.isChecked"
                                                    @change="toggleManager(member)">
                                                <span>
                                                    {{ member.deptNm != null ? '[' + member.deptNm + ']' : '' }}
                                                    {{ member.mberNm || '' }}
                                                    {{ member.gradeNm || '' }}</span>
                                                <i class="bi bi-x" @click="btnProjectMemRemove(member)"></i>
                                                <i class="fa-solid fa-crown master mlp5" aria-hidden="true"
                                                v-show="member.mgrSt === 'A01'"></i>
                                            </div>
                                        </li>
                                    </ul>
                                </draggable>
                            </div>
                        </div>
                    </card>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import { onBeforeMount, ref, defineComponent } from 'vue';
import axios from "../../assets/js/customAxios.js";

//========================== 컴포넌트 ==========================
import Swal from 'sweetalert2';
import Card from '../../components/Cards/Card.vue'
import { VueDraggableNext } from 'vue-draggable-next'
import DepartmentComponent from "../../components/Department/DepartmentComponent.vue";

//========================== 데이터 ==========================
const isTreeLoaded = ref(false);
const departmentTree = ref([]);

defineComponent({
    components: { VueDraggableNext }
});

onBeforeMount(async () => {
    memberGetList();
    projectGetList();
    await departmentGetList();
    isTreeLoaded.value = true;
});

const toggleMemMenu = (memberMenu) => {
    memberMenu.isHidden = !memberMenu.isHidden;
};

const draggedMem = ref([]);
const onDragStart = (event) => {
    draggedMem.value = members.value[event.oldIndex];
    console.log(draggedMem.value);
};

const onDrop = (event) => {
    const projectIndex = event.newIndex; // 드롭된 위치의 인덱스
    const project = projectList.value[projectIndex];

    project.memberArr.push(draggedMem);
    ProjectMemInsert(draggedMem.value.mberId);
    project.memberArr.value = project.memberArr.filter(emp => emp.mberId !== draggedMem.value.mberId);

};

const toggleManager = (member) => {
  member.mgrSt = member.isChecked ? "A01" : ""; // 체크되면 A01, 아니면 빈 값
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

const departmentGetList = async () => {
    try {
        const response = await axios.get('/api/department');
        const tree = buildPrimeVueTree(response.data);
        departmentTree.value = tree;

    } catch (err) {
        departmentTree.value = []
        Swal.fire({
            icon: "error",
            title: "API 조회 실패",
            text: `Error: ${err.response?.data?.error || err.message}`
        })
    }
}

const buildPrimeVueTree = (flatList) => {
    const map = new Map()

    // 전체 데이터 Map에 먼저 등록
    flatList.forEach(item => {
        map.set(item.deptCd, {
            key: item.deptCd,
            label: item.deptNm,
            children: []
        })
    })

    const tree = []

    // 부모-자식 연결
    flatList.forEach(item => {
        if (item.parentCd) {
            map.get(item.parentCd).children.push(map.get(item.deptCd))
        } else {
            tree.push(map.get(item.deptCd))
        }
    })

    return tree
}


//프로젝트 참여자 등록
const prCd = ref('');
const mberId = ref('');
const ProjectMemInsert = async () => {

    await axios.post("/api/project", {
        prCd: prCd.value,
        mberId: mberId.value
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