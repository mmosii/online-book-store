package mmosii.bookstore.service;

import java.util.List;
import mmosii.bookstore.dto.order.CreateOrderRequestDto;
import mmosii.bookstore.dto.order.OrderDto;
import mmosii.bookstore.dto.order.OrderItemDto;
import mmosii.bookstore.dto.order.UpdateStatusRequestDto;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    OrderDto addOrder(CreateOrderRequestDto requestDto);

    List<OrderDto> getOrders(Pageable pageable);

    OrderDto updateOrder(Long id, UpdateStatusRequestDto requestDto);

    List<OrderItemDto> getOrderItemsByOrder(Long id);

    OrderItemDto getOrderItemById(Long orderId, Long id);
}
