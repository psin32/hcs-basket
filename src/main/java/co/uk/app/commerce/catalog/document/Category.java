package co.uk.app.commerce.catalog.document;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import co.uk.app.commerce.catalog.bean.Association;
import co.uk.app.commerce.catalog.bean.Description;
import co.uk.app.commerce.catalog.bean.Image;

@Document(collection = "basketcategory")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Category {

	@Id
	private String id;

	private String identifier;

	private Description description;

	private List<Image> thumbnail;

	private List<Image> fullimage;

	private Integer published;

	private Integer display;

	private Integer sequence;

	private Boolean topnav;

	private List<Association> parentcategories;

	private String url;

	private String lastupdate = new SimpleDateFormat("dd-MM-yy HH:mm:SS").format(new Date());

	@JsonCreator
	public static Category Create(String jsonString) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		Category module = null;
		module = mapper.readValue(jsonString, Category.class);
		return module;
	}

	public String getLastupdate() {
		return lastupdate;
	}

	public void setLastupdate(String lastupdate) {
		this.lastupdate = lastupdate;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public Description getDescription() {
		return description;
	}

	public void setDescription(Description description) {
		this.description = description;
	}

	public List<Image> getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(List<Image> thumbnail) {
		this.thumbnail = thumbnail;
	}

	public List<Image> getFullimage() {
		return fullimage;
	}

	public void setFullimage(List<Image> fullimage) {
		this.fullimage = fullimage;
	}

	public Integer getPublished() {
		return published;
	}

	public void setPublished(Integer published) {
		this.published = published;
	}

	public Integer getDisplay() {
		return display;
	}

	public void setDisplay(Integer display) {
		this.display = display;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public Boolean isTopnav() {
		return topnav;
	}

	public void setTopnav(Boolean topnav) {
		this.topnav = topnav;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<Association> getParentcategories() {
		return parentcategories;
	}

	public void setParentcategories(List<Association> parentcategories) {
		this.parentcategories = parentcategories;
	}

	public Boolean getTopnav() {
		return topnav;
	}
}
