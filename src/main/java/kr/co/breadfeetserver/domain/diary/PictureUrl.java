package kr.co.breadfeetserver.domain.diary;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Table(name = "hashtag")
@NoArgsConstructor
public class PictureUrl {
    @Id
    private Long id;

    @Column(nullable = false)
    private String pic_url;

    @Column(nullable = false)
    private Long diaryId;
}