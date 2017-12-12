package co.uk.app.commerce.basket.repository;

import java.util.Collection;

import org.springframework.data.mongodb.repository.MongoRepository;

import co.uk.app.commerce.basket.document.Basket;

public interface BasketRepository extends MongoRepository<Basket, String> {

	Collection<Basket> findByUserIdAndStatus(String userId, String status);
}
