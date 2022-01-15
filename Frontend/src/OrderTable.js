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

  const fillData = (json) => {
    // const values = Object.values(json.orders);
    // console.log("values: ", values);

    for (let index = 0; index < json.orders.length; index++) {
      const element = json.orders[index];
      setData(element);
      console.log("element: ", element);
    }

    // for (let index = 0; index < json.orders.length; index++) {
    //   // const element = json.orders[index];
    //   // console.log("element: ", element);
    //   for (
    //     let secondIndex = 0;
    //     secondIndex < json.orders[0].length;
    //     secondIndex++
    //   ) {
    //     console.log("item: ", json.orders[index][secondIndex]);
    //     setData(json.orders[index][secondIndex]);
    //     // const elementItem = element[secondIndex];
    //     // setData(element[secondIndex]);
    //     // console.log("element[]: ", elementItem);
    //   }
    // }

    // for (let index = 0; index < values.length; index++) {
    //   const element = values[index];
    //   console.log("element: ", element);
    //   setData(element[0]);
    //   // setData(element);
    // }

    // for (let index = 0; index < json.orders.length; index++) {
    //   const element = Object.keys(json.orders[index]).map((u) => {
    //     console.log("u: ", u);
    //   });
    // }

    // Object.keys(json.orders).map((u) => {
    //   for (let index = 0; index < u.length; index++) {
    //     const element = u[index].toString;
    //     console.log("u.element: ", element);
    //   }
    // });

    // for (let index = 0; index < json.orders.length; index++) {
    //   for (
    //     let secondIndex = 0;
    //     secondIndex < json.orders[index].length;
    //     secondIndex++
    //   ) {
    //     const element = json.orders[index][secondIndex];
    //     console.log("element: ", element);
    //     setData(json.orders[index][secondIndex]);
    //   }
    // }
    console.log("json: ", json);
    console.log("data: ", data);
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
      </div>
      <div style={{ height: 580, width: "100%" }}>
        <DataGrid
          getRowId={(r) => r.orderId}
          rows={data.orders}
          columns={orderColumns}
          pageSize={9}
        />
      </div>
    </div>
  );
};
