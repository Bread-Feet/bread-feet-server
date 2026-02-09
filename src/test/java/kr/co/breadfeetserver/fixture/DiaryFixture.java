package kr.co.breadfeetserver.fixture;

import kr.co.breadfeetserver.domain.diary.Diary;
import kr.co.breadfeetserver.presentation.diary.dto.request.DiaryCreateRequest;
import kr.co.breadfeetserver.presentation.diary.dto.request.DiaryUpdateRequest;

import java.time.LocalDateTime;
import java.util.List;

import static kr.co.breadfeetserver.fixture.AddressFixture.address;
import static kr.co.breadfeetserver.fixture.AddressFixture.addressCreateRequest;
import static kr.co.breadfeetserver.fixture.AddressFixture.addressUpdateRequest;

public class DiaryFixture {

    public static String thumbnailUrl = "https://example.com/diary.jpg";
    // public static int score = 5; // score field is not in Diary entity
    public static boolean isPublic = true;
    public static LocalDateTime visitDate = LocalDateTime.now();
    public static String content = "오늘도 맛있는 빵을 먹었다.";

    public static Diary aDiary(Long id) {
        return Diary.builder()
                .id(id)
                .address(address())
                .thumbnailUrl(thumbnailUrl)
                // .score(score) // score field is not in Diary entity
                .isPublic(isPublic)
                .visitDate(visitDate)
                .content(content)
                .build();
    }

    public static DiaryCreateRequest aDiaryCreateRequest(List<String> hashtags, List<String> pictureUrls) {
        return new DiaryCreateRequest(
                isPublic,
                5, // Assuming a default score for tests
                address(),
                thumbnailUrl,
                visitDate,
                content,
                hashtags,
                pictureUrls
        );
    }

    public static DiaryUpdateRequest aDiaryUpdateRequest(Long diaryId) {
        return new DiaryUpdateRequest(
                diaryId,
                isPublic,
                5, // Assuming a default score for tests
                addressUpdateRequest(),
                thumbnailUrl,
                visitDate,
                content,
                List.of(), // Placeholder for hashtags
                List.of()  // Placeholder for pictureUrls
        );
    }
}
