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

package kr.co.generic.wifianalyzer.about;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import kr.co.generic.wifianalyzer.BuildConfig;
import kr.co.generic.wifianalyzer.MainContext;
import kr.co.generic.wifianalyzer.R;
import kr.co.generic.wifianalyzer.settings.Settings;
import kr.co.generic.wifianalyzer.settings.ThemeStyle;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setCustomTheme();

        super.onCreate(savedInstanceState);

        setContentView(R.layout.about_content);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setExtraInformation();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setCustomTheme() {
        Settings settings = MainContext.INSTANCE.getSettings();
        if (settings != null) {
            ThemeStyle themeStyle = settings.getThemeStyle();
            setTheme(themeStyle.themeAppCompatStyle());
        }
    }

    private void setExtraInformation() {
        //((TextView) findViewById(R.id.about_version_info)).setText(
        //    BuildConfig.VERSION_NAME + " - " + BuildConfig.VERSION_CODE + " (" + Build.VERSION.RELEASE + "-" + Build.VERSION.SDK_INT + ")");
        //((TextView) findViewById(R.id.about_package_name)).setText(BuildConfig.APPLICATION_ID);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
