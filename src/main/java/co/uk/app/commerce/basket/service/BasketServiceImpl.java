package co.uk.app.commerce.basket.service;

import java.util.Collection;
import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.uk.app.commerce.basket.document.Basket;
import co.uk.app.commerce.basket.repository.BasketRepository;

@Component
public class BasketServiceImpl implements BasketService {

	@Autowired
	private BasketRepository basketRepository;

	@Override
	public Basket getBasketById(String basketId) {
		return basketRepository.findByBasketId(basketId);
	}

	@Override
	public Basket getCurrentBasketByUserId(String userId) {
		Collection<Basket> baskets = basketRepository.findByUserIdAndStatus(userId, "P");
		if (null == baskets || baskets.isEmpty()) {
			return null;
		}
		Basket basket = null;
		Iterator<Basket> iter = baskets.iterator();
		while (iter.hasNext()) {
			basket = iter.next();
		}
		return basket;
	}

	@Override
	public Collection<Basket> getBasketByUserIdAndStatus(String userId, String status) {
		return basketRepository.findByUserIdAndStatus(userId, status);
	}
}
