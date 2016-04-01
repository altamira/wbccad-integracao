package br.com.altamira.data.wbccad.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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

			orccab = orccabRepository.findByNumeroOrcamento(orclst.getOrclstNumero().trim() + (orclst.getOrclstRevisao() == null ? "" : orclst.getOrclstRevisao().trim()));

			if (orccab != null) {
				
				Set<String> codigos = new HashSet<String>();
				
				orccab.setPrdOrc(new HashSet<Prdorc>());
				
				// Carrega previamente a lista de produtos deste orcamento
				// isso melhora muito o tempo de acesso, pois evita que seja feitas muitas requisições no banco de dados 
				// para buscar os dados do PrdOrc individualmente
				List<Prdorc> prdorcList = prdorcRepository.listarPrdorc(numero);
				
				System.out.println(" +-----------------------------------------------------------------------------------------------+");
				System.out.println(" |                           CARREGA A ARVORE DE PRODUTOS                                        |");
				System.out.println(" +-----------------------------------------------------------------------------------------------+");
				
				for (Prdorc prdorc : prdorcList) {
					
					System.out.println(String.format("--> %s %s\n", prdorc.getProduto(), prdorc.getDescricao()));
					
					prdorc.setPrdest(Prdest(orccab, prdorc));

					orccab.getPrdOrc().add(prdorc);

				}
					
				System.out.println(" +-----------------------------------------------------------------------------------------------+");
				System.out.println(" |                              FIM DA ARVORE DE PRODUTOS                                        |");
				System.out.println(" +-----------------------------------------------------------------------------------------------+");
				
				orccab.setOrcMat(orcMatRepository.findAllByIdNumeroOrcamento(orccab
						.getNumeroOrcamento()));
				
				for (OrcMat mat : orccab.getOrcMat()) {
						
					Optional<Prdorc> exist = orccab.getPrdOrc()
							.stream()
							.filter(i -> i.getProduto().trim().equals(mat.getOrcmatCodigoPai().trim()))
							.findFirst();
					
					if (exist.isPresent()) {
						exist.get().setRef();
						mat.setPrdorc(exist.get());
					} else {
						Prdorc prdorc = prdorcRepository.findByProduto(mat.getOrcmatCodigoPai().trim());
						
						orccab.setCount();

						prdorc.setPrdest(Prdest(orccab, prdorc));

						orccab.getPrdOrc().add(prdorc);
						
						mat.setPrdorc(prdorc);
					}
				}
				
				orccab.setOrcItm(orcitmRepository
						.findAllByNumeroOrcamentoOrderByOrcitmItemAscOrcitmGrupoAscOrcitmSubgrupoAsc(orccab
								.getNumeroOrcamento()));
				
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

						Optional<Prdorc> exist = orccab.getPrdOrc()
								.stream()
								.filter(i -> i.getProduto().trim().equals(orcdet.getOrcdetCodigoOri().trim()))
								.findFirst();
						
						if (exist.isPresent()) {
							exist.get().setRef();
							orcdet.setPrdorc(exist.get());
						} else {
							Prdorc prdorc = prdorcRepository.findByProduto(orcdet.getOrcdetCodigoOri());
							
							orccab.setCount();
							
							prdorc.setPrdest(Prdest(orccab, prdorc));
							
							orccab.getPrdOrc().add(prdorc);
							
							orcdet.setPrdorc(prdorc);
						}
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

	/*public Prdorc prdorc(Orccab orccab, String codigo) {
		System.out.println(String.format("\n EXPORTANDO ESTRUTURA DO PRODUTO [%s] PARA FILA MATERIAL-IMPORTAR", codigo));

		Prdorc prdorc = prdorcRepository.findByProduto(codigo);
		if (prdorc != null) {
			prdorc.setPrdest(Prdest(prdorc));
		}
		return prdorc;
	}*/

	private List<Prdest> Prdest(Orccab orccab, Prdorc produtopai) {
		List<Prdest> list = null;

		Prdorc prdorc = null;
		
		Optional<Prdorc> existPai = orccab.getPrdOrc()
				.stream()
				.filter(i -> i.getProduto().trim().equals(produtopai.getProduto().trim()))
				.findFirst();
		
		if (existPai.isPresent()) {
			System.out.println(String.format(" %s: Encontrou produto pai: %s\n%s", existPai.get().getProduto(), existPai.get().getDescricao(), existPai.get().toString(" ")));
			existPai.get().setRef();
			list = existPai.get().getPrdest();
		} else {
			list = prdestRepository.findAllByIdPrdorccodigopai(produtopai
					.getProduto());

			System.out.println(String.format(" %s: Carregando estrutura do produto pai: %s, encontrado: %d\n", produtopai.getProduto(), produtopai.getDescricao(), list.size()));

			for (Prdest prdest : list) {
				
				Optional<Prdorc> exist = orccab.getPrdOrc()
						.stream()
						.filter(i -> i.getProduto().trim().equals(prdest.getId()
								.getPrdorccodigofilho().trim()))
						.findFirst();
				
				if (exist.isPresent()) {
					System.out.println(String.format(" %s: Encontrou produto filho: %s\n", exist.get().getProduto(), exist.get().getDescricao()));
					exist.get().setRef();
					prdorc = exist.get();
				} else {
					prdorc = prdorcRepository.findByProduto(prdest.getId()
							.getPrdorccodigofilho());
					
					System.out.println(String.format(" %s: Carregando produto filho: %s %s\n", prdest.getId().getPrdorccodigopai(), prdorc.getProduto(), prdorc.getDescricao()));

					orccab.setCount();
					
					prdorc.setPrdest(Prdest(orccab, prdorc));
					
					orccab.getPrdOrc().add(prdorc);

				}

				prdest.setPrdorc(prdorc);
			}
		}


		return list;
	}

}
