package kr.co.breadfeetserver.presentation.file.dto;

public record FileUploadResponse(
        String fileName,
        String publicUrl
) {

    public static FileUploadResponse of(String fileName, String publicUrl) {
        return new FileUploadResponse(fileName, publicUrl);
    }
}
