package soon.planhub.domain.task.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import soon.planhub.ControllerTestSupport;
import soon.planhub.domain.task.controller.dto.request.template.TaskTemplateCreateRequest;
import soon.planhub.global.annotation.TestMemberId;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TaskTemplateControllerTest extends ControllerTestSupport {

    private static final String BASE_URL = "/api/v1/teams/{teamId}/task-templates";

    @TestMemberId
    @DisplayName("탬플릿을 생성한다.")
    @Test
    void createTaskTemplate() throws Exception {
        // given
        Long teamId = 1L;
        var request = TaskTemplateCreateRequest.builder()
            .title("Test title")
            .description("Test description")
            .content("Test content")
            .type("Fix")
            .projectId(1L)
            .build();

        given(taskTemplateService.create(request.toServiceRequest(teamId), 1L))
            .willReturn(1L);

        // expected
        mockMvc.perform(
                post(BASE_URL, teamId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(request))
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").value(1L));
    }

    @TestMemberId
    @DisplayName("템플릿을 수정한다.")
    @Test
    void updateTaskTemplate() throws Exception {
        // given
        Long teamId = 1L;
        Long taskTemplateId = 1L;
        var request = TaskTemplateCreateRequest.builder()
            .title("Updated title")
            .description("Updated description")
            .content("Updated content")
            .type("Fix")
            .build();

        // expected
        mockMvc.perform(
                patch(BASE_URL + "/{taskTemplateId}", teamId, taskTemplateId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(request))
            )
            .andDo(print())
            .andExpect(status().isNoContent());
    }

}