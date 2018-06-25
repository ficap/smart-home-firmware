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

/**
 * Created by pmacik on 1.2.16.
 */
public class Channel {
   private int pca9685Id = -1;
   private int pwm = -1;
   private int value = -1;

   public int getPca9685Id() {
      return pca9685Id;
   }

   public Channel setPca9685Id(final int pca9685Id) {
      this.pca9685Id = pca9685Id;
      return this;
   }

   public int getPwm() {
      return pwm;
   }

   public Channel setPwm(final int pwm) {
      this.pwm = pwm;
      return this;
   }

   public int getValue() {
      return value;
   }

   public Channel setValue(final int value) {
      this.value = value;
      return this;
   }

   @Override
   public boolean equals(final Object o) {
      if (this == o) {
         return true;
      }
      if (!(o instanceof Channel)) {
         return false;
      }

      final Channel channel = (Channel) o;

      if (getPca9685Id() != channel.getPca9685Id()) {
         return false;
      }
      if (getPwm() != channel.getPwm()) {
         return false;
      }
      return getValue() == channel.getValue();

   }

   @Override
   public int hashCode() {
      int result = getPca9685Id();
      result = 31 * result + getPwm();
      result = 31 * result + getValue();
      return result;
   }
}
