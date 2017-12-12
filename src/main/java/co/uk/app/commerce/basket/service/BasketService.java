package co.uk.app.commerce.basket.service;

import java.util.Collection;

import co.uk.app.commerce.additem.bean.AddItemBean;
import co.uk.app.commerce.basket.document.Basket;

public interface BasketService {

	Basket persistBasket(Basket basket);

	Basket getCurrentBasketByUserId(String userId);

	Collection<Basket> getBasketByUserIdAndStatus(String userId, String status);

	Basket addItem(AddItemBean addItemBean, String userId, String currency);
	
	Basket updateBasket(AddItemBean addItemBean, String userId, String currency);
}
