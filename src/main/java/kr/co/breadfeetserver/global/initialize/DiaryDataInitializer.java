package kr.co.breadfeetserver.global.initialize;

import java.time.LocalDateTime;
import java.util.List;
import kr.co.breadfeetserver.domain.bakery.AddressJpaVO;
import kr.co.breadfeetserver.domain.diary.Diary;
import kr.co.breadfeetserver.domain.diary.DiaryJpaRepository;
import kr.co.breadfeetserver.domain.diary.Hashtag;
import kr.co.breadfeetserver.domain.diary.HashtagJpaRepository;
import kr.co.breadfeetserver.domain.diary.PictureUrl;
import kr.co.breadfeetserver.domain.diary.PictureUrlJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class DiaryDataInitializer implements ApplicationRunner {

    private final DiaryJpaRepository diaryJpaRepository;
    private final HashtagJpaRepository hashtagJpaRepository;
    private final PictureUrlJpaRepository pictureUrlJpaRepository;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (diaryJpaRepository.count() > 0) {
            log.info("[DiaryDataInitializer] 이미 다이어리 데이터가 존재하여 초기화를 건너뜁니다.");
            return;
        }

        // 1. 다이어리 데이터 생성
        List<Diary> diaries = List.of(
                Diary.builder()
                        .title("성수동 빵지순례")
                        .address(AddressJpaVO.builder()
                                .detail("1층")
                                .lotNumber("서울 강남구 청담동 84-4")
                                .roadAddress("서울 강남구 압구정로 416")
                                .build())
                        .thumbnailUrl("https://images.unsplash.com/photo-1509440159596-0249088772ff")
                        .isPublic(true)
                        .visitDate(LocalDateTime.now().minusDays(2))
                        .content("식빵이 정말 쫄깃하고 맛있어요. 다음에도 또 오고 싶네요!")
                        .memberId(1L)
                        .bakeryId(1L)
                        .build(),

                Diary.builder()
                        .title("인생 바게트를 만나다")
                        .address(AddressJpaVO.builder()
                                .detail("1층")
                                .lotNumber("서울 마포구 서교동 396-31")
                                .roadAddress("서울 마포구 와우산로 21")
                                .build())
                        .thumbnailUrl("https://images.unsplash.com/photo-1589367920969-ab8e050bcc04")
                        .isPublic(true)
                        .visitDate(LocalDateTime.now().minusDays(5))
                        .content("바게트 겉바속촉의 정석입니다. 무화과 잠봉뵈르 꼭 드셔보세요.")
                        .memberId(1L)
                        .bakeryId(3L)
                        .build(),

                Diary.builder()
                        .title("주말 아침 크루아상 한 잔")
                        .address(AddressJpaVO.builder()
                                .detail("지하 1층")
                                .lotNumber("서울 성동구 성수동2가 289-10")
                                .roadAddress("서울 성동구 연무장5가길 7")
                                .build())
                        .thumbnailUrl("https://images.unsplash.com/photo-1555507036-ab1f4038808a")
                        .isPublic(true)
                        .visitDate(LocalDateTime.now().minusDays(1))
                        .content("크루아상 결이 살아있어요. 커피랑 찰떡궁합입니다.")
                        .memberId(1L)
                        .bakeryId(6L)
                        .build()
        );

        diaryJpaRepository.saveAll(diaries);

        // 2. 해시태그 및 사진 데이터 생성
        diaries.forEach(diary -> {
            Long diaryId = diary.getId();
            
            // 해시태그 추가
            hashtagJpaRepository.saveAll(List.of(
                    Hashtag.builder().name("빵지순례").diaryId(diaryId).build(),
                    Hashtag.builder().name("맛있음").diaryId(diaryId).build(),
                    Hashtag.builder().name("추천").diaryId(diaryId).build()
            ));

            // 사진 URL 추가
            pictureUrlJpaRepository.saveAll(List.of(
                    PictureUrl.builder().pic_url(diary.getThumbnailUrl()).diaryId(diaryId).build(),
                    PictureUrl.builder().pic_url("https://images.unsplash.com/photo-1517433367423-c7e5b0f35086").diaryId(diaryId).build()
            ));
        });

        log.info("[DiaryDataInitializer] 다이어리 mock 데이터 {}건 및 관련 데이터 초기화 완료", diaries.size());
    }
}
