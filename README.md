# MetroFinder

### 210 Project by Parker Lee

**MetroFinder** is a metro navigation app, meant to help users familiarize themselves with various rapid transit 
networks around the world. Users will be able to:

- Find information about lines
- Find information about individual stations
- Find information about transfers
- Plan and save routes
- Learn about cities through their metro systems

This app is suitable for users who are interested in travel, especially those who are keen on visiting
urban cities with bustling subway lines, or anyone who is just intrigued by metro systems.

*Why is this of interest to me?*

I'm fascinated by metro systems and how they manage to operate in conjunction with each other, so I wanted to create
an app that fuels that interest while being practical. As someone who has an interest to travel outside of Canada
(and got denied that luxury recently), this app also serves as an opportunity for me to learn more about
transportation in cities I plan to visit, while also testing my programming skills. 

### User Stories

**Done**

- As a user I want to be able to view details about an individual station (name, line, transfers, adjacent stations)
- As a user I want to be able to view details about a line (number of stations, terminal stations, intersecting lines)
- As a user I want to be able to manually make a route from a start point and a destination
- As a user I want to be able to save and add routes to a list of planned routes and completed routes
- As a user I want to be able to set a current route as completed
- As a user I want to be able to save and load the state of the application

**Semi-done**

- As a user I want to be able to track interesting info based on my completed routes

**Planned or probably not possible for me *yet***

- As a user I want the app to provide me the shortest route given a start and end
- As a user I want to be able to switch between metro systems

### Phase 4: Task 2

- Made the Route class robust (along with RouteTest)
- addStation method and removeStation method handle exceptions
- AdjacentStationException when the Station passed is not adjacent to the prior station
- EmptyRouteException when the Route passed contains no stations
 
 ### Phase 4: Task 3
 
- Refactor and split the MetroFinderGUI (and MetroFinderApp) class to improve cohesion
    - Would probably involve making a new class for each 'window' of the app
- Override hashCode and equals for Station and Route
    - Would have allowed me to get rid of the ID field in Route as well as clean up the methods that added stations
- Change the 'self-association' in Line and Station
    - Not sure how I would fix this, but having the self-associations were not easy to work with
- Refactor methods like findStation and findRoute into respective classes
    - The way I designed these methods required them to be in the ui classes
- Refactor Tokyo to make objects initialized in a cleaner way