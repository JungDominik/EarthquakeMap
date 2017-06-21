package module5;

import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import processing.core.PConstants;
import processing.core.PGraphics;

/** Implements a visual marker for cities on an earthquake map
 * 
 * @author UC San Diego Intermediate Software Development MOOC team
 * @author Your name here
 *
 */
// TODO: Change SimplePointMarker to CommonMarker as the very first thing you do 
// in module 5 (i.e. CityMarker extends CommonMarker).  It will cause an error.
// That's what's expected.
public class CityMarker extends CommonMarker {
	
	public static int TRI_SIZE = 5;  // The size of the triangle marker
	protected static int BOX_FAKTOR = 8;
	
	public CityMarker(Location location) {
		super(location);
	}
	
	
	public CityMarker(Feature city) {
		super(((PointFeature)city).getLocation(), city.getProperties());
		// Cities have properties: "name" (city name), "country" (country name)
		// and "population" (population, in millions)
	}

	
	/**
	 * Implementation of method to draw marker on the map.
	 */
	public void draw(PGraphics pg, float x, float y) {
		
		if (!hidden) {
			drawMarker(pg, x, y);
			if(selected){
				showTitle(pg, x, y);
			}
		}
		

	}
	
	/** Show the title of the city if this marker is selected */
	public void showTitle(PGraphics pg, float x, float y)
	{
		//System.out.println("Starte showTitle f�r Stadt");
		String displaystring = this.getCity();
				
		pg.fill (200,200,10);
		pg.rect(x-7,y-40, displaystring.length() * BOX_FAKTOR, 20);
		pg.fill(0,0,0);
		pg.text(displaystring, x-5, y-25);
	}
	
	public void drawMarker(PGraphics pg, float x, float y){
		//System.out.println("draw in CityMarker wird ausgef�hrt");
		
		if (this.isSelected()) {
			//System.out.println("This ist " + this.properties.entrySet());
			//System.out.println("selected-strang wird ausgef�hrt  " );
			showTitle(pg, x, y);  // You will implement this in the subclasses
		}
		
		// Save previous drawing style
		pg.pushStyle();
		
		// IMPLEMENT: drawing triangle for each city
		pg.fill(150, 30, 30);
		pg.triangle(x, y-TRI_SIZE, x-TRI_SIZE, y+TRI_SIZE, x+TRI_SIZE, y+TRI_SIZE);
		
		// Restore previous drawing style
		pg.popStyle();
	}
	
	/* Local getters for some city properties.  
	 */
	public String getCity()
	{
		return getStringProperty("name");
	}
	
	public String getCountry()
	{
		return getStringProperty("country");
	}
	
	public float getPopulation()
	{
		return Float.parseFloat(getStringProperty("population"));
	}
}