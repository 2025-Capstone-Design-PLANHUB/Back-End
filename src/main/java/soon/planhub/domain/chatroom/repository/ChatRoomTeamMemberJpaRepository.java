package soon.planhub.domain.chatroom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import soon.planhub.domain.chatroom.entity.ChatRoomTeamMember;

public interface ChatRoomTeamMemberJpaRepository extends JpaRepository<ChatRoomTeamMember, Long> {

}