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

package kr.co.generic.wifianalyzer.wifi.graph.time;

import kr.co.generic.wifianalyzer.wifi.graph.tools.GraphViewBuilder;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TimeAxisLabelTest {
    private TimeAxisLabel fixture;

    @Before
    public void setUp() {
        fixture = new TimeAxisLabel();
    }

    @Test
    public void testYAxis() throws Exception {
        assertEquals(StringUtils.EMPTY, fixture.formatLabel(GraphViewBuilder.MIN_Y, false));
        assertEquals("-99", fixture.formatLabel(GraphViewBuilder.MIN_Y + 1, false));
        assertEquals("-20", fixture.formatLabel(GraphViewBuilder.MAX_Y, false));
        assertEquals(StringUtils.EMPTY, fixture.formatLabel(GraphViewBuilder.MAX_Y + 1, false));
    }

    @Test
    public void testXAxis() throws Exception {
        assertEquals(StringUtils.EMPTY, fixture.formatLabel(-2, true));
        assertEquals(StringUtils.EMPTY, fixture.formatLabel(-1, true));
        assertEquals(StringUtils.EMPTY, fixture.formatLabel(0, true));
        assertEquals(StringUtils.EMPTY, fixture.formatLabel(1, true));

        assertEquals("2", fixture.formatLabel(2, true));
        assertEquals("10", fixture.formatLabel(10, true));
    }

}