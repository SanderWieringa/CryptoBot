package Rest.Services;

import Rest.Entities.Product;
import Rest.Entities.User;
import Rest.Repositories.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private IUserRepository userRepository;

    public void update(User user) {
        List<Product> products = getUserProducts(user.getUserId());
        for (Product product:user.getCoinsToTradeIn()) {
            if (!products.contains(product)) {
                products.add(product);
            }
        }
        user.setCoinsToTradeIn(products);
        userRepository.save(user);
    }

    public List<Product> getUserProducts(int userId) {
        return userRepository.getById(userId).getCoinsToTradeIn();
    }
}
