<script setup>
  onMounted(() => {
    fetch("http://localhost:8080/")
      .then(res => res.text())
      .then(data => {
        console.log(data)
      })
  })
</script>

<template>
  <h1>You did it!</h1>
  <p>
    Visit <a href="https://vuejs.org/" target="_blank" rel="noopener">vuejs.org</a> to read the
    documentation
  </p>
</template>

<style scoped></style>
