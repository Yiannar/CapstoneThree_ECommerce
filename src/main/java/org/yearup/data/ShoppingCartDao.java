package org.yearup.data;

import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

public interface ShoppingCartDao
{
    ShoppingCart getByUserId(int userId);
    ShoppingCartItem addItemToCart (int userId, ShoppingCartItem shoppingCartItem);
    void updateItemInCart (int userId, ShoppingCartItem shoppingCartItem);
    void delete(int userId);


}
