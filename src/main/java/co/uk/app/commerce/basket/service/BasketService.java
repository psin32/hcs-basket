package co.uk.app.commerce.basket.service;

import java.util.Collection;

import co.uk.app.commerce.basket.document.Basket;

public interface BasketService {

	Basket getBasketById(String basketId);

	Basket getCurrentBasketByUserId(String userId);

	Collection<Basket> getBasketByUserIdAndStatus(String userId, String status);
}
