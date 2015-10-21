package br.com.altamira.data.wbccad.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.altamira.data.wbccad.model.OrcMat;
import br.com.altamira.data.wbccad.model.OrcMatPK;

@Repository
@Transactional(readOnly = true)
public interface OrcMatRepository extends CrudRepository<OrcMat, OrcMatPK> {

    List<OrcMat> findAllByIdNumeroOrcamento(String orcamento);
}