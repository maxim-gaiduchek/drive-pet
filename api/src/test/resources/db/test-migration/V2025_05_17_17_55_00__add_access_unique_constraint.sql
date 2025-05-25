alter table file
    add unique (access_token);

alter table folder
    add unique (access_token);

alter table user_to_file_access
    add unique (file_id, user_id);

alter table user_to_folder_access
    add unique (folder_id, user_id);
