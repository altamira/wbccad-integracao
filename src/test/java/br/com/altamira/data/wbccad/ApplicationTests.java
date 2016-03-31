package br.com.altamira.data.wbccad;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import br.com.altamira.data.wbccad.controller.OrcMatController;
import br.com.altamira.data.wbccad.controller.OrccabController;
import br.com.altamira.data.wbccad.model.Orccab;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class ApplicationTests {
	
	@Autowired
	private OrcMatController orcMatController;
	
	@Autowired
	private OrccabController orccabController;
	
	//@Test
	public void contextLoads() {
		String numero = "00089067C";
		
		System.out.println(String.format("\n--> Start Test %s...",
				numero));

		Orccab orccab = orcMatController.orccab(numero);
		
		System.out.println(String.format("\n--> Finalizado %s...",
				numero));
		
		Assert.assertNotNull(orccab);
	}
	
	@Test
	public void Orccab() {
		String numero = 
				/*
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
				00073028                                           7*/
				"00073028"; // MOINHO PACIFICO 7 items;
		
		System.out.println(String.format("\n--> Inicio do teste do orcamento %s...",
				numero));

		Orccab orccab = orccabController.get(numero);
		
		Assert.assertNotNull(String.format("\n--> Fim do teste do orcamento %s com Erro(s)...",
				numero), orccab);
		
		System.out.println(String.format("\n--> Fim do teste do orcamento %s, resultado: Ok...",
				orccab.getNumeroOrcamento()));
	}
	
}
