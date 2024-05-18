package com.example.cadastro.controller;

import com.example.cadastro.model.Servico;
import com.example.cadastro.repository.ServicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/servico")
public class ServicoController {

    @Autowired
    private ServicoRepository servicoRepository;

    @GetMapping("/cadastro")
    public String mostrarFormularioCadastro(Model model) {
        // Crie um objeto Servico e adicione-o ao modelo
        Servico servico = new Servico();
        model.addAttribute("servico", servico);
        return "cadastro_servico";
    }

    @PostMapping("/salvar")
    public String salvarServico(@ModelAttribute("servico") Servico servico) {
        // Se o tipo de serviço for Cabelo, defina o tamanho do cabelo como 'null' se não estiver definido
        if (servico.getTipo().equals("Cabelo") && servico.getTamanhoCabelo() == null) {
            servico.setTamanhoCabelo("Não especificado");
        }
        servicoRepository.save(servico);
        return "redirect:/servico/listar";
    }

    @GetMapping("/listar")
    public String listarServicos(Model model) {
        List<Servico> servicos = servicoRepository.findAll();
        model.addAttribute("servicos", servicos);
        return "lista_servicos";
    }

}

