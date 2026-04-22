package com.example.simposium.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.simposium.config.SecurityConfig;
import com.example.simposium.dto.DashboardSummaryDTO;
import com.example.simposium.service.DashboardService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(DashboardController.class)
@Import(SecurityConfig.class)
class DashboardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DashboardService dashboardService;

    @Test
    void shouldReturnDashboardSummary() throws Exception {
        DashboardSummaryDTO summary = new DashboardSummaryDTO(5L, 2L, 3L, 8L);
        when(dashboardService.getDashboardSummary()).thenReturn(summary);

        mockMvc.perform(get("/api/dashboard"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.totalUsers").value(5))
            .andExpect(jsonPath("$.totalPonentes").value(2))
            .andExpect(jsonPath("$.totalPagos").value(3))
            .andExpect(jsonPath("$.totalPonencias").value(8));
    }
}
