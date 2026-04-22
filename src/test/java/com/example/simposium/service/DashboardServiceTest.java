package com.example.simposium.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.example.simposium.dto.DashboardSummaryDTO;
import com.example.simposium.repository.PagoRepository;
import com.example.simposium.repository.PonenciaRepository;
import com.example.simposium.repository.PonenteRepository;
import com.example.simposium.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DashboardServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PonenteRepository ponenteRepository;

    @Mock
    private PagoRepository pagoRepository;

    @Mock
    private PonenciaRepository ponenciaRepository;

    @InjectMocks
    private DashboardService dashboardService;

    @Test
    void shouldBuildDashboardSummaryFromRepositoryCounts() {
        when(userRepository.count()).thenReturn(10L);
        when(ponenteRepository.count()).thenReturn(4L);
        when(pagoRepository.count()).thenReturn(7L);
        when(ponenciaRepository.count()).thenReturn(12L);

        DashboardSummaryDTO summary = dashboardService.getDashboardSummary();

        assertEquals(10L, summary.getTotalUsers());
        assertEquals(4L, summary.getTotalPonentes());
        assertEquals(7L, summary.getTotalPagos());
        assertEquals(12L, summary.getTotalPonencias());
    }
}
