package co.uk.app.commerce.basket.service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.uk.app.commerce.additem.bean.AddItemBean;
import co.uk.app.commerce.basket.bean.Items;
import co.uk.app.commerce.basket.document.Basket;
import co.uk.app.commerce.basket.repository.BasketRepository;
import co.uk.app.commerce.catalog.bean.Image;
import co.uk.app.commerce.catalog.bean.ListPrice;
import co.uk.app.commerce.catalog.document.Catentry;
import co.uk.app.commerce.catalog.repository.CatentryRepository;

@Component
public class BasketServiceImpl implements BasketService {

	@Autowired
	private BasketRepository basketRepository;

	@Autowired
	private CatentryRepository catentryRepository;

	@Override
	public Basket getCurrentBasketByUserId(String userId) {
		Collection<Basket> baskets = basketRepository.findByUserIdAndStatus(userId, "P");
		if (null == baskets || baskets.isEmpty()) {
			return null;
		}
		return baskets.stream().findFirst().orElse(null);
	}

	@Override
	public Collection<Basket> getBasketByUserIdAndStatus(String userId, String status) {
		return basketRepository.findByUserIdAndStatus(userId, status);
	}

	@Override
	public Basket addItem(AddItemBean addItemBean, String userId, String currency) {
		Catentry catentry = catentryRepository.findByPartnumber(addItemBean.getPartnumber());
		Basket basket = null;
		if (null != catentry) {
			basket = getCurrentBasketByUserId(userId);
			List<Items> items = new ArrayList<>();
			if (null == basket) {
				basket = new Basket();
				basket.setUserId(userId);
				basket.setStatus("P");
			} else {
				items = basket.getItems();
			}
			items = getItemDetails(addItemBean, currency, catentry, items, true);
			basket.setItems(items);
			DecimalFormat formatter = new DecimalFormat("#0.00");
			basket.setBasketTotal(Double.valueOf(formatter.format(items.stream().mapToDouble(item -> item.getItemtotal()).sum())));
		}
		return basket;
	}

	private Double calculateItemTotal(Double listPrice, Integer quantity) {
		Double itemTotal = listPrice * quantity;
		DecimalFormat formatter = new DecimalFormat("#0.00"); 
		return Double.valueOf(formatter.format(itemTotal));
	}

	private List<Items> getItemDetails(AddItemBean addItemBean, String currency, Catentry catentry,
			List<Items> itemsList, boolean isItemAdded) {
		String partnumber = addItemBean.getPartnumber();
		Integer quantity = addItemBean.getQuantity();
		List<Items> newList = new ArrayList<>();
		Items items = null;

		if (itemsList.size() > 0) {
			items = itemsList.stream().filter(item -> item.getPartnumber().equalsIgnoreCase(partnumber)).findAny()
					.orElse(null);
			newList = itemsList.stream().filter(item -> !item.getPartnumber().equalsIgnoreCase(partnumber))
					.collect(Collectors.toList());
		}

		if (null == items) {
			items = new Items();
			items.setQuantity(quantity);
		} else {
			items.setQuantity(quantity + items.getQuantity());
		}

		if (!isItemAdded) {
			items.setQuantity(quantity);
		}

		items.setPartnumber(partnumber);
		items.setName(catentry.getDescription().getName());
		items.setCurrency(currency);
		items.setUrl(catentry.getUrl());

		List<ListPrice> listPrices = catentry.getListprice();
		ListPrice price = listPrices.stream().filter(prices -> prices.getCurrency().equalsIgnoreCase(currency))
				.findAny().orElse(null);
		if (null != price) {
			Double listPrice = Double.valueOf(price.getPrice());
			items.setListprice(listPrice);

			items.setItemtotal(calculateItemTotal(listPrice, items.getQuantity()));
		} else {
			// TODO: Throw exception here in case price does not exists
			// for this product.
		}

		List<Image> thumbnailImages = catentry.getThumbnail();

		Image image = thumbnailImages.stream().filter(images -> images.getName().equalsIgnoreCase("front-view"))
				.findAny().orElse(null);
		if (null != image) {
			items.setImage(image.getUrl());
		} else {
			// TODO: Add default image if image does not exists.
		}
		if (items.getQuantity() > 0) {
			newList.add(items);
		}
		Collections.sort(newList, (p1, p2) -> p1.getPartnumber().compareTo(p2.getPartnumber()));
		return newList;
	}

	@Override
	public Basket persistBasket(Basket basket) {
		return basketRepository.save(basket);
	}

	@Override
	public Basket updateBasket(AddItemBean addItemBean, String userId, String currency) {
		Catentry catentry = catentryRepository.findByPartnumber(addItemBean.getPartnumber());
		Basket basket = null;
		if (null != catentry) {
			basket = getCurrentBasketByUserId(userId);
			List<Items> items = new ArrayList<>();
			if (null == basket) {
				basket = new Basket();
				basket.setUserId(userId);
				basket.setStatus("P");
			} else {
				items = basket.getItems();
			}
			items = getItemDetails(addItemBean, currency, catentry, items, false);
			basket.setItems(items);
			basket.setBasketTotal(items.stream().mapToDouble(item -> item.getItemtotal()).sum());
		}
		return basket;
	}
}
