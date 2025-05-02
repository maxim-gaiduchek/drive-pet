alter table drive_user
    add column if not exists first_name varchar(255);

alter table drive_user
    add column if not exists last_name varchar(255);

update drive_user
set first_name = username, last_name = username;

alter table drive_user
    alter column first_name
        set not null;

alter table drive_user
    alter column last_name
        set not null;
