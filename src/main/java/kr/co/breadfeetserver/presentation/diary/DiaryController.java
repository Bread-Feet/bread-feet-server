package kr.co.breadfeetserver.presentation.diary;

import kr.co.breadfeetserver.application.diary.DiaryService;
import kr.co.breadfeetserver.infra.util.ApiResponseWrapper;
import kr.co.breadfeetserver.presentation.annotation.Memberid;
import kr.co.breadfeetserver.presentation.diary.dto.request.DiaryCreateRequest;
import kr.co.breadfeetserver.presentation.diary.dto.request.DiaryUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/diaries")
@RequiredArgsConstructor
public class DiaryController {

    private final DiaryService diaryService;

    @PostMapping
    public ResponseEntity<ApiResponseWrapper<Long>> createDiary(
            @Memberid Long memberId,
            @RequestBody DiaryCreateRequest request) {
        Long diaryId = diaryService.createDiary(memberId, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponseWrapper.success(HttpStatus.CREATED, diaryId));
    }

    @PutMapping("/{diaryId}")
    public ResponseEntity<ApiResponseWrapper<Void>> updateDiary(
            @Memberid Long memberId,
            @PathVariable Long diaryId,
            @RequestBody DiaryUpdateRequest request) {
        diaryService.updateDiary(memberId, diaryId, request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponseWrapper.success(HttpStatus.OK, "다이어리 수정 성공"));
    }

    @DeleteMapping("/{diaryId}")
    public ResponseEntity<ApiResponseWrapper<Void>> deleteDiary(
            @Memberid Long memberId,
            @PathVariable Long diaryId) {
        diaryService.deleteDiary(memberId, diaryId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponseWrapper.success(HttpStatus.OK, "다이어리 삭제 성공"));
    }
}
