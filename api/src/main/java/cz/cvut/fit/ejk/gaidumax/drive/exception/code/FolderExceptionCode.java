package cz.cvut.fit.ejk.gaidumax.drive.exception.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum FolderExceptionCode implements ExceptionCode {

    FOLDER_DOES_NOT_EXIST("API-FLDR-001", "Folder with id %s does not exist"),
    FOLDER_AND_ITS_PARENT_FOLDER_MUST_NOT_BE_EQUALS("API-FLDR-002", "Folder and its parent folder with id %s must not have equal ids"),
    USER_HAS_NO_ACCESS_TO_FOLDER("API-FLDR-003", "User with id %d has no access to the folder with id %s"),
    ONLY_ONE_USER_CAN_BE_OWNER("API-FLDR-004", "No other users can be owners"),
    ;

    private final String code;
    private final String description;
}
