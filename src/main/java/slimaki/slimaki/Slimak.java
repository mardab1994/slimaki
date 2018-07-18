package slimaki.slimaki;

import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import slimaki.modelRuchuSlimaka.modelRuchu;
import slimaki.poleTrway.PoleTrawy;

public class Slimak implements Runnable, modelRuchu{
	
	private static int nextId=1;
	private int id;
	private int xPos;
	private int yPos;
	private int tempoZjadania;
	private PoleTrawy poleTrawy;

	private boolean[] listaMozliwychRuchow=new boolean[8];
	private Lock myLock=new ReentrantLock();
	
	public Slimak(int X, int Y, int TempoZjadania, PoleTrawy pole) {
		id=nextId;
		nextId++;
		
		this.xPos=X;
		this.yPos=Y;
		this.tempoZjadania=TempoZjadania;
		this.poleTrawy=pole;
		for(int i=0;i<8;i++) {					//narazie brak mo¿liwych ruchow
			this.listaMozliwychRuchow[i]=false;
		}
	}
	
	public void run() {
		try {
			while(true) {
				if(poleTrawy.getPoziomTrawy(this.xPos, this.yPos)>0){
					this.zjadaj();
				}
				else{// brak trawy na danym polu trzeba zmienic pozycje 
					this.myLock.lock();
					this.znajdzWolnePola();//znajduje wolne pola wokó³ slimaka
					int kierunek=this.losujKierunekRuchu();
					if(kierunek>=0) {
						
						poleTrawy.zwolnijPole(this.xPos, this.yPos);
						if(poleTrawy.czyWolne(this.xPos, this.yPos)) {
							switch(kierunek) {
								case 0:
									this.moveUp();
									break;
								case 1:
									this.moveUp();
									this.moveRight();
									break;
								case 2:
									this.moveRight();
									break;
								case 3:
									this.moveRight();
									this.moveDown();
									break;
								case 4:
									this.moveDown();
									break;
								case 5:
									this.moveDown();
									this.moveLeft();
									break;
								case 6:
									this.moveLeft();
									break;
								case 7:
									this.moveLeft();
									this.moveUp();
									break;
							}
						poleTrawy.zajmijPole(this.xPos, this.yPos);
						this.zerujMozliweRuchy();
						myLock.unlock();
						}
					
					}else {
						int oldX=this.xPos;
						int oldY=this.yPos;
							
						teleport();
						poleTrawy.zwolnijPole(oldX, oldY);

						poleTrawy.zajmijPole(this.xPos, this.yPos);
						this.zerujMozliweRuchy();
						myLock.unlock();
					}	
				}
				Thread.sleep(150);		
			}
		}catch(InterruptedException e) {
			e.printStackTrace();
		}finally {
			myLock.unlock();
		}
	}
	
		
	private void zjadaj() {
		poleTrawy.zmniejszPoziomTrawy(this.xPos, this.yPos, this.tempoZjadania);
	}
	
	private void znajdzWolnePola() {
		zerujMozliweRuchy();
		if(this.xPos==0 && this.yPos==0) {//lewy gorny rog
			if(poleTrawy.czyWolne(this.xPos+1, this.yPos)) 	{listaMozliwychRuchow[2]=true;}
			if(poleTrawy.czyWolne(this.xPos+1, this.yPos+1)){listaMozliwychRuchow[3]=true;}
			if(poleTrawy.czyWolne(this.xPos, this.yPos+1)) 	{listaMozliwychRuchow[4]=true;}
		}
		else if(this.xPos==(poleTrawy.getRozmiar()-1) && this.yPos==0) {//prawy gorny rog
			if(poleTrawy.czyWolne(this.xPos-1, this.yPos)) 	{listaMozliwychRuchow[6]=true;}
			if(poleTrawy.czyWolne(this.xPos-1, this.yPos+1)){listaMozliwychRuchow[5]=true;}
			if(poleTrawy.czyWolne(this.xPos, this.yPos+1)) 	{listaMozliwychRuchow[4]=true;}
		}
		else if(this.xPos==0 && this.yPos==(poleTrawy.getRozmiar()-1)) {//lewy dolny rog
			if(poleTrawy.czyWolne(this.xPos, this.yPos-1)) 	{listaMozliwychRuchow[0]=true;}
			if(poleTrawy.czyWolne(this.xPos+1, this.yPos-1)){listaMozliwychRuchow[1]=true;}
			if(poleTrawy.czyWolne(this.xPos+1, this.yPos)) 	{listaMozliwychRuchow[2]=true;}
		}
		else if(this.xPos==(poleTrawy.getRozmiar()-1) && this.yPos==(poleTrawy.getRozmiar()-1)) {//prawy dolny rog
			if(poleTrawy.czyWolne(this.xPos-1, this.yPos)) 	{listaMozliwychRuchow[6]=true;}
			if(poleTrawy.czyWolne(this.xPos-1, this.yPos-1)){listaMozliwychRuchow[7]=true;}
			if(poleTrawy.czyWolne(this.xPos, this.yPos-1)) 	{listaMozliwychRuchow[0]=true;}
		}
		else if(this.xPos==0 && (this.yPos>0 && this.yPos<(poleTrawy.getRozmiar()-1))) {//lewa sciana
			if(poleTrawy.czyWolne(this.xPos, this.yPos-1)) 	{listaMozliwychRuchow[0]=true;}
			if(poleTrawy.czyWolne(this.xPos+1, this.yPos-1)){listaMozliwychRuchow[1]=true;}
			if(poleTrawy.czyWolne(this.xPos+1, this.yPos)) 	{listaMozliwychRuchow[2]=true;}
			if(poleTrawy.czyWolne(this.xPos+1, this.yPos+1)){listaMozliwychRuchow[3]=true;}
			if(poleTrawy.czyWolne(this.xPos, this.yPos+1)) 	{listaMozliwychRuchow[4]=true;}
		}
		else if(this.yPos==0 && (this.xPos>0 && this.xPos<(poleTrawy.getRozmiar()-1))) { //górna sciana
			if(poleTrawy.czyWolne(this.xPos-1, this.yPos)) 	{listaMozliwychRuchow[6]=true;}
			if(poleTrawy.czyWolne(this.xPos-1, this.yPos+1)){listaMozliwychRuchow[5]=true;}
			if(poleTrawy.czyWolne(this.xPos, this.yPos+1)) 	{listaMozliwychRuchow[4]=true;}
			if(poleTrawy.czyWolne(this.xPos+1, this.yPos+1)){listaMozliwychRuchow[3]=true;}
			if(poleTrawy.czyWolne(this.xPos+1, this.yPos)) 	{listaMozliwychRuchow[2]=true;}
		}
		else if(this.xPos==(poleTrawy.getRozmiar()-1) && (this.yPos>0 && this.yPos< (poleTrawy.getRozmiar()-1))) {// prawa sciana
			if(poleTrawy.czyWolne(this.xPos, this.yPos-1)) 	{listaMozliwychRuchow[0]=true;}
			if(poleTrawy.czyWolne(this.xPos-1, this.yPos-1)){listaMozliwychRuchow[7]=true;}
			if(poleTrawy.czyWolne(this.xPos-1, this.yPos)) 	{listaMozliwychRuchow[6]=true;}
			if(poleTrawy.czyWolne(this.xPos-1, this.yPos+1)){listaMozliwychRuchow[5]=true;}
			if(poleTrawy.czyWolne(this.xPos, this.yPos+1)) 	{listaMozliwychRuchow[4]=true;}
		}
		else if(this.yPos==(poleTrawy.getRozmiar()-1) && (this.xPos>0 && this.xPos<(poleTrawy.getRozmiar()-1))) {//dolna sciana
			if(poleTrawy.czyWolne(this.xPos-1, this.yPos)) 	{listaMozliwychRuchow[6]=true;}
			if(poleTrawy.czyWolne(this.xPos-1, this.yPos-1)){listaMozliwychRuchow[7]=true;}
			if(poleTrawy.czyWolne(this.xPos, this.yPos-1)) 	{listaMozliwychRuchow[0]=true;}
			if(poleTrawy.czyWolne(this.xPos+1, this.yPos-1)){listaMozliwychRuchow[1]=true;}
			if(poleTrawy.czyWolne(this.xPos+1, this.yPos)) 	{listaMozliwychRuchow[2]=true;}
		}
		else {//dowolna pozycja z dala od rógów i scian 
			if(poleTrawy.czyWolne(this.xPos-1, this.yPos)) 	{listaMozliwychRuchow[6]=true;}
			if(poleTrawy.czyWolne(this.xPos-1, this.yPos-1)){listaMozliwychRuchow[7]=true;}
			if(poleTrawy.czyWolne(this.xPos, this.yPos-1)) 	{listaMozliwychRuchow[0]=true;}
			if(poleTrawy.czyWolne(this.xPos+1, this.yPos-1)){listaMozliwychRuchow[1]=true;}
			if(poleTrawy.czyWolne(this.xPos+1, this.yPos)) 	{listaMozliwychRuchow[2]=true;}
			if(poleTrawy.czyWolne(this.xPos+1, this.yPos+1)){listaMozliwychRuchow[3]=true;}
			if(poleTrawy.czyWolne(this.xPos, this.yPos+1)) 	{listaMozliwychRuchow[4]=true;}
			if(poleTrawy.czyWolne(this.xPos-1, this.yPos+1)){listaMozliwychRuchow[5]=true;}
		}
	}
	
	private void zerujMozliweRuchy() {
		for(int i=0;i<8;i++) {
			this.listaMozliwychRuchow[i]=false;
		}
	}
	
	private int losujKierunekRuchu() {
		int kierunek=-1;
		 Random generator = new Random();
		 if(listaMozliwychRuchow[0]==false && listaMozliwychRuchow[1]==false && listaMozliwychRuchow[2]==false &&	// gdy nie ma mozliwych ruchuchow zwroc -1
				 listaMozliwychRuchow[3]==false  && listaMozliwychRuchow[4]==false && listaMozliwychRuchow[5]==false &&
				 listaMozliwychRuchow[6]==false  && listaMozliwychRuchow[7]==false ) {
			 return kierunek;
		 }else {
		do {									// losuj kierunek ruchu do momentu az wylosuje mozliwy kierunek ruchu 
			kierunek=generator.nextInt(7);
		}while(listaMozliwychRuchow[kierunek]==false); 
		return kierunek;
	}}
	
	public void moveUp() {
		this.yPos-=1;
	}

	public void moveDown() {
		this.yPos+=1;
	}

	public void moveLeft() {
		this.xPos-=1;
	}

	public void moveRight() {
		this.xPos+=1;
	}
	
	private void teleport() {
		int x;
		int y;
   	 	Random generator = new Random();
   	 	do {
   	 		x=generator.nextInt(poleTrawy.getRozmiar()-1);
   	 		y=generator.nextInt(poleTrawy.getRozmiar()-1);
   	 	}while(poleTrawy.czyWolne(x,y)==false && x!=this.xPos && y!=this.yPos);
   	 	this.xPos=x;
   	 	this.yPos=y;
   	 	System.out.println("Teleport");
	}
	
	
}
