package kr.co.breadfeetserver.domain.diary;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Table(name = "pictureUrl")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class PictureUrl {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String pic_url;

    @Column(nullable = false)
    private Long diaryId;
}