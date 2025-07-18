package soon.planhub.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import soon.planhub.domain.member.repository.MemberRepository;

@RequiredArgsConstructor
@Repository
public class MemberService {

    private final MemberRepository memberRepository;

}