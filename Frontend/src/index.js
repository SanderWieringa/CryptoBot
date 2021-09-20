import React, { useState, useEffect } from "react";
import ReactDOM from 'react-dom';
import { Route, BrowserRouter as Router, Switch } from 'react-router-dom';
import './index.css';
import {ProtectedRoute} from './protected.route';
import {Datatable} from './datatable';
import { SignIn } from "./signIn";
import { SignUp } from "./signUp";

require("es6-promise").polyfill();
require("isomorphic-fetch");

function App() {
  

  

  return (
    <div className="App">
      <div className="Border">
        <Switch>
          <Route exact path="/" component={SignIn} />
          <Route exact path="/signup" component={SignUp} />
          <ProtectedRoute path="/home" exact component={Datatable}/>
        </Switch>
      </div>
    </div>
  );
}

const rootElement = document.getElementById("root");
ReactDOM.render(<Router><App /></Router>, rootElement);
