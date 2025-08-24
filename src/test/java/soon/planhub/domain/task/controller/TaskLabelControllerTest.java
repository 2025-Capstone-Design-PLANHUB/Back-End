package soon.planhub.domain.task.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import soon.planhub.ControllerTestSupport;
import soon.planhub.domain.task.controller.dto.request.label.TaskLabelCreateRequest;
import soon.planhub.domain.task.controller.dto.request.label.TaskLabelUpdateRequest;
import soon.planhub.global.annotation.TestMemberId;
import soon.planhub.global.exception.dto.ErrorDetail;
import soon.planhub.global.exception.task.label.AlreadyIssueLabelException;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TaskLabelControllerTest extends ControllerTestSupport {

    private static final String BASE_URL = "/api/v1/teams/{teamId}/task-labels";

    @TestMemberId
    @DisplayName("라벨을 생성한다.")
    @Test
    void createTaskLabel() throws Exception {
        // given
        var request = TaskLabelCreateRequest.builder()
            .projectId(1L)
            .title("Test title")
            .description("Test description")
            .color("#FFFFFF")
            .build();

        given(taskLabelService.createLabel(1L, 1L, request.toServiceRequest()))
            .willReturn(1L);

        // expected
        mockMvc.perform(
                post(BASE_URL, 1L)
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$").value(1L));
    }

    @TestMemberId
    @DisplayName("라벨 생성 시 제목은 필수 값이다.")
    @Test
    void createLabelWithoutTitle() throws Exception {
        // given
        var request = TaskLabelCreateRequest.builder()
            .projectId(1L)
            .description("Test description")
            .color("#FFFFFF")
            .build();

        // expected
        mockMvc.perform(
                post(BASE_URL, 1L)
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
            .andExpect(jsonPath("$.status").value(400))
            .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
            .andExpect(jsonPath("$.validation.title").value("제목을 입력해주세요."));
    }

    @TestMemberId
    @DisplayName("이미 존재하는 라벨의 경우 예외가 발생한다.")
    @Test
    void createLabelWhenLabelAlreadyExists() throws Exception {
        // given
        var request = TaskLabelCreateRequest.builder()
            .projectId(1L)
            .title("Existing label")
            .description("Test description")
            .color("#FFFFFF")
            .build();

        given(taskLabelService.createLabel(1L, 1L, request.toServiceRequest()))
            .willThrow(new AlreadyIssueLabelException(ErrorDetail.TASK_LABEL_ALREADY_EXISTS));

        // expected
        mockMvc.perform(
                post(BASE_URL, 1L)
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.status").value(ErrorDetail.TASK_LABEL_ALREADY_EXISTS.getStatus()))
            .andExpect(jsonPath("$.message").value(ErrorDetail.TASK_LABEL_ALREADY_EXISTS.getMessage()));
    }

    @TestMemberId
    @DisplayName("태스크 라벨을 성공적으로 수정한다.")
    @Test
    void updateLabel() throws Exception {
        // given
        Long teamId = 1L;
        Long labelId = 100L;
        Long memberId = 1L;

        var request = TaskLabelUpdateRequest.builder()
            .projectId(1L)
            .oldTitle("Old Label")
            .newTitle("Updated Label")
            .description("Updated description")
            .color("#FFFFFF")
            .build();

        // expected
        mockMvc.perform(
                patch(BASE_URL + "/{labelId}", teamId, labelId)
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            )
            .andDo(print())
            .andExpect(status().isNoContent());

        verify(taskLabelService).updateLabel(teamId, memberId, request.toServiceRequest(labelId));
    }

    @TestMemberId
    @DisplayName("태스크 라벨 수정 시 새로운 이름은 필수 값이다.")
    @Test
    void updateLabelWithoutTitle() throws Exception {
        // given
        Long teamId = 1L;
        Long labelId = 100L;

        var request = TaskLabelUpdateRequest.builder()
            .projectId(1L)
            .oldTitle("Old Label")
            .description("Updated description")
            .color("#FFFFFF")
            .build();

        // expected
        mockMvc.perform(
                patch(BASE_URL + "/{labelId}", teamId, labelId)
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(400))
            .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
            .andExpect(jsonPath("$.validation.newTitle").value("새로운 이름을 입력해주세요."));
    }

    @TestMemberId
    @DisplayName("이미 존재하는 라벨의 이름으로 수정 시 예외가 발생한다.")
    @Test
    void updateLabelWhenLabelAlreadyExists() throws Exception {
        // given
        var request = TaskLabelUpdateRequest.builder()
            .projectId(1L)
            .oldTitle("Old Label")
            .newTitle("Existing Label")
            .description("Updated description")
            .color("#FFFFFF")
            .build();

        doThrow(new AlreadyIssueLabelException(ErrorDetail.TASK_LABEL_ALREADY_EXISTS))
            .when(taskLabelService)
            .updateLabel(1L, 1L, request.toServiceRequest(1L));

        // expected
        mockMvc.perform(
                patch(BASE_URL + "/{labelId}", 1L, 1L)
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
            .andDo(print())
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.status").value(ErrorDetail.TASK_LABEL_ALREADY_EXISTS.getStatus()))
            .andExpect(jsonPath("$.message").value(ErrorDetail.TASK_LABEL_ALREADY_EXISTS.getMessage()));
    }

    @TestMemberId
    @DisplayName("라벨을 삭제한다.")
    @Test
    void deleteLabel() throws Exception {
        // given
        Long teamId = 1L;
        Long labelId = 100L;

        // expected
        mockMvc.perform(delete(BASE_URL + "/{labelId}", teamId, labelId))
            .andDo(print())
            .andExpect(status().isNoContent());
    }

}