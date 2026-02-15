package kr.co.breadfeetserver.domain.file;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@MappedSuperclass
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class File {

    @Column
    private String fileName;

    @Column(columnDefinition = "TEXT")
    private String s3Key;

    protected File(String fileName, String s3Key) {
        this.fileName = fileName;
        this.s3Key = s3Key;
    }
}
