import React from "react";

export default function Datatable({ data }) {
    //console.log(data.symbols)
    const columns = data.symbols[0] && Object.keys(data.symbols[0])
    //console.log(data)
    console.log(data.symbols)
    return (
        <table cellPadding={0} cellSpacing={0}>
            <thead>
                <tr>{data.symbols[0] && columns.map((heading, idx) => <th key={idx}>{heading}</th>)}</tr>
            </thead>
            <tbody>
                {data.symbols.map((row, idx) => <tr key={idx}>
                    {
                        columns.map((column, idx) => <td key={idx}>{row[column]}</td>)
                    }
                </tr>)}
            </tbody>
        </table>
    );
}