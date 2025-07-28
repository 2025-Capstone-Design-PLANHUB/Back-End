package soon.planhub.domain.member.repository;

import static soon.planhub.global.exception.dto.ErrorDetail.MEMBER_NOT_FOUND;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import soon.planhub.domain.member.entity.Member;
import soon.planhub.global.exception.dto.common.EntityNotFoundException;

@RequiredArgsConstructor
@Repository
public class MemberRepository {

    private final MemberJpaRepository memberJpaRepository;

    public Member findById(Long member) {
        return memberJpaRepository.findById(member)
            .orElseThrow(() -> new EntityNotFoundException(MEMBER_NOT_FOUND));
    }

    public Member findByNickname(String nickname) {
        return memberJpaRepository.findByNickname(nickname)
            .orElseThrow(() -> new EntityNotFoundException(MEMBER_NOT_FOUND));
    }

    public void save(Member member) {
        memberJpaRepository.save(member);
    }

    public void deleteAllInBatch() {
        memberJpaRepository.deleteAllInBatch();
    }

}