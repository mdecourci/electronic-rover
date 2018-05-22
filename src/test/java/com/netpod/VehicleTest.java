package com.netpod;

import com.netpod.types.Direction;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.nullValue;
import com.netpod.Vehicle.VehiclePosition;
import com.netpod.Vehicle.Coordinate;

public class VehicleTest {

    private Vehicle vehicle = new Vehicle();

    @Test
    public void shouldHaveAnyStartPositionZero() {
        VehiclePosition vehiclePosition = vehicle.startPosition(0, 0, Direction.NORTH);

        assertThat(vehiclePosition.getCoordinate(), hasProperty("x", equalTo(0)));
        assertThat(vehiclePosition.getCoordinate(), hasProperty("y", equalTo(0)));
        assertThat(vehiclePosition, hasProperty("direction", equalTo(Direction.NORTH)));
    }

    @Test
    public void shouldHaveAnyStartPosition() {
        VehiclePosition vehiclePosition = vehicle.startPosition(2, 1, Direction.SOUTH);

        assertThat(vehiclePosition.getCoordinate(), hasProperty("x", equalTo(2)));
        assertThat(vehiclePosition.getCoordinate(), hasProperty("y", equalTo(1)));
        assertThat(vehiclePosition, hasProperty("direction", equalTo(Direction.SOUTH)));

        assertThat(vehicle.getPreviousVehiclePosition(), nullValue());
    }

    @Test
    public void shouldMoveToSamePositionNoDirection() {
        vehicle.startPosition(2, 1, Direction.SOUTH);

        VehiclePosition vehiclePosition = vehicle.move(new char[]{'L','R', 'U', 'D' });

        assertThat(vehiclePosition.getCoordinate(), hasProperty("x", equalTo(2)));
        assertThat(vehiclePosition.getCoordinate(), hasProperty("y", equalTo(1)));
        assertThat(vehiclePosition, hasProperty("direction", equalTo(Direction.SOUTH)));
    }

    @Test
    public void shouldMoveToSamePositionSameDirection() {
        vehicle.startPosition(2, 1, Direction.SOUTH);

        VehiclePosition vehiclePosition = vehicle.move(new char[]{'L','R', 'S', 'U', 'D' });

        assertThat(vehiclePosition.getCoordinate(), hasProperty("x", equalTo(2)));
        assertThat(vehiclePosition.getCoordinate(), hasProperty("y", equalTo(1)));
        assertThat(vehiclePosition, hasProperty("direction", equalTo(Direction.SOUTH)));
    }

    @Test
    public void shouldMoveToSamePositionSameDirectionDifferentCommandOrder() {
        vehicle.startPosition(2, 1, Direction.SOUTH);

        VehiclePosition vehiclePosition = vehicle.move(new char[]{'L', 'E', 'U','R', 'N', 'D', 'S' });

        assertThat(vehiclePosition.getCoordinate(), hasProperty("x", equalTo(2)));
        assertThat(vehiclePosition.getCoordinate(), hasProperty("y", equalTo(1)));
        assertThat(vehiclePosition, hasProperty("direction", equalTo(Direction.SOUTH)));
    }

    @Test
    public void shouldMoveToDifferentPositionDifferentDirectionDifferentCommandOrder() {
        vehicle.startPosition(2, 1, Direction.SOUTH);

        VehiclePosition vehiclePosition = vehicle.move(new char[]{'R', 'R', 'R', 'R', 'E', 'U', 'U','L','U', 'N', 'D', 'W' });

        assertThat(vehiclePosition.getCoordinate(), hasProperty("x", equalTo(5)));
        assertThat(vehiclePosition.getCoordinate(), hasProperty("y", equalTo(3)));
        assertThat(vehiclePosition, hasProperty("direction", equalTo(Direction.WEST)));
    }

    @Test
    public void shouldAvoidObstacleGoingForward() {
        Obstacle obstacle = new Obstacle(new Coordinate(3, 2), new Coordinate(5, 4));
        vehicle.setObstacle(obstacle);

        vehicle.startPosition(2, 1, Direction.NORTH);

        VehiclePosition vehiclePosition = vehicle.move(new char[]{'U', 'U', 'U', 'U', 'E', 'R', 'R', 'R', 'R'});

        assertThat(vehiclePosition.getCoordinate(), hasProperty("x", equalTo(6)));
        assertThat(vehiclePosition.getCoordinate(), hasProperty("y", equalTo(5)));
        assertThat(vehiclePosition, hasProperty("direction", equalTo(Direction.EAST)));
        assertThat(vehiclePosition, hasProperty("obstructed", equalTo(false)));
    }

    @Test
    public void shouldReportObstacleGoingForward() {
        Obstacle obstacle = new Obstacle(new Coordinate(3, 2), new Coordinate(5, 4));
        vehicle.setObstacle(obstacle);

        vehicle.startPosition(2, 1, Direction.NORTH);

        VehiclePosition vehiclePosition = vehicle.move(new char[]{'U', 'U', 'E', 'R', 'R'});

        assertThat(vehiclePosition.getCoordinate(), hasProperty("x", equalTo(3)));
        assertThat(vehiclePosition.getCoordinate(), hasProperty("y", equalTo(3)));
        assertThat(vehiclePosition, hasProperty("direction", equalTo(Direction.EAST)));
        assertThat(vehiclePosition, hasProperty("obstructed", equalTo(true)));
    }

    @Test
    public void shouldAvoidObstacleGoingBackward() {
        Obstacle obstacle = new Obstacle(new Coordinate(3, 2), new Coordinate(5, 4));
        vehicle.setObstacle(obstacle);

        vehicle.startPosition(6, 5, Direction.NORTH);

        VehiclePosition vehiclePosition = vehicle.move(new char[]{'D', 'D', 'D', 'D', 'E', 'L', 'L', 'L', 'L'});

        assertThat(vehiclePosition.getCoordinate(), hasProperty("x", equalTo(2)));
        assertThat(vehiclePosition.getCoordinate(), hasProperty("y", equalTo(1)));
        assertThat(vehiclePosition, hasProperty("direction", equalTo(Direction.EAST)));
        assertThat(vehiclePosition, hasProperty("obstructed", equalTo(false)));
    }

    @Test
    public void shouldReportObstacleGoingBackward() {
        Obstacle obstacle = new Obstacle(new Coordinate(3, 2), new Coordinate(5, 4));
        vehicle.setObstacle(obstacle);

        vehicle.startPosition(6, 5, Direction.NORTH);

        VehiclePosition vehiclePosition = vehicle.move(new char[]{'D', 'D', 'E', 'L', 'L'});

        assertThat(vehiclePosition.getCoordinate(), hasProperty("x", equalTo(5)));
        assertThat(vehiclePosition.getCoordinate(), hasProperty("y", equalTo(3)));
        assertThat(vehiclePosition, hasProperty("direction", equalTo(Direction.EAST)));
        assertThat(vehiclePosition, hasProperty("obstructed", equalTo(true)));
    }
}