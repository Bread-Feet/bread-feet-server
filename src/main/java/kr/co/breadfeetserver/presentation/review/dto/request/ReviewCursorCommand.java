package kr.co.breadfeetserver.presentation.review.dto.request;

public record ReviewCursorCommand(
    Long cursorId,
    int size
) {}
