create table settlement_policies
(
    id                bigint primary key auto_increment,
    settlement_id     bigint unique,
    platform_fee_rate decimal(5, 4) not null,
    leader_share_rate decimal(5, 4) not null,
    policy_version    varchar(20),
    created_at        datetime      not null,

    foreign key (settlement_id) references settlements (id)
);