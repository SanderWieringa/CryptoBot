import React from "react";
import { Button } from "@material-ui/core";
import auth from './auth'

require("es6-promise").polyfill();
require("isomorphic-fetch");

export default function Header(props) {

  return (
    <button onClick={() => {
      auth.logout(() => {
        props.history.push("/");
      })
    }}>Logout</button>
  );
}
