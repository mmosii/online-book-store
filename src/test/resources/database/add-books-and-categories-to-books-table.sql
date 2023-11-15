insert into books (id, title, author, isbn, price)
values (2, 'Shantaram', 'Gregory David Roberts', 222333555, 250.55),
       (1, 'The Godfather', 'Mario Puzo', 222333666, 350.55);

INSERT INTO books_categories (book_id, category_id)
VALUES (2, 2),
       (1, 1);

insert into categories (id, name)
values  (1, 'fantasy'),
        (2, 'historic');
