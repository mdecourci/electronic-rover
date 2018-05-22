package com.netpod;

import com.netpod.types.Direction;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class Vehicle {

    private Obstacle obstacle;
    private VehiclePosition previousVehiclePosition;
    private VehiclePosition currentVehiclePosition;

    public VehiclePosition startPosition(final int x, int y, Direction pDirection) {
        this.currentVehiclePosition = new VehiclePosition(new Coordinate(x, y), pDirection);
        this.previousVehiclePosition = null;
        return this.currentVehiclePosition;
    }

    public VehiclePosition getPreviousVehiclePosition() {
        return previousVehiclePosition;
    }

    public VehiclePosition move(final char[] pChars) {
        previousVehiclePosition = currentVehiclePosition;
        if (pChars != null && pChars.length > 0) {
            int positionsAlongX = 0;
            int positionsAlongY = 0;
            boolean obstaclePresent = false;
            Direction direction = previousVehiclePosition.getDirection();

            for (int i = 0; i < pChars.length; i++) {
                switch (pChars[i]) {
                    case 'L' : positionsAlongX--;
                        break;
                    case 'R' : positionsAlongX++;
                        break;
                    case 'U' : positionsAlongY++;
                        break;
                    case 'D' : positionsAlongY--;
                        break;
                    case 'N' : if (!direction.is('N')) direction = Direction.NORTH;
                        break;
                    case 'S' : if (!direction.is('S')) direction = Direction.SOUTH;
                        break;
                    case 'E' : if (!direction.is('E')) direction = Direction.EAST;
                        break;
                    case 'W' : if (!direction.is('W')) direction = Direction.WEST;
                        break;
                }
                if ((obstacle != null) && obstacle.isPresent(previousVehiclePosition.coordinate.x + positionsAlongX,
                        previousVehiclePosition.coordinate.y + positionsAlongY)) {
                    obstaclePresent = true;
                    break;
                }
            }
            currentVehiclePosition.coordinate.x = previousVehiclePosition.coordinate.x + positionsAlongX;
            currentVehiclePosition.coordinate.y = previousVehiclePosition.coordinate.y + positionsAlongY;
            currentVehiclePosition.direction = direction;
            currentVehiclePosition.obstructed = obstaclePresent;
        }
        return new VehiclePosition(new Coordinate(currentVehiclePosition.coordinate), currentVehiclePosition.direction, currentVehiclePosition.obstructed);
    }

    public void setObstacle(final Obstacle pObstacle) {
        obstacle = pObstacle;
    }

    public static class Coordinate {
        private int x;
        private int y;

        public Coordinate() {
        }

        public Coordinate(final int pX, final int pY) {
            x = pX;
            y = pY;
        }

        public Coordinate(final Coordinate pCoordinate) {
            x = pCoordinate.x;
            y = pCoordinate.y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("Coordinate{");
            sb.append("x=").append(x);
            sb.append(", y=").append(y);
            sb.append('}');
            return sb.toString();
        }
    }

    public static class VehiclePosition {
        private final Coordinate coordinate;
        private Direction direction;
        private boolean obstructed;

        public VehiclePosition(final Coordinate pCoordinate, final Direction pDirection, final boolean pObstructed) {
            coordinate = pCoordinate;
            direction = pDirection;
            obstructed = pObstructed;
        }

        public VehiclePosition(final Coordinate pCoordinate, final Direction pDirection) {
            coordinate = pCoordinate;
            direction = pDirection;
            obstructed = false;
        }

        public Coordinate getCoordinate() {
            return coordinate;
        }

        public Direction getDirection() {
            return direction;
        }

        public boolean isObstructed() {
            return obstructed;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("VehiclePosition{");
            sb.append("coordinate=").append(coordinate);
            sb.append(", direction=").append(direction);
            sb.append(", obstructed=").append(obstructed);
            sb.append('}');
            return sb.toString();
        }
    }
}
