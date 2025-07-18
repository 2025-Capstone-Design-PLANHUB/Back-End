package soon.planhub.domain.chatroom.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import soon.planhub.domain.chatroom.repository.ChatRoomTeamMemberRepository;

@RequiredArgsConstructor
@Service
public class ChatRoomTeamMemberService {

    private final ChatRoomTeamMemberRepository chatRoomTeamMemberRepository;

}