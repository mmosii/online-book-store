package mmosii.bookstore.mapper;

import mmosii.bookstore.config.MapperConfig;
import mmosii.bookstore.dto.BookDto;
import mmosii.bookstore.dto.CreateBookRequestDto;
import mmosii.bookstore.model.Book;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    BookDto toDto(Book book);

    Book fromDto(CreateBookRequestDto requestDto);
}
