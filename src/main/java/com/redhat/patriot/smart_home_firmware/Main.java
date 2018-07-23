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

package com.redhat.patriot.smart_home_firmware;

import com.redhat.patriot.smart_home_firmware.routes.*;

/**
 * This class is intended only for debugging purposes
 * It starts this app without using silverware
 */
public class Main {
    public static void main(String[] args) throws Exception {
        org.apache.camel.main.Main main = new org.apache.camel.main.Main();

//        Class[] clss = new Class[]{
//                AcRouteBuilder.class,
//                FireplaceRouteBuilder.class,
//                Pca9685RouteBuilder.class,
//                ResetRouteBuilder.class,
//                RfidRouteBuilder.class,
//                RgbLedRouteBuilder.class,
//                SensorRouteBuilder.class,
//                ServoRouteBuilder.class,
//                TvRouteBuilder.class,
//        };
//
//        for(Class c : clss) {
//            main.addRouteBuilder((RouteBuilder) c.newInstance());
//        }

        main.addRouteBuilder(new AcRouteBuilder());
        main.addRouteBuilder(new FireplaceRouteBuilder());
        main.addRouteBuilder(new Pca9685RouteBuilder());
        main.addRouteBuilder(new ResetRouteBuilder());
        main.addRouteBuilder(new RfidRouteBuilder());
        main.addRouteBuilder(new RgbLedRouteBuilder());
        main.addRouteBuilder(new SensorRouteBuilder());
        main.addRouteBuilder(new ServoRouteBuilder());
        main.addRouteBuilder(new TvRouteBuilder());

        main.run(args);


    }
}
