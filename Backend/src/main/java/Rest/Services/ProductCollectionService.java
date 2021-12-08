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
            continue;
        }
    }

    public void setBlackList(Product product) {
        if (!productCollectionRepository.existsById(product.getId())) {
            productCollectionRepository.save(product);
        }
    }

    public List<Product> getProductsToTradeIn() {
        List<Product> coinsToTradeIn = new ArrayList<>();
        productCollectionRepository.findAll().forEach(coinsToTradeIn::add);
        return coinsToTradeIn;
    }
}
