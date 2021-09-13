import * as React from 'react';
import { DataGrid } from '@material-ui/data-grid';

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


export default function Datatable({ data }) {
    
    return (
        <div style={{ height: 580, width: '100%' }}>
            <DataGrid 
            rows={data.symbols}
            columns={columns}
            pageSize={9}
            checkboxSelection
            disableSelectionOnClick
            />
        </div>
    );
}