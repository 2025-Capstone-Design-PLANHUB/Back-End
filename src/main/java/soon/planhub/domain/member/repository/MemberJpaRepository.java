package soon.planhub.domain.member.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import soon.planhub.domain.member.entity.Member;

public interface MemberJpaRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByNickname(String nickname);

    boolean existsByIdAndRefreshToken(Long memberId, String refreshToken);

}