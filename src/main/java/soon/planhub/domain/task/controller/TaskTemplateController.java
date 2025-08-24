package soon.planhub.domain.task.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import soon.planhub.domain.task.controller.dto.request.template.TaskTemplateCreateRequest;
import soon.planhub.domain.task.controller.dto.request.template.TaskTemplateUpdateRequest;
import soon.planhub.domain.task.service.template.TaskTemplateService;
import soon.planhub.global.annotation.AuthMemberId;

@RequiredArgsConstructor
@RequestMapping("/api/v1/teams/{teamId}/task-templates")
@RestController
public class TaskTemplateController {

    private final TaskTemplateService taskTemplateService;

    @PostMapping
    public ResponseEntity<Long> createTaskTemplate(
        @Valid @RequestBody TaskTemplateCreateRequest request,
        @AuthMemberId Long memberId,
        @PathVariable Long teamId
    ) {
        Long taskTemplateId = taskTemplateService.create(teamId, memberId, request.toServiceRequest());
        return ResponseEntity.status(HttpStatus.CREATED).body(taskTemplateId);
    }

    @PatchMapping("/{taskTemplateId}")
    public ResponseEntity<Void> updateTaskTemplate(
        @Valid @RequestBody TaskTemplateUpdateRequest request,
        @AuthMemberId Long memberId,
        @PathVariable Long teamId,
        @PathVariable Long taskTemplateId
    ) {
        taskTemplateService.update(teamId, memberId, request.toServiceRequest(taskTemplateId));
        return ResponseEntity.noContent().build();
    }

}