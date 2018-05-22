package com.netpod.api;

import com.netpod.Obstacle;
import com.netpod.Vehicle.VehiclePosition;
import com.netpod.Vehicle;
import com.netpod.types.Direction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vehicle")
public class VehicleController {

    final static Logger LOGGER = LoggerFactory.getLogger(VehicleController.class);


    @Autowired
    private Vehicle vehicle;

    @PostMapping(path = "position/start/{x}/{y}/{direction}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public VehiclePosition startPosition(@PathVariable("x") final int pX, @PathVariable("y") final int pY, @PathVariable("direction") final Direction pDirection) {
        LOGGER.info("start x = {} y = {} direction = {}", pX, pY, pDirection);

        return vehicle.startPosition(pX, pY, pDirection);
    }

    @PostMapping(path = "position/move", produces = MediaType.APPLICATION_JSON_VALUE)
    public VehiclePosition move(@RequestBody final String pNewPosition) {
        LOGGER.info("move x = {}", pNewPosition);

        return vehicle.move(pNewPosition.toCharArray());
    }

    @PostMapping(path = "obstacle", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void createObstacle(@RequestBody final Obstacle pObstacle) {
        LOGGER.info("createObstacle obstacle = {}", pObstacle);
        vehicle.setObstacle(pObstacle);
    }
}
