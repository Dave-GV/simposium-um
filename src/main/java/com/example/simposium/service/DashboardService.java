package com.example.simposium.service;

import com.example.simposium.dto.DashboardSummaryDTO;
import com.example.simposium.repository.PagoRepository;
import com.example.simposium.repository.PonenciaRepository;
import com.example.simposium.repository.PonenteRepository;
import com.example.simposium.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {

    private final UserRepository userRepository;
    private final PonenteRepository ponenteRepository;
    private final PagoRepository pagoRepository;
    private final PonenciaRepository ponenciaRepository;

    public DashboardService(
        UserRepository userRepository,
        PonenteRepository ponenteRepository,
        PagoRepository pagoRepository,
        PonenciaRepository ponenciaRepository
    ) {
        this.userRepository = userRepository;
        this.ponenteRepository = ponenteRepository;
        this.pagoRepository = pagoRepository;
        this.ponenciaRepository = ponenciaRepository;
    }

    public DashboardSummaryDTO getDashboardSummary() {
        return new DashboardSummaryDTO(
            userRepository.count(),
            ponenteRepository.count(),
            pagoRepository.count(),
            ponenciaRepository.count()
        );
    }
}
