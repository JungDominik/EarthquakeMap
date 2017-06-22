This is a class project as part of the UC San Diego MOOC "Object-oriented Programming in Java", offered through Coursera
( https://www.coursera.org/specializations/java-object-oriented )

It automatically generates an interactive map of the earthquakes which happened during the past week and their proximity to major cities, via the following steps
- parsing an RSS feed by the US Geological Survey 
- processing the parsed data into UnfoldingMaps marker objects with properties such as GPS location and earthquake magnitude
- differentiating between ocean quakes and land quakes, matching land quakes into their respective countries
- displaying the marker objects with different shapes and colors acoording to their properties using a zoomable Java Applet map (drawing on Google Maps services)
- processing and reacting to user input via a listener method (i.e. clicking on earthquake markers displays possibly affected cities in their vicinity)

![Alt text](/data/screenshot.jpg?raw=true "Screenshot")


The project has been assembled over a time of 10 weeks as a means of teaching the basic concepts of object-oriented programming and the java language such as
- Java Syntax
- Java data structures
- Classes, objects, inheritance, polymorphism
- Memory models 
- Event driven programming (response to user input)
- Basics of algorithms (sorting, searching)

It makes use of some initial skeleton code such as the parser for the RSS feed
and draws on the "UnfoldingMaps" package by Till Nagel for displaying the map objects (available at http://unfoldingmaps.org).





