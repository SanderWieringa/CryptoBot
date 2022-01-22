import React from "react";
import ReactDOM from "react-dom";
import "./index.css";
import { Route, BrowserRouter as Router, Switch } from "react-router-dom";
import { SignIn } from "./SignIn";
import { SignUp } from "./SignUp";
import { ProtectedRoute } from "./Protected.route";
import { ProductTable } from "./Product.Table";
import { Account } from "./Account";
import { OrderTable } from "./OrderTable";
import { InteractiveOrderTable } from "./InteractiveOrderTable";

function App() {
  return (
    <div className="App">
      <div className="Border">
        <Switch>
          <Route exact path="/" component={SignIn} />
          <Route exact path="/signup" component={SignUp} />
          <Route
            exact
            path="/interactiveOrders"
            component={InteractiveOrderTable}
          />
          <ProtectedRoute exact path="/products" component={ProductTable} />
          <ProtectedRoute exact path="/account" component={Account} />
          {/* <ProtectedRoute exact path="/orders" component={OrderTable} /> */}
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
