package com.example.cadastro.controller;

import com.example.cadastro.model.OrdemServico;
import com.example.cadastro.repository.ClienteRepository;
import com.example.cadastro.repository.FuncionarioRepository;
import com.example.cadastro.repository.OrdemServicoRepository;
import com.example.cadastro.repository.ServicoRepository;
import com.example.cadastro.service.OrdemServicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Controller
public class OrdemServicoController {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ServicoRepository servicoRepository;

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private OrdemServicoRepository ordemServicoRepository;

    @GetMapping("/emitirOrdemServico")
    public String emitirOrdemServico(Model model) {
        // Verificar se há pelo menos uma ordem de serviço
        List<OrdemServico> ordensServico = ordemServicoRepository.findAll();
        if (ordensServico.isEmpty()) {
            // Adicionar uma ordem de serviço padrão se não houver outras
            OrdemServico ordemPadrao = ordemServicoRepository.save(criarOrdemServicoPadrao());
            ordensServico = Collections.singletonList(ordemPadrao);
        }

        // Adicionar as ordens de serviço ao modelo
        model.addAttribute("clientes", clienteRepository.findAll());
        model.addAttribute("servicos", servicoRepository.findAll());
        model.addAttribute("funcionarios", funcionarioRepository.findAll());
        model.addAttribute("ordensServico", ordensServico);
        model.addAttribute("ordemServico", new OrdemServico());
        return "emitirOrdemServico";
    }

    private OrdemServico criarOrdemServicoPadrao() {
        // Aqui você pode criar uma ordem de serviço padrão com valores irrelevantes
        // Por exemplo:
        OrdemServico ordemPadrao = new OrdemServico();
        ordemPadrao.setCliente(clienteRepository.findById(1L).orElse(null)); // Aqui você pode substituir 1L pelo ID de um cliente padrão
        ordemPadrao.setServico(servicoRepository.findById(1L).orElse(null)); // Aqui você pode substituir 1L pelo ID de um serviço padrão
        ordemPadrao.setFuncionario(funcionarioRepository.findById(1L).orElse(null)); // Aqui você pode substituir 1L pelo ID de um funcionário padrão
        ordemPadrao.setDescricao("Ordem de Serviço Padrão");
        ordemPadrao.setValor(BigDecimal.ZERO); // Valor irrelevante
        ordemPadrao.setDataEmissao(LocalDateTime.now()); // Data de emissão atual
        ordemPadrao.setStatus("pendente");
        ordemPadrao.setFormaPagamento("Forma de Pagamento Padrão");
        ordemPadrao.setOrdemPadrao(true); // Marcar como ordem padrão
        return ordemPadrao;

    }

    @PostMapping("/emitirOrdemServico")
    public String emitirOrdemServico(@ModelAttribute OrdemServico ordemServico) {
        ordemServico.setDescricao(ordemServico.getServico().getDescricao());
        ordemServico.setValor(ordemServico.getServico().getValor());
        ordemServico.setDataEmissao(LocalDateTime.now());
        ordemServico.setStatus("pendente");
        ordemServicoRepository.save(ordemServico);
        return "redirect:/emitirOrdemServico";
    }

    @PostMapping("/deleteOrdemServico")
    public String deleteOrdemServico(@RequestParam List<Long> selectedIds) {
        selectedIds.removeIf(id -> ordemServicoRepository.findById(id).orElseThrow().isOrdemPadrao());
        ordemServicoRepository.deleteAllById(selectedIds);
        return "redirect:/emitirOrdemServico";
    }

    @PostMapping("/executarOrdemServico/{id}")
    public String executarOrdemServico(@PathVariable Long id, @RequestParam String formaPagamento) {
        OrdemServico ordemServico = ordemServicoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ordem de Serviço inválida:" + id));
        ordemServico.setStatus("executada");
        ordemServico.setFormaPagamento(formaPagamento);
        ordemServicoRepository.save(ordemServico);
        return "redirect:/emitirOrdemServico";
    }

    @PostMapping("/atualizarFormaPagamento/{id}")
    public String atualizarFormaPagamento(@PathVariable Long id, @RequestParam String formaPagamento) {
        OrdemServico ordemServico = ordemServicoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Ordem de Serviço inválida:" + id));
        ordemServico.setFormaPagamento(formaPagamento);
        ordemServicoRepository.save(ordemServico);
        return "redirect:/emitirOrdemServico";
    }

    @Autowired
    private OrdemServicoService ordemServicoService;

    @GetMapping("/buscarOrdensExecutadas")
    public ResponseEntity<List<OrdemServico>> buscarOrdensExecutadas(
            @RequestParam("dataInicial") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataInicial,
            @RequestParam("dataFinal") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataFinal) {

        List<OrdemServico> ordensExecutadas = ordemServicoService.buscarOrdensExecutadas(dataInicial, dataFinal);

        return ResponseEntity.ok(ordensExecutadas);
    }
}


