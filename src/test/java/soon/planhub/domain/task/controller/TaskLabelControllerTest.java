package soon.planhub.domain.task.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import soon.planhub.ControllerTestSupport;
import soon.planhub.domain.task.controller.dto.request.label.TaskLabelCreateRequest;
import soon.planhub.global.annotation.TestMemberId;
import soon.planhub.global.exception.dto.ErrorDetail;
import soon.planhub.global.exception.task.label.AlreadyIssueLabelException;

import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

        given(taskLabelService.createLabel(request.toServiceRequest(1L), 1L))
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

        given(taskLabelService.createLabel(request.toServiceRequest(1L), 1L))
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

}