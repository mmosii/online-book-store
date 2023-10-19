package mmosii.bookstore.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record CreateBookRequestDto(@NotBlank String title,
                                  @NotBlank String author,
                                  @NotBlank String isbn,
                                  @NotNull @Positive BigDecimal price,
                                  String description,
                                  String coverImage) {
}
