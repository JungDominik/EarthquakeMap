import java.util.*;
import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.providers.Microsoft;
import de.fhpotsdam.unfolding.utils.MapUtils;


public class MyMapAttempt extends PApplet{
	
	private UnfoldingMap mapobject; //"Ist eine Instance variable"
									//	a) warum nicht "=new UnfoldingMap"?
									//	b) Heisst das, es wird ein "mymapattempt"-objekt instantiiert, das wiederum eine "UnfoldingMap"-instanz namens mapobject hat?
	
									//Wer ruft setup auf? Eine main-method die noch geschrieben werden muss? 
									//Ein Konstruktor von MyMapAttempt-Objekten?
									//ODER: Funktioniert "Run as Java Applet" so, dass es einfach ein Objekt 
									//dieser Klasse erstellt? (Datei enthält immer eine Klasse mit dem Dateinamen)
	public void setup() {
		size(950,600,OPENGL);
		mapobject = new UnfoldingMap(this, 200,50,700,500, new Microsoft.RoadProvider());	//Warum "this?" - 
											//- Als referenz auf PApplet, um was darin ist irgendwie zu benutzen?
		mapobject.zoomToLevel(2);
		MapUtils.createDefaultEventDispatcher(this,mapobject);
		
		
		
		
		//Hier werden die Werte als Pointfeature-Objekte verpackt und in eine Liste gesteckt
		List<PointFeature> pfliste = new ArrayList<PointFeature>();
		
		
		
		Location location_for_test =  new Location(-38.1,-73.03);
		Location location_for_test2 = new Location(30,30);
		
		PointFeature beispielpf = new PointFeature(location_for_test);
		PointFeature beispielpf2 = new PointFeature(location_for_test2);
		
		//Das PointFeature-objekt hat schon Location (haben wir gebraucht um es zu bauen), jetzt fuegen wir noch andere eigenschaften dieses Markers (der fuer ein Erdbeben-Ereignis steht) hinzu 
		beispielpf.addProperty("title", "beispielerdbeben");
		beispielpf.addProperty("richterschwere", "9.0");
		beispielpf.addProperty("date", "May 22, 1960");
		beispielpf.addProperty("year", "1960");
		
		beispielpf2.addProperty("title", "und noch ein erdbeben");
		beispielpf2.addProperty("richterschwere", "7");
		beispielpf2.addProperty("date", "May 22, 1960");
		beispielpf2.addProperty("year", "2006");
		
		//Pointfeature fuer den Marker ist fertig - kommt in die liste
		pfliste.add(beispielpf);
		pfliste.add(beispielpf2);
		//Aus diesen Pointfeatureobjekten in der Liste werden Marker-Objekte gebastelt und in eine markerliste gesteckt
		ArrayList<SimplePointMarker> markerlist = new ArrayList<SimplePointMarker>();
		for (PointFeature listenelement : pfliste) {
			SimplePointMarker temp_marker = new SimplePointMarker(listenelement.getLocation(), listenelement.getProperties());
			markerlist.add(temp_marker);
			
		
		//Die Markerliste wird ausgelesen und die objekte darin dem Mapobjekt hinzugefuegt(zumindest die Referenzen)	
		for (SimplePointMarker markergrade : markerlist) {
			mapobject.addMarker(markergrade);
			}
		
		//Farbueberlegungen - erst farben definieren, dann die Marker der reihe nach auslesen und anmalen
		int yellow 	= color(255,255,0);
		int grey 	= color (100,100,100);
		
		for (SimplePointMarker anzumalend_mk : markerlist){
		if ( Integer.parseInt((String)anzumalend_mk.getProperty("year")) > 2000) {
				anzumalend_mk.setColor (yellow);
				}
			else {
				anzumalend_mk.setColor(grey);
			}
			
			}
		
		

		
		}
		
	}
	public void addKey(){
		//Die Legende basteln
		fill(255,255,255);	
		rect(25,40, 150, 200,15);
		
		fill(0,0,0);
		text("Big earthquake", 65, 80);
		fill (255,0,0);
		ellipse(40,78,3,3);
		
		fill(0,0,0);
		text("Medium earthquake", 65, 110);
		fill (0,255,0);
		ellipse(40,108,3,3);
		
		
		fill(0,0,0);
		text("Small earthquake", 65, 140);
		fill (0,0,255);
		ellipse(40,138,3,3);
	}
	
	
	public void draw(){
		background(220);
		mapobject.draw();			//Nimm karte und alle overlay-dinger UND UPDATE
		
		addKey();					//Soll eine Legende zeichnen [aber funktioniert nicht ... =( ]
		
	}
}