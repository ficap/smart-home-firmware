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

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pmacik on 1.2.16.
 */
public class RgbLed {
   private Map<String, Channel> channelMap = new HashMap<>();

   public Map<String, Channel> getChannelMap() {
      return channelMap;
   }

   public RgbLed setChannelMap(final Map<String, Channel> channelMap) {
      this.channelMap = channelMap;
      return this;
   }

   @Override
   public boolean equals(final Object o) {
      if (this == o) {
         return true;
      }
      if (!(o instanceof RgbLed)) {
         return false;
      }

      final RgbLed rgbLed = (RgbLed) o;

      return getChannelMap() != null ? getChannelMap().equals(rgbLed.getChannelMap()) : rgbLed.getChannelMap() == null;

   }

   @Override
   public int hashCode() {
      return getChannelMap() != null ? getChannelMap().hashCode() : 0;
   }
}
