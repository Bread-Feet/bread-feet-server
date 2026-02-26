package kr.co.breadfeetserver.presentation.diary.dto.request;

import java.time.LocalDateTime;
import java.util.List;
import kr.co.breadfeetserver.presentation.annotation.IsEssential;
import kr.co.breadfeetserver.presentation.bakery.dto.request.AddressUpdateRequest;

public record DiaryUpdateRequest(
        @IsEssential Boolean isPublic,
        @IsEssential AddressUpdateRequest address,
        @IsEssential String thumbnail,
        @IsEssential String title,
        @IsEssential Long bakeryId,
        LocalDateTime visitDate,
        String content,
        List<String> hashtags,
        List<String> pictureUrls
) {

}
