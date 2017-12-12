package co.uk.app.commerce.catalog.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import co.uk.app.commerce.catalog.document.Category;

@JsonIgnoreProperties(ignoreUnknown = true)
public class KafkaResponse {

	private Category after;

	public Category getAfter() {
		return after;
	}

	public void setAfter(Category after) {
		this.after = after;
	}
}
