import React, { useState, useEffect } from "react";
import { DataGrid } from "@material-ui/data-grid";
import jwt from "jwt-decode";

export const Account = (props) => {
  const [data, setData] = useState({ products: [] });
  const [select, setSelection] = useState([]);
  let token = jwt(localStorage.getItem("jwtToken"), { header: true });
  const userToGetProducts = {
    userId: token.userId,
    username: "",
    password: "",
    coinsToTradeIn: [],
  };

  const coinColumns = [
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
      body: JSON.stringify(userToGetProducts),
    };
    fetch("http://localhost:3337/account/getUserProducts", requestOptions)
      .then((response) => response.json())
      .then((json) => setData(json))
      .catch(function (error) {
        console.log(error);
      });
  }, []);

  const handleRemoveCoins = () => {
    let token = jwt(localStorage.getItem("jwtToken"), { header: true });
    const userToUpdate = {
      userId: token.userId,
      username: "",
      password: "",
      coinsToTradeIn: select,
    };
    const requestOptions = {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      mode: "cors",
      body: JSON.stringify(userToUpdate),
    };
    fetch("http://localhost:3337/account/removeUserProducts", requestOptions)
      .then((response) => response.json())
      .then((json) => setData(json))
      .catch(function (error) {
        console.log(error);
      });
  };

  return (
    <div>
      <div>
        <button
          onClick={() => {
            handleRemoveCoins();
          }}
        >
          Remove Coin
        </button>
        <h1>Crypto Trading Bot Platform</h1>
      </div>
      <div style={{ height: 580, width: "100%" }}>
        <DataGrid
          rows={data.products}
          columns={coinColumns}
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
