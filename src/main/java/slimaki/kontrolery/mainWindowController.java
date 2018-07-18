package slimaki.kontrolery;

import java.util.Random;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import slimaki.poleTrway.PoleTrawy;
import slimaki.slimaki.Slimak;

public class mainWindowController {


    @FXML
    private TextField getRozmiar;

    @FXML
    private TextField getLiczbaSlimakow;

    @FXML
    private Button ButtonStartSymulacji;

    @FXML
    private GridPane GridPole;

    @FXML
    void StartSim(ActionEvent event) {
    	int rozmiarPola=Integer.parseInt(getRozmiar.getText());
    	int liczbaSlimakow=Integer.parseInt(getLiczbaSlimakow.getText());
    	
    	int rozmiarKafelka=370/rozmiarPola;
    	Rectangle[][] ReckTab=new Rectangle[rozmiarPola+1][rozmiarPola+1];//tablica prostok¹tów które sa
    																	// kolorowane 
  	    
    	//generowanie grida o zadanej lliczbie kolumn i wierszy i wypelnienie go prostokatami o zielonym kolorze
    	System.out.println(rozmiarKafelka);
         for(int i=1;i<=rozmiarPola;i++) {
        	 for(int j=1;j<=rozmiarPola;j++) {
           		 ReckTab[i][j]=createElement(rozmiarKafelka);
        		 GridPole.add( ReckTab[i][j], i, j); 
        	 }
         }
         
      
         PoleTrawy poleTrawy = new PoleTrawy(rozmiarPola, 10, ReckTab);   // utworzenie pola trawy 
         Slimak[] tabSlimakow=new Slimak[liczbaSlimakow];// utworzenie listy slimakow
         Thread[] tabWatkowSlimakow = new Thread[liczbaSlimakow];// lista watkow slimakow
         // losowe generowanie pozycji startowej dla slimakow 
         // oznaczenie ich pozycji na polu trawy 
         // przypisanie im ich pozycji startowej
         for(int i=0;i<liczbaSlimakow;i++) {
        	 int x,y;
        	 Random generator = new Random();
        	 do {
        		 x=generator.nextInt(rozmiarPola);
        		 y=generator.nextInt(rozmiarPola);
        	 }while(poleTrawy.czyWolne(x,y)==false);
        	 
        	 
        	 tabSlimakow[i]=new Slimak(x,y,generator.nextInt(2)+1,poleTrawy);
        	 poleTrawy.zajmijPole(x,y);
        	 tabWatkowSlimakow[i]=new Thread(tabSlimakow[i]);
        	 tabWatkowSlimakow[i].start();
         }
         Thread wzrostTrawy=new Thread(poleTrawy);
         wzrostTrawy.start();
         
    	
    	
}


    private Rectangle createElement(int size) {
        Rectangle rectangle = new Rectangle(size, size);
        rectangle.setStroke(Color.WHITE);
        rectangle.setFill(Color.GREEN);

        return rectangle;
    }

   

}
