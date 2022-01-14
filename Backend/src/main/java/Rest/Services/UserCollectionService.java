package Rest.Services;

import Rest.Entities.User;
import Rest.Repositories.IUserCollectionRepository;
import Rest.Responses.AuthenticationRequest;
import Rest.util.PasswordHasher;
import Rest.util.PasswordValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserCollectionService {

    @Autowired
    private IUserCollectionRepository userCollectionRepository;

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        for (User user:userCollectionRepository.findAll()) {
            users.add(user);
        }
        return users;
    }

    public User login(AuthenticationRequest authenticationRequest) throws InvalidKeySpecException, NoSuchAlgorithmException, AccessDeniedException {
        User user = new User();
        Optional<User> userToCheck = userCollectionRepository.findByUsername(authenticationRequest.getUsername());
        if (userToCheck.isPresent()) {
            if (PasswordValidator.validatePassword(authenticationRequest.getPassword(), userToCheck.get().getPassword())) {
                user.setUsername(authenticationRequest.getUsername());
                user.setPassword(authenticationRequest.getPassword());
                return user;
            }
        }

        throw new AccessDeniedException("Access Denied!");
    }

    public void addUser(User user) throws InvalidKeySpecException, NoSuchAlgorithmException {
        if (userCollectionRepository.findByUsername(user.getUsername()).isPresent()) {
            return;
        }
        String hashedPassword = PasswordHasher.generateStrongPasswordHash(user.getPassword());
        user.setPassword(hashedPassword);
        userCollectionRepository.save(user);
    }

    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return convertOptionalUser(userCollectionRepository.findByUsername(username));

    }

    public User getUserById(int userId) {
        return convertOptionalUser(userCollectionRepository.findById(userId));
    }

    private User convertOptionalUser(Optional<User> userToConvert) {
        if (userToConvert.isPresent()) {
            return new User(
                    userToConvert.get().getUserId(),
                    userToConvert.get().getUsername(),
                    userToConvert.get().getPassword(),
                    userToConvert.get().getCoinsToTradeIn()
            );
        }

        return null;
    }
}
