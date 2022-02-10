create table client
(
    id  bigserial not null primary key,
    name varchar(50)
);

create table address
(
    id  bigserial not null primary key,
    street varchar(150),
     client_id bigint not null references client (id)
);

create table phone
(
    id   bigserial not null primary key,
    client_id bigint not null references client (id),
    number varchar(20)
);

create index idx_address_client_id on address(client_id);
create index idx_phone_client_id on phone(client_id);