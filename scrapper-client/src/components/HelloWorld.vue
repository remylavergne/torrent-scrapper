<template>
  <div class="hello">
    <h1>{{ msg }}</h1>
    <h1>{{ test }}</h1>

    <div>
      <input v-model="request" type="text" />
      <button v-on:click="getQueryResults">search</button>
    </div>

    <div>
      <TorrentResultsList v-if="displayList" :torrents="results"></TorrentResultsList>
    </div>
  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from "vue-property-decorator";
import TorrentResultsList from "@/components/TorrentResultsList.vue";
import { Torrent } from "@/models/Torrent.model";

@Component({
  name: "home",
  components: {
    TorrentResultsList
  }
})
export default class HelloWorld extends Vue {
  @Prop() private msg!: string;
  @Prop() private test!: string;
  request = "";
  results: Torrent[] = []; // Ma liste qui change
  displayList = false;

  public getQueryResults(): void {
    console.log("getQueryResults() called");
    if (this.request.length > 0) {
      console.log("request", this.request);
      fetch(`http://localhost:7070/${this.request.trim()}`, {
        method: "GET"
        //   headers: {
        //     "Content-Type": "application/json"
        //   }
      }).then(
        (data: Response) => {
          data.json().then((r: Torrent[]) => {
            this.results = [...r]; // Update ici
            this.displayList = true;
            console.log(r);
          });
        },
        error => {
          console.log("error", error);
        }
      );
    }
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
