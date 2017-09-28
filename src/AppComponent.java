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

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Service;
import org.onosproject.net.device.DeviceService;
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
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Skeletal ONOS application component.
 */
@Component(immediate = true)
public class AppComponent {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private DeviceService deviceService = DefaultServiceDirectory.getService(DeviceService.class);
    private Iterable<Device> devices = deviceService.getAvailableDevices();
    
    private class Query implements Runnable {

	    @Override
	    public void run() {
		while(true) {
			log.info("Hello World!");
			for (Device d : devices) {
				log.info(d.toString());
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
        thread.start();
    }

    @Deactivate
    protected void deactivate() {
        log.info("Stopped");
        thread.suspend();
    }
    
    @Override
    public void run() {
		while(true) {
			log.info("Hello World!");
			for (Device d : devices) {
				List<PortStatistics> portStats = deviceService.getPortDeltaStatistics(d.id());
				for (PortStatistics p : portStats) {
					log.info("Port " + p.port() + ": sent " + p.bytesSent() + " bytes | Received " + p.bytesReceived() + " bytes");
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