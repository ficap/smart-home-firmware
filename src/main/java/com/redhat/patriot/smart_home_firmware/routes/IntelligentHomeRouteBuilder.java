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

import org.apache.camel.builder.RouteBuilder;

import com.redhat.patriot.smart_home_firmware.Configuration;

/**
 * Created by pmacik on 2.2.16.
 */
public abstract class IntelligentHomeRouteBuilder extends RouteBuilder {
   protected Configuration config = Configuration.getInstance();

   public String restBaseUri() {
      return "jetty:http://" + config.getRestHost();
   }

   public String mqttHost() {
      return config.getMqttHost();
   }
}
