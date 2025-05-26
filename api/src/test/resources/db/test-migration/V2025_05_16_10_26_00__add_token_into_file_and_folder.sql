alter table file
    add if not exists access_token varchar(255);

alter table file
    add if not exists access_token_created_at timestamp with time zone;

alter table folder
    add if not exists access_token varchar(255);

alter table folder
    add if not exists access_token_created_at timestamp with time zone;
