package com.example.cadastro.service;

import com.example.cadastro.model.OrdemServico;
import com.example.cadastro.repository.OrdemServicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class OrdemServicoService {

    @Autowired
    private OrdemServicoRepository ordemServicoRepository;

    public List<OrdemServico> buscarOrdensExecutadas(LocalDateTime dataInicial, LocalDateTime dataFinal) {
        return ordemServicoRepository.findByStatusAndDataEmissaoBetween("executada", dataInicial, dataFinal);
    }
}

