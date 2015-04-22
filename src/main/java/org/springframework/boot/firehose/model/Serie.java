package org.springframework.boot.firehose.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Representation of a InfluxDB database serie.
 *
 * @author stefan.majer [at] gmail.com
 *
 */
public class Serie {

    private final String name;
    final String[] columns;

    private List<Object[]> points;



    public void addPoint(Object[] point){
        points.add(point);
    }

    /**
     * @param name
     *            the name of the serie.
     */
    public Serie(final String name, String ... columns) {
        this.name = name;
        this.columns = columns;
        this.points = new ArrayList<>();
    }


    @JsonProperty("name")
    public String getName(){
        return name;
    }

    @JsonProperty("columns")
    public String[] getColumns(){
        return columns;
    }

    @JsonProperty("points")
    public Object[][] getPoints(){
        return points.toArray(new Object[][]{null});
    }

}
