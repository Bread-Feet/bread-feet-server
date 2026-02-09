package kr.co.breadfeetserver.presentation.bookmark;

import kr.co.breadfeetserver.application.bookmark.BookmarkService;
import kr.co.breadfeetserver.infra.util.ApiResponseWrapper;
import kr.co.breadfeetserver.presentation.annotation.Memberid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/bookmarks")
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @PostMapping("/{bakeryId}")
    public ResponseEntity<ApiResponseWrapper<Void>> bookmark(
            @Memberid Long memberId,
            @PathVariable Long bakeryId
    ) {
        bookmarkService.bookmark(memberId, bakeryId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponseWrapper.success(HttpStatus.OK, "북마크 성공"));
    }

    @DeleteMapping("/{bakeryId}")
    public ResponseEntity<ApiResponseWrapper<Void>> unbookmark(
            @Memberid Long memberId,
            @PathVariable Long bakeryId
    ) {
        bookmarkService.unbookmark(memberId, bakeryId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponseWrapper.success(HttpStatus.OK, "북마크 취소 성공"));
    }

}
