<template>
  <div>
    <div class="torrent-results">Results</div>
    <div>
      <ul id="example-1">
        <li v-for="item in torrents" :key="item.id">{{ item.domain }} - {{ item.filename }} - {{ item.completion }} - {{ item.seeders }} - {{ item.leechers }}</li>
      </ul>
    </div>
  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from "vue-property-decorator";
import { Torrent } from "@/models/Torrent.model";

@Component
export default class TorrentResultsList extends Vue {
  @Prop() torrents!: Torrent[];

  mounted() {
    console.log("TorrentResultsList mounted()");
    console.log("data into list", this.torrents);
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="scss">
h3 {
  margin: 40px 0 0;
}
ul {
  list-style-type: none;
  padding: 0;
}
li {
  display: inline-block;
  margin: 0 10px;
}
a {
  color: #42b983;
}
</style>
