create table if not exists company_order
(
    id                 bigserial
        primary key,
    delivery_date_time timestamp(6),
    is_deleted         boolean,
    status             varchar(255)
);



create table if not exists employee_order
(
    id                        bigserial
        primary key,
    final_price               double precision,
    is_deleted                boolean,
    status                    varchar(255),
    supplier_default_price    double precision,
    supplier_discounted_price double precision,
    company_order_id          bigint
        constraint fkqvscldfr79p1b5rmw1jxrrms
            references company_order
);



create table if not exists notification_config
(
    id         bigserial
        primary key,
    amount     integer not null,
    interval   numeric(21),
    start_time time
);



create table if not exists employee_notification_config
(
    id          bigint not null
        primary key
        constraint fkbnhrjhxpfcq1kukapb7sntg0f
            references notification_config,
    employee_id bigint not null
);



create table if not exists organization
(
    id          bigserial
        primary key,
    description varchar(255),
    email       varchar(255)          not null
        constraint uk_t4bamosb7eleheafmlcalbhjf
            unique,
    is_deleted  boolean default false not null,
    logo        oid,
    name        varchar(255)          not null
        constraint uk_8j5y8ipk73yx2joy9yr653c9t
            unique,
    phone       varchar(255)          not null
);



create table if not exists app_user
(
    id                      bigserial
        primary key,
    organization_id         bigint
        constraint fkfl8n74ioc02b9cpjqdlpi0m9f
            references organization,
    organization_request_id bigint
        constraint fkghwdwdvb4fi9r6x8n6ttj37ud
            references organization
);



create table if not exists app_user_details
(
    id                 bigserial
        primary key,
    email              varchar(255)          not null
        constraint uk_kue4yx16w26e885ycs76ithak
            unique,
    first_name         varchar(255)          not null,
    is_enabled         boolean default false not null,
    last_name          varchar(255)          not null,
    password           varchar(255)          not null,
    registration_token varchar(255),
    app_user_id        bigint
        constraint fk2nqeenb7cxoc0v6nxacqut56e
            references app_user
);



create table if not exists company
(
    id bigint not null
        primary key
        constraint fk7ro8b2esd1xq6ro8xhx6m6hcf
            references organization
);



create table if not exists company_preference_config
(
    id                    bigserial
        primary key,
    company_discount_type varchar(255),
    delivery_address      varchar(255) not null,
    company_id            bigint       not null
        constraint uk_1cqh1dlrltxd6lt1u3w6rrevw
            unique
        constraint fk30bibp91mk013p1e014iocci6
            references company
);



create table if not exists company_notification_config
(
    id                   bigint not null
        primary key
        constraint fkeoncf5wuvv9egx47ske47lddo
            references notification_config,
    preference_config_id bigint not null
        constraint fk6a682ylp51a8e8itjwy7txnj9
            references company_preference_config
);



create table if not exists employee_preference_config
(
    id                     bigserial
        primary key,
    employee_id            bigint not null
        constraint fk94nbs3cr26nm0m1wxdbcdv0hf
            references app_user,
    notification_config_id bigint not null
        constraint uk_gbxpqp9vu314hqu3ltccvumqr
            unique
        constraint fk7npv31e6qty94a4ilk0ha6q2l
            references employee_notification_config
);



alter table employee_notification_config
    add constraint fkrf8rn345xoqr6d6c15005subp
        foreign key (employee_id) references employee_preference_config;

create table if not exists supplier
(
    menu_url    bytea  not null,
    website_url bytea  not null,
    id          bigint not null
        primary key
        constraint fkds7b6bobtxgfomiiojrlo5a4a
            references organization
);



create table if not exists lunch_subscriptions
(
    supplier_id bigint not null
        constraint fkndkh1xxgivsg1h8j1vtsyin18
            references supplier,
    company_id  bigint not null
        constraint fkg9a7l1qr5qi0stvy4nmj37ab2
            references company,
    primary key (supplier_id, company_id)
);



create table if not exists lunch_subscriptions_requests
(
    supplier_id bigint not null
        constraint fk6j83mbsnkc6qaebymtf5772bg
            references supplier,
    company_id  bigint not null
        constraint fk8rr1yxyu7hbgughlj70sigjqq
            references company,
    primary key (supplier_id, company_id)
);



create table if not exists menu
(
    id          bigserial
        primary key,
    supplier_id bigint not null
        constraint uk_6irs3dy5w1xjl99e53q3k8it7
            unique
        constraint fksmno5l8x78u3ce3stlg8ukgt5
            references supplier
);



create table if not exists category
(
    id         bigserial
        primary key,
    is_deleted boolean default false,
    name       varchar(255),
    menu_id    bigint not null
        constraint fk7ld4ysop2r15rbwxiue1ko5eb
            references menu
);



create table if not exists option
(
    id          bigserial
        primary key,
    is_deleted  boolean,
    category_id bigint not null
        constraint fk9mlor7lii2a564hilwr233cb0
            references category
);



create table if not exists employees_orders_options
(
    employee_order_id bigint not null
        constraint fktla2ewblx6xhxahtdhicrigff
            references employee_order,
    option_id         bigint not null
        constraint fkef48um2eyrx05k4e7p2kx3ah7
            references option,
    primary key (employee_order_id, option_id)
);



create table if not exists option_description
(
    id         bigserial
        primary key,
    is_deleted boolean,
    value      varchar(255) not null,
    option_id  bigint       not null
        constraint fksx8gr8mv4ouu9deg3hbf9fd73
            references option
);



create table if not exists supplier_preference_config
(
    id                         bigserial
        primary key,
    delivery_period_end_time   time         not null,
    delivery_period_start_time time         not null,
    minimum_orders_per_request integer default 1,
    order_type                 varchar(255) not null,
    request_offset             numeric(21)  not null,
    supplier_id                bigint       not null
        constraint uk_hirs7jowcnvx9my6k83qwr5m7
            unique
        constraint fkrj7796qkn1qpn1fk6yqj1g5ij
            references supplier
);



create table if not exists order_capacity
(
    id                      bigserial
        primary key,
    duration                numeric(21),
    employees_orders_amount integer not null,
    preference_config_id    bigint  not null
        constraint uk_7k2bkvwl6ccosmu9i1614oo1l
            unique
        constraint fkggwoy12x6x4h2h047gsq4qdou
            references supplier_preference_config
);



create table if not exists price_by
(
    id                   bigserial
        primary key,
    preference_config_id bigint not null
        constraint fkfyve89jq91tvv78njc23scat0
            references supplier_preference_config
);



create table if not exists price_by_category
(
    amount integer          not null,
    price  double precision not null,
    id     bigint           not null
        primary key
        constraint fk2eb9rdxi2ddovyu6hk20a0ey3
            references price_by
);



create table if not exists price_by_option
(
    price     double precision not null,
    id        bigint           not null
        primary key
        constraint fktcsup5nmb00ra8j4afv2n51nv
            references price_by,
    option_id bigint           not null
        constraint uk_hptx3yl1j77xchwmmb3unka05
            unique
        constraint fkiqiosi81royj5gev7dftlq2ts
            references option
);



create table if not exists user_roles
(
    user_id bigint       not null
        constraint fk73vkp271vqj93y1gwnwnl7eo
            references app_user_details,
    role    varchar(255) not null,
    primary key (user_id, role)
);


