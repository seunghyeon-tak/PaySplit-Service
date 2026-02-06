create table payments
(
    id                  bigint primary key auto_increment,
    amount              decimal(15, 2) not null,
    policy_id           bigint         not null,
    status              varchar(20)    not null,
    method              varchar(20)   not null default 'CARD' comment '결제 수단',
    payer_id            bigint         not null,
    external_payment_id varchar(100),
    currency            varchar(10)    not null,
    settled_at          datetime,
    created_at          datetime       not null,
    updated_at          datetime       not null,

    constraint fk_payments_policy foreign key (policy_id)
        references settlement_policies (id) on delete restrict
);