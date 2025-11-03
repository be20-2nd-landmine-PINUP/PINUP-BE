package pinup.backend.member.command.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pinup.backend.member.command.domain.UserBlock;
import pinup.backend.member.command.repository.UserBlockRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserBlockService {

    private final UserBlockRepository userBlockRepository;

    @Transactional
    public void blockUser(Long blockerId, Long blockedId) {
        userBlockRepository.findByBlockerIdAndBlockedId(blockerId, blockedId)
                .ifPresentOrElse(
                        block -> block.setActive(true),
                        () -> userBlockRepository.save(
                                UserBlock.builder()
                                        .blockerId(blockerId)
                                        .blockedId(blockedId)
                                        .active(true)
                                        .build()
                        )
                );
    }

    @Transactional
    public void unblockUser(Long blockerId, Long blockedId) {
        userBlockRepository.findByBlockerIdAndBlockedId(blockerId, blockedId)
                .ifPresent(block -> block.setActive(false));
    }

    public List<UserBlock> getBlockedUsers(Long blockerId) {
        return userBlockRepository.findAllByBlockerId(blockerId);
    }

    public boolean isBlocked(Long blockerId, Long blockedId) {
        return userBlockRepository.findByBlockerIdAndBlockedId(blockerId, blockedId)
                .map(UserBlock::isActive)
                .orElse(false);
    }
}
