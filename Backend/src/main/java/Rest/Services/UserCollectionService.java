package Rest.Services;

import Rest.Entities.User;
import Rest.Repositories.IUserCollectionRepository;
import Rest.Responses.AuthenticationRequest;
import Rest.util.PasswordHasher;
import Rest.util.PasswordValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.nio.file.AccessDeniedException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class UserCollectionService implements UserDetailsService {
    @Autowired
    private IUserCollectionRepository userCollectionRepository;

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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userToConvert = userCollectionRepository.findByUsername((username));
        if (userToConvert.isPresent()) {
            return new org.springframework.security.core.userdetails.User(
                    userToConvert
                    .get()
                    .getUsername(), userToConvert.get()
                    .getPassword(), new ArrayList<>());
        }

        return null;
    }
}
