package br.com.altamira.data.wbccad.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.altamira.data.wbccad.model.IntegracaoOrcprdarv;

@Repository
@Transactional(readOnly = true)
public interface IntegracaoOrcprdarvRepository extends CrudRepository<IntegracaoOrcprdarv, String> {
	List<IntegracaoOrcprdarv> findByOrcnum(String orcamento);
}