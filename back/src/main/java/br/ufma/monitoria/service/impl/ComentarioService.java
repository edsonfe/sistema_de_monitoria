package br.ufma.monitoria.service.impl;

import br.ufma.monitoria.model.Comentario;
import br.ufma.monitoria.repository.ComentarioRepository;
import br.ufma.monitoria.service.contract.ComentarioServiceInterface;
import br.ufma.monitoria.service.exceptions.RegraNegocioRuntime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComentarioService implements ComentarioServiceInterface {

    @Autowired
    private ComentarioRepository comentarioRepository;

    @Override
    public Comentario salvar(Comentario comentario) {
        return comentarioRepository.save(comentario);
    }

    @Override
    public Comentario buscarPorId(Integer id) {
        return comentarioRepository.findById(id)
            .orElseThrow(() -> new RegraNegocioRuntime("Comentário não encontrado"));
    }

    @Override
    public List<Comentario> listarTodos() {
        return comentarioRepository.findAll();
    }
}
