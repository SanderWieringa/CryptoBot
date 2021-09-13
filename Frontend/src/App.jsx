
import React, { useState, useEffect } from "react";
import Datatable from "./datatable";

require("es6-promise").polyfill();
require("isomorphic-fetch");


export default function App() {
  const [data, setData] = useState({symbols: []})
  const [q, setQ] = useState("")

  useEffect(() => {
    fetch("http://localhost:8080/symbols")
    .then((response) => response.json())
    .then((json) => setData(json));
  }, [])

  return (
  <div>
    <div>filter goes here</div>
    <Datatable data={data}/>
  </div>
  );
}
