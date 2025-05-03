-- create uuids

alter table file
    add column if not exists id_uuid uuid default gen_random_uuid();

alter table folder
    add column if not exists id_uuid uuid default gen_random_uuid();

alter table file
    add column if not exists folder_id_uuid uuid default gen_random_uuid();

alter table folder
    add column if not exists folder_id_uuid uuid default gen_random_uuid();

-- link new ids using old ids

update file fil
set folder_id_uuid = (select fol.id_uuid from folder fol where fil.folder_id = fol.id);

update folder fol0
set folder_id_uuid = (select fol1.id_uuid from folder fol1 where fol0.folder_id = fol1.id);

-- drop folder's old ids and set new ids up

alter table file
    drop constraint fkdfgd9qovcgebjry9mynttnijc;

alter table folder
    drop constraint fk1mamgf3edma7c1nqdb2evscy7;

alter table folder
    drop constraint folder_pkey;

alter table folder
    add primary key (id_uuid);

alter table folder
    add foreign key (folder_id_uuid)
        references folder (id_uuid);

-- drop file's old ids and set new ids up

alter table file
    drop constraint file_pkey;

alter table file
    add primary key (id_uuid);

alter table file
    add foreign key (folder_id_uuid)
        references folder (id_uuid);

-- drop old ids

alter table file
    drop column id;

alter table file
    drop column folder_id;

alter table file
    rename id_uuid to id;

alter table folder
    drop column id;

alter table folder
    drop column folder_id;

alter table folder
    rename id_uuid to id;
