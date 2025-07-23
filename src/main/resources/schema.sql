create table product
(
    id        bigint auto_increment,
    name      varchar(255) not null,
    price     int          not null,
    image_url varchar(255) not null,
    primary key (id)
);

create table member
(
    id       bigint auto_increment,
    email    varchar(255) not null,
    password varchar(255) not null,
    role     varchar(10)  not null,
    primary key (id),
    unique (email)
);

create table option
(
    id         bigint auto_increment,
    name       varchar(255) not null,
    quantity   int          not null,
    price      int          not null,
    product_id bigint,
    primary key (id),
    foreign key (product_id) references product (id)
);

create table wishlist
(
    id        bigint auto_increment,
    member_id bigint not null,
    option_id bigint not null,
    quantity  int    not null,
    primary key (id),
    foreign key (option_id) references option (id),
    foreign key (member_id) references member (id)
);