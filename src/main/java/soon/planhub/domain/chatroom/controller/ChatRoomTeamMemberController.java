package soon.planhub.domain.chatroom.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import soon.planhub.domain.chatroom.service.ChatRoomTeamMemberService;

@RequiredArgsConstructor
@RequestMapping("/api/v1/teams/{teamId}/chat-rooms/{chatRoomId}/members")
@RestController
public class ChatRoomTeamMemberController {

    private final ChatRoomTeamMemberService chatRoomTeamMemberService;

}