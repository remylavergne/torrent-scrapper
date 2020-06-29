<template>
  <div class="hello">
    <h1>{{ msg }}</h1>
    <h1>{{ test }}</h1>

    <div>
      <input v-model="request" type="text" />
      <button v-on:click="getQueryResults" class="search-button">search</button>
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
      fetch(`http://localhost:7070/classic-search/${this.request.trim()}`, {
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
input {
  width: 200px;
  height: 35px;
  padding-left: 8px;
  border: 1px solid #ccc;
  border-radius: 4px;
  box-sizing: border-box;
  margin-right: 10px;
}
.search-button {
	box-shadow:inset 0px 1px 0px 0px #dcecfb;
	background:linear-gradient(to bottom, #bddbfa 5%, #80b5ea 100%);
	background-color:#bddbfa;
	border-radius:6px;
	border:1px solid #84bbf3;
	display:inline-block;
	cursor:pointer;
	color:#ffffff;
	font-family:Arial;
	font-size:15px;
	font-weight:bold;
	padding:6px 24px;
	text-decoration:none;
	text-shadow:0px 1px 0px #528ecc;
}
.search-button:hover {
	background:linear-gradient(to bottom, #80b5ea 5%, #bddbfa 100%);
	background-color:#80b5ea;
}
.search-button:active {
	position:relative;
	top:1px;
}


</style>
