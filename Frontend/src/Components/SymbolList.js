import React from 'react';
import axios from 'axios';

export default class SymbolList extends React.Component {
    state = {
        symbols: []
    };

    componentDidMount() {
        axios.get('http://localhost:8080/symbols')
        .then(response => {
          console.log(response);
          this.setState({ symbols: response.data.symbols });
        });
    }

    render() {
        return <ul>{this.state.symbols.map(symbol => <li>{symbol.baseAsset}</li>)}</ul>
    }
}