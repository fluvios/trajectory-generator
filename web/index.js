var shapefile = require("shapefile");
 
shapefile.open("https://cdn.rawgit.com/mbostock/shapefile/master/test/points.shp")
  .then(source => source.read()
    .then(function log(result) {
      if (result.done) return;
      console.log(result.value);
      return source.read().then(log);
    }))
  .catch(error => console.error(error.stack));