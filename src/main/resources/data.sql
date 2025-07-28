insert into member (role, email, password)
values ('ADMIN', 'testadmin1@kakao.com', '12345678');

insert into member (role, email, password)
values ('ADMIN', 'testadmin@samsung.com', '12345678');

insert into member (role, email, password)
values ('USER', 'testuser1@naver.com', '12345678');

insert into member (role, email, password)
values ('USER', 'testuser2@apple.com', '12345678');

-- 아이폰16
insert into product(id, name, price, image_url)
values (9999L, '아이폰16Pro', 1550000,
        'https://encrypted-tbn1.gstatic.com/shopping?q=tbn:ANd9GcQtIiGp7I3QmpgBrbgqKJP4TMmjCAQQ5PKZADLBuzDere8Z9iWFUzfv-jvW0F7qP9gLewthFhuVUA');

insert into option(name, quantity, price, product_id)
values ('128GB', 999, 100000, 9999L),
       ('256GB', 999, 200000, 9999L),
       ('512GB', 999, 300000, 9999L),
       ('1TB', 999, 400000, 9999L);

-- 맥북에어
insert into product(id, name, price, image_url)
values (9998L, '맥북에어', 1250000,
        'https://encrypted-tbn3.gstatic.com/shopping?q=tbn:ANd9GcTVEsMB9-Mm17xUSCrL2oIxZF6H7mn1CZweoW_lyPRCuTKZ066oiJuHaLGVgf1mDqbLf9SAeKmAdA');

insert into option(name, quantity, price, product_id)
values ('256GB', 999, 200000, 9998L),
       ('512GB', 999, 300000, 9998L),
       ('1TB', 999, 400000, 9998L),
       ('2TB', 999, 100000, 9998L);

-- 애플워치
insert into product(id, name, price, image_url)
values (9997L, '애플워치', 123000,
        'https://encrypted-tbn1.gstatic.com/shopping?q=tbn:ANd9GcQFcxc2JlxNMqqmlpX3_D6hC5ITnTDEZmytNsgR6K3jnwTF5G_Lk3LJ1xivRZz9yWLh3Tmlw3nN');
insert into option(name, quantity, price, product_id)
values ('41mm', 999, 50000, 9997L),
       ('44mm', 999, 200000, 9997L);

-- 맥북프로
insert into product(id, name, price, image_url)
values (9996L, '맥북프로', 2250000,
        'https://encrypted-tbn0.gstatic.com/shopping?q=tbn:ANd9GcS5oCkTuSJ0409B_5FFITHRaXypcr3-hJHtplxcXiXyYK9y8K7Lx-kgWbVhFaHVUP1NeexGHAsqVg');
insert into option(name, quantity, price, product_id)
values ('512GB', 999, 200000, 9996L),
       ('1TB', 999, 600000, 9996L),
       ('2TB, 10코어', 999, 800000, 9996L),
       ('3TB, 10코어', 999, 1000000, 9996L);