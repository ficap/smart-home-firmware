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

import com.redhat.patriot.smart_home_firmware.Configuration;

/**
 * @author <a href="mailto:pavel.macik@gmail.com">Pavel Macík</a>
 */
public class RgbLedBatchProcessor implements Processor {

   private static final Logger LOG = Logger.getLogger(RgbLedBatchProcessor.class);

   public static final String SMOOTH_SET_HEADER = "smooth";
   public static final String BATCH_DELAY_HEADER = "delay";

   private Configuration config = Configuration.getInstance();

   private String processStringBatch(String[] batchLines, boolean smoothChange) {
       StringBuilder pwmBatch = new StringBuilder();

       for (String batchLine : batchLines) {
           if (batchLine.trim().isEmpty()) {
               continue;
           }
           // input message batch line "<led>;<channel(r,g,b)>;<value(0-100)>"
           //TODO: add batch line format validation
           if (LOG.isDebugEnabled()) {
               LOG.debug("Batch line: " + batchLine);
           }
           final String[] parts = batchLine.split(";");
           final int led = Integer.valueOf(parts[0]);
           final String channel = parts[1];
           final int value = Integer.valueOf(parts[2]);
           if (LOG.isTraceEnabled()) {
               LOG.trace("LED #: " + led);
               LOG.trace("LED channel: " + channel);
               LOG.trace("Value: " + value);
           }
           // output message batch line "<i2c address>;<pwm output(0-15)>;<value(0-4095)>"
           if (led == 0xFFFF) {
               for (int i = 0; i < Configuration.RGB_LED_COUNT; i++) {
                   batchAppend(pwmBatch, i, channel, value, smoothChange);
               }
           } else {
               batchAppend(pwmBatch, led, channel, value, smoothChange);
           }
       }

       return pwmBatch.toString();
   }

   @Override
   public void process(final Exchange exchange) throws Exception {
      final Message msg = exchange.getIn();

      final String[] batchLines = msg.getBody(String.class).split("\n");
      final Object smoothChangeHeader = msg.getHeader(SMOOTH_SET_HEADER);
      boolean smoothChange = false;
      if (smoothChangeHeader != null) {
         smoothChange = Boolean.valueOf((String) smoothChangeHeader);
      }

      String pwmBatch = processStringBatch(batchLines, smoothChange);
      msg.setBody(pwmBatch);

      final Object delayHeader = msg.getHeader(BATCH_DELAY_HEADER);
      long delay = 0;
      if (delayHeader != null) {
         delay = Long.valueOf((String) delayHeader);
         if (delay > 0) {
            msg.setHeader("batchDelay", delay);
         }
      }
   }

   private String generateSmoothModeString(int step, String i2cAddress, int rgbLedPwm, int originalVal, int targetVal) {
       StringBuilder batch = new StringBuilder();
       for (int smoothedValue = originalVal + step; smoothedValue != targetVal + step;
            smoothedValue += step) {
           batch.append(i2cAddress); // I2C address
           batch.append(";");
           batch.append(rgbLedPwm); // pwm output
           batch.append(";");
           batch.append((int) (40.95 * smoothedValue)); // pwm value
           batch.append("\n");
           if (LOG.isDebugEnabled()) {
               LOG.debug("Smoothed value: " + smoothedValue);
           }
       }
       return batch.toString();
   }

   private boolean validateConfig(int led, String channel) {
       final String i2cAddress = config.getRgbLedPca9685Address(led, channel);
       if (i2cAddress == null) {
           if (LOG.isWarnEnabled()) {
               LOG.warn("I2C address of PCA9685 driver for LED '" + led + "' and channel '" + channel +
                       "' not found in configuration file (" + Configuration.CONFIG_FILE + "), skipping.");
           }
           return false;
       }
       final int rgbLedPwm = config.getRgbLedPwm(led, channel);
       if (rgbLedPwm < 0) {
           if (LOG.isWarnEnabled()) {
               LOG.warn("PWM output for LED '" + led + "' and channel '" + channel +
                       "' not assigned in configuration file (" + Configuration.CONFIG_FILE + "), skipping.");
           }
           return false;
       }
       return true;
   }

   private void batchAppend(StringBuilder pwmBatch, int led, String channel, int targetValue, boolean smooth) {
      if (!validateConfig(led, channel)) {
          return;
      }

      final String i2cAddress = config.getRgbLedPca9685Address(led, channel);
      final int rgbLedPwm = config.getRgbLedPwm(led, channel);
      final StringBuilder batch = new StringBuilder();
      final int originalValue = config.getRgbLedValue(led, channel);

      if (smooth && originalValue != targetValue) {
         final int step = (targetValue - originalValue > 0) ? 1 : -1;
         if (LOG.isDebugEnabled()) {
            LOG.debug("Smoothing LED's (" + led + ") channel (" + channel +
                      ") value change from " + originalValue + " to " + targetValue);
         }

         batch.append(generateSmoothModeString(step, i2cAddress, rgbLedPwm, originalValue, targetValue));
         config.setRgbLedValue(led, channel, targetValue);
      } else {
         batch.append(i2cAddress).append(";").append(rgbLedPwm).append(";"); // i2c_address;pwm_output;
         batch.append((int) (40.95 * targetValue)).append("\n"); // pwm_value
      }

      if (LOG.isDebugEnabled()) {
         LOG.debug("Appending to PWM batch: [" + batch.toString() + "]");
      }
      pwmBatch.append(batch);
   }
}
