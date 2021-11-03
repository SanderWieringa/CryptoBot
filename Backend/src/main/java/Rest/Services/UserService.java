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
        userRepository.save(user);
    }

    public List<Product> getUserProducts(int userId) {
        User user = userRepository.getById(userId);
        return user.getCoinsToTradeIn();
    }
}
