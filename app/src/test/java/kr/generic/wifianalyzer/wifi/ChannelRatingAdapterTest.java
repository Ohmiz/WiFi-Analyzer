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

package kr.co.generic.wifianalyzer.wifi;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import kr.co.generic.wifianalyzer.BuildConfig;
import kr.co.generic.wifianalyzer.MainActivity;
import kr.co.generic.wifianalyzer.MainContextHelper;
import kr.co.generic.wifianalyzer.R;
import kr.co.generic.wifianalyzer.RobolectricUtil;
import kr.co.generic.wifianalyzer.settings.Settings;
import kr.co.generic.wifianalyzer.wifi.band.WiFiBand;
import kr.co.generic.wifianalyzer.wifi.band.WiFiChannel;
import kr.co.generic.wifianalyzer.wifi.model.ChannelAPCount;
import kr.co.generic.wifianalyzer.wifi.model.ChannelRating;
import kr.co.generic.wifianalyzer.wifi.model.SortBy;
import kr.co.generic.wifianalyzer.wifi.model.Strength;
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
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class ChannelRatingAdapterTest {

    private ChannelRatingAdapter fixture;
    private Settings settings;
    private ChannelRating channelRating;
    private TextView bestChannels;
    private MainActivity mainActivity;

    @Before
    public void setUp() {
        mainActivity = RobolectricUtil.INSTANCE.getActivity();

        channelRating = mock(ChannelRating.class);
        bestChannels = new TextView(mainActivity);
        settings = MainContextHelper.INSTANCE.getSettings();

        fixture = new ChannelRatingAdapter(mainActivity, bestChannels);
        fixture.setChannelRating(channelRating);
    }

    @After
    public void tearDown() {
        MainContextHelper.INSTANCE.restore();
    }

    @Test
    public void testGetView() throws Exception {
        // setup
        int expectedSize = Strength.values().length;
        Strength expectedStrength = Strength.reverse(Strength.FOUR);
        WiFiChannel wiFiChannel = new WiFiChannel(1, 2);
        fixture.add(wiFiChannel);
        when(channelRating.getCount(wiFiChannel)).thenReturn(5);
        when(channelRating.getStrength(wiFiChannel)).thenReturn(Strength.FOUR);
        // execute
        View actual = fixture.getView(0, null, null);
        // validate
        assertNotNull(actual);

        assertEquals("1", ((TextView) actual.findViewById(R.id.channelNumber)).getText());
        assertEquals("5", ((TextView) actual.findViewById(R.id.accessPointCount)).getText());

        RatingBar ratingBar = (RatingBar) actual.findViewById(R.id.channelRating);
        assertEquals(expectedSize, ratingBar.getMax());
        assertEquals(expectedSize, ratingBar.getNumStars());
        assertEquals(expectedStrength.ordinal() + 1, (int) ratingBar.getRating());

        assertEquals("", bestChannels.getText());

        verify(channelRating).getCount(wiFiChannel);
        verify(channelRating).getStrength(wiFiChannel);
    }

    @Test
    public void testUpdate() throws Exception {
        // setup
        String expected = mainActivity.getResources().getText(R.string.channel_rating_best_none).toString();
        WiFiData wiFiData = new WiFiData(new ArrayList<WiFiDetail>(), WiFiConnection.EMPTY, new ArrayList<String>());
        List<WiFiDetail> wiFiDetails = wiFiData.getWiFiDetails(WiFiBand.GHZ5, SortBy.STRENGTH);
        when(settings.getWiFiBand()).thenReturn(WiFiBand.GHZ5);
        when(settings.getCountryCode()).thenReturn(Locale.US.getCountry());
        // execute
        fixture.update(wiFiData);
        // validate
        assertEquals(expected, bestChannels.getText());
        verify(channelRating).setWiFiDetails(wiFiDetails);
        verify(settings).getWiFiBand();
        verify(settings).getCountryCode();
    }

    @Test
    public void testBestChannelsGHZ2ErrorMessage() throws Exception {
        // setup
        Resources resources = mainActivity.getResources();
        String expected = resources.getText(R.string.channel_rating_best_none).toString()
            + resources.getText(R.string.channel_rating_best_alternative)
            + " " + WiFiBand.GHZ5.getBand();
        List<WiFiChannel> wiFiChannels = new ArrayList<>();
        List<ChannelAPCount> channelAPCounts = new ArrayList<>();
        when(channelRating.getBestChannels(wiFiChannels)).thenReturn(channelAPCounts);
        // execute
        fixture.bestChannels(WiFiBand.GHZ2, wiFiChannels);
        // validate
        assertEquals(expected, bestChannels.getText());
        verify(channelRating).getBestChannels(wiFiChannels);
    }

    @Test
    public void testBestChannelsGHZ5WithErrorMessage() throws Exception {
        // setup
        String expected = mainActivity.getResources().getText(R.string.channel_rating_best_none).toString();
        List<WiFiChannel> wiFiChannels = new ArrayList<>();
        List<ChannelAPCount> channelAPCounts = new ArrayList<>();
        when(channelRating.getBestChannels(wiFiChannels)).thenReturn(channelAPCounts);
        // execute
        fixture.bestChannels(WiFiBand.GHZ5, wiFiChannels);
        // validate
        assertEquals(expected, bestChannels.getText());
        verify(channelRating).getBestChannels(wiFiChannels);
    }

    @Test
    public void testBestChannelsGHZ5WithChannels() throws Exception {
        // setup
        String expected = "1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11...";
        List<WiFiChannel> wiFiChannels = new ArrayList<>();
        List<ChannelAPCount> channelAPCounts = withChannelAPCounts();
        when(channelRating.getBestChannels(wiFiChannels)).thenReturn(channelAPCounts);
        // execute
        fixture.bestChannels(WiFiBand.GHZ5, wiFiChannels);
        // validate
        assertEquals(expected, bestChannels.getText());
        verify(channelRating).getBestChannels(wiFiChannels);
    }

    @NonNull
    private List<ChannelAPCount> withChannelAPCounts() {
        List<ChannelAPCount> channelAPCounts = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            channelAPCounts.add(new ChannelAPCount(new WiFiChannel(i + 1, i + 100), 0));
        }
        return channelAPCounts;
    }
}