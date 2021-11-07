import React, { useState, useEffect } from "react";
import { DataGrid } from "@material-ui/data-grid";
import auth from "./auth";
import jwt from "jwt-decode";

const columns = [
  { field: "fullName", headerName: "Coin", width: 150 },
  {
    field: "price",
    headerName: "Price",
    width: 150,
    editable: true,
  },
  {
    field: "marketCap",
    headerName: "Market Cap",
    width: 150,
    editable: true,
  },
  {
    field: "symbol",
    headerName: "Symbol",
    width: 150,
    editable: true,
  },
];

export const ProductTable = (props) => {
  const [data, setData] = useState({ products: [] });
  const [select, setSelection] = useState([]);

  useEffect(() => {
    fetch("http://localhost:3337/products/list")
      .then((response) => response.json())
      .then((json) => setData(json))
      .catch(function (error) {
        console.log(error);
      });
  }, []);

  const handleSubscribe = () => {
    fetch("http://localhost:3337/binance/subscribe")
      .then(function (response) {
        console.log(response);
      })
      .catch(function (error) {
        console.log(error);
      });
  };

  const handleTradeSubmit = () => {
    let token = jwt(localStorage.getItem("jwtToken"), { header: true });
    const userToUpdate = {
      userId: token["user"].userId,
      username: token["user"].username,
      password: token["user"].password,
      coinsToTradeIn: select,
    };
    const requestOptions = {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      mode: "cors",
      body: JSON.stringify(userToUpdate),
    };

    fetch("http://localhost:3337/account/setUserProducts", requestOptions)
      .then(function (response) {
        return response.json();
      })
      .then(function (data) {
        console.log(data);
      })
      .catch(function (error) {
        console.log(error);
      });
  };

  const handleTradeLogging = () => {
    console.log(select);
  };

  const handleGetProductsToTradeIn = () => {
    let token = jwt(localStorage.getItem("jwtToken"), { header: true });
    const userToGetProducts = {
      userId: token["user"].userId,
      username: token["user"].username,
      password: token["user"].password,
      coinsToTradeIn: select,
    };
    const requestOptions = {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      mode: "cors",
      body: JSON.stringify(userToGetProducts),
    };
    fetch("http://localhost:3337/account/getUserProducts", requestOptions)
      .then(function (response) {
        return response.json();
      })
      .then(function (data) {
        console.log(data);
      })
      .catch(function (error) {
        console.log(error);
      });
  };

  return (
    <div>
      <div>
        <button
          onClick={() => {
            handleLogParse();
          }}
        >
          Log Token Parse
        </button>
        <button
          onClick={() => {
            handleGetProductsToTradeIn();
          }}
        >
          Get Products
        </button>
        <button
          onClick={() => {
            handleTradeLogging();
          }}
        >
          Log Products
        </button>
        <button
          onClick={() => {
            handleTradeSubmit();
          }}
        >
          Set Products
        </button>
        <button
          onClick={() => {
            handleSubscribe();
          }}
        >
          Start Trading
        </button>
        <button
          onClick={() => {
            auth.logout(() => {
              localStorage.clear();
              props.history.push("/");
            });
          }}
        >
          Logout
        </button>
        <h1>Crypto Trading Bot Platform</h1>
      </div>
      <div style={{ height: 580, width: "100%" }}>
        <DataGrid
          rows={data.products}
          columns={columns}
          pageSize={9}
          checkboxSelection
          disableSelectionOnClick
          onSelectionModelChange={(ids) => {
            const selectedIDs = new Set(ids);
            const selectedRowData = data.products.filter((row) =>
              selectedIDs.has(row.id)
            );
            setSelection(selectedRowData);
          }}
        />
      </div>
    </div>
  );
};
