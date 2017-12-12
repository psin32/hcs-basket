package co.uk.app.commerce.basket.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import co.uk.app.commerce.additem.bean.AddItemBean;
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

	@PatchMapping
	public ResponseEntity<?> updateBasket(@RequestBody AddItemBean addItemBean, HttpServletRequest request) {
		String userId = String.valueOf(request.getAttribute("USER_ID"));
		Basket basket = basketService.updateBasket(addItemBean, userId, "GBP");
		if (null != basket) {
			return ResponseEntity.ok(basketService.persistBasket(basket));
		}
		return ResponseEntity.status(HttpStatus.CONFLICT).build();
	}
}
