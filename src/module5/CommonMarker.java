package module5;

import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import processing.core.PGraphics;

/** Implements a common marker for cities and earthquakes on an earthquake map
 * 
 * @author UC San Diego Intermediate Software Development MOOC team
 * @author Your name here
 *
 */
public abstract class CommonMarker extends SimplePointMarker {

	// Records whether this marker has been clicked (most recently)
	protected boolean clicked = false;
	
	
	
	public CommonMarker(Location location) {
		super(location);
	}
	
	public CommonMarker(Location location, java.util.HashMap<java.lang.String,java.lang.Object> properties) {
		super(location, properties);
	}
	
	// Getter method for clicked field
	public boolean getClicked() {
		return clicked;
	}
	
	// Setter method for clicked field
	public void setClicked(boolean state) {
		clicked = state;
	}
	
	// Common piece of drawing method for markers; 
	// Note that you should implement this by making calls 
	// drawMarker and showTitle, which are abstract methods 
	// implemented in subclasses
	
	//Ist jetzt keine Anweisung mehr sondern eine Fallunterscheidung. 
	//Unterscheidet je nach Zustand des Markerobjekts in der Runtimeumgebung (Normal, Versteckt, 
	//Ausgewählt), was eigentlich in der visuellen Darstellung gedrawt wird 
	public void draw(PGraphics pg, float x, float y) { 
		// For starter code just drawMaker(...)
		//System.out.println("starte draw");
		if (!hidden) {
			drawMarker(pg, x, y);
			//System.out.println("Location von dem den ich draw ist: ");
			if (this.isSelected()) {
				//System.out.println("This ist " + this.properties.entrySet());
				//System.out.println("selected-strang wird ausgeführt  " );
				showTitle(pg, x, y);  // You will implement this in the subclasses
			}
		}
	}
	public abstract void drawMarker(PGraphics pg, float x, float y);
	public abstract void showTitle(PGraphics pg, float x, float y);
	public abstract double threatCircle();
}