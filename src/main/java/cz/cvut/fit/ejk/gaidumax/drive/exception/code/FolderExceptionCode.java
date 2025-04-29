package cz.cvut.fit.ejk.gaidumax.drive.exception.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum FolderExceptionCode implements ExceptionCode {

    FOLDER_DOES_NOT_EXIST("API-FLD-001", "Folder with id %d does not exist"),
    ;

    private final String code;
    private final String description;
}
