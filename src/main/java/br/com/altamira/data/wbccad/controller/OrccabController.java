package br.com.altamira.data.wbccad.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import br.com.altamira.data.wbccad.model.IntegracaoOrcitm;
import br.com.altamira.data.wbccad.model.OrcDet;
import br.com.altamira.data.wbccad.model.OrcItm;
import br.com.altamira.data.wbccad.model.OrcMat;
import br.com.altamira.data.wbccad.model.Orccab;
import br.com.altamira.data.wbccad.model.Orclst;
import br.com.altamira.data.wbccad.model.Prdest;
import br.com.altamira.data.wbccad.model.Prdorc;
import br.com.altamira.data.wbccad.repository.IntegracaoOrcitmRepository;
import br.com.altamira.data.wbccad.repository.OrcDetRepository;
import br.com.altamira.data.wbccad.repository.OrcMatRepository;
import br.com.altamira.data.wbccad.repository.OrccabRepository;
import br.com.altamira.data.wbccad.repository.OrcitmRepository;
import br.com.altamira.data.wbccad.repository.OrclstRepository;
import br.com.altamira.data.wbccad.repository.PrdestRepository;
import br.com.altamira.data.wbccad.repository.PrdorcRepository;


@Controller
public class OrccabController {

	@Autowired
	private OrccabRepository orccabRepository;

	@Autowired
	private OrcitmRepository orcitmRepository;

	@Autowired
	private OrcMatRepository orcMatRepository;

	@Autowired
	private PrdorcRepository prdorcRepository;

	@Autowired
	private PrdestRepository prdestRepository;

	@Autowired
	private OrcDetRepository orcDetRepository;	
	
	@Autowired
	private OrclstRepository orclstRepository;
	
	@Autowired
	private IntegracaoOrcitmRepository integracaoOrcitmRepository;
	
	@JmsListener(destination = "/wbccad/orccab/v1/in")
	@SendTo("/wbccad/orccab/v1/out")
	public Orccab get(String numero) {
		Orccab orccab = null;

		System.out.println(String.format("\n--> Solicitado dados do orcamento %s...\n\n",
				numero));
		
		Orclst orclst = orclstRepository.findByOrclstNumero(numero);
		
		if (orclst != null) {

			orccab = orccabRepository.findByNumeroOrcamento(orclst.getOrclstNumero().trim() + orclst.getOrclstRevisao().trim());

			if (orccab != null) {
				
				orccab.setOrcItm(orcitmRepository
						.findAllByNumeroOrcamentoOrderByOrcitmItemAscOrcitmGrupoAscOrcitmSubgrupoAsc(orccab
								.getNumeroOrcamento()));
				
				for (OrcItm itm : orccab.getOrcItm()) {
					itm.setOrcMat(orcMatRepository
							.findAllByIdNumeroOrcamentoAndOrcMatGrupoAndOrcMatSubGrupo(
									itm.getNumeroOrcamento(),
									itm.getOrcitmGrupo(), itm.getOrcitmSubgrupo()));
				}
					
				orccab.setOrcMat(orcMatRepository.findAllByIdNumeroOrcamento(orccab
						.getNumeroOrcamento()));
				
				for (OrcMat mat : orccab.getOrcMat()) {
					
					Prdorc prdorc = prdorcRepository.findByProduto(mat
							.getOrcmatCodigoPai());
					
					prdorc.setPrdest(Prdest(prdorc));
					mat.setPrdorc(prdorc);
				}
				
				for (OrcItm itm : orccab.getOrcItm()) {
					
					IntegracaoOrcitm integracaoOrcitm = integracaoOrcitmRepository
							.findByOrcnumAndOrcitmAndGrpcodAndSubgrpcod(itm.getNumeroOrcamento(),
									Integer.parseInt(itm.getOrcitmItem()),
									itm.getOrcitmGrupo(),
									Integer.parseInt(itm.getOrcitmSubgrupo()));
					
					if (integracaoOrcitm != null) {
						itm.setOrctxt(integracaoOrcitm.getOrctxt());
					}

					itm.setOrcdet(orcDetRepository
							.findAllByNumeroOrcamentoAndOrcdetItemAndOrcdetGrupoAndOrcdetSubgrupo(
									itm.getNumeroOrcamento(), itm.getOrcitmItem(),
									itm.getOrcitmGrupo(), itm.getOrcitmSubgrupo()));
					
					for (OrcDet orcdet : itm.getOrcdet()) {

						Prdorc prdorc = prdorcRepository.findByProduto(orcdet
								.getOrcdetCodigoOri());
						
						prdorc.setPrdest(Prdest(prdorc));
						orcdet.setPrdorc(prdorc);
					}
				}

				System.out.println(orccab);
				
			}
			
		} else {
			System.out.println(String.format("\n--> Orcamento não encontrado %s...",
					numero));
		}
		
		System.out.println(String.format("\n--> Finalizado conversao do orcamento %s...",
				numero));
		
		return orccab;
		
	}

	public Prdorc prdorc(String codigo) {
		System.out.println(String.format("\n EXPORTANDO ESTRUTURA DO PRODUTO [%s] PARA FILA MATERIAL-IMPORTAR", codigo));

		Prdorc prdorc = prdorcRepository.findByProduto(codigo);
		if (prdorc != null) {
			prdorc.setPrdest(Prdest(prdorc));
		}
		return prdorc;
	}

	private List<Prdest> Prdest(Prdorc produtopai) {
		List<Prdest> list = new ArrayList<Prdest>();

		list = prdestRepository.findAllByIdPrdorccodigopai(produtopai
				.getProduto());

		for (Prdest prdest : list) {
			Prdorc produtofilho = prdorcRepository.findByProduto(prdest.getId()
					.getPrdorccodigofilho());
			produtofilho.setPrdest(Prdest(produtofilho));
			prdest.setPrdorc(produtofilho);
		}
		return list;
	}

}
