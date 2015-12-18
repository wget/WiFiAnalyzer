/*
 *    Copyright (C) 2010 - 2015 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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
package com.vrem.wifianalyzer.wifi;

import android.os.Handler;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ScannerTest {
    @Mock private Handler handler;
    @Mock private Updater updater;
    @Mock private WiFi wifi;
    @Mock private Scanner scanner;

    private Scanner fixture;
    private Information information;

    @Before
    public void setUp() throws Exception {
        information = new Information();

        Mockito.when(wifi.scan()).thenReturn(information);

        fixture = Scanner.performPeriodicScans(wifi, updater, handler);
    }

    @Test
    public void testPerformPeriodicScans() throws Exception {
        // validate
        Mockito.verify(wifi).enable();
        Mockito.verify(wifi).scan();
        Mockito.verify(updater).update(information);
        Mockito.verify(handler).postDelayed(fixture.getPerformPeriodicScan(), Scanner.DELAY_MILLIS);
    }

    @Test
    public void testUpdate() throws Exception {
        // execute
        fixture.update();
        // validate
        Mockito.verify(wifi, Mockito.times(2)).enable();
        Mockito.verify(wifi, Mockito.times(2)).scan();
        Mockito.verify(updater, Mockito.times(2)).update(information);
    }

    @Test
    public void testPerformPeriodicScanRun() throws Exception {
        // setup
        Scanner.PerformPeriodicScan fixture = new Scanner.PerformPeriodicScan(scanner, handler);
        // execute
        fixture.run();
        // validate
        Mockito.verify(scanner).update();
        Mockito.verify(handler).removeCallbacks(fixture);
        Mockito.verify(handler).postDelayed(fixture, Scanner.DELAY_MILLIS);
    }

}