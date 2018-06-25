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
package com.redhat.patriot.smart_home_firmware.processors;

import com.redhat.patriot.smart_home_firmware.routes.Pca9685RouteBuilder;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.log4j.Logger;

import com.redhat.patriot.smart_home_firmware.Pca9685Util;

/**
 * @author <a href="mailto:pavel.macik@gmail.com">Pavel Macík</a>
 */
public class Pca9685PwmSetProcessor implements Processor {
   private static final Logger LOG = Logger.getLogger(Pca9685PwmSetProcessor.class);

   @Override
   public void process(final Exchange exchange) throws Exception {
      Message msg = exchange.getIn();
      final Object pwmHeader = msg.getHeader(Pca9685RouteBuilder.PWM_HEADER);
      if (pwmHeader == null) {
         throw new RuntimeException("'pwm' header not found! Value 0 - 15 expected.");
      }
      // bound value to 0-4095 range
      final Object valueHeader = msg.getHeader(Pca9685RouteBuilder.VALUE_HEADER);
      if (valueHeader == null) {
         throw new RuntimeException("'value' header not found! Value 0 - 4095 expected.");
      }
      String i2cMsg = Pca9685Util.hexMessage(Integer.valueOf(pwmHeader.toString()),
                                             Integer.valueOf(valueHeader.toString()));

      if (LOG.isDebugEnabled()) {
         LOG.debug("Sending I2C message: " + i2cMsg);
      }
      msg.setBody(i2cMsg);
   }
}
