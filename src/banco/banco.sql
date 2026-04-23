create database if not exists db_supermercado;
use db_supermercado;

create table if not exists usuarios (
    id int auto_increment primary key,
    nome varchar(50) not null,
    cpf varchar(14) unique not null,
    perfil varchar(20) not null
);

create table if not exists produtos (
    id int auto_increment primary key,
    nome varchar(50) not null,
    preco decimal(10, 2) not null,
    estoque int not null default 0
);
