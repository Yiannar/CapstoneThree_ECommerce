package org.yearup.data.mysql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.yearup.data.ShoppingCartDao;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class MySqlShoppingCartDao extends MySqlDaoBase implements ShoppingCartDao {

    private DataSource dataSource;

    public MySqlShoppingCartDao(DataSource dataSource) {
        super(dataSource);
    }



    @Override
    public ShoppingCart getByUserId(int userId) {
        // join table to pass product
        // user id = ?

        ShoppingCart shoppingCart = new ShoppingCart();
        String query = "SELECT * FROM shopping_cart " +
                "JOIN products ON products.product_id=shopping_cart.product_id " +
                "WHERE user_id=?;";

        try(
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)
                ){
            preparedStatement.setInt(1, userId);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                ShoppingCartItem shoppingCartItem = new ShoppingCartItem();
                shoppingCartItem.setProduct(MySqlProductDao.mapRow(resultSet));
                shoppingCartItem.setQuantity(resultSet.getInt("quantity"));
                shoppingCart.add(shoppingCartItem);
            }

        } catch (SQLException sqlException){
            sqlException.printStackTrace();
        }
        return shoppingCart;
    }

    @Override
    public void addItemToCart(int userId, int productId) {
        ShoppingCart shoppingCart = getByUserId(userId);

        String query = "INSERT INTO shopping_cart (user_id,product_id,quantity) VALUES (?,?,?);";

        try(
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)
        ){
           preparedStatement.setInt(1,userId);
           preparedStatement.setInt();
            ResultSet resultSet =
        }
    }
}
