package br.com.altamira.data.wbccad;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


//import br.com.altamira.data.wbccad.controller.OrcMatController;
import br.com.altamira.data.wbccad.controller.OrccabController;
import br.com.altamira.data.wbccad.exception.PrdorcNotFoundException;
import br.com.altamira.data.wbccad.model.Orccab;
import br.com.altamira.data.wbccad.model.Orclst;
import br.com.altamira.data.wbccad.repository.OrclstRepository;

/*
 Consultas usadas para retornar a estrutura do orcamento

	DECLARE @ORCNUM NVARCHAR(10)
	
	SET @ORCNUM = '00073028B' --'00090555'
	
	SELECT * FROM [WBCCAD].[dbo].[ORCCAB] WHERE numeroOrcamento = @ORCNUM
	SELECT * FROM [WBCCAD].[dbo].[ORCMAT] WHERE numeroOrcamento = @ORCNUM
	SELECT * FROM [WBCCAD].[dbo].[ORCITM] WHERE numeroOrcamento = @ORCNUM
	SELECT DISTINCT ORCDET_CODIGO FROM [WBCCAD].[dbo].[ORCDET] WHERE numeroOrcamento = @ORCNUM
	SELECT * FROM [WBCCAD].[dbo].[ORCDTC] WHERE numeroOrcamento = @ORCNUM

 Numeros de orçamentos com o maior quantidade de items usado nos testes
  
	SELECT TOP 100 P.* FROM (SELECT numeroOrcamento, COUNT(*) QTD FROM ORCMAT GROUP BY numeroOrcamento) AS P ORDER BY QTD DESC
	
	NUMERO ORCAMENTO                                   QTD ITEMS
	-------------------------------------------------- -----------
	00090555                                           207
	00085797                                           193
	00083001                                           185
	00069120                                           184
	00077779                                           182
	00081050                                           180
	00077528B                                          176
	00077528C                                          176
	0000T641                                           176
	00077528D                                          176
	00077528                                           176
	00077528A                                          176
	00088748A                                          175
	00072026F                                          173
	00090490                                           171
	00091709                                           171
	0000T293                                           170
	00088747                                           169
	0000T262                                           168
	00077528E                                          168
	00077528F                                          168
	00088929C                                          166
	00088929B                                          166
	00083001A                                          166
	00091709A                                          165
	00090394                                           163
	00078890                                           161
	00072026E                                          161
	00071651A                                          161
	00072026D                                          160
	00090393                                           157
	00088929                                           156
	00088929A                                          156
	00076193A                                          155
	00082942                                           153
	0000T084                                           151
	00071651                                           150
	00066269                                           149
	00091840F                                          149
	00087202                                           148
	00087687E                                          148
	00068860                                           148
	00068860B                                          148
	00065716                                           147
	00090472                                           146
	00076193B                                          146
	0000T265                                           144
	00083758A                                          143
	00089164D                                          143
	0000T271                                           143
	00076193                                           143
	00083758                                           143
	00088748                                           143
	00089164C                                          143
	00085453                                           142
	00086155                                           142
	00086155A                                          142
	00087246                                           141
	00078821                                           141
	00086155B                                          141
	00086155C                                          141
	00076408                                           140
	00088297                                           140
	00071651B                                          140
	00076962                                           140
	00089468                                           140
	00083473                                           139
	00088738B                                          139
	0000T246                                           139
	00073745A                                          138
	00077066                                           138
	00090554                                           138
	00083473A                                          137
	00083626                                           137
	00083473B                                          137
	00076073                                           136
	00072026B                                          135
	00081284                                           135
	00072026C                                          135
	00074235                                           135
	00077066C                                          135
	00077266B                                          135
	00077066B                                          134
	00077066A                                          134
	0000T088                                           133
	00083473C                                          133
	00087402B                                          132
	00087402C                                          132
	00087402D                                          132
	00080499                                           132
	00075120                                           132
	00072528C                                          131
	00072528A                                          131
	00072528                                           131
	00085786                                           130
	00088155                                           130
	00090129                                           130
	00074119C                                          130
	0000T091                                           130
	00073028                                           7
*/

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class ApplicationTests {
	
	//@Autowired
	//private OrcMatController orcMatController;
	
	@Autowired
	private OrccabController orccabController;
	
	@Autowired
	private OrclstRepository orclstRepository;
	
	/*@Test
	public void orcmatTestOne() {
		String numero = "00093214";
		
		System.out.println(String.format("\n--> Start Test %s...",
				numero));

		Orccab orccab = orcMatController.orccab(numero);
		
		System.out.println(String.format("\n--> Finalizado %s...",
				numero));
		
		Assert.assertNotNull(orccab);
	}*/
	
	@Test
	public void orccabControllerTestBiggerOne() throws Exception {
		Orccab orccab = null;
		
		String numero = 
				"00093214"; // 00093214, 23288 produtos unicos carregados
				//"00093280"; // Produto não encontrado: PAICAN000N2000000765 CANTONEIRA N2 PAINEL 765MM
		
		System.out.println(String.format("\n--> Inicio do teste do orcamento %s...",
				numero));

		List<String> ex = new ArrayList<String>();
		List<String> nf = new ArrayList<String>();

        try {
            orccab = orccabController.get(numero);
            
            Assert.assertNotNull(String.format("--> Teste do orcamento %s, resultado: Com Erro(s).",
    				numero), orccab);
            System.out.println(String.format("--> Teste do orcamento %s, resultado: Ok.",
    				orccab.getNumeroOrcamento().trim()));
            System.out.println(String.format(" Acessos na tabela PrdOrc: %d\n Quant. Produtos unicos carregados: %d\n Quant. Materiais: %d\n Quant. Items: %d", 
    				orccab.getCount(), orccab.getPrdOrc().size(), orccab.getOrcMat().size(), orccab.getOrcItm().size()));

        } catch(PrdorcNotFoundException e) {
        	nf.add(String.format("%s: %s", numero, e.getMessage()));
        } catch(Exception e) {
        	ex.add(String.format("%s: %s", numero, e.getMessage()));
        }

        System.out.println(String.format("\nProdutos não encontrados (%d):", nf.size()));
        for(String e : nf) {
        	System.out.println(e);
        }
        System.out.println(String.format("\nOutros erros encontrados (%d):", ex.size()));
        for(String e : ex) {
        	System.out.println(e);
        }
        System.out.println("\nFim dos testes.\n\n");
		
		Assert.assertNotNull(String.format("\n--> Fim do teste do orcamento %s com Erro(s)...",
				numero), orccab);
				
		System.out.println(orccab);
	}
	
	/*@Test
	public void orccabControllerTestOne() {
		String numero = 
				"00073028"; // MOINHO PACIFICO 7 items;
		
		System.out.println(String.format("\n--> Inicio do teste do orcamento %s...",
				numero));

		Orccab orccab = orccabController.get(numero);
		
		Assert.assertNotNull(String.format("\n--> Fim do teste do orcamento %s com Erro(s)...",
				numero), orccab);
				
		System.out.println(orccab);
		
		Assert.assertEquals("Numero do orçamento invalido", numero.trim() + orccab.getOrccabRevisao().trim(), orccab.getNumeroOrcamento().trim());
		
		Assert.assertEquals(String.format("Quantidade de itens deveria ser 1, encontrado %d", orccab.getOrcItm().size()), 1, orccab.getOrcItm().size());
		
		Assert.assertEquals(String.format("Quantidade de mateirais deveria ser 5, encontrado %d", orccab.getOrcMat().size()), 5, orccab.getOrcMat().size());
		
		Assert.assertEquals(String.format("Quantidade de registro de detalhes DO ITEM 1 deveria ser 110, encontrado %d",orccab.getOrcItm().get(0).getOrcdet().size()), 110, orccab.getOrcItm().get(0).getOrcdet().size());
		
		System.out.println(String.format("\n--> Fim do teste do orcamento %s, resultado: Ok...",
				orccab.getNumeroOrcamento()));
	}*/
	
	@Test
	public void orccabControllerFullTest() throws Exception {
		List<String> ex = new ArrayList<String>();
		List<String> nf = new ArrayList<String>();
		List<String> zero = new ArrayList<String>();
		
		Pageable top = new PageRequest(0, 100);
		Iterable<Orclst> range = orclstRepository.findAllByOrderByOrclstNumeroDesc(top);
		
		int maxPrdorc = 0;
		String maxPrdorcOrccab = "";

		int maxMat = 0;
		String maxMatOrccab = "";
		
		int maxItm = 0;
		String maxItmOrccab = "";
		
		Iterator<Orclst> it = range.iterator();
        while(it.hasNext()) {
            String numero = it.next().getOrclstNumero().trim();
            
            try {
            	Integer.parseInt(numero);
            } catch(Exception e) {
            	continue;
            }

            try {
                Orccab orccab = orccabController.get(numero);
                
                Assert.assertNotNull(String.format("--> Teste do orcamento %s, resultado: Com Erro(s).",
        				numero), orccab);
                System.out.println(String.format("--> Teste do orcamento %s, resultado: Ok.",
        				orccab.getNumeroOrcamento().trim()));
                System.out.println(String.format(" Acessos na tabela PrdOrc: %d\n Quant. Produtos unicos carregados: %d\n Quant. Materiais: %d\n Quant. Items: %d", 
        				orccab.getCount(), orccab.getPrdOrc().size(), orccab.getOrcMat().size(), orccab.getOrcItm().size()));

                if (orccab.getPrdOrc().size() > maxPrdorc) {
                	maxPrdorc = orccab.getPrdOrc().size();
                	maxPrdorcOrccab = orccab.getNumeroOrcamento().trim();
                	System.out.println(String.format("**********  Maior orcamento encontrado: %s, %d produtos unicos carregados ********************", maxPrdorcOrccab, maxPrdorc));
                }
                
                if (orccab.getOrcMat().size() > maxMat) {
                	maxMat = orccab.getOrcMat().size();
                	maxMatOrccab = orccab.getNumeroOrcamento().trim();
                	System.out.println(String.format("**********  Maior orcamento encontrado: %s, %d materiais ********************", maxMatOrccab, maxMat));
                }    
           
                if (orccab.getOrcItm().size() > maxItm) {
                	maxItm = orccab.getOrcItm().size();
                	maxItmOrccab = orccab.getNumeroOrcamento().trim();
                	System.out.println(String.format("**********  Maior orcamento encontrado: %s, %d items ********************", maxItmOrccab, maxItm));
                }      
                
                if (orccab.getPrdOrc().size() == 0 ||
            		orccab.getOrcMat().size() == 0 ||
            		orccab.getOrcItm().size() == 0) {
                	zero.add(orccab.getNumeroOrcamento().trim());
                }
                
            } catch(PrdorcNotFoundException e) {
            	nf.add(String.format("%s: %s", numero, e.getMessage()));
            } catch(Exception e) {
            	ex.add(String.format("%s: %s", numero, e.getMessage()));
            }

        }
        System.out.println(String.format("\n\nMaior orcamento encontrado: %s, %d produtos unicos carregados", maxPrdorcOrccab, maxPrdorc));
        System.out.println(String.format("Maior orcamento encontrado: %s, %d materiais", maxMatOrccab, maxMat));
        System.out.println(String.format("Maior orcamento encontrado: %s, %d items", maxItmOrccab, maxItm));
        
        System.out.println(String.format("\nProdutos não encontrados (%d):", nf.size()));
        for(String e : nf) {
        	System.out.println(e);
        }
        System.out.println(String.format("\nOutros erros encontrados (%d):", ex.size()));
        for(String e : ex) {
        	System.out.println(e);
        }
        System.out.println(String.format("\nOrcamentos vazios, sem nenhum items/produtos/materiais (%d):", zero.size()));
        for(String e : zero) {
        	System.out.println(e);
        }
        System.out.println("\nFim dos testes.\n\n");
	}
	
}
