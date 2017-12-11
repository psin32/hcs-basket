package co.uk.app.commerce.basket.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import co.uk.app.commerce.basket.document.Basket;
import co.uk.app.commerce.basket.service.BasketService;

@RestController
@RequestMapping("/api/basket")
public class BasketController {

	@Autowired
	private BasketService basketService;

	@GetMapping
	public @ResponseBody Basket getBasketByUserId(HttpServletRequest request) {
		 String userId = String.valueOf(request.getAttribute("USER_ID"));
		 return basketService.getCurrentBasketByUserId(userId);
	}

	@PutMapping
	public ResponseEntity<?> persistBasket(@RequestBody Basket basket, HttpServletRequest request) {
		String userId = String.valueOf(request.getAttribute("USER_ID"));
		Basket currentBasket = basketService.getCurrentBasketByUserId(userId);
		if (null == currentBasket) {
		}
		return ResponseEntity.status(HttpStatus.CONFLICT).build();
	}
}
