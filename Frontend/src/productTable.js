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
    },
    {
        sortable: false,
        width: 160,
        valueGetter: (params) =>
          `${params.getValue(params.id, 'id') || ''}`,
      },
]

export const ProductTable = (props) => {
    const [data, setData] = useState({products: []})
    

    useEffect(() => {
        fetch("http://localhost:3337/products")
        .then((response) => response.json())
        .then((json) => setData(json))
        .catch(function(error) {
            console.log(error)
        });
    }, [])

    return (
        <div>
            <div>
                <button onClick={() => {
                    auth.logout(() => {
                        localStorage.clear()
                        props.history.push('/')
                    })
                }}>Logout</button>
                <h1>Crypto Trading Bot Platform</h1>
                
            </div>
            <div style={{ height: 580, width: '100%' }}>
                <DataGrid 
                rows={data.products}
                columns={columns}
                pageSize={9}
                checkboxSelection
                disableSelectionOnClick
                />
            </div>
        </div>
    )
}