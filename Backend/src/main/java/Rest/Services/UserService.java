package Rest.Services;

import Rest.Entities.Product;
import Rest.Entities.User;
import Rest.Repositories.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private IUserRepository userRepository;

    public void update(User user) {
        List<Product> products = getUserProducts(user.getUserId());
        for (Product product:user.getCoinsToTradeIn()) {
            if (!products.contains(product)) {
                products.add(product);
            }
        }
        user.setCoinsToTradeIn(products);
        userRepository.save(user);
    }

//    private void removeProduct(List<Product> allProducts, Product productToRemove) {
//        allProducts.remove(productToRemove);
//    }

    private List<Product> checkMatch(List<Product> allProducts, List<Product> productsToDelete) {
        List<Product> newProducts = allProducts;
        for (int iterator = 0; iterator < allProducts.toArray().length; iterator++) {
            for (Product productToDelete : productsToDelete) {
                if (allProducts.get(iterator).getId() == productToDelete.getId()) {
                    newProducts.remove(iterator);
                }
            }
        }

        return newProducts;


//        List<Product> newProducts = new ArrayList<>();
//        Iterator<Product> productsToIterate = allProducts.iterator();
//        while (productsToIterate.hasNext()) {
//            Product productIter = productsToIterate.next();
//            for (Product productToDelete : productsToDelete) {
//                System.out.println("product: " + productIter.toString());
//                System.out.println("productToDelete: " + productToDelete);
//                if (productIter.getSymbol().equals(productToDelete.getSymbol())) {
//                    productsToIterate.remove();
//                }
//            }
//        }
//
//        productsToIterate.forEachRemaining(newProducts::add);
//        return newProducts;
    }

    public List<Product> removeUserProducts(User user) {
        List<Product> productsToSave = checkMatch(getUserProducts(user.getUserId()), user.getCoinsToTradeIn());
        user.setCoinsToTradeIn(productsToSave);
        userRepository.save(user);
        return user.getCoinsToTradeIn();
    }

    public List<Product> getUserProducts(int userId) {
        return userRepository.getById(userId).getCoinsToTradeIn();
    }
}
