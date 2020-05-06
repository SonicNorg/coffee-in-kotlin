CREATE TYPE buyin_status AS ENUM ('PLANNING', 'OPEN', 'AWAITING', 'FIXED', 'SHIPPING', 'FINISHED', 'CANCELLED');

CREATE TYPE user_role AS ENUM ('GUEST', 'VERIFIED', 'MODERATOR', 'ADMIN', 'OWNER');

CREATE TABLE bot_user (
   id bigint NOT NULL PRIMARY KEY,
   username character varying(250),
   phone character varying(12),
   first_name character varying(40),
   last_name character varying(60),
   address character varying(500),
   not_blocked boolean NOT NULL DEFAULT TRUE
);

CREATE TABLE assigned_role(
    id SERIAL NOT NULL PRIMARY KEY,
    user_id bigint REFERENCES bot_user NOT NULL,
    user_role user_role
);

CREATE TABLE coffee_sort(
    id SERIAL NOT NULL PRIMARY KEY,
    name character varying(100) NOT NULL,
    description character varying(250) NOT NULL
);

CREATE TABLE price_list(
    id SERIAL NOT NULL PRIMARY KEY,
    date_from date,
    date_to date
);

CREATE TABLE price_row(
    id BIGSERIAL NOT NULL PRIMARY KEY,
    price_id integer REFERENCES price_list NOT NULL,
    coffee_sort_id integer REFERENCES coffee_sort NOT NULL,
    price_25 float NOT NULL,
    price_50 float NOT NULL
);

CREATE UNIQUE INDEX single_coffee_in_price ON price_row (price_id, coffee_sort_id);

CREATE TABLE buyin(
    id SERIAL NOT NULL PRIMARY KEY,
    status buyin_status NOT NULL,
    created_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    next_status_planned date,
    final_price integer REFERENCES price_list,
    shipment float NOT NULL
);

CREATE TABLE buyin_row(
    id BIGSERIAL NOT NULL PRIMARY KEY,
    buyin_id integer REFERENCES buyin NOT NULL,
    user_id bigint REFERENCES bot_user NOT NULL,
    coffee_sort_id integer REFERENCES coffee_sort NOT NULL,
    amount float NOT NULL
);

CREATE UNIQUE INDEX single_coffee_user_in_buyin ON buyin_row (buyin_id, user_id, coffee_sort_id);

CREATE TABLE xref_users_buyins_roles(
    id BIGSERIAL NOT NULL PRIMARY KEY,
    user_id bigint REFERENCES bot_user NOT NULL,
    buyin_id bigint REFERENCES buyin NOT NULL,
    user_role user_role
);