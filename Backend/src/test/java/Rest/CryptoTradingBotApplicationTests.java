package Rest;

import Rest.Controllers.BinanceController;
import Rest.Controllers.UserController;
import Rest.Entities.Product;
import Rest.Entities.User;
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


}
