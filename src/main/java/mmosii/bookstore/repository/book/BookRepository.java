package mmosii.bookstore.repository.book;

import java.util.List;
import mmosii.bookstore.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {
    @Query("select b from Book b join fetch b.categories c where c.id = :id")
    List<Book> findAllByCategoryId(Long id);
}
