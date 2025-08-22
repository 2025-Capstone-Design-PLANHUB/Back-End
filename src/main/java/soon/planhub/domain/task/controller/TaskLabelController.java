package soon.planhub.domain.task.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import soon.planhub.domain.task.controller.dto.label.request.TaskLabelCreateRequest;
import soon.planhub.domain.task.service.label.TaskLabelService;
import soon.planhub.global.annotation.AuthMemberId;

@RequiredArgsConstructor
@RequestMapping("/api/v1/teams/{teamId}/task-labels")
@RestController
public class TaskLabelController {

    private final TaskLabelService taskLabelService;

    @PostMapping
    public ResponseEntity<Long> createLabel(
        @Valid @RequestBody TaskLabelCreateRequest request,
        @AuthMemberId Long memberId,
        @PathVariable Long teamId
    ) {
        Long labelId = taskLabelService.createLabel(request.toServiceRequest(teamId), memberId);
        return ResponseEntity.status(HttpStatus.CREATED).body(labelId);
    }

}