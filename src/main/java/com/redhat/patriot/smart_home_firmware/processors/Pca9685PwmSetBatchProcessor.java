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

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.log4j.Logger;

import com.redhat.patriot.smart_home_firmware.Pca9685Util;

/**
 * @author <a href="mailto:pavel.macik@gmail.com">Pavel Macík</a>
 */
public class Pca9685PwmSetBatchProcessor implements Processor {
   private static final Logger LOG = Logger.getLogger(Pca9685PwmSetBatchProcessor.class);

   @Override
   public void process(final Exchange exchange) throws Exception {
      final Message msg = exchange.getIn();
      final StringBuffer i2cBatch = new StringBuffer();

      final String[] batchLines = msg.getBody(String.class).split("\n");

      boolean first = true;
      for (String batchLine : batchLines) {
         // input message batch line "<i2c address>;<pwm output(0-15)>;<value(0-4095)>"
         //TODO: add batch line format validation
         if (LOG.isDebugEnabled()) {
            LOG.debug("Batch line: " + batchLine);
         }
         final String[] parts = batchLine.split(";");
         final String i2cMsg = Pca9685Util.hexMessage(Integer.valueOf(parts[1]), Integer.valueOf(parts[2]));
         if (LOG.isTraceEnabled()) {
            LOG.trace("I2C Address: " + parts[0]);
            LOG.trace("Raw I2C Message: " + i2cMsg);
         }
         // output = "<i2c address>;<raw i2c hex message>"
         if (first) {
            first = false;
         } else {
            i2cBatch.append("\n");
         }
         i2cBatch.append(parts[0]); // address
         i2cBatch.append(";");
         i2cBatch.append(i2cMsg);
      }
      msg.setBody(i2cBatch.toString());
   }
}
