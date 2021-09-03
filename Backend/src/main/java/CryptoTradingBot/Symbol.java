package CryptoTradingBot;

public class Symbol {
    private String baseAsset;

    private String symbol;

    public Symbol(String baseAsset, String symbol) {
        this.baseAsset = baseAsset;
        this.symbol = symbol;

    }

    public Symbol() {

    }

    public String getBaseAsset() {
        return baseAsset;
    }

    public void setBaseAsset(String baseAsset) {
        this.baseAsset = baseAsset;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
}
