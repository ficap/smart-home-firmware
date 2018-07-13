/*
 * Copyright 2018 Patriot project
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.redhat.patriot.smart_home_firmware.routes;

import org.apache.camel.model.dataformat.JsonLibrary;

import com.redhat.patriot.smart_home_firmware.processors.SensorDataProcessor;

/**
 * @author <a href="mailto:pavel.macik@gmail.com">Pavel Macík</a>
 */
public class SensorRouteBuilder extends IntelligentHomeRouteBuilder {

   @Override
   public void configure() throws Exception {
      final SensorDataProcessor sensorDataProcessor = new SensorDataProcessor();

      from(restBaseUri() + "/sensorData?httpMethodRestrict=GET")
            .setHeader("address", simple(CONFIG.getSensorAddress()))
            .setBody(simple(""))
            .to("bulldog:i2c?readLength=2")
            .process(sensorDataProcessor)
            .marshal().json(JsonLibrary.Jackson, true);

      from("timer:sensorBroadcast?period=5000")
            .setHeader("address", simple(CONFIG.getSensorAddress()))
            .to("bulldog:i2c?readLength=2")
            .process(sensorDataProcessor)
            .marshal().json(JsonLibrary.Jackson, true)
            .to("websocket:weather?sendToAll=true");
   }
}
