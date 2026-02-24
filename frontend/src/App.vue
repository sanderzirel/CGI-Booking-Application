<template>
  <HomeView />
</template>

<script setup>
  import HomeView from "./views/HomeView.vue"
  import {onMounted} from "vue"
  
  onMounted(() => {
    fetch("http://localhost:8080/api/tables")
      .then(res => res.text())
      .then(data => {
        console.log(data)
      })
  })

</script>

<style scoped></style>
