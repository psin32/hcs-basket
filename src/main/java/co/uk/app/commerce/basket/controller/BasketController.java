package co.uk.app.commerce.basket.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
	public @ResponseBody Basket getBasketByUserId(HttpServletRequest request, HttpServletResponse response) {
		String userId = String.valueOf(request.getAttribute("USER_ID"));
		Basket basket = basketService.getCurrentBasketByUserId(userId);
		String cookieValue = "0";
		if (null != basket.getId()) {
			cookieValue = String.valueOf(basket.getItems().stream().mapToInt(item -> item.getQuantity()).sum());
		}
		Cookie cookie = new Cookie("BASKET_COUNT", cookieValue);
		cookie.setPath("/");
		response.addCookie(cookie);
		return basket;
	}

	@PatchMapping
	public ResponseEntity<?> updateBasket(@RequestBody AddItemBean addItemBean, HttpServletRequest request,
			HttpServletResponse response) {
		String userId = String.valueOf(request.getAttribute("USER_ID"));
		Basket basket = basketService.updateBasket(addItemBean, userId, "GBP");
		String cookieValue = "0";
		if (null != basket.getId()) {
			cookieValue = String.valueOf(basket.getItems().stream().mapToInt(item -> item.getQuantity()).sum());
		}
		Cookie cookie = new Cookie("BASKET_COUNT", cookieValue);
		cookie.setPath("/");
		response.addCookie(cookie);
		return ResponseEntity.ok(basket);
	}

	@DeleteMapping(path = "/{partnumber}")
	public ResponseEntity<?> deleteItem(@PathVariable("partnumber") String partnumber, HttpServletRequest request,
			HttpServletResponse response) {
		String userId = String.valueOf(request.getAttribute("USER_ID"));
		Basket basket = basketService.deleteItem(partnumber, userId, "GBP");
		String cookieValue = "0";
		if (null != basket.getId()) {
			cookieValue = String.valueOf(basket.getItems().stream().mapToInt(item -> item.getQuantity()).sum());
		}
		Cookie cookie = new Cookie("BASKET_COUNT", cookieValue);
		cookie.setPath("/");
		response.addCookie(cookie);
		return ResponseEntity.ok(basket);
	}
}
