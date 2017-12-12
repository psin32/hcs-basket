package co.uk.app.commerce.catalog.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import co.uk.app.commerce.catalog.document.Category;

public interface CategoryRepository extends MongoRepository<Category, String> {

}
