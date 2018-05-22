package com.netpod;

import com.netpod.Vehicle.Coordinate;

public class Obstacle {
    private Coordinate initialCoordinate;
    private Coordinate finalCoordinate;

    public Obstacle() {
    }

    public Obstacle(final Coordinate pInitialCoordinate, final Coordinate pFinalCoordinate) {
        initialCoordinate = pInitialCoordinate;
        finalCoordinate = pFinalCoordinate;
    }

    public boolean isPresent(final int pX, final int pY) {
        if ((pX >= initialCoordinate.getX()) && (pX <= finalCoordinate.getX()) &&
                (pY >= initialCoordinate.getY()) && (pY <= finalCoordinate.getY())) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Obstacle{");
        sb.append("initialCoordinate=").append(initialCoordinate);
        sb.append(", finalCoordinate=").append(finalCoordinate);
        sb.append('}');
        return sb.toString();
    }
}
