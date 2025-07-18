package soon.planhub.domain.chatroom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import soon.planhub.domain.chatroom.entity.ChatRoom;

public interface ChatRoomJpaRepository extends JpaRepository<ChatRoom, Long> {

}