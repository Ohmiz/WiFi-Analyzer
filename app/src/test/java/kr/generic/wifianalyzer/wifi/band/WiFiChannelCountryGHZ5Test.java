/*
 * WiFi Analyzer
 * Copyright (C) 2016  VREM Software Development <VREMSoftwareDevelopment@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package kr.co.generic.wifianalyzer.wifi.band;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class WiFiChannelCountryGHZ5Test {

    private final static SortedSet<Integer> CHANNELS_SET1 = new TreeSet<>(Arrays.asList(36, 40, 44, 48, 52, 56, 60, 64));
    private final static SortedSet<Integer> CHANNELS_SET2 = new TreeSet<>(Arrays.asList(100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140));
    private final static SortedSet<Integer> CHANNELS_SET3 = new TreeSet<>(Arrays.asList(149, 153, 157, 161, 165));

    private WiFiChannelCountryGHZ5 fixture;

    @Before
    public void setUp() {
        fixture = new WiFiChannelCountryGHZ5();
    }

    @Test
    public void testChannelsAustraliaCanada() throws Exception {
        SortedSet<Integer> exclude = new TreeSet<>(Arrays.asList(120, 124, 128));
        int expectedSize = CHANNELS_SET1.size() + CHANNELS_SET2.size() + CHANNELS_SET3.size() - exclude.size();
        String[] countries = new String[]{"AU", "CA"};
        for (String country : countries) {
            Set<Integer> actual = fixture.findChannels(country);
            assertEquals(expectedSize, actual.size());
            assertTrue(actual.containsAll(CHANNELS_SET1));
            assertTrue(actual.containsAll(CHANNELS_SET3));
            assertFalse(actual.containsAll(exclude));
        }
    }

    @Test
    public void testChannelsChinaSouthKorea() throws Exception {
        int expectedSize = CHANNELS_SET1.size() + CHANNELS_SET3.size();
        String[] countries = new String[]{"CN", "KR"};
        for (String country : countries) {
            Set<Integer> actual = fixture.findChannels(country);
            assertEquals(expectedSize, actual.size());
            assertTrue(actual.containsAll(CHANNELS_SET1));
            assertTrue(actual.containsAll(CHANNELS_SET3));
            assertFalse(actual.containsAll(CHANNELS_SET2));
        }
    }

    @Test
    public void testChannelsJapanTurkeySouthAfrica() throws Exception {
        int expectedSize = CHANNELS_SET1.size() + CHANNELS_SET2.size();
        String[] countries = new String[]{"JP", "TR", "ZA"};
        for (String country : countries) {
            Set<Integer> actual = fixture.findChannels(country);
            assertEquals(expectedSize, actual.size());
            assertTrue(actual.containsAll(CHANNELS_SET1));
            assertTrue(actual.containsAll(CHANNELS_SET2));
            assertFalse(actual.containsAll(CHANNELS_SET3));
        }
    }

    @Test
    public void testChannelsIsrael() throws Exception {
        int expectedSize = CHANNELS_SET1.size();
        Set<Integer> actual = fixture.findChannels("IL");
        assertEquals(expectedSize, actual.size());
        assertTrue(actual.containsAll(CHANNELS_SET1));
        assertFalse(actual.containsAll(CHANNELS_SET2));
        assertFalse(actual.containsAll(CHANNELS_SET3));
    }

    @Test
    public void testChannelsOther() throws Exception {
        int expectedSize = CHANNELS_SET1.size() + CHANNELS_SET2.size() + CHANNELS_SET3.size();
        String[] countries = new String[]{"US", "RU", "XYZ"};
        for (String country : countries) {
            Set<Integer> actual = fixture.findChannels(country);
            assertEquals(expectedSize, actual.size());
            assertTrue(actual.containsAll(CHANNELS_SET1));
            assertTrue(actual.containsAll(CHANNELS_SET2));
            assertTrue(actual.containsAll(CHANNELS_SET3));
        }
    }

}