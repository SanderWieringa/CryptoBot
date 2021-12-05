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

    @GetMapping(value = "/list")
    public ResponseEntity<GetProductCollectionResponse> getAllProducts() {
        GetProductCollectionResponse productResponse = new GetProductCollectionResponse();
        List<Product> products = new ArrayList<>();
        Request request = new Request.Builder()
                .url("https://www.binance.com/bapi/composite/v1/public/marketing/symbol/list")
                .build();

        try {
            Response response = client.newCall(request).execute();
            assert response.body() != null;
            JSONObject jsonObject = new JSONObject(response.body().string());
            JSONArray productList = jsonObject.getJSONArray("data");
            ObjectMapper objectMapper = new ObjectMapper();

            for (int i = 0; i < productList.length(); i++) {
                Product product = objectMapper.readValue(productList.get(i).toString(), Product.class);
                products.add(product);
            }
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        productResponse.setSuccess(true);
        productResponse.setProducts(products);

        return ResponseEntity.ok(productResponse);
    }
}
