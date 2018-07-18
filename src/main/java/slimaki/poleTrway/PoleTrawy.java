package slimaki.poleTrway;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class PoleTrawy implements Runnable{

	private int rozmiar;
	private int poziomTrawy;
	private int[][][] pole;
	private Rectangle[][] ReckTab;
	
	public PoleTrawy(int Rozmiar, int PoziomTrawy, Rectangle[][] reckTab) {
		this.rozmiar=Rozmiar;
		this.poziomTrawy=PoziomTrawy;
		pole=new int[this.rozmiar][this.rozmiar][2];
		this.ReckTab=reckTab;
		inicjujPole();
		
	}
	
	public void run() {
	try {	
		while(true) {
			oznaczWyjedzonaTrawe();
			wzrostTrawy();
			Thread.sleep(500);
			}
		}catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
		
	public int getRozmiar() {
		return this.rozmiar;
	}
	private void inicjujPole() {
		for(int i=0;i<rozmiar;i++) {
			for (int j=0;j<rozmiar;j++) {
				pole[i][j][0]=poziomTrawy;
				pole[i][j][1]=0;
			}
		}
	}
	/**@return true gdy wolne i gdy jest trawa, false gdy zajete lub nie ma trawy
	 * */
	public boolean czyWolne(int x,int y) {
		if(pole[x][y][1]==0 /*&& pole[x][y][0]>0*/) return true;
		return false;
	}
	
	public void zajmijPole(int x,int y) {
		pole[x][y][1]=1; 
	}
	public void zwolnijPole(int x, int y) {
		pole[x][y][1]=0;
	}
	
	public int getPoziomTrawy(int x, int y) {
		return pole[x][y][0];
	}
	
	public void wzrostTrawy() {
		for(int i=0;i<rozmiar;i++) {
			for(int j=0;j<rozmiar;j++) {
				{
					if(pole[i][j][0]<2) {
						pole[i][j][0]++;
					}
				}
			}
		}
	}

	private void oznaczWyjedzonaTrawe() {
		int llicznikSlimakow=0;
		for(int i=0;i<rozmiar;i++) {
			for(int j=0;j<rozmiar;j++) {
				if(pole[i][j][0]<=0) {
					ReckTab[i+1][j+1].setFill(Color.RED);
				}
				else if(pole[i][j][1]==0) {
					ReckTab[i+1][j+1].setFill(Color.GREEN);
					ReckTab[i+1][j+1].setStroke(Color.WHITE);
				}
				if(pole[i][j][1]==1) {
					ReckTab[i+1][j+1].setFill(Color.ORANGE);
					llicznikSlimakow++;
				}
			}
		}
		System.out.println(llicznikSlimakow);
	}
	
	public boolean zmniejszPoziomTrawy(int x, int y,int tempoZjadania) {
		if(pole[x][y][0]>=tempoZjadania) {
			pole[x][y][0]-=tempoZjadania;
			if(pole[x][y][0]>0) {return true;}
			return false;
		}
		else {
			pole[x][y][0]=0;
			return false;
		}
	}


}
