package br.com.altamira.data.wbccad.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.altamira.data.wbccad.model.IntegracaoOrccab;

@Repository
@Transactional(readOnly = true)
public interface IntegracaoOrccabRepository extends CrudRepository<IntegracaoOrccab, String> {
	
	IntegracaoOrccab findByOrcnum(String orcamento);
}
