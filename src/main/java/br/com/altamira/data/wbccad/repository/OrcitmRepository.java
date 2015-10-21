package br.com.altamira.data.wbccad.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.altamira.data.wbccad.model.OrcItm;


@Repository
@Transactional(readOnly = true)
public interface OrcitmRepository extends CrudRepository<OrcItm, String> {

    List<OrcItm> findAllByNumeroOrcamentoOrderByOrcitmItemAscOrcitmGrupoAscOrcitmSubgrupoAsc(String orcamento);
}