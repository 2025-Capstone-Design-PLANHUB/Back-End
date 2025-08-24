package soon.planhub.domain.task.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import soon.planhub.domain.task.controller.dto.request.label.TaskLabelCreateRequest;
import soon.planhub.domain.task.controller.dto.request.label.TaskLabelUpdateRequest;
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
        Long labelId = taskLabelService.createLabel(teamId, memberId, request.toServiceRequest());
        return ResponseEntity.status(HttpStatus.CREATED).body(labelId);
    }

    @PatchMapping("/{labelId}")
    public ResponseEntity<Void> updateLabel(
        @Valid @RequestBody TaskLabelUpdateRequest request,
        @AuthMemberId Long memberId,
        @PathVariable Long teamId,
        @PathVariable Long labelId
    ) {
        taskLabelService.updateLabel(teamId, memberId, request.toServiceRequest(labelId));
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{labelId}")
    public ResponseEntity<Void> deleteLabel(
        @AuthMemberId Long memberId,
        @PathVariable Long teamId,
        @PathVariable Long labelId
    ) {
        taskLabelService.deleteLabel(teamId, memberId, labelId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}