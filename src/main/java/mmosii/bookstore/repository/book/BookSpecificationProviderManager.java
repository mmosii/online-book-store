package mmosii.bookstore.repository.book;

import java.util.List;
import lombok.RequiredArgsConstructor;
import mmosii.bookstore.exception.SpecificationNotFoundException;
import mmosii.bookstore.model.Book;
import mmosii.bookstore.repository.SpecificationProvider;
import mmosii.bookstore.repository.SpecificationProviderManager;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookSpecificationProviderManager implements SpecificationProviderManager<Book> {
    private final List<SpecificationProvider<Book>> bookSpecificationProviders;

    @Override
    public SpecificationProvider<Book> getSpecificationProvider(String key) {
        return bookSpecificationProviders.stream()
                .filter(p -> p.getKey().equals(key))
                .findFirst()
                .orElseThrow(()
                        -> new SpecificationNotFoundException("Can't find specification provider"
                        + " by key " + key));
    }
}
