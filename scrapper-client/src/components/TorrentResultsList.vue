<template>
  <div>
    <h1 class="torrent-results">Results</h1>
    <!-- component template -->
    <div>
      <table class="torrent-list-results">
        <!-- Headers -->
        <thead class="torrent-list-results-header">
          <tr>
            <th v-for="column in columns" :key="column" class="headers-titles">
              {{ column | capitalize }}
              <span class="arrow"></span>
            </th>
          </tr>
        </thead>

        <tbody>
          <tr v-for="torrent in torrents" :key="torrent.id" v-on:click="openTorrent(torrent)">
            <td>{{ torrent.domain }}</td>
            <td>{{ torrent.filename }}</td>
            <td>{{ torrent.seeders }}</td>
            <td>{{ torrent.leechers }}</td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from "vue-property-decorator";
import { Torrent } from "@/models/Torrentorrent.model";

@Component
export default class TorrentResultsList extends Vue {
  @Prop() torrents!: Torrent[];

  private columns: string[] = ["Domain", "Filename", "Seeders", "Leechers"];

  mounted() {
    console.log("TorrentResultsList mounted()");
    console.log("data into list", this.torrents);
  }

  openTorrent(torrent: Torrent) {
    console.log('Click torrent', torrent);
     window.open(torrent.url, "_blank");
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
  white-space: nowrap;
  margin: 0 10px;
}
a {
  color: #42b983;
}
.torrent-results {
    margin-top: 20px;
    font-style: italic;
}
.torrent-list-results {
  margin: 20px auto;
  border-collapse: collapse;
}
.torrent-list-results-header {
    border: 1px solid lightblue;
}
.headers-titles {
   padding: 10px;
}
</style>
