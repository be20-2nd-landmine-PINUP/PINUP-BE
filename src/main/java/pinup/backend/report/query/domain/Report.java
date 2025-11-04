package pinup.backend.report.query.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pinup.backend.feed.entity.Feed;
import pinup.backend.member.command.domain.Admin;
import pinup.backend.member.command.domain.Users;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "report")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Integer reportId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id")
    private Feed feed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private Admin admin;

    @Column(name = "reason", length = 30)
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ReportStatus status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Builder
    public Report(Users user, Feed feed, Admin admin, String reason, ReportStatus status) {
        this.user = user;
        this.feed = feed;
        this.admin = admin;
        this.reason = reason;
        this.status = status != null ? status : ReportStatus.ACTIVE;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = null;
    }
}
