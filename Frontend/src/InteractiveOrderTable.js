import React, { useState, useEffect } from "react";
import { DataGrid } from "@material-ui/data-grid";
import jwt from "jwt-decode";
import Stomp from "stompjs";

export const InteractiveOrderTable = (props) => {
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

  const connect = (e) => {
    e.preventDefault();

    console.log("here1");

    let token = jwt(localStorage.getItem("jwtToken"), { header: true });
    global.userId = token.userId;

    if (global.userId) {
      console.log("here2");
      const socket = new WebSocket("ws://localhost:3337/orders-websocket");
      console.log("here5");
      global.stompClient = Stomp.over(socket);

      global.stompClient.connect({}, onConnected, onError);
    }
  };

  const onConnected = () => {
    console.log("here4");
    global.stompClient.subscribe("/topic/public", onMessageReceived);
    global.stompClient.send(
      "/app/orders.newUser",
      {},
      JSON.stringify({ sender: global.userId, type: "CONNECT" })
    );
  };

  const onError = (error) => {
    const status = document.querySelector("#status");
    status.innerHTML =
      "Could not find the connection you were looking for. Move along. Or, Refresh the page!";
    status.style.color = "red";
  };

  const hashCode = (str) => {
    let hash = 0;
    for (let i = 0; i < str.length; i++) {
      hash = str.charCodeAt(i) + ((hash << 5) - hash);
    }
    return hash;
  };

  const getAvatarColor = (messageSender) => {
    const colours = ["#2196F3", "#32c787", "#1BC6B4", "#A1B4C4"];
    const index = Math.abs(hashCode(messageSender) % colours.length);
    return colours[index];
  };

  const sendMessage = (event) => {
    event.preventDefault();
    let messageContent = select;
    console.log("1messageContent: ", messageContent);

    if (messageContent && global.stompClient) {
      const orderMessage = {
        sender: global.userId,
        content: messageContent,
        type: "CHAT",
      };
      global.stompClient.send(
        "/app/orders.send",
        {},
        JSON.stringify(orderMessage)
      );
      messageContent = data;
    }
  };

  const onMessageReceived = (payload) => {
    const message = JSON.parse(payload.body);

    const messageElement = document.createElement("div");
    messageElement.className = "msg_container_send";

    if (message.type === "CONNECT") {
      console.log("here3");
      console.log("message: ", message);
      messageElement.classList.add("event-message");
    } else if (message.type === "DISCONNECT") {
      messageElement.classList.add("event-message");
      message.content = message.sender + " left!";
    } else {
      messageElement.classList.add("chat-message");
      console.log("herenew: ");
      console.log("message: ", message);
      const avatarContainer = document.createElement("div");
      avatarContainer.className = "img_cont_msg";
      const avatarElement = document.createElement("div");
      avatarElement.className = "circle user_img_msg";
      const avatarText = document.createTextNode(message.sender[0]);
      avatarElement.appendChild(avatarText);
      avatarElement.style["background-color"] = getAvatarColor(message.sender);
      avatarContainer.appendChild(avatarElement);

      messageElement.style["background-color"] = getAvatarColor(message.sender);
    }

    messageElement.innerHTML = message.content;
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
        <div className="main row justify-content-center h-100">
          <form id="login-form" name="login-form" onSubmit={(e) => connect(e)}>
            <div className="input-group">
              <div className="input-group-append">
                <button className="fas fa-location-arrow" type="submit">
                  Join Lobby
                </button>
              </div>
            </div>
          </form>
        </div>
        <div id="checkers-page" className="hide main">
          <div className="row justify-content-center h-100">
            <div className="col-md-8 col-xl-6">
              <div className="card-header">
                <div className="d-flex bd-highlight"></div>
              </div>

              <div className="card-body">
                <div id="chat"></div>
              </div>

              <form
                id="message-controls"
                name="message-controls"
                className="card-footer"
                onSubmit={(e) => sendMessage(e)}
              >
                <div className="input-group">
                  <div className="input-group-append">
                    <button className="fas fa-location-arrow" type="submit">
                      Send Message
                    </button>
                  </div>
                </div>
              </form>
            </div>
          </div>
        </div>
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
