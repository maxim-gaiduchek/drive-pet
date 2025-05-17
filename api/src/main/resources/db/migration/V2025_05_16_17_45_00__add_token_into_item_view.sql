create or replace view item_view as
select item.*
from (select fil.id            as id,
             fil.file_name     as name,
             'FILE'            as type,
             fil.size          as size,
             fil.folder_id     as folder_id,
             outfia.user_id    as owner_id,
             fil.created_at    as created_at,
             fil.updated_at    as updated_at,
             fil.s3_file_path  as path,
             fil.file_type     as file_type,
             utfia.user_id     as user_id,
             utfia.access_type as user_access_type,
             fil.access_token as access_token,
             fil.access_token_created_at as access_token_created_at
      from file fil
               join user_to_file_access utfia on fil.id = utfia.file_id
               join user_to_file_access outfia on fil.id = outfia.file_id
      where outfia.access_type = 'OWNER'
      union
      select fol.id            as id,
             fol.name          as name,
             'FOLDER'          as type,
             null              as size,
             fol.folder_id     as folder_id,
             outfoa.user_id    as owner_id,
             fol.created_at    as created_at,
             fol.updated_at    as updated_at,
             null              as path,
             null              as file_type,
             utfoa.user_id     as user_id,
             utfoa.access_type as user_access_type,
             fol.access_token as access_token,
             fol.access_token_created_at as access_token_created_at
      from folder fol
               join user_to_folder_access utfoa on fol.id = utfoa.folder_id
               join user_to_folder_access outfoa on fol.id = outfoa.folder_id
      where outfoa.access_type = 'OWNER') item;
