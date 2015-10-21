package br.com.altamira.data.wbccad.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.altamira.data.wbccad.model.Prdest;

@Repository
@Transactional(readOnly = true)
public interface PrdestRepository extends CrudRepository<Prdest, String> {

	List<Prdest> findAllByIdPrdorccodigopai(String codigo);
	List<Prdest> findAllByIdPrdorccodigofilho(String codigo);
}