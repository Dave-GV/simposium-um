package com.example.simposium.dto;

public class DashboardSummaryDTO {

    private Long totalUsers;
    private Long totalPonentes;
    private Long totalPagos;
    private Long totalPonencias;

    public DashboardSummaryDTO() {
    }

    public DashboardSummaryDTO(Long totalUsers, Long totalPonentes, Long totalPagos, Long totalPonencias) {
        this.totalUsers = totalUsers;
        this.totalPonentes = totalPonentes;
        this.totalPagos = totalPagos;
        this.totalPonencias = totalPonencias;
    }

    public Long getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(Long totalUsers) {
        this.totalUsers = totalUsers;
    }

    public Long getTotalPonentes() {
        return totalPonentes;
    }

    public void setTotalPonentes(Long totalPonentes) {
        this.totalPonentes = totalPonentes;
    }

    public Long getTotalPagos() {
        return totalPagos;
    }

    public void setTotalPagos(Long totalPagos) {
        this.totalPagos = totalPagos;
    }

    public Long getTotalPonencias() {
        return totalPonencias;
    }

    public void setTotalPonencias(Long totalPonencias) {
        this.totalPonencias = totalPonencias;
    }
}
