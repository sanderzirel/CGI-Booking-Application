<template>
    <div class="header"> 
        <h3>Welcome to restaurant PastaWorld!</h3>
    </div>
    <div class="container">
            <div class="restaurantPlan">

                <div class="inside-section">
                    <p class="section-name" :class="{ active: selectedLocation === 'Inside' }">Inside</p>
                    <div class="tables-grid">
                        <div v-for="table in tablesInside" :key="table.id" :class="['table', table.reserved ? 'reserved' : '', isSuggested(table) ? 'suggested' : '', isSelected(table) ? 'selected' : '']" @click="selectTable(table)">
                            {{table.size + " people"}}
                        </div>
                    </div>
                </div>
                
                <div class="divider"></div>

                <div class="outside-section">
                    <p class="section-name" :class="{ active: selectedLocation === 'Outside' }">Outside</p>
                    <div class="tables-grid">
                        <div v-for="table in tablesOutside" :key="table.id" :class="['table', table.reserved ? 'reserved' : '', isSuggested(table) ? 'suggested' : '', isSelected(table) ? 'selected' : '']" @click="selectTable(table)">
                            {{table.size + " people"}}
                        </div>
                    </div>
                </div>

                <div class="private-section">
                    <p class="section-name" :class="{ active: selectedLocation === 'Private' }">Private</p>
                    <div class="tables-grid">
                        <div v-for="table in tablesPrivate" :key="table.id" :class="['table', table.reserved ? 'reserved' : '', isSuggested(table) ? 'suggested' : '', isSelected(table) ? 'selected' : '']" @click="selectTable(table)">
                            {{table.size + " people"}}
                        </div>
                    </div>
                </div>
            </div>
        <div class="containerbox">
            <div class="filters">
                <div class="filter-row">
                    <dropdownMenu :items="locations" :defaultValue="locations[0]" @update="selectedLocation = $event"/>
                </div>
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
                </div>
                <div class="preferences-buttons">
                    <button 
                        :class="{ active: selectedPreference === 'Couch area' }"
                        @click="selectedPreference = selectedPreference === 'Couch area' ? '' : 'Couch area'">
                        Couch area
                    </button>
                    <button 
                        :class="{ active: selectedPreference === 'Window seat' }"
                        @click="selectedPreference = selectedPreference === 'Window seat' ? '' : 'Window seat'">
                        Window seat
                    </button>
                    <button 
                        :class="{ active: selectedPreference === 'Quiet area' }"
                        @click="selectedPreference = selectedPreference === 'Quiet area' ? '' : 'Quiet area'">
                        Quiet area
                    </button>
                </div>    
            </div>

            <div v-if="showWarning" class="bookingForm-overlay">
                <div class="bookingForm">
                    <p>For private dining, we require a minimum of <strong>8 people</strong>. Please adjust your preferences.</p>
                    <div class="bookingForm-buttons">
                        <button @click="showWarning = false">OK</button>
                    </div>
                </div>
            </div>

            <div class="booking" v-if="selectedTableId !== null">
                <button @click="openBookingForm = true">Book Table</button>
            </div>

            <div v-if="openBookingForm" class="bookingForm-overlay">
                <div v-if="!successfulBooking" class="bookingForm">
                    <h3>Complete your booking</h3>
                    <label>Name</label>
                    <input v-model="guestName" type="text" placeholder="Full name" />
                    <label>Email</label>
                    <input v-model="guestEmail" type="email" placeholder="Email address" />
                    <label>Phone</label>
                    <input v-model="guestPhone" type="tel" placeholder="Phone number" />
                    <div class="bookingForm-buttons">
                        <button @click="bookTable()">Confirm</button>
                        <button @click="openBookingForm = false">Cancel</button>
                    </div>
                </div>
                <div v-if="successfulBooking" class="thankYouMessage">
                    <h2>Thank you for your reservation!</h2>
                    <p>We look forward to welcoming you at PastaWorld.</p>
                    <div class="bookingForm-buttons">
                        <button @click="openBookingForm = false">Close</button>
                    </div>
                </div>
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
    const timeSlots = ["12:00", "12:30", "13:00", "13:30","14:00", "14:30", "15:00", "15:30", "16:00", "16:30", 
    "17:00", "17:30", "18:00", "18:30", "19:00", "19:30", "20:00", "20:30", "21:00"];
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
    const selectedPreference = ref("");
    const suggestedTableIds = ref([]);
    const selectedTableId = ref(null);
    
    function isSuggested(table) {
        return suggestedTableIds.value.includes(table.id);
    }

    function isSelected(table) {
        return selectedTableId.value === table.id;
    }

    function selectTable(table) {
        if(!isSuggested(table) || table.reserved) return;
        selectedTableId.value = selectedTableId.value === table.id ? null : table.id;
    }

    function fetchTables() {
        fetch(`http://localhost:8080/api/tables?date=${selectedDate.value}&time=${selectedTime.value}`)
            .then(response => response.json())
            .then(data => {
                console.log("Fetched tables:", data);
                tablesInside.value = data.filter(t => t.zone === "inside")
                tablesOutside.value = data.filter(t => t.zone === "outside")
                tablesPrivate.value = data.filter(t => t.zone === "private")

                if ((peopleCount.value > 0 || selectedPreference.value) && !enoughPeople()) {
                    fetchSuggestions();
                } else {
                    suggestedTableIds.value = [];
                }
                selectedTableId.value = null;
            })
            
            .catch(error => console.error("Error fetching tables:", error));
    };

    function fetchSuggestions() {
        const params = new URLSearchParams({
            date: selectedDate.value,
            time: selectedTime.value,
            peopleCount: peopleCount.value || 1,
            location: selectedLocation.value
        });

        if (selectedPreference.value) {
            params.append("preference", selectedPreference.value);
        }

        fetch(`http://localhost:8080/api/tables/suggest?${params}`)
            .then(response => response.json())
            .then(data => {
                console.log("Suggested tables:", data);
                suggestedTableIds.value = data.map(t => t.id);
            })
            .catch(error => console.error("Error fetching suggestions:", error));
    }

    const openBookingForm = ref(false);
    const guestName = ref("");
    const guestEmail = ref("");
    const guestPhone = ref("");
    const successfulBooking = ref(false);
    const showWarning = ref(false);

    function bookTable() {
        if (!guestName.value || !guestEmail.value || !guestPhone.value) {
            alert("Please fill in all guest details.");
            return;
        }
        const reservationDetails = {
            tableId: selectedTableId.value,
            date: selectedDate.value,
            time: selectedTime.value,
            guestName: guestName.value,
            guestEmail: guestEmail.value,
            guestPhone: guestPhone.value
        }
        fetch(`http://localhost:8080/api/reservations`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(reservationDetails)
        })
        .then(response => {
            if (response.ok) {
                selectedTableId.value = null;
                successfulBooking.value = true;
                guestName.value = "";
                guestEmail.value = "";
                guestPhone.value = "";
                fetchTables();
            } else {
                alert("Failed to make reservation.");
            }
        })
        .catch(error => console.error("Error booking table:", error));
    }

    function warningCheck() {
        if (enoughPeople()) {
            showWarning.value = true;
            suggestedTableIds.value = [];
            selectedTableId.value = null;
            return true;
        }
        return false;
    }

    function enoughPeople() {
        return selectedLocation.value === "Private" && peopleCount.value > 0 && peopleCount.value < 8;
    }
    
    onMounted(fetchTables);
    watch([selectedDate, selectedTime], fetchTables);

    watch([peopleCount, selectedPreference], () => {
        if (warningCheck()) return;

        showWarning.value = false;
        if (peopleCount.value > 0 || (selectedPreference.value && peopleCount.value > 0)) {
            fetchSuggestions();
        } else {
            suggestedTableIds.value = [];
        }
        selectedTableId.value = null;
    });

    watch(selectedLocation, () => {
        selectedPreference.value = "";
        suggestedTableIds.value = [];
        selectedTableId.value = null;
        if (warningCheck()) return;

        showWarning.value = false;
        if (peopleCount.value > 0) {
            fetchSuggestions();
        }
    });
</script>

<style>
.header {
    box-sizing: border-box;
    width: 100%;
    padding: 15px;
    border-radius: 15px;
    font-size: 20px;
    display: flex;
    background-color: #41431B;
    color: #F8F3E1;
}

.container {
    display: flex;
    padding: 20px;
    margin-top: 20px;
    gap: 10px;
    min-height: 600px;
}

#app {
  min-height: 100vh;
  width: 100%;
  display: flex;
  flex-direction: column;
  text-align: center;
  min-width: max-content;
}

.section-name.active {
    color: #45a049;
}

.section-name {
    display: flex;
    justify-content: flex-start;
    font-weight: bold;
}

.restaurantPlan {
    display: grid;
    flex: 1;
    grid-template-columns: 1fr 1fr;
    grid-template-rows: auto auto;
    gap: 10px;
    padding: 10px;
    background-color: #E3DBBB;
    border-radius: 10px;
    position: relative;
}

.restaurantPlan::after {
    content: "";
    position: absolute;
    left: 0;
    top: 35%;
    transform: translateY(-50%);
    width: 4px;
    height: 70%;
    background-color: #0277bd;
}

.restaurantPlan::before {
    content: "";
    position: absolute;
    left: 20%;
    bottom: 0;
    transform: translateX(-50%);
    width: 40%;
    height: 4px;
    background-color: #0277bd;
}

.inside-section {
    grid-column: 1;
    grid-row: 1;
}

.inside-section .tables-grid {
    display: grid;
    grid-template-columns: repeat(3, 1fr);
    gap: 40px 20px;
    position: relative;
}

.inside-section .tables-grid::before {
    content: "";
    position: absolute;
    left: 32%;
    top: 25%;
    height: 200px;
    width: 2px;
    background-color: black;
    transform: translateY(-40%);
}

.inside-section .tables-grid::after {
    content: "";
    position: absolute;
    left: 66%;
    top: 25%;
    height: 200px;
    width: 2px;
    background-color: black;
    transform: translateY(-40%);
}

.divider {
    position: absolute;
    left: 49%;
    top: 10%;
    height: 200px;
    width: 2px;
    background-color: black;
    
}

.outside-section {
    grid-column: 2;
    grid-row: 1;
}

.private-section {
    grid-column: 1;
    grid-row: 2;
}
.private-section .tables-grid {
    grid-template-columns: repeat(2, 1fr);
}

.tables-grid {
    display: grid;
    grid-template-columns: repeat(3, 1fr);
    gap: 40px 20px;
}


.table {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 80px;
  border-radius: 8px;
  font-weight: bold;
  width: 100px;

  border: 2px solid #222;
  box-sizing: border-box;
  background-color: #F8F3E1;
}

.containerbox {
    display: flex;
    flex-direction: column;
    gap: 20px;
    width: 300px;
}
.filters {
    display: flex;
    flex-direction: column;
    gap: 15px;
    padding: 15px;
    background: #41431B;
    color: black;
    border-radius: 10px;
    height: fit-content;
}

.filters p {
    color: #F8F3E1;
    margin: 0;
    white-space: nowrap;
}

.filter-row {
    display: flex;
    width: 100%;
    position: relative;
    z-index: 2;
}

.people-count,
.date-picker,
.time-picker {
    display: flex;
    align-items: center;
    justify-content: space-between;
    width: 100%;
    gap: 10px;
}

.time-picker {
    position: relative;
    z-index: 1;
}

.people-count input,
.date-picker input {
    width: 120px;
    flex: none;
    padding: 4px 8px;
    box-sizing: border-box;
    font-size: 13px;
    height: 28px;
}

.time-picker .dropdown {
    width: 120px;
}

.time-picker .dropdown-btn {
    padding: 4px 8px;
    font-size: 13px;
    height: 28px;
    box-sizing: border-box;
}

.table.reserved {
  background-color: rgb(95, 91, 91);
}

.preferences {
    width: 100%;
}

.preferences-buttons {
    display: flex;
    flex-wrap: wrap;
    gap: 8px;
    width: 100%;
}

.preferences-buttons button {
    flex: 1 1 0;
    min-width: 0;
    padding: 10px;
    box-sizing: border-box;
    cursor: pointer;
}

.table.suggested {
  box-shadow: 0 0 0 4px #45a048;
  border-color: #45a048;
  cursor: pointer;
}

.table.selected {
    background-color: cornsilk;
    border-color: #ff9800;
    box-shadow: 0 0 0 4px #ff9800;
}
.preferences-buttons button.active {
    background-color: white;
    color: #41431B;
    font-weight: bold;
}

.preferences-buttons button:hover {
    background-color: #f2f2f2;
}

.booking button {
    padding: 10px 20px;
    font-size: 16px;
    background-color: #ff9800;
    color: cornsilk;
    border: none;
    border-radius: 5px;
    width: 100%;
    cursor: pointer;
}
.booking button:hover {
    background-color: #45a049;
}

.bookingForm-overlay {
    position: fixed;
    inset: 0;
    background: rgba(0, 0, 0, 0.5);
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 100;
}

.bookingForm, .thankYouMessage {
    background: #F8F3E1;
    border-radius: 10px;
    padding: 30px;
    display: flex;
    flex-direction: column;
    gap: 10px;
    min-width: 300px;
}

.bookingForm h3 {
    margin: 0 0 10px 0;
}

.bookingForm input {
    padding: 8px;
    border-radius: 6px;
    border: 1px solid #ccc;
    font-size: 14px;
}

.bookingForm-buttons {
    display: flex;
    gap: 10px;
    margin-top: 10px;
}

.bookingForm p {
    color: inherit;
}

</style>