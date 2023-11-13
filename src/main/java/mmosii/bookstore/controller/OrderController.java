package mmosii.bookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mmosii.bookstore.dto.order.CreateOrderRequestDto;
import mmosii.bookstore.dto.order.OrderDto;
import mmosii.bookstore.dto.order.OrderItemDto;
import mmosii.bookstore.dto.order.UpdateStatusRequestDto;
import mmosii.bookstore.service.OrderService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Orders management",
        description = "Endpoints for managing user`s orders")
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(value = "api/orders")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = "Place an order",
            description = "Creates order from user's current shopping cart items")
    public OrderDto placeOrder(@RequestBody @Valid CreateOrderRequestDto requestDto) {
        return orderService.addOrder(requestDto);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = "Get order history",
            description = "Get a list of all user's previous orders")
    public List<OrderDto> getOrdersHistory(
            @ParameterObject @PageableDefault(size = 5) Pageable pageable) {
        return orderService.getOrders(pageable);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Update order status",
            description = "Change status of order by id")
    public OrderDto updateOrderStatus(@PathVariable @Positive Long id,
                                      @RequestBody @Valid UpdateStatusRequestDto requestDto) {
        return orderService.updateOrder(id, requestDto);
    }

    @GetMapping("/{orderId}/items")
    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = "Get list of order items",
            description = "Retrieve all OrderItems for a specific order")
    public List<OrderItemDto> getOrderItems(@PathVariable @Positive Long orderId) {
        return orderService.getOrderItemsByOrder(orderId);
    }

    @GetMapping("/{orderId}/items/{id}")
    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = "Get order item by id",
            description = "Retrieve a specific OrderItem within an order")
    public OrderItemDto getOrderItems(@PathVariable @Positive Long orderId,
                                      @PathVariable @Positive Long id) {
        return orderService.getOrderItemById(orderId, id);
    }
}
