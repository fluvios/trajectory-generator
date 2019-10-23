// index.js

const path = require("path");
const express = require("express");

const app = express();
const port = process.env.PORT || "8000";

app.set("views", path.join(__dirname, "views"));
app.set("view engine", "pug");

app.get("/", (req, res) => {
  res.render("index", { title: "Home" });
});

app.listen(port, () => {
  console.log(`Listening to requests on http://localhost:${port}`);
});