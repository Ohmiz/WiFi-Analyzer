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

package kr.co.generic.wifianalyzer.wifi.graph.channel;

import android.support.v4.util.Pair;
import android.view.View;

import com.jjoe64.graphview.GraphView;
import kr.co.generic.wifianalyzer.BuildConfig;
import kr.co.generic.wifianalyzer.MainContextHelper;
import kr.co.generic.wifianalyzer.RobolectricUtil;
import kr.co.generic.wifianalyzer.settings.Settings;
import kr.co.generic.wifianalyzer.wifi.band.WiFiBand;
import kr.co.generic.wifianalyzer.wifi.band.WiFiChannel;
import kr.co.generic.wifianalyzer.wifi.graph.tools.GraphLegend;
import kr.co.generic.wifianalyzer.wifi.graph.tools.GraphViewWrapper;
import kr.co.generic.wifianalyzer.wifi.model.SortBy;
import kr.co.generic.wifianalyzer.wifi.model.WiFiConnection;
import kr.co.generic.wifianalyzer.wifi.model.WiFiData;
import kr.co.generic.wifianalyzer.wifi.model.WiFiDetail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class ChannelGraphViewTest {
    private Settings settings;
    private GraphViewWrapper graphViewWrapper;

    private ChannelGraphView fixture;

    @Before
    public void setUp() {
        RobolectricUtil.INSTANCE.getActivity();

        graphViewWrapper = mock(GraphViewWrapper.class);

        settings = MainContextHelper.INSTANCE.getSettings();

        fixture = new ChannelGraphView(WiFiBand.GHZ2, new Pair<>(WiFiChannel.UNKNOWN, WiFiChannel.UNKNOWN));
        fixture.setGraphViewWrapper(graphViewWrapper);
    }

    @After
    public void tearDown() {
        MainContextHelper.INSTANCE.restore();
    }

    @Test
    public void testUpdate() throws Exception {
        // setup
        WiFiData wiFiData = new WiFiData(new ArrayList<WiFiDetail>(), WiFiConnection.EMPTY, new ArrayList<String>());
        withSettings();
        // execute
        fixture.update(wiFiData);
        // validate
        //noinspection unchecked
        verify(graphViewWrapper).removeSeries(any(Set.class));
        verify(graphViewWrapper).updateLegend(GraphLegend.RIGHT);
        verify(graphViewWrapper).setVisibility(View.VISIBLE);
        verifySettings();
    }

    private void verifySettings() {
        verify(settings, times(2)).getChannelGraphLegend();
        verify(settings).getSortBy();
        verify(settings).getWiFiBand();
    }

    private void withSettings() {
        when(settings.getChannelGraphLegend()).thenReturn(GraphLegend.RIGHT);
        when(settings.getSortBy()).thenReturn(SortBy.CHANNEL);
        when(settings.getWiFiBand()).thenReturn(WiFiBand.GHZ2);
    }

    @Test
    public void testGetGraphView() throws Exception {
        // setup
        GraphView expected = mock(GraphView.class);
        when(graphViewWrapper.getGraphView()).thenReturn(expected);
        // execute
        GraphView actual = fixture.getGraphView();
        // validate
        assertEquals(expected, actual);
        verify(graphViewWrapper).getGraphView();
    }
}