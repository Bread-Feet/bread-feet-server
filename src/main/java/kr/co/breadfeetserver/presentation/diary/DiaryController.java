package kr.co.breadfeetserver.presentation.diary;

import kr.co.breadfeetserver.application.diary.DiaryQuaryService;
import kr.co.breadfeetserver.application.diary.DiaryService;
import kr.co.breadfeetserver.infra.util.ApiResponseWrapper;
import kr.co.breadfeetserver.presentation.bakery.dto.request.BakeryUpdateRequest;
import kr.co.breadfeetserver.presentation.diary.dto.request.DiaryCreateRequest;
import kr.co.breadfeetserver.presentation.diary.dto.response.DiaryResponse;
import kr.co.breadfeetserver.presentation.diary.dto.request.DiaryUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/diaries")
@RequiredArgsConstructor
public class DiaryController {

    private final DiaryService diaryService;
    private final DiaryQuaryService diaryQueryService;

    @PostMapping
    public ResponseEntity<ApiResponseWrapper<Long>> createDiary(
            @RequestParam long memberId,
            @RequestBody DiaryCreateRequest request) {
        Long diaryId = diaryService.createDiary(memberId, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponseWrapper.success(HttpStatus.CREATED, diaryId));
    }

    @PutMapping("/{diaryId}")
    public ResponseEntity<ApiResponseWrapper<Void>> updateDiary(
            @RequestParam long memberId,
            @PathVariable Long diaryId,
            @RequestBody DiaryUpdateRequest request) {
        diaryService.updateDiary(memberId, diaryId, request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponseWrapper.success(HttpStatus.OK, "다이어리 수정 성공"));
    }

    @GetMapping("/{diaryId}")
    public ResponseEntity<ApiResponseWrapper<DiaryResponse>> getDiary(
            @PathVariable Long diaryId,
            @RequestParam long memberId) {
        DiaryResponse response = diaryQueryService.getDiary(diaryId, memberId);

        return ResponseEntity.ok(ApiResponseWrapper.success(HttpStatus.OK, response));
    }

    @DeleteMapping("/{diaryId}")
    public ResponseEntity<ApiResponseWrapper<Void>> deleteDiary(
            @RequestParam long memberId,
            @PathVariable Long diaryId) {
        diaryService.deleteDiary(memberId, diaryId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponseWrapper.success(HttpStatus.OK, "다이어리 삭제 성공"));
    }
}
