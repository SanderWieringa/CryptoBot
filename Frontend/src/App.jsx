import React, { useState, useEffect } from "react";
import Datatable from "./datatable";
import SignIn from "./SignIn";
import SignUp from "./SignUp";
import { Route, BrowserRouter as Router, Switch } from 'react-router-dom';
import {ProtectedRoute} from './protected.route'

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
    <Router>
      <Switch>
        <Route path="/" exact component={SignIn}/>
        <Route path="/signup" exact component={SignUp}/>
        <ProtectedRoute path="/home" exact >
          <Datatable data={data}/>
        </ProtectedRoute>
        <Route path="*" component={() => "404 NOT FOUND!"} />
      </Switch>
    </Router>
  );
}
