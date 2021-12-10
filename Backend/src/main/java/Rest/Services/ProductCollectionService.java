package Rest.Services;

import Rest.Entities.Product;
import Rest.Repositories.IProductCollectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductCollectionService {
    @Autowired
    public IProductCollectionRepository productCollectionRepository;

    public void setProductCollection(List<Product> coinsToTradeIn) {
        for (Product product:coinsToTradeIn) {
            if (!productCollectionRepository.existsById(product.getId())) {
                productCollectionRepository.save(product);
            }
        }
    }

    public List<Product> getProductCollection() {
        return new ArrayList<>(productCollectionRepository.findAll());
    }
}
