import React from "react";
import ReactDOM from "react-dom";
import "./index.css";
import { Route, BrowserRouter as Router, Switch } from "react-router-dom";
import { SignIn } from "./SignIn";
import { SignUp } from "./SignUp";
import { ProtectedRoute } from "./Protected.route";
import { DataTable } from "./DataTable";
import { ProductTable } from "./ProductTable";
import { Account } from "./Account";
import { OrderTable } from "./OrderTable";

function App() {
  return (
    <div className="App">
      <div className="Border">
        <Switch>
          <Route exact path="/" component={SignIn} />
          <Route exact path="/signup" component={SignUp} />
          <ProtectedRoute exact path="/home" component={DataTable} />
          <ProtectedRoute exact path="/products" component={ProductTable} />
          <ProtectedRoute exact path="/account" component={Account} />
          <ProtectedRoute exact path="/orders" component={OrderTable} />
        </Switch>
      </div>
    </div>
  );
}

const rootElement = document.getElementById("root");
ReactDOM.render(
  <Router>
    <App />
  </Router>,
  rootElement
);
