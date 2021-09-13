package CryptoTradingBot;

public class Symbol {
    private String id;

    private String symbol;

    public Symbol(String id, String symbol) {
        this.id = id;
        this.symbol = symbol;

    }

    public Symbol() {

    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
}
