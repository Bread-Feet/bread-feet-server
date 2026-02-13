package kr.co.breadfeetserver.domain.file;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import kr.co.breadfeetserver.global.base.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@MappedSuperclass
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class FileWithTime extends BaseTimeEntity {

    @Column
    private String fileName;

    @Column(columnDefinition = "TEXT")
    private String s3Key;

    protected FileWithTime(String fileName, String s3Key) {
        this.fileName = fileName;
        this.s3Key = s3Key;
    }

    protected void updateFile(String fileName, String s3Key) {
        this.fileName = fileName;
        this.s3Key = s3Key;
    }
}
