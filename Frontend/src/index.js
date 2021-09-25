import React, { useState, useEffect } from "react";
import ReactDOM from 'react-dom';
import { Route, BrowserRouter as Router, Switch } from 'react-router-dom';
import './index.css';
import {ProtectedRoute} from './protected.route';
import {Datatable} from './datatable';
import { SignUp } from "./signUp";
import { SignIn } from "./signIn";
import { ProductTable } from "./productTable";

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
          <Route path="/products" exact component={ProductTable}/>
        </Switch>
      </div>
    </div>
  );
}

const rootElement = document.getElementById("root");
ReactDOM.render(<Router><App /></Router>, rootElement);
