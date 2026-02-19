package kr.co.breadfeetserver.presentation.review.dto.request;

import jakarta.validation.constraints.Min;
import kr.co.breadfeetserver.presentation.annotation.CursorSize;

public record ReviewCursorCommand(
    @Min(0) Long cursorId,
    @CursorSize int size
) {}
