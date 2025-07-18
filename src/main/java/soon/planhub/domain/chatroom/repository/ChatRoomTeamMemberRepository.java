package soon.planhub.domain.chatroom.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ChatRoomTeamMemberRepository {

    private final ChatRoomTeamMemberJpaRepository chatRoomTeamMemberJpaRepository;

}