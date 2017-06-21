package guimodule;

import processing.core.PApplet;

//Warum machen wir das hier alles nochmal? Also Methoden definieren etc. 
//Wir haben doch schon eine mit der das alles geht (haben wir schon mal benutzt)

public class MyDisplay extends PApplet {

	public void setup(){
		size(400,400);
		background(100,100,100);
	}
		
	public void draw(){
		fill(255,255,0);
		ellipse (width/2,height/2,390,390);
		
		fill (255,255,255);
		ellipse (width/4, height/3, 20,20);
		ellipse (width*3/4, height/3, 20,20);
		
		
		noFill();
		arc(width/2, height*3/4, width*2/3, height/5, 0, PI);
		

		}
	}
		

