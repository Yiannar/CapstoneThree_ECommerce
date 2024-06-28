package org.yearup.data.mysql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.yearup.data.ShoppingCartDao;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class MySqlShoppingCartDao extends MySqlDaoBase implements ShoppingCartDao {

    public MySqlShoppingCartDao(DataSource dataSource) {
        super(dataSource);
    }


    @Override
    public ShoppingCart getByUserId(int userId) {

        ShoppingCart shoppingCart = new ShoppingCart();

        String sql = "SELECT products.*, shopping_cart.quantity " +
                "FROM products " +
                "JOIN shopping_cart " +
                "ON products.product_id = shopping_cart.product_id " +
                "WHERE shopping_cart.user_id = ?";

        try(
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
        )
        {
            statement.setInt(1, userId);

            try(ResultSet row = statement.executeQuery();)
            {
                while(row.next())
                {
                    Product product = MySqlProductDao.mapRow(row);

                    int quantity = row.getInt("quantity");

                    ShoppingCartItem shoppingCartItem = new ShoppingCartItem();

                    shoppingCartItem.setProduct(product);
                    shoppingCartItem.setQuantity(quantity);

                    shoppingCart.add(shoppingCartItem);
                }
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
        return shoppingCart;

    }

    @Override
    public ShoppingCartItem addItemToCart(int userId, ShoppingCartItem shoppingCartItem) {
        ShoppingCart shoppingCart = getByUserId(userId);

        String query = "INSERT INTO shopping_cart (user_id,product_id) VALUES (?,?);";

        try (
                Connection connection = getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, shoppingCartItem.getProductId());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return shoppingCartItem;
    }


    public void updateItemInCart(int userId, ShoppingCartItem shoppingCartItem) {

        String query = "UPDATE shopping_cart SET user_id=?, product_id=? WHERE user_id=?";

        try (Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, shoppingCartItem.getProductId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void delete(int userId) {

        String query = "DELETE FROM shopping_cart WHERE user_id=?";

        try (Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userId);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
