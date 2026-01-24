package kr.co.breadfeetserver.domain.diary;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import kr.co.breadfeetserver.domain.bakery.AddressJpaVO;
import kr.co.breadfeetserver.domain.bakery.Bakery;
import kr.co.breadfeetserver.domain.member.Member;
import kr.co.breadfeetserver.global.base.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "diary")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Diary extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "diary_id")
    private Long id;

    @Embedded
    private AddressJpaVO address;

    @Column(name = "thumb_url")
    private String thumbnailUrl;

    @Column(name = "score")
    private Integer score;

    @Column(name = "ispublic")
    private Boolean isPublic;

    @Column(name = "visitDate")
    private LocalDateTime visitDate;

    @Column(name = "content")
    private String content;

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "bakery_id")
    private Long bakeryId;

    public void updateDiary(
            AddressJpaVO address,
            String thumbnailUrl,
            Integer score,
            Boolean isPublic,
            LocalDateTime visitDate,
            String content
    ) {
        this.address = address;
        this.thumbnailUrl = thumbnailUrl;
        this.score = score;
        this.isPublic = isPublic;
        this.visitDate = visitDate;
        this.content = content;
    }

}