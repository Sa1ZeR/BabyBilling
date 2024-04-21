create table customers
(
    balance   numeric(38, 2) not null,
    id        bigserial      not null,
    tariff_id bigint,
    msisnd    varchar(12)    not null unique,
    password  varchar(96)    not null,
    primary key (id)
);

create table tariffs
(
    id   bigserial not null,
    name varchar(255),
    primary key (id)
);

create index phone_index
    on customers (msisnd);

alter table if exists customers
    add constraint FK_tariff
    foreign key (tariff_id)
    references tariffs;

create table customers_to_roles
(
    roles       smallint check (roles >= 0),
    customer_id bigint not null
);

alter table if exists customers_to_roles
    add constraint FK_user_role
    foreign key (customer_id)
    references customers;