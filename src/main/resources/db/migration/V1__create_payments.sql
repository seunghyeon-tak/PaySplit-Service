create table payments
(
    id         bigint primary key auto_increment,
    amount     decimal(15, 2) not null,
    created_at datetime       not null,
    updated_at datetime       not null
);