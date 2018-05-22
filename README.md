# Project Electronic Rover

Develop an API that moves an electronic rover around on a grid based on passed instructions (in all directions – not just forward).

## API Contract
* The program will receive starting point coordinates (x,y) of a rover along with the direction (N,S,E,W) it is facing.
* The rover receives a character array of commands;
  * 'R' for move to the right from previous position
  * 'L' for move to the left from previous position
  * 'U' for move up from previous position
  * 'D' for move down from previous position
  * 'N', 'S', 'E' and 'W' for North, South, East and West direction of vehicle.
* Movement will be relative to the previous point and the rover should always maintain it’s latest coordinates (assume grid is rectangular and has data points starting from (0,0) to infinity.
* Implement obstacle detection before each move to a new square. If a given sequence of commands encounters an obstacle, the rover moves up to the last possible point and reports the obstacle.


## Design
A Vehicle object mimics the electronic rover an a Position object encapsulates the position.
The Spring Boot framework has been used to creat a REST API.
The Vehicle object provides a JAVA API
The VehicleController provides a REST API
#### Object Relationships
* A Vehicle has-a Position (previous and current)                                             .
* A Position has-a Coordinate, direction and indication of whether the position is obstructed.
* A Coordinate has x,y points.
* An Obstacle has Coordinates
#### Assumptions
* Only one obstacle at is time allowed in grid.
* The direction of the vehicle is not used to predict positions.

## Getting Started

git clone git@github.com:mdecourci/electronic-rover.git

### Installing

mvn clean install

## Running the tests

mvn clean test

### To run

* java -jar target/electronic-rover-1.0-SNAPSHOT.jar
* Invoke REST API

### REST API

* To create a start point
  * `http://localhost:8080/vehicle/position/start/<x>/<y>/<direction>`

* To move to a position from the last position
  * `http://localhost:8080/vehicle/position/move`
  * Example REST body string:
`RRRREUULUNDW`
