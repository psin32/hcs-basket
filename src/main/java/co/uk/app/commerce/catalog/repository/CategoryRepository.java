package co.uk.app.commerce.catalog.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import co.uk.app.commerce.catalog.bean.Category;

public interface CategoryRepository extends MongoRepository<Category, String> {

}
