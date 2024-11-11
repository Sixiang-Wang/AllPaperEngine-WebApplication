<template>
  <pdf :src="require('@/assets/pdf/test.pdf')"></pdf>
</template>

<script>
import pdf from 'vue-pdf'

export default {
  components: {
    pdf
  }
}
</script>