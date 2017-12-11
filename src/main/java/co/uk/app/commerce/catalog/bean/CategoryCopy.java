package co.uk.app.commerce.catalog.bean;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CategoryCopy {

	public CategoryCopy() {
	}

	public CategoryCopy(String topnav, String sequence, String display, String published, String identifier,
			String url) {
		this.topnav = topnav;
		this.sequence = sequence;
		this.display = display;
		this.published = published;
		this.identifier = identifier;
		this.url = url;
	}

	@JsonCreator
	public static CategoryCopy Create(String jsonString) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		CategoryCopy module = null;
		module = mapper.readValue(jsonString, CategoryCopy.class);
		return module;
	}

	private String topnav;

	private String sequence;
	private String display;

	private String published;

	private String identifier;

	private String url;

	public String getTopnav() {
		return topnav;
	}

	public void setTopnav(String topnav) {
		this.topnav = topnav;
	}

	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

	public String getDisplay() {
		return display;
	}

	public void setDisplay(String display) {
		this.display = display;
	}

	public String getPublished() {
		return published;
	}

	public void setPublished(String published) {
		this.published = published;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
