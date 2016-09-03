SET lc_messages TO 'en_US.UTF-8';

CREATE DATABASE phonebook;
CREATE SCHEMA phonebook;

CREATE TABLE phonebook.People (
    id_person         serial  PRIMARY KEY,
    firstname       varchar(40) NOT NULL,
    lastname        varchar(40) NOT NULL
);

CREATE TABLE phonebook.Contacts (
    _id         serial  PRIMARY KEY,
    _type       varchar(40) NOT NULL,
    _value        varchar(40) NOT NULL,
    id_person serial references phonebook.People(id_person)
);

select * from phonebook.People;