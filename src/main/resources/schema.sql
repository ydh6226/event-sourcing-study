create table deposit_entity
(
    id         bigint auto_increment
        primary key,
    account_no varchar(100) null,
    balance    decimal(19, 2) null
);

create index deposit_entity_account_no_index
    on deposit_entity (account_no);


create table deposit_event
(
    id         bigint auto_increment
        primary key,
    event_type varchar(50)   not null,
    event_id   varchar(100)  not null,
    account_no varchar(100)  not null,
    payload    varchar(4096) not null,
    created_at datetime(6) not null,
    constraint deposit_uk_1
        unique (account_no, event_id)
);

create table deposit_event_snapshot
(
    id         bigint auto_increment
        primary key,
    account_no varchar(100)  not null,
    event_id   varchar(100)  not null,
    payload    varchar(4096) not null,
    created_at datetime(6) not null,
    constraint deposit_uk_1
        unique (account_no, event_id)
);
