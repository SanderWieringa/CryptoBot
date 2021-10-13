package Rest.Responses;

import Rest.Entities.Symbol;

import java.util.List;

public class SymbolCollectionResponse {
    private boolean success;
    private List<Symbol> symbols;
    public boolean isSccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    public List<Symbol> getSymbols() { return symbols; }
    public void setSymbols(List<Symbol> symbols) { this.symbols = symbols;}
}
