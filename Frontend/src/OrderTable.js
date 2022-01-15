import React, { useState, useEffect } from "react";
import { DataGrid } from "@material-ui/data-grid";
import jwt from "jwt-decode";

export const OrderTable = (props) => {
  const [data, setData] = useState({ orders: [] });
  const [select, setSelection] = useState([]);
  let token = jwt(localStorage.getItem("jwtToken"), { header: true });
  const userToGetOrders = {
    userId: token.userId,
    username: "",
    password: "",
    coinsToTradeIn: [],
  };

  const orderColumns = [
    {
      field: "symbol",
      headerName: "Symbol",
      width: 150,
    },
    {
      field: "price",
      headerName: "Price",
      width: 150,
    },
    {
      field: "stopPrice",
      headerName: "StopPrice",
      width: 130,
    },
    {
      field: "type",
      headerName: "Type",
      width: 110,
    },
    {
      field: "executedQty",
      headerName: "ExecutedQty",
      width: 150,
    },
    {
      field: "side",
      headerName: "Side",
      width: 110,
    },
    {
      field: "time",
      headerName: "Time",
      width: 150,
    },
    {
      field: "clientOrderId",
      headerName: "ClientOrderId",
      width: 150,
      hide: true,
    },
    {
      field: "cummulativeQuoteQty",
      headerName: "CummulativeQuoteQty",
      width: 150,
      hide: true,
    },
    {
      field: "icebergQty",
      headerName: "IcebergQty",
      width: 150,
      hide: true,
    },
    {
      field: "isWorking",
      headerName: "IsWorking",
      width: 150,
      hide: true,
    },
    {
      field: "orderId",
      headerName: "OrderId",
      width: 150,
      hide: true,
    },
    {
      field: "origQty",
      headerName: "OrigQty",
      width: 150,
      hide: true,
    },
    {
      field: "origQuoteOrderQty",
      headerName: "OrigQuoteOrderQty",
      width: 150,
      hide: true,
    },
    {
      field: "status",
      headerName: "Status",
      width: 150,
      hide: true,
    },
    {
      field: "stopLimitPrice",
      headerName: "StopLimitPrice",
      width: 150,
      hide: true,
    },
    {
      field: "timeInForce",
      headerName: "TimeInForce",
      width: 150,
      hide: true,
    },

    {
      field: "updateTime",
      headerName: "UpdateTime",
      width: 150,
      hide: true,
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
        <h1>Crypto Trading Bot Platform</h1>
      </div>
      <div style={{ height: 580, width: "100%" }}>
        <DataGrid
          getRowId={(r) => r.orderId}
          rows={data.orders}
          columns={orderColumns}
          pageSize={9}
          checkboxSelection
          disableSelectionOnClick
          onSelectionModelChange={(ids) => {
            const selectedIDs = new Set(ids);
            const selectedRowData = data.orders.filter((row) =>
              selectedIDs.has(row.id)
            );
            setSelection(selectedRowData);
          }}
        />
      </div>
    </div>
  );
};
