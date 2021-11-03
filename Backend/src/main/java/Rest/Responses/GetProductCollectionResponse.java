package Rest.Responses;

import Rest.Entities.Product;

import java.util.List;

public class GetProductCollectionResponse {
    private boolean success;
    private List<Product> products;

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    public List<Product> getProducts() { return products; }
    public void setProducts(List<Product> products) { this.products = products;}
}
