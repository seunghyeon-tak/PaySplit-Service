create table settlements
(
    id                     bigint primary key auto_increment,
    original_settlement_id bigint,
    payment_id             bigint,
    type                   varchar(20)    not null,
    status                 varchar(20)    not null,
    total_amount           decimal(15, 2) not null,
    created_at             datetime       not null,
    completed_at           datetime,

    foreign key (original_settlement_id) references settlements (id),
    foreign key (payment_id) references payments (id)
);