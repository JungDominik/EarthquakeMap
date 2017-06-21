
import processing.core.*;

public class MyPApplet_test extends PApplet {

	private String URL = "http://events-media.nationalgeographic.com/media/images/dynamic_leads/PooleDL.jpg"; 
	private PImage backgroundImg;	
	
	//Erstmal aufsetzen ( a)Eigenschaften wie Groesse, b) zu benutzende Objekte in den Speicher)
	public void setup() {
		size (650,500);
		backgroundImg = loadImage(URL, "jpg");		//Bild erstmal nur im Speicher der Javaumgebung abspeichern, danach kann mans verwenden
		}
	
	
	
	//Dann tatsächlich Zeichnen (Hier nur einmal, spaeter kann auch geloopt werden). 2 Elemente mit aenderungen die aber in ihrer eigenen zeile stehen. a) Hintergrundbild, geresized. b)Ellipse, gefuellt mit farbe
	public void draw(){
		backgroundImg.resize(width,height);		//Erstmal objekt im speicher groesse aendern
		image (backgroundImg, 0, 0); //Koordinaten: Wo ist die linke obere Ecke des Bilds
		int[] colorarray = colorSecondsMethod(second()); 
		System.out.println(colorarray[0]);
		System.out.println(colorarray[1]);
		System.out.println(colorarray[2]);
		fill(colorarray[0], colorarray[1],colorarray[2]);
		ellipse (width/4, height/8, 50,50);
		
		fill (255,255,255);
		textSize(15);
		text("Testtext: Lorem Ipsum dubidum. Cogito ergo sum, item carthago inferno", 100,100);
		
	}
	
	private int[] colorSecondsMethod (int timeinput) {
		
		float entfernung = Math.abs(30-timeinput);
		float ratio = entfernung/30;
		int [] arrayoutput = {(int) (ratio *255),(int) (ratio *255),0};
		return arrayoutput; 
		
		
	}
	
}