package it.polito.tdp.poweroutages.model;

import java.util.*;


import it.polito.tdp.poweroutages.DAO.PowerOutageDAO;

public class Model {
	
	PowerOutageDAO podao;
	private List<PowerOut> best;
	private List<PowerOut> eventList;
	

	int contPersone; 
	
	// in parziale : 
	int recent=0;
	int vecchio=10000; 
	
	int maxPersoneCoinvolte=0; 
	
	
	public Model() {
		podao = new PowerOutageDAO();
	
	}
	
	public List<Nerc> getNercList() {
		return podao.getNercList();
	}
	
	public int getMaxPersone() {
		return maxPersoneCoinvolte; 
	}
	
	
	/** devo MASSIMIZZARE il numero di persone, entrando nei parametri **/ 
	
	public List<PowerOut> eventiSelezionati(int anni, int oreDisservizio, Nerc nrc) {
		
		 List<PowerOut> parziale= new ArrayList<PowerOut>();  
		 
		 //tutti gli eventi successi al nerc specifico.
		 eventList= podao.getAllEventi(nrc); 
		 contPersone=0;
		 
		 eventList.sort(new ComparatoreByData());   //new 


		 best = new ArrayList<>();		
		 
		 cerca(parziale, anni, oreDisservizio );
		 return best; 
	}

	private void cerca(List<PowerOut> parziale,  int anni, int oreDisservizio) {
		
		/** qui la soluzione è sicuramente valida... ma verifico se è il numero Max di persone  */ 
		
		if( contPersone(parziale) >= maxPersoneCoinvolte  ) {  // da cambiare
			maxPersoneCoinvolte= contPersone(parziale);
			best = new ArrayList<>(parziale);
			//return   NON SERVE
		}
			
  /**  2. Calcolo seq di eventi, in "parziale" */		
		
		 //  ********************   NON serve "ELSE"   ******************** 
		
	    //caso intermedio, vedo se è il caso di aggiungere l'evento in parziale
			for(PowerOut pp: eventList ) {
				
				if(aggiuntaValida(pp, parziale,anni,oreDisservizio) == true && !parziale.contains(pp)) {
					
					parziale.add(pp);
					cerca(parziale, anni, oreDisservizio);
					// back-tracking
					parziale.remove(parziale.size()-1);	
				}	
		}	
	}


/** conta le persone presenti in una data PARZIALE **/ 
	private int contPersone(List<PowerOut> parziale) {
		int cont=0;
		
		for(PowerOut pp: parziale) {
			cont+= pp.getTotPersone();
		}
		return cont;
	}


	
	private boolean aggiuntaValida( PowerOut pp, List<PowerOut> parziale, int anni, int oreDisservizio) {
   /** 
    * 1. n ore TOT di disservizio <= "oreDisservizio"
    * 
    * 2. anno newest - anno oldest <= "anni1"
    * */
		
		//   #1 
			int contOreDisservizio= summOre(parziale);
			if( (contOreDisservizio + pp.getDurata() ) > oreDisservizio )
				return false;
		
		//   #2 
			/* 
	        valutaData(pp);
			if(recent-vecchio > anni) {
				return false;
			} */ 
		
			
			
			
		/** sfrutto il fatto che  la lista iniziale è ordinata cronologicamente 
		 
		 * ****** ALT: non si aggiorna mai così  ****** */
			
			if (parziale.size() >=1) {
				
				parziale.add(pp);  //senza che ordino dopo perche' la lista originaria è già ordinata
				int y1 = parziale.get(0).getYear();
				int y2 = parziale.get(parziale.size() - 1).getYear();
				
				parziale.remove(pp); // non è sicuro che verrà inserito
				
				if ((y2 - y1) > anni) {
					return false;
				}
			}
			
		
			return true;
			
			
	
	}
/*  MI SA CHE NON VA BENE PERCHE' NON SI AGGIORNA SOLO QUANDO DOVREBBE in questo modo : */ 
	
	/*
	private void valutaData(PowerOut pp) {
		
		if(pp.getYear() >= recent) {
	//	** è sbaglaito aggiornare ora...
		
			// data più recente	
		}
		
		if(pp.getYear() <= vecchio) {
			// data più recente
		}
		
	} 
	
	/////////////////////////////////////////////*/

	private int summOre(List<PowerOut> parziale) {
		int sum=0;
		
		for(PowerOut pp: parziale) 
			sum+= pp.getDurata();
		
		
		return sum;
	}



	
	
	
	
	
	
}
