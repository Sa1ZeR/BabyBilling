create table customers
(
    balance   numeric(10, 2) not null,
    id        bigserial      not null,
    tariff_id bigint,
    msisnd    varchar(12)    not null unique,
    password  varchar(96)    not null,
    primary key (id)
);

create table tariff_call_limits
(
    incoming_call_cost                      numeric(10, 2) not null,
    incoming_call_cost_other                numeric(10, 2) not null,
    incoming_call_cost_other_without_packet numeric(10, 2) not null,
    incoming_call_cost_without_packet       numeric(10, 2) not null,
    outgoing_call_cost                      numeric(10, 2) not null,
    outgoing_call_cost_other                numeric(10, 2) not null,
    outgoing_call_cost_other_without_packet numeric(10, 2) not null,
    outgoing_call_cost_without_packet       numeric(10, 2) not null,
    id                                      bigserial      not null,
    primary key (id)
);

create table tariffs_minutes
(
    common_minutes boolean   not null,
    minutes        integer   not null,
    minutes_other  integer   not null,
    id             bigserial not null,
    primary key (id)
);

create table tariffs
(
    monthly_cost      numeric(10, 2) not null,
    id                bigserial      not null,
    tariff_calls_id   bigint,
    tariff_minutes_id bigint,
    name              varchar(255),
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

create table customer_call_data
(
    minutes       integer   not null,
    minutes_other integer   not null,
    month         integer   not null,
    year          integer   not null,
    customer_id   bigint,
    id            bigserial not null,
    primary key (id)
);

create table customer_payments
(
    month       integer        not null,
    year        integer        not null,
    amount      numeric(10, 2) not null,
    customer_id bigint         not null,
    id          bigserial      not null,
    primary key (id)
);

alter table if exists customer_payments
    add constraint FKxc521vx929xnd26fu3dr6scf
    foreign key (customer_id)
    references customers;

alter table if exists customer_call_data
    add constraint FKf48sfdl96ujex80wnnnnihh5p
    foreign key (customer_id)
    references customers;

alter table if exists customers_to_roles
    add constraint FK_user_role
    foreign key (customer_id)
    references customers;

alter table if exists tariffs
    add constraint FK8n1nmu1wbypp3nwtnhfuvhiup
    foreign key (tariff_calls_id)
    references tariff_call_limits;

alter table if exists tariffs
    add constraint FKasv2vsbir9wg1h4weud48prjo
    foreign key (tariff_minutes_id)
    references tariffs_minutes;