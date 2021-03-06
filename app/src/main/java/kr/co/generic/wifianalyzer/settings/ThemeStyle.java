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

package kr.co.generic.wifianalyzer.settings;

public enum ThemeStyle {
    DARK(kr.co.generic.wifianalyzer.R.style.ThemeAppCompatDark, kr.co.generic.wifianalyzer.R.style.ThemeDeviceDefaultDark),
    LIGHT(kr.co.generic.wifianalyzer.R.style.ThemeAppCompatLight, kr.co.generic.wifianalyzer.R.style.ThemeDeviceDefaultLight);

    private final int themeAppCompatStyle;
    private final int themeDeviceDefaultStyle;

    ThemeStyle(int themeAppCompatStyle, int themeDeviceDefaultStyle) {
        this.themeAppCompatStyle = themeAppCompatStyle;
        this.themeDeviceDefaultStyle = themeDeviceDefaultStyle;
    }

    public static ThemeStyle find(int index) {
        if (index < 0 || index >= values().length) {
            return DARK;
        }
        return values()[index];

    }

    public int themeAppCompatStyle() {
        return themeAppCompatStyle;
    }

    public int themeDeviceDefaultStyle() {
        return themeDeviceDefaultStyle;
    }
}
