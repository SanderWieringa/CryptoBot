import React, { useState, useEffect } from "react";
import { DataGrid } from "@material-ui/data-grid";
import jwt from "jwt-decode";

export const OrderTable = (props) => {
  const [data, setData] = useState({ orders: [] });
  let token = jwt(localStorage.getItem("jwtToken"), { header: true });
  const userToGetOrders = {
    userId: token.userId,
    username: "",
    password: "",
    coinsToTradeIn: [],
  };

  const orderColumns = [
    { field: "fullName", headerName: "Coin", width: 150 },
    {
      field: "price",
      headerName: "Price",
      width: 150,
    },
    {
      field: "marketCap",
      headerName: "Market Cap",
      width: 150,
    },
    {
      field: "symbol",
      headerName: "Symbol",
      width: 150,
    },
  ];

  useEffect(() => {
    const requestOptions = {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      mode: "cors",
      body: JSON.stringify(userToGetOrders),
    };
    fetch("http://localhost:3337/binance/getUserOrders", requestOptions)
      .then((response) => response.json())
      .then((json) => setData(json))
      .then(console.log("data.orders: ", data))
      .catch(function (error) {
        console.log(error);
      });
  }, []);

  const handleGetOpenOrders = () => {
    let token = jwt(localStorage.getItem("jwtToken"), { header: true });
    const userToGetOpenOrders = {
      userId: token.userId,
      username: "",
      password: "",
      coinsToTradeIn: [],
    };
    const requestOptions = {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      mode: "cors",
      body: JSON.stringify(userToGetOpenOrders),
    };
    fetch("http://localhost:3337/binance/getAllOpenOrders", requestOptions)
      .then((response) => response.json())
      .then((json) => setData(json))
      .then(console.log("data: ", data))
      .catch(function (error) {
        console.log(error);
      });
  };

  const handleGetUserOrders = () => {
    const requestOptions = {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      mode: "cors",
      body: JSON.stringify(userToGetOrders),
    };
    fetch("http://localhost:3337/binance/getUserOrders", requestOptions)
      .then((response) => response.json())
      .then((json) => setData(json))
      .then(console.log("data: ", data))
      .catch(function (error) {
        console.log(error);
      });
  };

  return (
    <div>
      <div>
        <button
          onClick={() => {
            handleGetOpenOrders();
          }}
        >
          Get All Open Orders
        </button>
        <button
          onClick={() => {
            handleGetUserOrders();
          }}
        >
          Get All Orders
        </button>
      </div>
      <div style={{ height: 580, width: "100%" }}>
        <DataGrid rows={data.orders} columns={orderColumns} pageSize={9} />
      </div>
    </div>
  );
};
