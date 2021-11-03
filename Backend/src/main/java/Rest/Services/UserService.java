package Rest.Services;

import Rest.Entities.User;
import Rest.Repositories.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private IUserRepository userRepository;

    public void update(User user) {
        userRepository.save(user);
    }
}
