package br.com.altamira.data.wbccad.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.altamira.data.wbccad.model.Orclst;

@Repository
@Transactional(readOnly = true)
public interface OrclstRepository extends CrudRepository<Orclst, String> {

    Orclst findByOrclstNumero(String orcamento);
}