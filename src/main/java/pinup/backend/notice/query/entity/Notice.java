package pinup.backend.notice.query.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notice {
    @Id
    @Column(name = "notice_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer noticeId;

    /*
    * Admin entity 클래스 선언 필요함
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "admin_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_notice_admin_id")
    )
    private Admin admin;
    */

    @Column(name = "notice_title")
    private String noticeTitle;

    @Column(name = "notice_content")
    private String noticeContent;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Builder
    public Notice(
            String noticeTitle,
            String noticeContent
//            Admin admin
    ) {
        this.noticeTitle = noticeTitle;
        this.noticeContent = noticeContent;
//        this.admin = admin;   Todo: admin 엔티티 추가에 따라 리펙토링 예정
    }
}
