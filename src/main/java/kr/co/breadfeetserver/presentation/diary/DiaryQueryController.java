package kr.co.breadfeetserver.presentation.diary;

import kr.co.breadfeetserver.application.diary.DiaryQueryService;
import kr.co.breadfeetserver.infra.util.ApiResponseWrapper;
import kr.co.breadfeetserver.presentation.annotation.Memberid;
import kr.co.breadfeetserver.presentation.diary.dto.request.DiaryCursorCommand;
import kr.co.breadfeetserver.presentation.diary.dto.response.DiaryListResponse;
import kr.co.breadfeetserver.presentation.diary.dto.response.DiaryResponse;
import kr.co.breadfeetserver.presentation.support.CursorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/diaries")
public class DiaryQueryController {

    private final DiaryQueryService diaryQueryService;

    @GetMapping
    public ResponseEntity<ApiResponseWrapper<CursorResponse<DiaryListResponse>>> getDiaryList(
            @RequestParam Long cursor,
            @RequestParam(defaultValue = "10") int size,
            @Memberid(required = false) Long memberId
    ) {
        DiaryCursorCommand command = new DiaryCursorCommand(cursor, size);

        CursorResponse<DiaryListResponse> response = diaryQueryService.getDiaryList(command);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponseWrapper.success(HttpStatus.OK, "다이어리 목록 조회 성공",
                        response));
    }

    @GetMapping("/{diaryId}")
    public ResponseEntity<ApiResponseWrapper<DiaryResponse>> getDiary(
            @PathVariable Long diaryId,
            @Memberid(required = false) Long memberId) {
        DiaryResponse response = diaryQueryService.getDiary(diaryId, memberId);

        return ResponseEntity.ok(ApiResponseWrapper.success(HttpStatus.OK, response));
    }
}
