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

import android.net.wifi.WifiInfo;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import kr.co.generic.wifianalyzer.BuildConfig;
import kr.co.generic.wifianalyzer.MainActivity;
import kr.co.generic.wifianalyzer.MainContextHelper;
import kr.co.generic.wifianalyzer.R;
import kr.co.generic.wifianalyzer.RobolectricUtil;
import kr.co.generic.wifianalyzer.navigation.NavigationMenu;
import kr.co.generic.wifianalyzer.settings.Settings;
import kr.co.generic.wifianalyzer.wifi.band.WiFiWidth;
import kr.co.generic.wifianalyzer.wifi.model.WiFiAdditional;
import kr.co.generic.wifianalyzer.wifi.model.WiFiConnection;
import kr.co.generic.wifianalyzer.wifi.model.WiFiData;
import kr.co.generic.wifianalyzer.wifi.model.WiFiDetail;
import kr.co.generic.wifianalyzer.wifi.model.WiFiSignal;

import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class ConnectionViewTest {
    private static final String SSID = "SSID";
    private static final String BSSID = "BSSID";
    private static final String IP_ADDRESS = "IPADDRESS";

    private MainActivity mainActivity;
    private AccessPointView currentAccessPointView;
    private ConnectionView fixture;

    private Settings settings;
    private WiFiData wiFiData;
    private AccessPointDetail accessPointDetail;
    private AccessPointPopup accessPointPopup;

    @Before
    public void setUp() {
        mainActivity = RobolectricUtil.INSTANCE.getActivity();
        currentAccessPointView = mainActivity.getCurrentAccessPointView();
        mainActivity.setCurrentAccessPointView(AccessPointView.COMPLETE);

        accessPointDetail = mock(AccessPointDetail.class);
        accessPointPopup = mock(AccessPointPopup.class);

        wiFiData = mock(WiFiData.class);
        settings = MainContextHelper.INSTANCE.getSettings();

        fixture = new ConnectionView(mainActivity);
        fixture.setAccessPointDetail(accessPointDetail);
        fixture.setAccessPointPopup(accessPointPopup);
    }

    @After
    public void tearDown() {
        MainContextHelper.INSTANCE.restore();
        mainActivity.getNavigationMenuView().setCurrentNavigationMenu(NavigationMenu.ACCESS_POINTS);
        mainActivity.setCurrentAccessPointView(currentAccessPointView);
    }

    @Test
    public void testConnectionGoneWithNoConnectionInformation() throws Exception {
        // setup
        withConnectionInformation(withConnection(WiFiAdditional.EMPTY));
        // execute
        fixture.update(wiFiData);
        // validate
        assertEquals(View.GONE, mainActivity.findViewById(R.id.connection).getVisibility());
        verifyConnectionInformation();
    }

    @Test
    public void testConnectionVisibleWithConnectionInformation() throws Exception {
        // setup
        WiFiDetail connection = withConnection(withWiFiAdditional());
        withConnectionInformation(connection);
        withAccessPointDetailView(connection);
        // execute
        fixture.update(wiFiData);
        // validate
        assertEquals(View.VISIBLE, mainActivity.findViewById(R.id.connection).getVisibility());
        verifyConnectionInformation();
    }

    @Test
    public void testConnectionWithConnectionInformation() throws Exception {
        // setup
        WiFiAdditional wiFiAdditional = withWiFiAdditional();
        WiFiDetail connection = withConnection(wiFiAdditional);
        withConnectionInformation(connection);
        withAccessPointDetailView(connection);
        // execute
        fixture.update(wiFiData);
        // validate
        WiFiConnection wiFiConnection = wiFiAdditional.getWiFiConnection();
        View view = mainActivity.findViewById(R.id.connection);
        assertEquals(wiFiConnection.getIpAddress(), ((TextView) view.findViewById(R.id.ipAddress)).getText().toString());
        TextView linkSpeedView = (TextView) view.findViewById(R.id.linkSpeed);
        assertEquals(View.VISIBLE, linkSpeedView.getVisibility());
        assertEquals(wiFiConnection.getLinkSpeed() + WifiInfo.LINK_SPEED_UNITS, linkSpeedView.getText().toString());
    }

    @Test
    public void testConnectionWithInvalidLinkSpeed() throws Exception {
        // setup
        WiFiConnection wiFiConnection = new WiFiConnection(SSID, BSSID, IP_ADDRESS, WiFiConnection.LINK_SPEED_INVALID);
        WiFiDetail connection = withConnection(new WiFiAdditional(StringUtils.EMPTY, wiFiConnection));
        withConnectionInformation(connection);
        withAccessPointDetailView(connection);
        // execute
        fixture.update(wiFiData);
        // validate
        View view = mainActivity.findViewById(R.id.connection);
        TextView linkSpeedView = (TextView) view.findViewById(R.id.linkSpeed);
        assertEquals(View.GONE, linkSpeedView.getVisibility());
    }

    @Test
    public void testNoDataIsVisibleWithNoWiFiDetails() throws Exception {
        // setup
        when(wiFiData.getConnection()).thenReturn(withConnection(WiFiAdditional.EMPTY));
        // execute
        fixture.update(wiFiData);
        // validate
        assertEquals(View.VISIBLE, mainActivity.findViewById(R.id.nodata).getVisibility());
        assertEquals(View.VISIBLE, mainActivity.findViewById(R.id.nodatageo).getVisibility());
        assertEquals(View.VISIBLE, mainActivity.findViewById(R.id.nodatageourl).getVisibility());
        verify(wiFiData).getWiFiDetails(settings.getWiFiBand(), settings.getSortBy());
        verify(wiFiData).getWiFiDetails();
    }

    @Test
    public void testNoDataIsGoneWithNonWiFiBandSwitchableNavigationMenu() throws Exception {
        // setup
        mainActivity.getNavigationMenuView().setCurrentNavigationMenu(NavigationMenu.VENDOR_LIST);
        when(wiFiData.getConnection()).thenReturn(withConnection(WiFiAdditional.EMPTY));
        // execute
        fixture.update(wiFiData);
        // validate
        assertEquals(View.GONE, mainActivity.findViewById(R.id.nodata).getVisibility());
        assertEquals(View.GONE, mainActivity.findViewById(R.id.nodatageo).getVisibility());
        assertEquals(View.GONE, mainActivity.findViewById(R.id.nodatageourl).getVisibility());
        verify(wiFiData, never()).getWiFiDetails(settings.getWiFiBand(), settings.getSortBy());
        verify(wiFiData, never()).getWiFiDetails();
    }

    @Test
    public void testNoDataIsGoneWithWiFiDetails() throws Exception {
        // setup
        WiFiDetail wiFiDetail = withConnection(WiFiAdditional.EMPTY);
        when(wiFiData.getConnection()).thenReturn(wiFiDetail);
        //noinspection ArraysAsListWithZeroOrOneArgument
        when(wiFiData.getWiFiDetails(settings.getWiFiBand(), settings.getSortBy())).thenReturn(Arrays.asList(wiFiDetail));
        // execute
        fixture.update(wiFiData);
        // validate
        assertEquals(View.GONE, mainActivity.findViewById(R.id.nodata).getVisibility());
        assertEquals(View.GONE, mainActivity.findViewById(R.id.nodatageo).getVisibility());
        assertEquals(View.GONE, mainActivity.findViewById(R.id.nodatageourl).getVisibility());
        verify(wiFiData).getWiFiDetails(settings.getWiFiBand(), settings.getSortBy());
        verify(wiFiData, never()).getWiFiDetails();
    }

    @Test
    public void testViewCompactAddsPopup() throws Exception {
        // setup
        mainActivity.setCurrentAccessPointView(AccessPointView.COMPACT);
        WiFiDetail connection = withConnection(withWiFiAdditional());
        withConnectionInformation(connection);
        View view = withAccessPointDetailView(connection);
        // execute
        fixture.update(wiFiData);
        // validate
        verify(accessPointPopup).attach(view.findViewById(R.id.attachPopup), connection);
        verify(accessPointPopup).attach(view.findViewById(R.id.ssid), connection);
    }

    private WiFiDetail withConnection(@NonNull WiFiAdditional wiFiAdditional) {
        return new WiFiDetail(SSID, BSSID, StringUtils.EMPTY,
            new WiFiSignal(2435, 2435, WiFiWidth.MHZ_20, -55), wiFiAdditional);
    }

    private WiFiAdditional withWiFiAdditional() {
        WiFiConnection wiFiConnection = new WiFiConnection(SSID, BSSID, IP_ADDRESS, 11);
        return new WiFiAdditional(StringUtils.EMPTY, wiFiConnection);
    }

    private View withAccessPointDetailView(@NonNull WiFiDetail connection) {
        int layout = mainActivity.getCurrentAccessPointView().getLayout();
        ViewGroup parent = (ViewGroup) mainActivity.findViewById(R.id.connection).findViewById(R.id.connectionDetail);
        View view = mainActivity.getLayoutInflater().inflate(layout, parent, false);
        when(accessPointDetail.makeView(null, parent, connection, false)).thenReturn(view);
        when(accessPointDetail.makeView(parent.getChildAt(0), parent, connection, false)).thenReturn(view);
        return view;
    }

    private void withConnectionInformation(@NonNull WiFiDetail connection) {
        when(wiFiData.getConnection()).thenReturn(connection);
        when(wiFiData.getWiFiDetails(settings.getWiFiBand(), settings.getSortBy())).thenReturn(new ArrayList<WiFiDetail>());
    }

    private void verifyConnectionInformation() {
        verify(wiFiData).getConnection();
        verify(wiFiData).getWiFiDetails(settings.getWiFiBand(), settings.getSortBy());
    }

}