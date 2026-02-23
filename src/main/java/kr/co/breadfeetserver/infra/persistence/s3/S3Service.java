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
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
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
    private static final String TAG_KEY_STATUS = "status";
    private static final String TAG_VALUE_TEMP = "temp";
    private static final String TAG_VALUE_ACTIVE = "active";

    private final S3Client s3Client;
    private final S3Properties s3Properties;

    public FileUploadResponse uploadFile(MultipartFile file, Folder folder) {
        String s3Key = buildS3Key(folder, file.getOriginalFilename());

        try {
            s3Client.putObject(
                    buildPutRequest(s3Key, file.getContentType()),
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
                        .tagSet(List.of(Tag.builder()
                                .key(TAG_KEY_STATUS)
                                .value(TAG_VALUE_ACTIVE)
                                .build()))
                        .build())
                .build();

        s3Client.putObjectTagging(tagReq);
    }

    private String buildS3Key(Folder folder, String originalFilename) {
        String prefix = UUID.randomUUID().toString();
        String sanitizedName = sanitizeFileName(originalFilename);
        String key = folder.getPath() + "/" + prefix + "_" + sanitizedName;

        if (key.length() <= MAX_S3_KEY_LENGTH) {
            return key;
        }

        int excess = key.length() - MAX_S3_KEY_LENGTH;
        sanitizedName = sanitizedName.substring(0, sanitizedName.length() - excess);
        return folder.getPath() + "/" + prefix + "_" + sanitizedName;
    }

    private PutObjectRequest buildPutRequest(String s3Key, String contentType) {
        return PutObjectRequest.builder()
                .bucket(s3Properties.getBucketName())
                .key(s3Key)
                .acl(ObjectCannedACL.PUBLIC_READ)
                .tagging(TAG_KEY_STATUS + "=" + TAG_VALUE_TEMP)
                .contentType(contentType)
                .build();
    }

    private String toPublicUrl(String s3Key) {
        return s3Properties.getBaseUrl() + "/" + s3Key;
    }

    private String sanitizeFileName(String originalFilename) {
        String encoded = URLEncoder.encode(originalFilename, StandardCharsets.UTF_8)
                .replaceAll("\\+", "%20");

        int dotIndex = encoded.lastIndexOf('.');
        String extension = dotIndex != -1 ? encoded.substring(dotIndex) : "";
        String baseName = dotIndex != -1 ? encoded.substring(0, dotIndex) : encoded;

        if (baseName.length() > MAX_FILENAME_LENGTH) {
            baseName = baseName.substring(0, MAX_FILENAME_LENGTH);
        }

        return baseName + extension;
    }
}
