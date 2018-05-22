package com.netpod.api;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netpod.Application;
import com.netpod.Obstacle;
import com.netpod.Vehicle;
import com.netpod.Vehicle.Coordinate;
import com.netpod.types.Direction;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class VehicleControllerIT {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;
    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {

        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
                .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
                .findAny()
                .orElse(null);

        assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter);
    }

    @Before
    public void setup() throws Exception {
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    
    @Test
    public void shouldCreateAStartPosition() throws Exception {
        int startX = 2;
        int startY = 3;
        Direction startDirection = Direction.EAST;

        mockMvc.perform(post("/vehicle/position/start/{x}/{y}/{direction}", startX, startY, startDirection)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.coordinate.x", is(startX)))
                .andExpect(jsonPath("$.coordinate.y", is(startY)))
                .andExpect(jsonPath("$.direction", is(startDirection.name())));
    }

    @Test
    public void shouldMoveToPosition() throws Exception {

        int startX = 2;
        int startY = 1;
        Direction startDirection = Direction.SOUTH;

        mockMvc.perform(post("/vehicle/position/start/{x}/{y}/{direction}", startX, startY, startDirection)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/vehicle/position/move")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(String.valueOf(new char[] {'R', 'R', 'R', 'R', 'E', 'U', 'U','L','U', 'N', 'D', 'W'})))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.coordinate.x", is(5)))
                .andExpect(jsonPath("$.coordinate.y", is(3)))
                .andExpect(jsonPath("$.direction", is(Direction.WEST.name())));
    }

    @Test
    public void shouldMoveToPositionWithOnstacle() throws Exception {

        int startX = 2;
        int startY = 1;
        Direction startDirection = Direction.NORTH;
        Obstacle obstacle = new Obstacle(new Coordinate(3, 2), new Coordinate(5, 4));

        mockMvc.perform(post("/vehicle/obstacle")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(json(obstacle)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/vehicle/position/start/{x}/{y}/{direction}", startX, startY, startDirection)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/vehicle/position/move")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(String.valueOf(new char[] {'U', 'U', 'E', 'R', 'R'})))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.coordinate.x", is(3)))
                .andExpect(jsonPath("$.coordinate.y", is(3)))
                .andExpect(jsonPath("$.obstructed", is(true)))
                .andExpect(jsonPath("$.direction", is(Direction.EAST.name())));
    }

    protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }
}