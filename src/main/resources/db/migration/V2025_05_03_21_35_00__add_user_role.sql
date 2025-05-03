alter table drive_user
    add column if not exists role varchar(255) default 'ROLE_USER';

alter table drive_user
    alter column role
        drop default;
