<template>
    <div class="header"> 
        <h3>Welcome to restaurant PastaWorld!</h3>
    </div>
    <div class="container">
        <div class="filters">
            <p>Will add filters here later</p>
        </div>
        <div class="hallPlan">
            <div v-for="table in tablesInside" :key="table.id" :class="['table', tableSize(table.size)]">
                {{table.size + " persons"}}
            </div>
        </div>

    </div>
</template>

<script setup>
    import {ref} from "vue";

    function tableSize(size) {
        if (size <= 2) return "small";
        if (size <= 4) return "medium";
        else return "big"
    }

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

#app {
  min-height: 100vh;
  width: 100%;
  display: flex;
  flex-direction: column;
  text-align: center;
}

.hallPlan {
    display: grid;
    grid-template-columns: repeat(3, 1fr);
    gap: 15px;
}

.table {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 80px;
  border-radius: 8px;
  font-weight: bold;
  background-color: white;

  border: 2px solid #222;
  box-sizing: border-box;
  background-color: #388087;
}

.table.small { width: 70px; }
.table.medium { width: 90px; }
.table.large { width: 120px; }
</style>