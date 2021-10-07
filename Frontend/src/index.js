import React from 'react';
import ReactDOM from 'react-dom';
import { Route, BrowserRouter as Router, Switch } from 'react-router-dom';
import { DataTable } from './DataTable';
import { ProductTable } from './ProductTable';
import { ProtectedRoute } from './Protected.route';
import { SignIn } from './SignIn';
import { SignUp } from './SignUp';

function App() {
  return (
    <div className="App">
      <div className="Border">
        <Switch>
          <Route exact path="/" component={SignIn} />
          <Route exact path="/signup" component={SignUp} />
          <ProtectedRoute exact path="home" component={DataTable} />
          <ProtectedRoute exact path="products" component={ProductTable} />
        </Switch>
      </div>
    </div>
  )
}

const rootElement = document.getElementById("root");
ReactDOM.render(<Router><App /></Router>, rootElement);
