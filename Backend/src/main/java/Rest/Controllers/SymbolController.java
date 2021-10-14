package Rest.Controllers;

import Rest.Entities.Symbol;
import Rest.Responses.SymbolCollectionResponse;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000/")
@RestController
public class SymbolController {
    private static final String baseUrl = "https://api.binance.com";
    OkHttpClient client = new OkHttpClient();

    @GetMapping(value = "/symbols")
    public ResponseEntity<SymbolCollectionResponse> getAllSymbols() {
        Request request = new Request.Builder()
                .url(baseUrl + "/api/v3/exchangeInfo")
                .build();

        try {
            SymbolCollectionResponse symbolResponse = new SymbolCollectionResponse();
            Response response = client.newCall(request).execute();
            JSONObject jsonObject = new JSONObject(response.body().string());
            JSONArray symbolList = jsonObject.getJSONArray("symbols");
            List<Symbol> symbols = new ArrayList<>();
            for (int i = 0; i < symbolList.length(); i++) {
                JSONObject jsonObj = symbolList.getJSONObject(i);
                Symbol symbol = new Symbol();
                symbol.setId(jsonObj.getString("baseAsset"));
                symbol.setSymbol(jsonObj.getString("symbol"));
                symbols.add(symbol);
            }
            symbolResponse.setSymbols(symbols);
            return ResponseEntity.ok(symbolResponse);
        } catch (IOException e) {
            System.out.println(e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/info")
    public String info() {
        Request request = new Request.Builder()
                .url(baseUrl + "/api/v3/exchangeInfo")
                .build();

        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            System.out.println(e);
        } catch (Exception e) {
            System.out.println(e);
        }

        return "Try again";
    }
}
