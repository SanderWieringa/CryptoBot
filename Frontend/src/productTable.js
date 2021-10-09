import React, { useState, useEffect } from "react";
import { DataGrid } from '@material-ui/data-grid';
import auth from './auth'

const columns = [
    {   field: 'fullName', headerName: 'Coin', width: 150 },
    {
        field: 'price',
        headerName: 'Price',
        width: 150,
        editable: false,
    },
    {
        field: 'marketCap',
        headerName: 'Market Cap',
        width: 150,
        editable: false,
    }
]

export const ProductTable = (props) => {
    const [data, setData] = useState({products: []})
    const [selectionModel, setSelectionModel] = React.useState([]);

    useEffect(() => {
        fetch('http://localhost:3337/products')
        .then((response) => response.json())
        .then((json) => setData(json))
        .catch(function(error) {
            console.log(error)
        });
    }, [])

    const handleTradeSubmit = (() => {

        const requestOptions = {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            mode: 'cors',
            body: JSON.stringify(selectionModel)
        };

        fetch('http://localhost:3337/setProductsToTradeIn', requestOptions)
        .then(response => response.json())
        .then(data => {
            console.log(data)
          })
        .catch(function(error) {
            console.log(error)
        })
    })

    const handleTradeLogging = (() => {
        console.log(selectionModel);
    })

    return (
        <div>
            <div>
            <button onClick={() => {
                handleTradeLogging()
            }}>
                Log Products
            </button>
            <button onClick={() => {
                handleTradeSubmit()
            }}>
                Trade
            </button>
            <button onClick={() => {
                auth.logout(() => {
                    localStorage.clear()
                    props.history.push('/')
                })
            }}>
                Logout
            </button>
                <h1>Crypto Trading Bot Platform</h1>
                
            </div>
            <div style={{ height: 580, width: '100%' }}>
                <DataGrid 
                rows={data.products}
                columns={columns}
                pageSize={9}
                checkboxSelection
                disableSelectionOnClick
                onSelectionModelChange={(newSelectionModel) => {
                    setSelectionModel(newSelectionModel);
                  }}
                  selectionModel={selectionModel}
                  {...data}
                />
            </div>
        </div>
    )
}