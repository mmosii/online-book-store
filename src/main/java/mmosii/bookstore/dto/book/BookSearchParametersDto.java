package mmosii.bookstore.dto.book;

public record BookSearchParametersDto(String[] authors,
                                      String[] titles) {
}
