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

package kr.co.generic.wifianalyzer.navigation;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.MenuItem;

import kr.co.generic.wifianalyzer.MainActivity;
import kr.co.generic.wifianalyzer.about.AboutActivity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ActivityItemTest {

    @Mock
    private MainActivity mockMainActivity;
    @Mock
    private MenuItem mockMenuItem;
    @Mock
    private Intent mockIntent;

    @Test
    public void testActivate() throws Exception {
        // setup
        ActivityItem fixture = new ActivityItem(AboutActivity.class) {
            @Override
            Intent createIntent(@NonNull MainActivity mainActivity) {
                assertEquals(mockMainActivity, mainActivity);
                return mockIntent;
            }
        };
        // execute
        fixture.activate(mockMainActivity, mockMenuItem, NavigationMenu.ABOUT);
        // validate
        verify(mockMainActivity).startActivity(mockIntent);
    }
}