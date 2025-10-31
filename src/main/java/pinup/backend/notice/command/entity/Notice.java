package pinup.backend.notice.command.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import pinup.backend.member.command.domain.Admin;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Notice {
    @Id
    @Column(name = "notice_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer noticeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "admin_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_notice_admin_id")
    )
    private Admin admin;

    @Column(name = "notice_title")
    private String noticeTitle;

    @Column(name = "notice_content")
    private String noticeContent;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Builder
    public Notice(
            String noticeTitle,
            String noticeContent,
            Admin admin
    ) {
        this.noticeTitle = noticeTitle;
        this.noticeContent = noticeContent;
        this.admin = admin;
    }
}
