//Description of the basic design for the GUI and the user input listener: 
//
//The map processes clicks on objects as a shift between two distinct modes 
//	- the default mode and the "clicked" mode. 
//
//The indicator for the state of the map is the state of the "lastClicked" variable - whether it 
//references a marker object or not. Depending on the initial state of the map, clicking the mouse 
//makes the map switch either from default to clicked or the other way around - from clicked mode 
//back to default. 
//
//The way back from clicked to default is relatively simple (all the markers get unhidden), 
//so I'll just describe the initial procedure, from default to clicked.
//
//When the map is in default state and a click gets registered, the program checks whether the
//location of the click was in one of the lists of markers (quakeMarkers or cityMarkers). If this is
//the case, it checks in which of these the click happened and sets all other markers to hidden. 
//It also sets the marker reference as the lastClicked variable, so that it can be recognised in which 
//state the map is. The visual map constantly gets updated (re-drawn) by itself, so nothing else is 
//needed to have the effect show up.




package module5;

import java.util.ArrayList;
import java.util.List;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.AbstractShapeMarker;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.MultiMarker;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.providers.Microsoft;
import de.fhpotsdam.unfolding.providers.MBTilesMapProvider;
import de.fhpotsdam.unfolding.utils.MapUtils;
import parsing.ParseFeed;
import processing.core.PApplet;

/** EarthquakeCityMap
 * An application with an interactive map displaying earthquake data.
 * Author: UC San Diego Intermediate Software Development MOOC team
 * @author Your name here
 * Date: July 17, 2015
 * */


public class EarthquakeCityMap extends PApplet {

	// We will use member variables, instead of local variables, to store the data
	// that the setup and draw methods will need to access (as well as other methods)
	// You will use many of these variables, but the only one you should need to add
	// code to modify is countryQuakes, where you will store the number of earthquakes
	// per country.
	
	// You can ignore this.  It's to get rid of eclipse warnings
	private static final long serialVersionUID = 1L;

	// IF YOU ARE WORKING OFFILINE, change the value of this variable to true
	private static final boolean offline = true;
	
	/** This is where to find the local tiles, for working without an Internet connection */
	public static String mbTilesString = "blankLight-1-3.mbtiles";
	
	//feed with magnitude 2.5+ Earthquakes
	private String earthquakesURL = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/2.5_week.atom";
	
	// The files containing city names and info and country names and info
	private String cityFile = "city-data.json";
	private String countryFile = "countries.geo.json";
	
	// The map
	private UnfoldingMap map;
	
	// Markers for each city
	private List<Marker> cityMarkers;
	// Markers for each earthquake
	private List<Marker> quakeMarkers;

	// A List of country markers
	private List<Marker> countryMarkers;
	
	// NEW IN MODULE 5
	private CommonMarker lastSelected;
	private CommonMarker lastClicked;
	
	public void setup() {		
		// (1) Initializing canvas and map tiles
		size(900, 700, OPENGL);
		if (offline) {
		    map = new UnfoldingMap(this, 200, 50, 650, 600, new MBTilesMapProvider(mbTilesString));
		    earthquakesURL = "2.5_week.atom";  // The same feed, but saved August 7, 2015
		}
		else {
			map = new UnfoldingMap(this, 200, 50, 650, 600, new Microsoft.HybridProvider());
			// IF YOU WANT TO TEST WITH A LOCAL FILE, uncomment the next line
		    //earthquakesURL = "2.5_week.atom";
		}
		MapUtils.createDefaultEventDispatcher(this, map);
		
		
		// (2) Reading in earthquake data and geometric properties
	    //     STEP 1: load country features and markers
		List<Feature> countries = GeoJSONReader.loadData(this, countryFile);
		countryMarkers = MapUtils.createSimpleMarkers(countries);
		
		//     STEP 2: read in city data
		List<Feature> cities = GeoJSONReader.loadData(this, cityFile);
		cityMarkers = new ArrayList<Marker>();
		for(Feature city : cities) {
		  cityMarkers.add(new CityMarker(city));
		}
	    
		//     STEP 3: read in earthquake RSS feed
	    List<PointFeature> earthquakes = ParseFeed.parseEarthquake(this, earthquakesURL);
	    quakeMarkers = new ArrayList<Marker>();
	    
	    for(PointFeature feature : earthquakes) {
		  //check if LandQuake
		  if(isLand(feature)) {
		    quakeMarkers.add(new LandQuakeMarker(feature));
		  }
		  // OceanQuakes
		  else {
		    quakeMarkers.add(new OceanQuakeMarker(feature));
		  }
	    }

	    // could be used for debugging
	    printQuakes();
	 		
	    // (3) Add markers to map
	    //     NOTE: Country markers are not added to the map.  They are used
	    //           for their geometric properties
	    map.addMarkers(quakeMarkers);
	    map.addMarkers(cityMarkers);
	    
	    //Test der Buttons

	    
	    
	}  // End setup
	
	
	public void draw() {
		background(0);
		map.draw();
		addKey();
		drawButtons();
	}
	
	public void drawButtons() {

	    fill (255,255,255);
	    rect(100,350,25,25);
	    

	    fill (100,100,100);
	    rect(100,400,25,25);
	}

	/** Event handler that gets called automatically when the 
	 * mouse moves.
	 */
	public int movezaehler = 0;
	@Override
	public void mouseMoved()					//Jedes mal wenn du die Maus bewegst...
	{
		// clear the last selection
		//System.out.println(movezaehler + "fuehre mousemoved aus, erst unselecten...");
		if (lastSelected != null) {
			lastSelected.setSelected(false);
			lastSelected = null;
			}
		//System.out.println("... dann checke ich die marker ob der curser wodrueber ist, und selecte in dem fall");
		selectMarkerIfHover(quakeMarkers); //...teste ob der Cursor durch die Bewegung auf einen Marker aus der Liste gekommen ist (mit dieser Helpermethode)  
		selectMarkerIfHover(cityMarkers);

		//movezaehler++;
	}
	
	// If there is a marker under the cursor, and lastSelected is null 
	// set the lastSelected to be the first marker found under the cursor
	// Make sure you do not select two markers.
	// 
	private void selectMarkerIfHover(List<Marker> markers)
	{
		// TODO: Implement this method
		
		Marker tempSelected;
		
		//Checken ob neues Schild nötig ist und ob alte weg können
		for (Marker marker:markers) {
			if (marker.isInside(map, mouseX, mouseY)) {
				marker.setSelected(true);
				tempSelected = marker;
				}
			else marker.setSelected(false);				
		}


		
	}
	
	/** The event handler for mouse clicks
	 * It will display an earthquake and its threat circle of cities
	 * Or if a city is clicked, it will display all the earthquakes 
	 * where the city is in the threat circle
	 */
	@Override
	public void mouseClicked()
	{
		//Alles deselektieren und Fallunterscheidung - Bedeutet der Klick dass wir vom default zum clicked-mode gehen oder umgekehrt. Je nachdem weitere Methoden 
		System.out.println("Der Mouse-Listener hat einen Mausklick registriert. Jetzt wird die Fallunterscheidung abgespult.");
	
		System.out.println("Fallunterscheidung - Checke ob lastClicked null war oder nicht");
		if (this.lastClicked != null) {	//Teste ob lastClick leer ist oder nicht ("Wurde in der letzten Runde auf etwas geklickt?")
				System.out.println("War nicht null - gehe zu default mode");
				lastClicked = null;
				this.toDefaultMode();
					}
		else 	{
			System.out.println("War null, teste ob der klick auf einen marker war (erst quake dann city");
			if (clickInMarkerList(quakeMarkers) || clickInMarkerList(cityMarkers)) {
				System.out.println("Eines war wahr, gehe zu clicked Mode");
				this.toClickedMode();
				}
			else {
					System.out.println("Klick war nicht auf einen marker, keine weitere aktion");
					
				}
			}
			
		
	}
	
	
	// loop over and unhide all markers
	private void unhideMarkers() {
		for(Marker marker : quakeMarkers) {
			marker.setHidden(false);
		}
			
		for(Marker marker : cityMarkers) {
			marker.setHidden(false);
		}
	}
	
	// helper method to draw key in GUI
	private void addKey() {	
		// Remember you can use Processing's graphics methods here
		fill(255, 250, 240);
		
		int xbase = 25;
		int ybase = 50;
		
		rect(xbase, ybase, 150, 250);
		
		fill(0);
		textAlign(LEFT, CENTER);
		textSize(12);
		text("Earthquake Key", xbase+25, ybase+25);
		
		fill(150, 30, 30);
		int tri_xbase = xbase + 35;
		int tri_ybase = ybase + 50;
		triangle(tri_xbase, tri_ybase-CityMarker.TRI_SIZE, tri_xbase-CityMarker.TRI_SIZE, 
				tri_ybase+CityMarker.TRI_SIZE, tri_xbase+CityMarker.TRI_SIZE, 
				tri_ybase+CityMarker.TRI_SIZE);

		fill(0, 0, 0);
		textAlign(LEFT, CENTER);
		text("City Marker", tri_xbase + 15, tri_ybase);
		
		text("Land Quake", xbase+50, ybase+70);
		text("Ocean Quake", xbase+50, ybase+90);
		text("Size ~ Magnitude", xbase+25, ybase+110);
		
		fill(255, 255, 255);
		ellipse(xbase+35, 
				ybase+70, 
				10, 
				10);
		rect(xbase+35-5, ybase+90-5, 10, 10);
		
		fill(color(255, 255, 0));
		ellipse(xbase+35, ybase+140, 12, 12);
		fill(color(0, 0, 255));
		ellipse(xbase+35, ybase+160, 12, 12);
		fill(color(255, 0, 0));
		ellipse(xbase+35, ybase+180, 12, 12);
		
		textAlign(LEFT, CENTER);
		fill(0, 0, 0);
		text("Shallow", xbase+50, ybase+140);
		text("Intermediate", xbase+50, ybase+160);
		text("Deep", xbase+50, ybase+180);

		text("Past hour", xbase+50, ybase+200);
		
		fill(255, 255, 255);
		int centerx = xbase+35;
		int centery = ybase+200;
		ellipse(centerx, centery, 12, 12);

		strokeWeight(2);
		line(centerx-8, centery-8, centerx+8, centery+8);
		line(centerx-8, centery+8, centerx+8, centery-8);
			
	}

	
	
	// Checks whether this quake occurred on land.  If it did, it sets the 
	// "country" property of its PointFeature to the country where it occurred
	// and returns true.  Notice that the helper method isInCountry will
	// set this "country" property already.  Otherwise it returns false.	
	private boolean isLand(PointFeature earthquake) {
		
		// IMPLEMENT THIS: loop over all countries to check if location is in any of them
		// If it is, add 1 to the entry in countryQuakes corresponding to this country.
		for (Marker country : countryMarkers) {
			if (isInCountry(earthquake, country)) {
				return true;
			}
		}
		
		// not inside any country
		return false;
	}
	
	// prints countries with number of earthquakes
	private void printQuakes() {
		int totalWaterQuakes = quakeMarkers.size();
		for (Marker country : countryMarkers) {
			String countryName = country.getStringProperty("name");
			int numQuakes = 0;
			for (Marker marker : quakeMarkers)
			{
				EarthquakeMarker eqMarker = (EarthquakeMarker)marker;
				if (eqMarker.isOnLand()) {
					if (countryName.equals(eqMarker.getStringProperty("country"))) {
						numQuakes++;
					}
				}
			}
			if (numQuakes > 0) {
				totalWaterQuakes -= numQuakes;
				System.out.println(countryName + ": " + numQuakes);
			}
		}
		System.out.println("OCEAN QUAKES: " + totalWaterQuakes);
	}
	
	
	
	// helper method to test whether a given earthquake is in a given country
	// This will also add the country property to the properties of the earthquake feature if 
	// it's in one of the countries.
	// You should not have to modify this code
	private boolean isInCountry(PointFeature earthquake, Marker country) {
		// getting location of feature
		Location checkLoc = earthquake.getLocation();

		// some countries represented it as MultiMarker
		// looping over SimplePolygonMarkers which make them up to use isInsideByLoc
		if(country.getClass() == MultiMarker.class) {
				
			// looping over markers making up MultiMarker
			for(Marker marker : ((MultiMarker)country).getMarkers()) {
					
				// checking if inside
				if(((AbstractShapeMarker)marker).isInsideByLocation(checkLoc)) {
					earthquake.addProperty("country", country.getProperty("name"));
						
					// return if is inside one
					return true;
				}
			}
		}
			
		// check if inside country represented by SimplePolygonMarker
		else if(((AbstractShapeMarker)country).isInsideByLocation(checkLoc)) {
			earthquake.addProperty("country", country.getProperty("name"));
			
			return true;
		}
		return false;
	}
	
	public int zaehleSelektierte(List<Marker> in_mlist) {
		int anzahlSelektiert =0;
		for (Marker marker:in_mlist) {
			if (marker.isSelected()) {
				anzahlSelektiert++;
			}
		}
	return anzahlSelektiert;
	}
	
	public void setDeselected(List<Marker> in_mlist){
		for (Marker marker:in_mlist){
			marker.setSelected(false);
		}
	}

	public void setHiddenMarkers(List <Marker> in_mlist) {
		for (Marker marker:in_mlist) {
			marker.setHidden(true);
		}
	}
	
	public boolean clickInMarkerList(List<Marker> in_mlist){
		boolean returnbool = false;
		for (Marker marker : in_mlist) {
			if (marker.isInside(map, mouseX, mouseY)) {
				System.out.println("Habe MouseX und Y in einem Marker gefunden, setze Marker als lastClicked");
				this.lastClicked = (CommonMarker) marker;
				returnbool = true;
				}
			
		}
		System.out.println("bin fertig");
		return returnbool;
	}
	
	public void toClickedMode(){
		System.out.println("führe to ClickedMode aus - verstecke alle marker und entscheide ob city oder quake neugemalt werden muss (showlastclicked*");
		setHiddenMarkers(quakeMarkers);
		setHiddenMarkers(cityMarkers);
		
		if (clickInMarkerList(cityMarkers)){
			showLastClickedCity();
		}
		else showLastClickedQuake();

		
	}
	
	public void showLastClickedCity() {
		System.out.println("Starte showLastClicked");
		for (Marker eqmarker:quakeMarkers){
			if (eqmarker.getDistanceTo(lastClicked.getLocation()) < ((EarthquakeMarker) eqmarker).threatCircle()){
				System.out.println("distanceTo war kleiner als Threatcircle, setze hidden auf false");
				eqmarker.setHidden(false);
				}
			else System.out.println("war nicht zu nahe");
		}
		lastClicked.setHidden(false);
	}
	
	public void showLastClickedQuake() {
		System.out.println("Starte showLastClickedQuake");
		this.setHiddenMarkers(cityMarkers);
		
		for (Marker cmarker:cityMarkers){
			if (lastClicked.getDistanceTo(cmarker.getLocation()) < ((EarthquakeMarker) lastClicked).threatCircle()){
				System.out.println("distanceTo war kleiner als Threatcircle, setze hidden auf false");
				cmarker.setHidden(false);
				}
			else {
				System.out.println("war nicht zu nahe");
				cmarker.setHidden(true);
				System.out.println(((CityMarker)cmarker).getCity() + "wurde auf hidden gesetzt");
				System.out.println("ishiddenzustand von cmarker ist " +cmarker.isHidden());
			}
		}
		
		//Potentiell: Wenns ein Seebeben ist: Linien zu Städten ziehen
//		if(!((EarthquakeMarker) lastClicked).isOnLand) {
//			lastClicked.drawCityLines(pg, );
//		}

		
		lastClicked.setHidden(false);
		
		
		
	}
	
	
	
	
	public void toDefaultMode(){
		System.out.println("Führe toDefaultMode aus");
		this.unhideMarkers();
	}
}
