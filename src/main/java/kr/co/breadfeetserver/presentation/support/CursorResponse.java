package kr.co.breadfeetserver.presentation.support;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Slice;

@Getter
public class CursorResponse<T> {

    private final List<T> content;
    private final SliceInfo sliceInfo;

    private CursorResponse(final List<T> content, final SliceInfo sliceInfo) {
        this.content = (content == null) ? java.util.Collections.emptyList() : content;
        this.sliceInfo = sliceInfo;
    }

    public static <T> CursorResponse<T> of(final Slice<T> slice, final Long cursor) {
        SliceInfo pageInfo = SliceInfo.builder()
                .size(slice.getSize())
                .numberOfElements(slice.getNumberOfElements())
                .last(slice.isLast())
                .empty(slice.isEmpty())
                .cursor(cursor)
                .build();
        return new CursorResponse<>(slice.getContent(), pageInfo);
    }

    record SliceInfo(
            int size,
            int numberOfElements,
            boolean last,
            boolean empty,
            @JsonInclude(NON_NULL) Long cursor
    ) {

        @Builder
        public SliceInfo {
        }
    }
}