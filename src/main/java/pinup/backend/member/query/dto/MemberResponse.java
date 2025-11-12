package pinup.backend.member.query.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pinup.backend.member.command.domain.Users;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberResponse {
    private Long userId;
    private String name;
    private String email;
    private String nickname;
    private String profileImage;
    private String gender;
    private String birthDate;
    private String preferredCategory;
    private String preferredSeason;
    private String status;

    public static MemberResponse from(Users user) {
        return MemberResponse.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .profileImage(user.getProfileImage())
                .gender(user.getGender().name())
                .birthDate(user.getBirthDate() != null ? user.getBirthDate().toString() : "") // string으로 변환
                .preferredCategory(user.getPreferredCategory().name())
                .preferredSeason(user.getPreferredSeason().name())
                .status(user.getStatus() != null ? user.getStatus().toString() : "") // Enum → String 변환
                .build();
    }
}
