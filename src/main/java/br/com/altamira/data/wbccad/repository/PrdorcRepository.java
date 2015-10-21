package br.com.altamira.data.wbccad.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.altamira.data.wbccad.model.Prdorc;

@Repository
@Transactional(readOnly = true)
public interface PrdorcRepository extends CrudRepository<Prdorc, String> {

	Prdorc findByProduto(String orcamento);
}