package Rest.Controllers;

import Rest.Entities.Product;
import Rest.Responses.GetProductCollectionResponse;
import Rest.Responses.SetProductCollectionResponse;
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

@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(value = "/products")
@RestController
public class ProductController {
    OkHttpClient client = new OkHttpClient();

    @Autowired
    private ProductCollectionService productCollectionService;

    @PostMapping(value = "/setProductsToTradeIn")
    public ResponseEntity<SetProductCollectionResponse> setAllProducts(@RequestBody List<Product> coinsToTradeIn) {
        try {
            SetProductCollectionResponse setProductCollectionResponse = new SetProductCollectionResponse();
            productCollectionService.setProductCollection(coinsToTradeIn);
            setProductCollectionResponse.setSuccess(true);

            return ResponseEntity.ok(setProductCollectionResponse);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/getProductsToTradeIn")
    public ResponseEntity<GetProductCollectionResponse> getProductsToTradeIn() {
        try {
            GetProductCollectionResponse getProductCollectionResponse = new GetProductCollectionResponse();
            List<Product> products = productCollectionService.getProductsToTradeIn();
            getProductCollectionResponse.setProducts(products);
            getProductCollectionResponse.setSuccess(true);

            return ResponseEntity.ok(getProductCollectionResponse);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/list")
    public ResponseEntity<GetProductCollectionResponse> getAllProducts() {
        Request request = new Request.Builder()
                .url("https://www.binance.com/bapi/composite/v1/public/marketing/symbol/list")
                .build();

        try {
            GetProductCollectionResponse productResponse = new GetProductCollectionResponse();
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
