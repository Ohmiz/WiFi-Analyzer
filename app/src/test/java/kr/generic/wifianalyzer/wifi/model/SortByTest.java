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

package kr.co.generic.wifianalyzer.wifi.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SortByTest {

    @Test
    public void testSortByNumber() throws Exception {
        assertEquals(3, SortBy.values().length);
    }

    @Test
    public void testComparator() throws Exception {
        assertTrue(SortBy.STRENGTH.comparator() instanceof SortBy.StrengthComparator);
        assertTrue(SortBy.SSID.comparator() instanceof SortBy.SSIDComparator);
        assertTrue(SortBy.CHANNEL.comparator() instanceof SortBy.ChannelComparator);
    }

    @Test
    public void testFind() throws Exception {
        assertEquals(SortBy.STRENGTH, SortBy.find(-1));
        assertEquals(SortBy.STRENGTH, SortBy.find(SortBy.values().length));

        assertEquals(SortBy.STRENGTH, SortBy.find(SortBy.STRENGTH.ordinal()));
        assertEquals(SortBy.SSID, SortBy.find(SortBy.SSID.ordinal()));
        assertEquals(SortBy.CHANNEL, SortBy.find(SortBy.CHANNEL.ordinal()));
    }
}