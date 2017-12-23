package co.uk.app.commerce.basket.document;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import co.uk.app.commerce.basket.bean.Items;
import co.uk.app.commerce.basket.bean.Promotion;

@Document(collection = "basket")
public class Basket {

	@Id
	private String id;

	private String userId;

	private String basketId;

	private String status;

	private List<Items> items;

	private List<Promotion> promotions;

	private Double basketTotal;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getBasketId() {
		return basketId;
	}

	public void setBasketId(String basketId) {
		this.basketId = basketId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<Items> getItems() {
		return items;
	}

	public void setItems(List<Items> items) {
		this.items = items;
	}

	public List<Promotion> getPromotions() {
		return promotions;
	}

	public void setPromotions(List<Promotion> promotions) {
		this.promotions = promotions;
	}

	public Double getBasketTotal() {
		return basketTotal;
	}

	public void setBasketTotal(Double basketTotal) {
		this.basketTotal = basketTotal;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
