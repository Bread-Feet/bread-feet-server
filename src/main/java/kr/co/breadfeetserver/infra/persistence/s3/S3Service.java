package kr.co.breadfeetserver.infra.persistence.s3;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;
import kr.co.breadfeetserver.domain.file.Folder;
import kr.co.breadfeetserver.infra.persistence.s3.property.S3Properties;
import kr.co.breadfeetserver.presentation.file.dto.FileUploadResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectTaggingRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.model.Tag;
import software.amazon.awssdk.services.s3.model.Tagging;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {

    private static final int MAX_FILENAME_LENGTH = 150;
    private static final int MAX_S3_KEY_LENGTH = 255;

    private final S3Client s3Client;
    private final S3Properties s3Properties;

    public FileUploadResponse uploadFile(MultipartFile file, Folder folder) {
        String sanitizedName = sanitizeFileName(file.getOriginalFilename());
        String randomPrefix = UUID.randomUUID().toString();
        String s3Key = folder.getPath() + "/" + randomPrefix + "_" + sanitizedName;

        if (s3Key.length() > MAX_S3_KEY_LENGTH) {
            int excess = s3Key.length() - MAX_S3_KEY_LENGTH;
            sanitizedName = sanitizedName.substring(0, sanitizedName.length() - excess);
            s3Key = folder.getPath() + "/" + randomPrefix + "_" + sanitizedName;
        }

        try {
            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(s3Properties.getBucketName())
                            .key(s3Key)
                            .tagging("status=temp")
                            .contentType(file.getContentType())
                            .build(),
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize())
            );
        } catch (IOException e) {
            throw new RuntimeException("파일 스트림을 읽는 중 오류 발생", e);
        } catch (S3Exception e) {
            throw new RuntimeException("S3 업로드 실패", e);
        }

        return FileUploadResponse.of(file.getOriginalFilename(), toPublicUrl(s3Key));
    }

    public void updateFileTagToActive(String key) {
        PutObjectTaggingRequest tagReq = PutObjectTaggingRequest.builder()
                .bucket(s3Properties.getBucketName())
                .key(key)
                .tagging(Tagging.builder()
                        .tagSet(List.of(Tag.builder().key("status").value("active").build()))
                        .build())
                .build();

        s3Client.putObjectTagging(tagReq);
    }

    public String toPublicUrl(String s3Key) {
        return s3Properties.getBaseUrl() + "/" + s3Key;
    }

    private String sanitizeFileName(String originalFilename) {

        String encoded = URLEncoder.encode(originalFilename, StandardCharsets.UTF_8)
                .replaceAll("\\+", "%20");

        // 파일명만 잘라내기 (확장자 유지)
        String extension = "";
        int dotIndex = encoded.lastIndexOf('.');
        if (dotIndex != -1) {
            extension = encoded.substring(dotIndex);
            encoded = encoded.substring(0, dotIndex);
        }

        if (encoded.length() > MAX_FILENAME_LENGTH) {
            encoded = encoded.substring(0, MAX_FILENAME_LENGTH);
        }

        return encoded;
    }
}
