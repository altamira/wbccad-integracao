package br.com.altamira.data.wbccad.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.altamira.data.wbccad.model.OrcDet;

@Repository
@Transactional(readOnly = true)
public interface OrcDetRepository extends CrudRepository<OrcDet, String> {

    List<OrcDet> findAllByNumeroOrcamentoAndOrcdetItemAndOrcdetGrupoAndOrcdetSubgrupo(String orcamento, String item, Integer grupo, String subgrupo);
}