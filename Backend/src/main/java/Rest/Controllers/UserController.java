package Rest.Controllers;

import Rest.Entities.Product;
import Rest.Entities.User;
import Rest.Responses.*;
import Rest.Services.UserCollectionService;
import Rest.Services.UserService;
import Rest.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000/")
@RequestMapping(value = "/account")
@RestController
public class UserController {
    @Autowired
    private UserCollectionService userCollectionService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtTokenUtil;

    @PostMapping(value = "/register")
    public ResponseEntity<RegisterResponse> addUser(@RequestBody User user) {
        try {
            userCollectionService.addUser(user);
            RegisterResponse registerResponse = new RegisterResponse();
            registerResponse.setSuccess(true);

            return ResponseEntity.ok(registerResponse);
        }  catch (Exception e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
     }

    @PostMapping(value="/authenticate")
    public ResponseEntity<LoginResponse> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) {
        try {
            userCollectionService.login(authenticationRequest);

            User user = userCollectionService.loadUserByUsername(authenticationRequest.getUsername());

            final String jwt = jwtTokenUtil.generateToken(user);
            System.out.println(jwt);
            LoginResponse loginResponse = new LoginResponse(jwt);
            loginResponse.setSuccess(true);

            return ResponseEntity.ok(loginResponse);
        } catch (AccessDeniedException e) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = "setUserProducts")
    public ResponseEntity<SetProductCollectionResponse> update(@RequestBody User user) {
        try {
            SetProductCollectionResponse setProductCollectionResponse = new SetProductCollectionResponse();
            userService.update(user);
            setProductCollectionResponse.setSuccess(true);

            return ResponseEntity.ok(setProductCollectionResponse);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/getUserProducts")
    public ResponseEntity<GetProductCollectionResponse> getUserProducts(@RequestBody User user) {
        try {
            GetProductCollectionResponse getProductCollectionResponse = new GetProductCollectionResponse();
            List<Product> coinsToTradeIn = userService.getUserProducts(user.getUserId());
            getProductCollectionResponse.setProducts(coinsToTradeIn);
            getProductCollectionResponse.setSuccess(true);

            return ResponseEntity.ok(getProductCollectionResponse);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

}
