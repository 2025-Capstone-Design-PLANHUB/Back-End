package soon.planhub.domain.meetinglog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import soon.planhub.domain.meetinglog.entity.MeetingLog;

public interface MeetingLogJpaRepository extends JpaRepository<MeetingLog, Long> {

}