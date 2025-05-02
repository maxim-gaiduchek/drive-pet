package cz.cvut.fit.ejk.gaidumax.drive.exception.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum FolderExceptionCode implements ExceptionCode {

    FOLDER_DOES_NOT_EXIST("API-FLD-001", "Folder with id %d does not exist"),
    FOLDER_AND_ITS_PARENT_FOLDER_MUST_NOT_BE_EQUALS("API-FLD-002", "Folder and its parent folder with id %d must not have equal ids"),
    ;

    private final String code;
    private final String description;
}
