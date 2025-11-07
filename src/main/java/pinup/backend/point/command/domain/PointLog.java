package pinup.backend.point.command.domain;

import jakarta.persistence.*;
import lombok.*;
import pinup.backend.member.command.domain.Users;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "point_log")
public class PointLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer logId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    private Integer pointSourceId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PointSourceType sourceType;

    @Column(nullable = false, unique = true, length = 120)
    private String eventKey;

    @Column(nullable = false)
    private int pointValue;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}