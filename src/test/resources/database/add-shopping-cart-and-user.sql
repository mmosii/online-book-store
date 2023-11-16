insert into users (id, email, password, first_name, last_name, shipping_address, is_deleted)
values (5,'test@gmail.com', 'testpass', 'Brad', 'Pitt', 'home', false);

insert into shopping_carts (id, user_id)
values (3, 5);

insert into cart_items (id, shopping_cart_id, book_id, quantity)
values (6, 3, 2, 10);
