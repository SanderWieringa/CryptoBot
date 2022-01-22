package Rest.Services;

import Rest.Entities.Product;
import Rest.Entities.User;
import Rest.Repositories.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class  UserService {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private UserCollectionService userCollectionService;

    public void update(User user) {
        List<Product> products = getUserProducts(user.getUserId());
        for (Product product : user.getCoinsToTradeIn()) {
            if (!products.contains(product)) {
                products.add(product);
            }
        }
        User usetToSetCoins = userCollectionService.getUserById(user.getUserId());
        usetToSetCoins.setCoinsToTradeIn(products);
        userRepository.save(usetToSetCoins);
    }

    private List<Product> checkMatch(List<Product> allProducts, List<Product> productsToDelete) {
        for (int iterator = 0; iterator < allProducts.toArray().length; iterator++) {
            for (Product productToDelete : productsToDelete) {
                if (allProducts.get(iterator).getId() == productToDelete.getId()) {
                    allProducts.remove(iterator);
                    iterator--;
                }
            }
        }

        return allProducts;
    }

    public List<Product> removeUserProducts(User user) {
        List<Product> productsToSave = checkMatch(getUserProducts(user.getUserId()), user.getCoinsToTradeIn());
        user.setCoinsToTradeIn(productsToSave);
        userRepository.save(user);
        return user.getCoinsToTradeIn();
    }

    public List<Product> getUserProducts(int userId) {
        return userRepository.getById(userId).getCoinsToTradeIn();
    }
}
