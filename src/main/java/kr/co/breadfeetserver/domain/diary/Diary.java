package kr.co.breadfeetserver.domain.diary;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import kr.co.breadfeetserver.domain.bakery.AddressJpaVO;
import kr.co.breadfeetserver.global.base.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SoftDelete;
import org.hibernate.annotations.SoftDeleteType;

@Getter
@Entity
@Table(name = "diary")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@SoftDelete(columnName = "deleted_at", strategy = SoftDeleteType.TIMESTAMP)
public class Diary extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "diary_id")
    private Long id;

    @Embedded
    private AddressJpaVO address;

    @Column(name = "thumb_url")
    private String thumbnailUrl;

    @Column(name = "ispublic")
    private Boolean isPublic;

    @Column(name = "visitDate")
    private LocalDateTime visitDate;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "bakery_id")
    private Long bakeryId;

    @Column(name = "drawing_data", columnDefinition = "LONGTEXT")
    private String drawingData;

    public void updateDiary(
            AddressJpaVO address,
            String thumbnailUrl,
            Boolean isPublic,
            LocalDateTime visitDate,
            String title,
            String content,
            Long bakeryId,
            String drawingData
    ) {
        this.address = address;
        this.thumbnailUrl = thumbnailUrl;
        this.isPublic = isPublic;
        this.visitDate = visitDate;
        this.title = title;
        this.content = content;
        this.bakeryId = bakeryId;
        this.drawingData = drawingData;
    }
}