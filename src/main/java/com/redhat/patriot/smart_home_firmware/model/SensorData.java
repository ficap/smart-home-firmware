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

package com.redhat.patriot.smart_home_firmware.model;

public class SensorData {

   private String sensorName = "DHT11";

   private int temperature = -1;

   private int humidity = -1;

   private String timestamp = "-1";

   // Bean methods

   public String getSensorName() {
      return this.sensorName;
   }

   public void setSensorName(String sensorName) {
      this.sensorName = sensorName;
   }

   public int getTemperature() {
      return this.temperature;
   }

   public void setTemperature(int temperature) {
      this.temperature = temperature;
   }

   public int getHumidity() {
      return this.humidity;
   }

   public void setHumidity(int humidity) {
      this.humidity = humidity;
   }

   public String getTimestamp() {
      return this.timestamp;
   }

   public void setTimestamp(String timestamp) {
      this.timestamp = timestamp;
   }

   // Fluent API

   public SensorData sensorName(String sensorName) {
      setSensorName(sensorName);
      return this;
   }

   public String sensorName() {
      return getSensorName();
   }

   public SensorData temperature(int temperature) {
      setTemperature(temperature);
      return this;
   }

   public int temperature() {
      return getTemperature();
   }

   public SensorData humidity(int humidity) {
      setHumidity(humidity);
      return this;
   }

   public int humidity() {
      return getHumidity();
   }

   public SensorData timestamp(String timestamp) {
      setTimestamp(timestamp);
      return this;
   }

   public String timestamp() {
      return getTimestamp();
   }
}
