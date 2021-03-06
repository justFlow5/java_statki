package statki;
import java.util.Scanner;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Gracz {
	private static final int[] typyStatkow = {5, 4, 3, 3, 2, 2, 1, 1};
    ArrayList<Statek> statki = new ArrayList<>();
    public  int liczbaPunktow;
    public  String ID;
    Scanner myObj = new Scanner(System.in);
    
    
    public Gracz(String ID) {
	    for (int i = 0; i < typyStatkow.length; i++) {
	    	Statek tempShip = new Statek(typyStatkow[i]);
	    	statki.add(tempShip);
			//System.out.println(statki.get(i));
	    }
	    this.liczbaPunktow = 0;
	    this.ID = ID;
	    }
       
        public int getLiczbaPunktow() {
        	return this.liczbaPunktow;
        }
        public String getID() {
        	return this.ID;
        }
	    
	    public void ustawStatek(Pole[][] pole, Plansza plansza, String[][] plansza2, Gracz wrog) { //Gracz wrog potrzebny?
	    	
	    	do { 
	    		System.out.println(getID() + "! Rozlokuj swoje statki!");
	    		}
	    	while (false);
	    	
	    	for (Statek statek : statki) {
	    		System.out.println("\nUstaw " + statek.liczbaMasztow + "-masztowca: ");
	    		int liczbaPozostalychMasztow = statek.liczbaMasztow;
	    		while (liczbaPozostalychMasztow > 0) {
	    			
	    			int locX;
	    			int locY;
	    			char[] c;
	    			System.out.println("\nWprowadz wsp�rz�dne statku:"); 
	    			c = myObj.next().toCharArray();
	    			int num1 = 0;
	    			if (c.length > 3) {
	    				System.out.println("Podano z�e wsp�rz�dne!"); 
    					continue;
	    			}
	    			else if(c.length == 3) {
	    				if (c[1] == '1' && c[2] == '0') {
	    					num1 = 10;
	    				}
	    				else {
	    					System.out.println("Podano z�e wsp�rz�dne!"); 
	    					continue;
	    				}
	    			}
	    			else{
	    				if (c.length == 2) {
	    				num1 = Character.getNumericValue(c[1]);
	    				}
	    				else
	    				{
	    					System.out.println("Podano z�e wsp�rz�dne!"); 
	    					continue;
	    				}
	    			}

	    			locX = helperFunctionWiersz(num1);
	    			locY = helperFunctionKol(c[0]);
	    			
	    			
	    			if (statek.prawidlowoUstawiony(pole, locX, locY)) { //maszt wstawiony
	    				pole[locX][locY].setStatek();
	    				pole[locX][locY].odkryj();
	    				plansza.sprawdzPlansze(plansza2, pole, wrog ); //wrog jako ostatni arg dodany
		    			plansza.rysujPlansze(plansza2);	
		    			liczbaPozostalychMasztow--; 
	    			}

	    			if (liczbaPozostalychMasztow == 0) {
	    				System.out.println("\n" + statek.liczbaMasztow + "-masztowiec zosta� ustawiony prawid�owo.\n");
	    				System.out.println("--------------------------------------------------------");
	    			}

	    		}			
	    	}
	    	
	    	System.out.println("\n| | | | | Wszystkie statki zostaly ustawione! | | | | |");
	    	System.out.println("--------------------------------------------------------\n");
	    	
	    	
	    	
	    	
	    	//zakryj statki dla przeciwnika    
            for (int i = 0; i < 10; i++) {
				for (int j = 0; j < 10; j++) {
					if(pole[i][j].statek()) {
						pole[i][j].setZakryte();
					}
				}
			}
            
    }
	    // dodac warunki do strzalu: nie moze w to samo miejsce, nie moze poza plansze
	    public void strzelaj(Pole[][] poleWroga, Plansza plansza, String[][] plansza2, Gracz przeciwnik ) {
			int locX;
			int locY;
			//Scanner myObj = new Scanner(System.in);
			System.out.println("Twoja kolej " + getID() + "!");
			System.out.println("\nWprowadz wsp�rz�dne statku: /n"); 
			char[] c;
			c = myObj.next().toCharArray();
			int num1 = 0;
			System.out.print(c.length);
			if (c.length > 3) {
				System.out.println("Podano z�e wsp�rz�dne!"); 
			}
			else if(c.length == 3) {
				if (c[1] == '1' && c[2] == '0') {
					num1 = 10;
				}
				else {
					System.out.println("Podano z�e wsp�rz�dne!"); 
				}
			}
			else{
				num1 = Character.getNumericValue(c[1]);
			}

			locX = helperFunctionWiersz(num1);
			locY = helperFunctionKol(c[0]);
			
			if (poleWroga[locX][locY].statek()) {
				for (Statek statek : przeciwnik.statki) {
					if (statek.zajmowanePola.contains(poleWroga[locX][locY])) {
						if(statek.okreslCzyTrafionyZatopiony(poleWroga, locX, locY)) {
							liczbaPunktow++;
							poleWroga[locX][locY].setTrafione(); // ustaw jako trafione
							plansza.sprawdzPlansze(plansza2, poleWroga, przeciwnik);
							plansza.rysujPlansze(plansza2);	
							System.out.println("Trafiony!\nGracz id: "+ getID() +" |  Liczba punkt�w: " + getLiczbaPunktow() + "\n");
							
						}
					}
				}
			}
			else {
				poleWroga[locX][locY].setTrafione();
				plansza.sprawdzPlansze(plansza2, poleWroga, przeciwnik);
				plansza.rysujPlansze(plansza2);	
				System.out.println("\nPud�o!\nGracz id: "+ getID() +" |  Liczba punkt�w: " + getLiczbaPunktow() + "\n"); 
				
			}

	    }
	    
	    public int helperFunctionWiersz(int wiersz) {
	        if (wiersz == 0) return 0;
  	       return wiersz - 1;
  }
	   
	    
	    
	    public int helperFunctionKol(char input) {
	    	int nowaKolumna = -1;
			
			switch(input){
			case 'A':
				nowaKolumna = 0;
				break;
			case 'B':
				nowaKolumna = 1;
				break;
			case 'C':
				nowaKolumna = 2;
				break;
			case 'D':
				nowaKolumna = 3;
				break;
			case 'E':
				nowaKolumna = 4;
				break;
			case 'F':
				nowaKolumna = 5;
				break;
			case 'G':
				nowaKolumna = 6;
				break;
			case 'H':
				nowaKolumna = 7;
				break;
			case 'I':
				nowaKolumna = 8;
				break;
			case 'J':
				nowaKolumna = 9;
				break;
			}
			return nowaKolumna;
			}
	    
	    
	    }

