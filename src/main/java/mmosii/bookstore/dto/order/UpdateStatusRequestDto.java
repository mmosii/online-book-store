package mmosii.bookstore.dto.order;

import jakarta.validation.constraints.NotBlank;

public record UpdateStatusRequestDto(@NotBlank String status) {
}
