create table settlements
(
    id                     bigint primary key auto_increment,
    original_settlement_id bigint comment '재정산/조정 시 원본 settlement',
    policy_id              bigint         not null,
    payment_id             bigint,
    type                   varchar(30)    not null,
    status                 varchar(20)    not null,
    total_amount           decimal(15, 2) not null,
    created_at             datetime       not null,
    completed_at           datetime,

    foreign key (original_settlement_id) references settlements (id) on delete restrict ,
    foreign key (policy_id) references settlement_policies (id) on delete restrict ,
    foreign key (payment_id) references payments (id) on delete restrict
);