package Rest.Controllers;

import Rest.Entities.User;
import Rest.Responses.AuthenticationRequest;
import Rest.Responses.AuthenticationResponse;
import Rest.Services.UserCollectionService;
import Rest.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@CrossOrigin(origins = "http://localhost:3000/")
@RequestMapping(value = "/account")
@RestController
public class UserController {
    @Autowired
    private UserCollectionService userCollectionService;

    @Autowired
    private JwtUtil jwtTokenUtil;

    @PostMapping(value = "/register")
    public ResponseEntity<?> addUser(@RequestBody User user) {
        try {
            userCollectionService.addUser(user);
            return new ResponseEntity(HttpStatus.OK);
        }  catch (Exception e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
     }

    @PostMapping(value="/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        try {
            userCollectionService.login(authenticationRequest);
        } catch (AccessDeniedException e) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) userCollectionService.loadUserByUsername(authenticationRequest.getUsername());

        final String jwt = jwtTokenUtil.generateToken(user);
        System.out.println(jwt);
        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }
}
