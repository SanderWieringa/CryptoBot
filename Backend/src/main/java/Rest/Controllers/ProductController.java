package Rest.Controllers;

import Rest.Entities.Product;
import Rest.Responses.ProductCollectionResponse;
import Rest.Services.ProductCollectionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000/")
@RestController
public class ProductController {
    private static final String baseUrl = "https://api.binance.com";
    OkHttpClient client = new OkHttpClient();

    @Autowired
    private ProductCollectionService productCollectionService;

    @CrossOrigin(origins = "http://localhost:3000/")
    @GetMapping(value = "/setFakeProductsToTradeIn")
    public ResponseEntity<String> setFakeProducts() {
        return ResponseEntity.ok("success");
    }

    @PostMapping(value = "/setProductsToTradeIn")
    public void setAllProducts(@RequestBody List<Integer> coinsToTradeIn) {
        for (int i = 0; i < coinsToTradeIn.toArray().length; i++) {
            System.out.println(coinsToTradeIn.get(i));
        }
        //return ResponseEntity.ok("success");
        //productCollectionService.setProductCollection(coinsToTradeIn);
    }

    @GetMapping(value = "/getProductsToTradeIn")
    public List<Product> getProductsToTradeIn() {
        return productCollectionService.getProductsToTradeIn();
    }


    @GetMapping(value = "/products")
    public ResponseEntity<ProductCollectionResponse> getAllProducts() {
        Request request = new Request.Builder()
                //.url("https://www.binance.com/exchange-api/v2/public/asset-service/product/get-products")
                .url("https://www.binance.com/bapi/composite/v1/public/marketing/symbol/list")
                .build();

        try {
            ProductCollectionResponse productResponse = new ProductCollectionResponse();
            Response response = client.newCall(request).execute();
            JSONObject jsonObject = new JSONObject(response.body().string());
            JSONArray productList = jsonObject.getJSONArray("data");
            List<Product> products = new ArrayList<>();
            ObjectMapper objectMapper = new ObjectMapper();
            for (int i = 0; i < productList.length(); i++) {
                Product product = objectMapper.readValue(productList.get(i).toString(), Product.class);
                products.add(product);
            }

            productResponse.setProducts(products);
            return ResponseEntity.ok(productResponse);
        } catch (IOException e) {
            System.out.println(e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (NumberFormatException e) {
            System.out.println(e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

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
