# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table game (
  id                        bigint not null,
  playground_name           varchar(255),
  constraint pk_game primary key (id))
;

create table path (
  id                        bigint not null,
  from_point_id             bigint,
  to_point_id               bigint,
  constraint pk_path primary key (id))
;

create table player (
  id                        bigint not null,
  name                      varchar(255),
  constraint pk_player primary key (id))
;

create table player_game_status (
  id                        bigint not null,
  constraint pk_player_game_status primary key (id))
;

create table playground (
  name                      varchar(255) not null,
  constraint pk_playground primary key (name))
;

create table point (
  id                        bigint not null,
  x                         integer,
  y                         integer,
  city                      boolean,
  constraint pk_point primary key (id))
;


create table game_player_game_status (
  game_id                        bigint not null,
  player_game_status_id          bigint not null,
  constraint pk_game_player_game_status primary key (game_id, player_game_status_id))
;

create table playground_path (
  playground_name                varchar(255) not null,
  path_id                        bigint not null,
  constraint pk_playground_path primary key (playground_name, path_id))
;
create sequence game_seq;

create sequence path_seq;

create sequence player_seq;

create sequence player_game_status_seq;

create sequence playground_seq;

create sequence point_seq;

alter table game add constraint fk_game_playground_1 foreign key (playground_name) references playground (name) on delete restrict on update restrict;
create index ix_game_playground_1 on game (playground_name);
alter table path add constraint fk_path_fromPoint_2 foreign key (from_point_id) references point (id) on delete restrict on update restrict;
create index ix_path_fromPoint_2 on path (from_point_id);
alter table path add constraint fk_path_toPoint_3 foreign key (to_point_id) references point (id) on delete restrict on update restrict;
create index ix_path_toPoint_3 on path (to_point_id);



alter table game_player_game_status add constraint fk_game_player_game_status_ga_01 foreign key (game_id) references game (id) on delete restrict on update restrict;

alter table game_player_game_status add constraint fk_game_player_game_status_pl_02 foreign key (player_game_status_id) references player_game_status (id) on delete restrict on update restrict;

alter table playground_path add constraint fk_playground_path_playground_01 foreign key (playground_name) references playground (name) on delete restrict on update restrict;

alter table playground_path add constraint fk_playground_path_path_02 foreign key (path_id) references path (id) on delete restrict on update restrict;

# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists game;

drop table if exists game_player_game_status;

drop table if exists path;

drop table if exists player;

drop table if exists player_game_status;

drop table if exists playground;

drop table if exists playground_path;

drop table if exists point;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists game_seq;

drop sequence if exists path_seq;

drop sequence if exists player_seq;

drop sequence if exists player_game_status_seq;

drop sequence if exists playground_seq;

drop sequence if exists point_seq;

