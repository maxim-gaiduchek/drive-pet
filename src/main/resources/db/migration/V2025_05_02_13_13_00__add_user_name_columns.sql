alter table drive_user
    add column if not exists first_name varchar(255);

alter table drive_user
    add column if not exists last_name varchar(255);

update drive_user
set first_name = username
where drive_user.first_name is null;

update drive_user
set last_name = username
where drive_user.last_name is null;

alter table drive_user
    alter column first_name
        set not null;

alter table drive_user
    alter column last_name
        set not null;
