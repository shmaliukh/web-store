package com.vshmaliukh.webstore.repositories.literature_items_repositories;

import com.vshmaliukh.webstore.model.items.literature_item_imp.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BookRepository extends BaseItemRepository<Book> {

}