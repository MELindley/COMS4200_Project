/*
 * Copyright 2017-present Open Networking Laboratory
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.hub.app;

import org.apache.commons.lang.StringUtils;
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Service;
//import org.apache.http.HttpEntity;
//import org.apache.http.HttpHost;
//import org.apache.http.entity.ContentType;
//import org.apache.http.nio.entity.NStringEntity;
//import org.elasticsearch.client.Response;
//import org.elasticsearch.client.RestClient;
import org.onosproject.net.device.DeviceService;
import org.onosproject.net.device.PortStatistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.onlab.osgi.DefaultServiceDirectory;
import org.onlab.util.StringFilter;
import org.onosproject.core.ApplicationId;
import org.onosproject.core.CoreService;
import org.onosproject.net.Device;
import org.onosproject.net.DeviceId;
import org.onosproject.net.flow.FlowEntry;
import org.onosproject.net.flow.FlowEntry.FlowEntryState;
import org.onosproject.net.flow.FlowRuleService;
import org.onosproject.net.flow.TrafficTreatment;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.time.*;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Skeletal ONOS application component.
 */
@Component(immediate = true)
public class AppComponent {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private DeviceService deviceService = DefaultServiceDirectory.getService(DeviceService.class);
    private Iterable<Device> devices = deviceService.getAvailableDevices();
    //private RestClient restClient;
    private PrintWriter output;

    private class Query implements Runnable {
	@Override
	public void run() {
		while(true) {
			//log.info("Hello World!");
			for (Device d : devices) {
				List<PortStatistics> portStats = deviceService.getPortDeltaStatistics(d.id());
				for (PortStatistics p : portStats) {
					Map<String, String> params = Collections.emptyMap();
					JSONObject obj = new JSONObject();

					try {
						obj.put("timeStamp", LocalDateTime.now().toString());
						String port = d.type().name() + d.id().toString() +"-PortID"+ p.port();
						obj.put("portID", port);
				    		obj.put("bytesOut", p.bytesSent());
					    	obj.put("bytesIn", p.bytesReceived());
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				    	/*HttpEntity entity = new NStringEntity(obj.toString(), ContentType.APPLICATION_JSON);
				    	try {
						Response response = restClient.performRequest("PUT", "Port_"+p.port(), params, entity);
						log.info(response.toString());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}*/
				    log.info("JSON: " + obj.toString());
				    output.print(obj.toString());
				    output.print(",");

					//log.info("Time: " + LocalDateTime.now() + "Port " + p.port() + ": sent " + p.bytesSent() + " bytes | Received " + p.bytesReceived() + " bytes");
				}
				//log.info(d.toString());
			}
			try {
				Thread.sleep(1000);
		    	} catch (InterruptedException e) {
				break;
		    	}

			}
		}
	}

    private Query query = new Query();
    private Thread thread = new Thread(query);

    @Activate
    protected void activate() {
        log.info("Started");
       	try {
        	//restClient = RestClient.builder(new HttpHost("10.0.2.2", 9200, "http")).build();
    		output = new PrintWriter("/home/ubuntu/output.csv");
        } catch (Exception e) {

        }

        thread.start();
    }

    @Deactivate
    protected void deactivate() {
        log.info("Stopped");
        /*try {
			restClient.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
        output.close();
        thread.suspend();
    }

}
