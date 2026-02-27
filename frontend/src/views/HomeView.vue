<template>
    <div class="header"> 
        <h3>Welcome to restaurant PastaWorld!</h3>
    </div>
    <div class="container">
        <div class="restaurantPlan">
            <div v-for="table in floorPlan" :key="table.id"  :class="['table', table.reserved ? 'reserved' : '']">
                {{table.size + " persons"}}
            </div>
        </div>
        <div class="filters">
            <dropdownMenu :items="locations" :defaultValue="locations[0]" @update="selectedLocation = $event"/>
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
                <dropdownMenu :items="timeSlots" :defaultValue="timeSlots[0]" @update="selectedTime = $event"/>
            </div>
            <div class="preferences">
                <p>Preferences:</p>
                <button>Couch area</button>
                <button>Window seat</button>
                <button>Quiet area</button>
            </div>
        </div>

    </div>
</template>

<script setup>
    import {ref, computed, onMounted, watch} from "vue";
    import dropdownMenu from "../components/dropdown-menu.vue";

    const tablesInside = ref([]);
    const tablesOutside = ref([]);
    const tablesPrivate = ref([]);
    const timeSlots = ["12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00"];
    const locations = ["Inside", "Outside", "Private"];
    
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

    function fetchTables() {
        fetch(`http://localhost:8080/api/tables?date=${selectedDate.value}&time=${selectedTime.value}`)
            .then(response => response.json())
            .then(data => {
                console.log("Fetched tables:", data);
                tablesInside.value = data.filter(t => t.zone === "inside")
                tablesOutside.value = data.filter(t => t.zone === "outside")
                tablesPrivate.value = data.filter(t => t.zone === "private")
            })
            .catch(error => console.error("Error fetching tables:", error));
    };
    
    onMounted(fetchTables);
    watch([selectedDate, selectedTime], fetchTables);
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

.table.reserved {
  background-color: rgb(95, 91, 91);
}

.time-picker {
    display: flex;
    flex-direction: row;
    align-items: center;
    gap: 10px;
}
.preferences.buttons {
    display: flex;
    flex-direction: row;
    gap: 10px;
}

</style>