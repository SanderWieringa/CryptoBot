package Rest.Controllers;

import Rest.Entities.User;
import Rest.Responses.AuthenticationRequest;
import Rest.Responses.AuthenticationResponse;
import Rest.Services.UserCollectionService;
import Rest.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {
    @Autowired
    private UserCollectionService userCollectionService;

    @Autowired
    private JwtUtil jwtTokenUtil;

    @CrossOrigin(origins = "http://localhost:3000/")
    @PostMapping(value = "/register")
    public ResponseEntity<?> addUser(@RequestBody User user) {
        try {
            userCollectionService.addUser(user);
            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin(origins = "http://localhost:3000/")
    @RequestMapping(value="/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        try {
            userCollectionService.login(authenticationRequest);
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }

        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) userCollectionService.loadUserByUsername(authenticationRequest.getUsername());

        final String jwt = jwtTokenUtil.generateToken(user);

        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }
}
