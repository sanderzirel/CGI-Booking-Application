<template>
    <div class="header"> 
        <h3>Welcome to restaurant PastaWorld!</h3>
    </div>
    <div class="container">
        <div class="restaurantPlan">
            <div v-for="table in floorPlan" :key="table.id" :class="'table'">
                {{table.size + " persons"}}
            </div>
        </div>
        <div class="filters">
            <dropdownMenu @update="selectedLocation = $event"/>
            <div class="people-count">
                <p>People:</p>
                <input type="number" v-model="peopleCount" min="0" max="20" @input="peopleLimit"/>
            </div>
            <div class="date-picker">
                <p>Date:</p>
                <input type="date" v-model="selectedDate" :min="today"/>
            </div>
            <div class="time-picker">
                <p>Time:</p>
                <input type="time" v-model="selectedTime" min="12:00" max="22:00" step="900"/>
            </div>
        </div>

    </div>
</template>

<script setup>
    import {ref, computed} from "vue";
    import dropdownMenu from "../components/dropdown-menu.vue";
    
    //Tables for restaurant
    const tablesInside = ref([
        {id: 1, size: 2},
        {id: 2, size: 2},
        {id: 3, size: 4},
        {id: 4, size: 4},
        {id: 5, size: 4},
        {id: 6, size: 4},
        {id: 7, size: 6},
        {id: 8, size: 6},
        {id: 9, size: 2},
        {id: 10, size: 2},
    ]);
     const tablesOutside = ref([
        {id: 1, size: 2},
        {id: 2, size: 2},
        {id: 3, size: 4},
        {id: 4, size: 4},
        {id: 5, size: 4},
        {id: 6, size: 6},
    ]);
     const tablesPrivate = ref([
        {id: 1, size: 20},
        {id: 2, size: 15}
    ])

    const selectedLocation = ref("Inside");

    const tablesSelection = {
        Inside: tablesInside,
        Outside: tablesOutside,
        Private: tablesPrivate
    };

    const floorPlan = computed(() => 
        tablesSelection[selectedLocation.value].value
    );

    const peopleCount = ref(0);

    function peopleLimit() {
        if (peopleCount.value === "" || peopleCount.value === null) return;
        if (peopleCount.value < 0) {
            peopleCount.value = 0;
        } else if (peopleCount.value > 20) {
            peopleCount.value = 20;
        }
    }

    const today = new Date().toISOString().substr(0, 10);
    const selectedDate = ref(today);
    const selectedTime = ref("");

</script>

<style>
.header {
    box-sizing: border-box;
    width: 100%;
    padding: 15px;
    border-radius: 15px;
    font-size: 20px;
    display: flex;
    background-color: #388087;
}

.container {
    display: grid;
    grid-template-columns: 80% 1fr;
    padding: 20px;
    margin-top: 20px;
    gap: 10px;
}

#app {
  min-height: 100vh;
  width: 100%;
  display: flex;
  flex-direction: column;
  text-align: center;
}

.restaurantPlan {
    display: grid;
    grid-template-columns: repeat(3, 1fr);
    gap: 15px;
    padding: 10px;
    background-color: #ccc;
    border-radius: 10px;
}

.table {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 80px;
  border-radius: 8px;
  font-weight: bold;
  background-color: white;
  width: 100px;

  border: 2px solid #222;
  box-sizing: border-box;
  background-color: #388087;
}

.filters {
    display: flex;
    flex-direction: column;
    gap: 10px;
    padding: 10px;
    align-items: center; 
    background: #388087;
    border-radius: 10px;
    height: fit-content;
}

.people-count {
    display: flex;
    align-items: center;
    gap: 10px;
}

.date-picker {
    display: flex;
    align-items: center;
    gap: 10px;
}

</style>