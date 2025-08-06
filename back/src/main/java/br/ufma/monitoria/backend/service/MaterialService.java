package br.ufma.monitoria.backend.service;

import br.ufma.monitoria.backend.model.Material;
import br.ufma.monitoria.backend.repository.MaterialRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MaterialService {

    private final MaterialRepository materialRepository;

    @Transactional
    public Material enviarMaterial(Material material) {
        if (material.getDataEnvio() == null) {
            material.setDataEnvio(LocalDateTime.now());
        }
        return materialRepository.save(material);
    }

    public Material buscarPorId(Long id) {
        return materialRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Material não encontrado"));
    }

    public List<Material> listarPorSessao(Long sessaoId) {
        return materialRepository.findBySessaoAgendada_SessaoId(sessaoId);
    }

    public List<Material> listarPorSessaoAposData(Long sessaoId, LocalDateTime dataEnvio) {
        return materialRepository.findBySessaoAgendada_SessaoIdAndDataEnvioAfter(sessaoId, dataEnvio);
    }

    @Transactional
    public void excluirMaterial(Long id) {
        if (!materialRepository.existsById(id)) {
            throw new EntityNotFoundException("Material não encontrado para exclusão");
        }
        materialRepository.deleteById(id);
    }
}
