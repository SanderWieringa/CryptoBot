import React, { useState, useEffect } from "react";
import { DataGrid } from '@material-ui/data-grid';
import auth from './auth'

const columns = [
    { field: 'id', headerName: 'Coin', width: 150 },
    {
      field: 'symbol',
      headerName: 'Symbol',
      width: 150,
      editable: true,
    },
    {
        sortable: false,
        width: 160,
        valueGetter: (params) =>
          `${params.getValue(params.id, 'symbol') || ''} ${
            params.getValue(params.id, 'id') || ''
          }`,
      },
]

export const Datatable = (props) => {
    const [data, setData] = useState({symbols: []})

    useEffect(() => {
        fetch("http://localhost:8080/symbols")
        .then((response) => response.json())
        .then((json) => setData(json));
      }, [])

    return (
        <div>
            <div>
                <button onClick={() => {
                    auth.logout(() => {
                        props.history.push('/')
                    })
                }}>Logout</button>
                <h1>Crypto Trading Bot Platform</h1>
                
            </div>
            <div style={{ height: 580, width: '100%' }}>
                <DataGrid 
                rows={data.symbols}
                columns={columns}
                pageSize={9}
                checkboxSelection
                disableSelectionOnClick
                />
            </div>
        </div>
    );
}