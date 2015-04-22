package org.springframework.boot.firehose.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.firehose.model.Stats;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by vcarvalho on 4/20/15.
 */
@Controller
@RequestMapping("/stats")
public class StatsController {

    @Autowired
    private Stats stats;

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Map<String,Object>> stats(){
        Map<String,Object> map = new HashMap<>();
        Long counter = stats.getCounterEvents().get();
        Long metrics = stats.getMetricEvents().get();
        Long total = stats.getTotalEvents().get();
        Long delta = (System.currentTimeMillis() - stats.getStartedTime())/1000;
        map.put("counterStats", counter);
        map.put("counterPerSec", counter / delta);
        map.put("metricStats", metrics);
        map.put("metricsPerSec", metrics / delta);
        map.put("totalStats", total);
        map.put("totalPerSec", total / delta);

        map.put("startedTime", stats.getStartedTime());
        ResponseEntity<Map<String,Object>> response = new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
        return response;

    }

}
