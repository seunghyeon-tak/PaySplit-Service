create table settlement_items
(
    id            bigint primary key auto_increment,
    settlement_id bigint         not null,
    receiver_type varchar(20)    not null,
    receiver_id   bigint         not null,
    amount        decimal(15, 2) not null,
    created_at    datetime       not null,

    unique (settlement_id, receiver_type, receiver_id),
    foreign key (settlement_id) references settlements (id) on delete restrict
);