package kr.co.breadfeetserver.presentation.file;

import kr.co.breadfeetserver.domain.file.Folder;
import kr.co.breadfeetserver.infra.exception.BreadFeetBusinessException;
import kr.co.breadfeetserver.infra.exception.ErrorCode;
import kr.co.breadfeetserver.infra.persistence.s3.S3Service;
import kr.co.breadfeetserver.infra.util.ApiResponseWrapper;
import kr.co.breadfeetserver.presentation.file.dto.FileUploadResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/s3")
public class FileController {

    private final S3Service s3Service;

    @PostMapping
    public ApiResponseWrapper<FileUploadResponse> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam(defaultValue = "temp") String folderName
    ) {

        if (file.isEmpty()) {
            throw new BreadFeetBusinessException(ErrorCode.NOTFOUND_FILE_NAME);
        }

        Folder folder = Folder.validate(folderName);

        FileUploadResponse fileResponse = s3Service.uploadFile(file, folder);
        return ApiResponseWrapper.success(HttpStatus.CREATED, "파일 업로드 성공", fileResponse);
    }
}
