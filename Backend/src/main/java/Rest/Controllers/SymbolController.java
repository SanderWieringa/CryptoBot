package Rest.Controllers;

import Rest.Entities.Product;
import Rest.Entities.Symbol;
import Rest.Responses.ProductCollectionResponse;
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

@RestController
public class SymbolController {
    private final String baseUrl = "https://api.binance.com";
    OkHttpClient client = new OkHttpClient();

    @CrossOrigin(origins = "http://localhost:3000/")
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
        }
    }

    @CrossOrigin(origins = "http://localhost:3000/")
    @GetMapping(value = "/products")
    public ResponseEntity<ProductCollectionResponse> getAllProducts() {
        Request request = new Request.Builder()
                .url("https://www.binance.com/exchange-api/v2/public/asset-service/product/get-products")
                .build();

        try {
            ProductCollectionResponse productResponse = new ProductCollectionResponse();
            Response response = client.newCall(request).execute();
            JSONObject jsonObject = new JSONObject(response.body().string());
            System.out.println(jsonObject);
            JSONArray productList = jsonObject.getJSONArray("data");
            List<Product> products = new ArrayList<>();
            for (int i = 0; i < productList.length(); i++) {
                JSONObject jsonObj = productList.getJSONObject(i);
                Product product = new Product();
                product.setId(jsonObj.getString("b"));
                product.setPrice(jsonObj.getFloat("c"));
                products.add(product);
            }
            productResponse.setProducts(products);
            return ResponseEntity.ok(productResponse);
        } catch (IOException e) {
            System.out.println(e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin(origins = "http://localhost:3000/")
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
        }

        return "Try again";
    }

    @CrossOrigin(origins = "http://localhost:3000/")
    @GetMapping(value = "/getAllProducts")
    public String products() {
        Request request = new Request.Builder()
                .url("https://www.binance.com/exchange-api/v2/public/asset-service/product/get-products")
                .build();

        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            System.out.println(e);
        }

        return "Try again";
    }
}
