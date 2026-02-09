package kr.co.breadfeetserver.presentation.bookmark;

import kr.co.breadfeetserver.application.bookmark.BookmarkService;
import kr.co.breadfeetserver.infra.util.ApiResponseWrapper;
import kr.co.breadfeetserver.presentation.annotation.Memberid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/bookmarks")
public class BookmarkQueryController {

    private final BookmarkService bookmarkService;

    @GetMapping("/{bakeryId}")
    public ResponseEntity<ApiResponseWrapper<Boolean>> isBookmarked(
            @Memberid Long memberId,
            @PathVariable Long bakeryId
    ) {
        Boolean isBookmarked = bookmarkService.isBookmarked(bakeryId, memberId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponseWrapper.success(HttpStatus.OK, "북마크 가능 여부 성공", isBookmarked));
    }
}
