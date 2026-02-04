package kr.co.breadfeetserver.application.support;

import java.util.List;
import kr.co.breadfeetserver.global.dto.Cursorable;
import kr.co.breadfeetserver.presentation.support.CursorResponse;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

@Component
public class CursorService {

    public <T extends Cursorable> CursorResponse<T> getCursorResponse(Slice<T> slice) {
        final List<T> content = slice.getContent();
        Long nextCursor = null;

        if (!content.isEmpty()) {
            nextCursor = content.get(content.size() - 1).getCursorId();
        }
        return CursorResponse.of(slice, nextCursor);
    }
}
