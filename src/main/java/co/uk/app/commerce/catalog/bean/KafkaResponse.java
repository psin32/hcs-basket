package co.uk.app.commerce.catalog.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import co.uk.app.commerce.catalog.document.Category;

@JsonIgnoreProperties(ignoreUnknown = true)
public class KafkaResponse {

	private Category after;

	private Category patch;

	public Category getPatch() {
		return patch;
	}

	public void setPatch(Category patch) {
		this.patch = patch;
	}

	public Category getAfter() {
		return after;
	}

	public void setAfter(Category after) {
		this.after = after;
	}
}
