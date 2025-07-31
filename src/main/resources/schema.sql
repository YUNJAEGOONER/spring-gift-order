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

create table orders
(
    id              bigint auto_increment,
    member_id       bigint   not null,
    option_id       bigint   not null,
    total_price           int      not null,
    quantity        int      not null,
    order_date_time datetime not null,
    message         varchar(500),
    primary key (id),
    foreign key (member_id) references member (id),
    foreign key (option_id) references option (id)
);

create table kakao_token
(
    id        bigint auto_increment,
    member_id bigint       not null,
    token     varchar(255) not null,
    primary key (id),
    foreign key (member_id) references member (id)
);