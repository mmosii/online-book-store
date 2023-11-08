package mmosii.bookstore.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import mmosii.bookstore.dto.order.CreateOrderRequestDto;
import mmosii.bookstore.dto.order.OrderDto;
import mmosii.bookstore.dto.order.OrderItemDto;
import mmosii.bookstore.dto.order.UpdateStatusRequestDto;
import mmosii.bookstore.exception.EntityNotFoundException;
import mmosii.bookstore.mapper.OrderItemMapper;
import mmosii.bookstore.mapper.OrderMapper;
import mmosii.bookstore.model.CartItem;
import mmosii.bookstore.model.Order;
import mmosii.bookstore.model.OrderItem;
import mmosii.bookstore.model.ShoppingCart;
import mmosii.bookstore.model.User;
import mmosii.bookstore.repository.order.OrderItemRepository;
import mmosii.bookstore.repository.order.OrderRepository;
import mmosii.bookstore.repository.shoppingcart.ShoppingCartRepository;
import mmosii.bookstore.repository.user.UserRepository;
import mmosii.bookstore.service.OrderService;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final UserRepository userRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final OrderItemRepository orderItemRepository;

    @Override
    @Transactional
    public OrderDto addOrder(CreateOrderRequestDto requestDto) {
        User user = findUser();
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(user.getId()).orElseThrow(()
                -> new EntityNotFoundException("Can't find shopping cart by users id: "
                                               + user.getId()));
        Set<CartItem> cartItems = shoppingCart.getCartItems();
        Order order = new Order();
        order.setOrderDate(LocalDateTime.now());
        order.setUser(user);
        order.setStatus(Order.Status.NEW);
        order.setShippingAddress(requestDto.shippingAddress());
        BigDecimal total = cartItems.stream()
                .map(i -> i.getBook().getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotal(total);
        orderRepository.save(order);

        Set<OrderItem> orderItems = new HashSet<>();
        for (CartItem cartItem : shoppingCart.getCartItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setBook(cartItem.getBook());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setOrder(order);
            orderItem.setPrice(orderItem.getBook().getPrice());
            orderItems.add(orderItemRepository.save(orderItem));
        }

        shoppingCartRepository.delete(shoppingCart);
        order.setOrderItems(orderItems);
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public List<OrderDto> getOrders(Pageable pageable) {
        return orderMapper.toDtoList(orderRepository.findByUserId(findUser().getId(), pageable));
    }

    @Transactional
    @Override
    public OrderDto updateOrder(Long id, UpdateStatusRequestDto requestDto) {
        Order order = orderRepository.getReferenceById(id);
        order.setStatus(Order.Status.valueOf(requestDto.status()));
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public List<OrderItemDto> getOrderItemsByOrder(Long id) {
        return orderItemRepository.findAllByOrderId(id)
                .stream()
                .map(orderItemMapper::toDto)
                .toList();
    }

    @Override
    public OrderItemDto getOrderItemById(Long orderId, Long id) {
        return orderItemMapper.toDto(orderItemRepository
                .findByIdAndOrderId(id, orderId)
                .orElseThrow(()
                        -> new EntityNotFoundException(
                        "Can't find order item with id: %d for order with id: %d"
                                .formatted(id, orderId))));
    }

    private User findUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email).orElseThrow(()
                -> new EntityNotFoundException("Can't find user by email " + email));
    }
}
