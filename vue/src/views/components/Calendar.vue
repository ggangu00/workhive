<script setup>
import { ref, computed, defineEmits } from "vue";

// 부모(Home.vue)로 클릭한 날짜를 전달하는 이벤트 설정
const emit = defineEmits(["dateSelected"]);

// 현재 날짜 기준으로 초기 월/연도 설정
const today = new Date();
const currentYear = ref(today.getFullYear());
const currentMonth = ref(today.getMonth()); // 0: 1월, 11: 12월
const nowDt = ref(today.getDate()); // 0: 1월, 11: 12월

// 요일 배열
const weekDays = ["일", "월", "화", "수", "목", "금", "토"];

// 현재 달의 날짜 리스트 계산
const calendarDays = computed(() => {
    const firstDay = new Date(currentYear.value, currentMonth.value, 1).getDay(); // 시작 요일
    const lastDate = new Date(currentYear.value, currentMonth.value + 1, 0).getDate(); // 마지막 날짜

    let days = [];
    let week = [];

    // 첫 주 빈 칸 채우기
    for (let i = 0; i < firstDay; i++) {
        week.push(null);
    }

    // 날짜 채우기
    for (let date = 1; date <= lastDate; date++) {
        week.push(date);

        // 한 주(7일) 채우면 추가 후 초기화
        if (week.length === 7) {
            days.push(week);
            week = [];
        }
    }

    // 마지막 주 빈 칸 채우기
    if (week.length > 0) {
        while (week.length < 7) {
            week.push(null);
        }
        days.push(week);
    }

    return days;
});

const selectDate = (date) => {
    if (date) {
        nowDt.value = date;
        const selectedDate = `${currentYear.value}-${(currentMonth.value + 1).toString().padStart(2, "0")}-${date.toString().padStart(2, "0")}`;
        emit("dateSelected", selectedDate);
    }
};



</script>

<template>

    <!-- 캘린더 -->
    <table class="calendar-table">
        <thead>
            <tr class="tr-day">
                <th v-for="day in weekDays" :key="day">{{ day }}</th>
            </tr>
        </thead>
        <tbody>
            <tr v-for="(week, weekIndex) in calendarDays" :key="weekIndex" class="tr-date">
                <td v-for="(date, dateIndex) in week" :key="dateIndex" :class="date == nowDt ? 'today' : ''" @click="selectDate(date)">
                    {{ date }}
                </td>
            </tr>
        </tbody>
    </table>
</template>