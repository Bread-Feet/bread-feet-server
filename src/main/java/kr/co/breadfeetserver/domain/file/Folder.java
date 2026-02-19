package kr.co.breadfeetserver.domain.file;

import kr.co.breadfeetserver.infra.exception.BreadFeetBusinessException;
import kr.co.breadfeetserver.infra.exception.ErrorCode;
import lombok.Getter;

@Getter
public enum Folder {
    BAKERY("bakery"),
    REVIEW("review"),
    DIARY("diary");

    private final String path;

    Folder(String path) {
        this.path = path;
    }

    public static Folder validate(String folderName) {
        for (Folder folder : Folder.values()) {
            if (folder.name().equals(folderName)) {
                return folder;
            }
        }
        throw new BreadFeetBusinessException(ErrorCode.WRONG_FOLDER_TYPE);
    }
}
