package mmosii.bookstore.dto;

import lombok.Data;

@Data
public class BookSearchParametersDto {
    private String[] authors;
    private String[] titles;
}
