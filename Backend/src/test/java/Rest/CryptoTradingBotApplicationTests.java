package Rest;

import Rest.Controllers.BinanceController;
import Rest.Controllers.UserController;
import Rest.Entities.Product;
import Rest.Entities.User;
import Rest.Responses.AuthenticationRequest;
import Rest.Responses.GetProductCollectionResponse;
import Rest.Responses.OrderResponse;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CryptoTradingBotApplicationTests {

	@LocalServerPort
	private int port;

	@Autowired
	private BinanceController binanceController;

	@Autowired
	private UserController userController;

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void contextLoads() {
		assertThat(binanceController).isNotNull();
		assertThat(userController).isNotNull();
	}

	@Test
	public void hello() {
		assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/binance/hello",
				String.class)).contains("Hello, World");
	}

	@Test
	public void testGetCoins() {
		assertThat(binanceController.getAllProducts()).isNotNull();

		ResponseEntity<GetProductCollectionResponse> entity = binanceController.getAllProducts();

		assertThat(entity.getBody().getProducts()).isNotNull();
	}

	@Test
	public void testGetUserOrders() {
		User user = new User(1, "asdf", "asdf", new ArrayList<>());

		assertThat(binanceController.getAllOrders(user)).isNotNull();

		ResponseEntity<OrderResponse> entity = binanceController.getAllOrders(user);

		assertThat(entity.getBody().getOrders()).isNotNull();
	}

	@Test
	public void testGetOpenOrders() {
		User user = new User(1, "asdf", "asdf", new ArrayList<>());

		assertThat(binanceController.getAllOpenOrders(user)).isNotNull();

		ResponseEntity<OrderResponse> entity = binanceController.getAllOpenOrders(user);

		assertThat(entity.getBody().getOrders()).isNotNull();
	}

	@Test
	public void testRegister() {
		User user = new User(1, "asdf", "asdf", new ArrayList<>());
		assertThat(userController.addUser(user).getBody().isSuccess()).isTrue();
	}

	@Test
	public void testExistingRegister() {
		User userOne = new User(1, "asdf", "asdf", new ArrayList<>());
		assertThat(userController.addUser(userOne).getBody().isSuccess()).isTrue();
		User userTwo = new User(2, "asdf", "asdf", new ArrayList<>());
		assertThat(userController.addUser(userTwo).getBody().isSuccess()).isFalse();
	}

	@Test
	public void testCorrectLogin() {
		User userOne = new User(1, "asdf", "asdf", new ArrayList<>());
		assertThat(userController.addUser(userOne).getBody().isSuccess()).isTrue();
		AuthenticationRequest authenticationRequest = new AuthenticationRequest(userOne.getUsername(), userOne.getPassword());
		assertThat(userController.createAuthenticationToken(authenticationRequest)).isNotNull();
		assertThat(userController.createAuthenticationToken(authenticationRequest)).isNotNull();
	}

	@Test
	public void testSubCoin() {
		List<Product> products = new ArrayList<>();

		User user = new User(1, "asdf", "asdf", products);
		assertThat(userController.addUser(user).getBody().isSuccess()).isTrue();

		products.add(new Product(1, "Bitcoin", 60000, 600000, "BTCBUSD"));

		user.setCoinsToTradeIn(products);

		assertThat(userController.update(user)).isNotNull();
	}
}
