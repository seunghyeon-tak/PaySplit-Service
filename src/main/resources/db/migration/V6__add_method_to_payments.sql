alter table payments
    add column method varchar(100) not null default 'CARD' comment '결제 수단';