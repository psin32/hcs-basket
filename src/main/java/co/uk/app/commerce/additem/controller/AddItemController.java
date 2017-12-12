package co.uk.app.commerce.additem.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.uk.app.commerce.additem.bean.AddItemBean;
import co.uk.app.commerce.basket.document.Basket;
import co.uk.app.commerce.basket.service.BasketService;

@RestController
@RequestMapping("/api/item/add")
public class AddItemController {

	@Autowired
	private BasketService basketService;

	@PutMapping
	public ResponseEntity<?> addItem(@RequestBody AddItemBean addItemBean, HttpServletRequest request,
			HttpServletResponse response) {
		String userId = String.valueOf(request.getAttribute("USER_ID"));
		Basket basket = basketService.addItem(addItemBean, userId, "GBP");
		if (null != basket) {
			Cookie cookie = new Cookie("BASKET_COUNT",
					String.valueOf(basket.getItems().stream().mapToInt(item -> item.getQuantity()).sum()));
			cookie.setPath("/");
			response.addCookie(cookie);
		}
		return ResponseEntity.ok(basket);
	}
}
