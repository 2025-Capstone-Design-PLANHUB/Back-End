package soon.planhub.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import soon.planhub.domain.member.entity.Member;

public interface MemberJpaRepository extends JpaRepository<Member, Long> {

}