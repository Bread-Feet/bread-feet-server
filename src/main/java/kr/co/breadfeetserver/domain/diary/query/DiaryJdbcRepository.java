package kr.co.breadfeetserver.domain.diary.query;

import kr.co.breadfeetserver.presentation.diary.dto.request.DiaryCursorCommand;
import kr.co.breadfeetserver.presentation.diary.dto.response.DiaryListResponse;
import org.springframework.data.domain.Slice;

public interface DiaryJdbcRepository {
    Slice<DiaryListResponse> findAll(DiaryCursorCommand command);
}
