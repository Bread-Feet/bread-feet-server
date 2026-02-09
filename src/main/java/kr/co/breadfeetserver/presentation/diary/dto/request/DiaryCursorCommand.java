package kr.co.breadfeetserver.presentation.diary.dto.request;

import jakarta.validation.constraints.Min;
import kr.co.breadfeetserver.presentation.annotation.CursorSize;

public record DiaryCursorCommand(
        @Min(0)
        Long cursor,
        @CursorSize
        int size
) {
}