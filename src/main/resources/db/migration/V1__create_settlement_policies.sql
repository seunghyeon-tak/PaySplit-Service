create table settlement_policies
(
    id                 bigint primary key auto_increment,
    policy_code        varchar(50)    not null,
    version            int            not null,
    platform_fee_type  varchar(20)    not null,
    platform_fee_value decimal(10, 2) not null,
    leader_share_type  varchar(20)    not null,
    leader_share_value decimal(10, 2) not null,
    active             boolean        not null default true,
    created_at         datetime       not null,
    updated_at         datetime       not null,

    unique (policy_code, version)
);