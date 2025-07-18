package soon.planhub.domain.chatroom.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import soon.planhub.domain.chatroom.service.ChatRoomService;

@RequiredArgsConstructor
@RequestMapping("/api/v1/teams/{teamId}/chat-rooms")
@RestController
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

}