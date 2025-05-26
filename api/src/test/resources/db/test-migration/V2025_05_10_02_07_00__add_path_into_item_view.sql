create or replace view item_view as
select
    item.*
from (
         select
             fil.id as id,
             fil.file_name as name,
             'FILE' as type,
             fil.size as size,
             fil.folder_id as folder_id,
             fil.user_id as author_id,
             fil.created_at as created_at,
             fil.updated_at as updated_at,
             fil.s3_file_path as path,
             fil.file_type as file_type
         from file fil
         union
         select
             fol.id as id,
             fol.name as name,
             'FOLDER' as type,
             null as size,
             fol.folder_id as folder_id,
             fol.user_id as author_id,
             fol.created_at as created_at,
             fol.updated_at as updated_at,
             null as path,
             null as file_type
         from folder fol
     ) item;
