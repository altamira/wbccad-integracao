package br.com.altamira.data.wbccad.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.altamira.data.wbccad.model.Orccab;
import br.com.altamira.data.wbccad.model.Prdorc;

@Repository
@Transactional(readOnly = true)
public interface OrccabRepository extends CrudRepository<Orccab, String> {

    Orccab findByNumeroOrcamento(String orcamento);

}
