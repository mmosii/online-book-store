package mmosii.bookstore.repository.book;

import lombok.RequiredArgsConstructor;
import mmosii.bookstore.dto.BookSearchParametersDto;
import mmosii.bookstore.model.Book;
import mmosii.bookstore.repository.SpecificationBuilder;
import mmosii.bookstore.repository.SpecificationProviderManager;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookSpecificationBuilder implements SpecificationBuilder<Book> {
    private final SpecificationProviderManager<Book> specificationProviderManager;

    @Override
    public Specification<Book> build(BookSearchParametersDto searchParameters) {
        Specification<Book> spec = Specification.where(null);
        if (searchParameters.getAuthors() != null && searchParameters.getAuthors().length > 0) {
            spec = spec.and(specificationProviderManager
                    .getSpecificationProvider("author")
                    .getSpecification(searchParameters.getAuthors()));
        }
        if (searchParameters.getTitles() != null && searchParameters.getTitles().length > 0) {
            spec = spec.and(specificationProviderManager
                    .getSpecificationProvider("title")
                    .getSpecification(searchParameters.getTitles()));
        }
        return spec;
    }
}
