package Rest.Services;

import Rest.Entities.User;
import Rest.Repositories.IUserCollectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService
{

    @Autowired
    private IUserCollectionRepository userCollectionRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException
    {
        Optional<User> userToConvert = userCollectionRepository.findByUsername(userName);

        if (userToConvert.isPresent()) {
            return new org.springframework.security.core.userdetails.User(userToConvert.get().getUsername(), userToConvert.get().getPassword(), new ArrayList<>());
        }

        return null;
    }
}
