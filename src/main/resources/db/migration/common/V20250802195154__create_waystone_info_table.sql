create table if not exists waystone_info (
    world_uid char(36) not null,
    x int not null,
    y int not null,
    z int not null,
    name varchar(64),
    primary key (world_uid, x, y, z)
);