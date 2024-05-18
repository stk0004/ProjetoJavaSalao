package com.example.cadastro.repository;

import com.example.cadastro.model.OrdemServico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrdemServicoRepository extends JpaRepository<OrdemServico, Long> {
     List<OrdemServico> findByStatusAndDataEmissaoBetween(String executada, LocalDateTime dataInicial, LocalDateTime dataFinal);
}
