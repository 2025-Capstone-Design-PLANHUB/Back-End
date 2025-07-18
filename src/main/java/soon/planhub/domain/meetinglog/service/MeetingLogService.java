package soon.planhub.domain.meetinglog.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import soon.planhub.domain.meetinglog.repository.MeetingLogRepository;

@RequiredArgsConstructor
@Service
public class MeetingLogService {

    private final MeetingLogRepository meetingLogRepository;

}