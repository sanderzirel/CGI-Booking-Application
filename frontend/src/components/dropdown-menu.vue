<template>
  <div class="dropdown">
    <button class="dropdown-btn" @click="toggle">
      {{ selected }}
    </button>

    <div v-if="isOpen" class="dropdown-menu">
      <div v-for="option in options" :key="option" class="dropdown-item"
        @click="select(option)">
        {{ option }}
      </div>
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref } from "vue"
const props = defineProps({
  items: {
    type: Array,
    default: () => []
  },
  defaultValue: {
    type: String,
    default: ""
  }
})
const isOpen = ref(false)

const options = props.items

const selected = ref(props.defaultValue)

const emit = defineEmits(["update"])


function toggle() {
  isOpen.value = !isOpen.value
}

function select(option) {
  selected.value = option
  isOpen.value = false
  emit("update", option)
}

onMounted(() => {
  document.addEventListener("click", (event) => {
    if (isOpen.value && !event.target.closest(".dropdown")) {
      isOpen.value = false
    }
  })
})
</script>

<style>
.dropdown {
  position: relative;
  width: 80%;
}

.dropdown-btn {
  width: 100%;
  padding: 10px;
  cursor: pointer;
}

.dropdown-menu {
  position: absolute;
  width: 100%;
  background: white;
  border: 1px solid #ccc;
  margin-top: 5px;
  border-radius: 6px;
  box-shadow: 0 4px 10px rgba(0,0,0,0.1);
  max-height: 150px;
  overflow-y: auto;
  min-width: 100px;
}

.dropdown-item {
  padding: 10px;
  cursor: pointer;
}

.dropdown-item:hover {
  background-color: #f2f2f2;
}
</style>