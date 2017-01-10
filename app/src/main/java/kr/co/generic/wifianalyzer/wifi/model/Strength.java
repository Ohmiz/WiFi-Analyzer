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

public enum Strength {
    ZERO(kr.co.generic.wifianalyzer.R.drawable.ic_signal_wifi_0_bar_black_36dp, kr.co.generic.wifianalyzer.R.color.error_color),
    ONE(kr.co.generic.wifianalyzer.R.drawable.ic_signal_wifi_1_bar_black_36dp, kr.co.generic.wifianalyzer.R.color.warning_color),
    TWO(kr.co.generic.wifianalyzer.R.drawable.ic_signal_wifi_2_bar_black_36dp, kr.co.generic.wifianalyzer.R.color.warning_color),
    THREE(kr.co.generic.wifianalyzer.R.drawable.ic_signal_wifi_3_bar_black_36dp, kr.co.generic.wifianalyzer.R.color.success_color),
    FOUR(kr.co.generic.wifianalyzer.R.drawable.ic_signal_wifi_4_bar_black_36dp, kr.co.generic.wifianalyzer.R.color.success_color);

    private final int imageResource;
    private final int colorResource;

    Strength(int imageResource, int colorResource) {
        this.imageResource = imageResource;
        this.colorResource = colorResource;
    }

    public static Strength calculate(int level) {
        int index = WiFiUtils.calculateSignalLevel(level, values().length);
        return Strength.values()[index];
    }

    public static Strength reverse(Strength strength) {
        int index = Strength.values().length - strength.ordinal() - 1;
        return Strength.values()[index];
    }

    public int colorResource() {
        return colorResource;
    }

    public int imageResource() {
        return imageResource;
    }

    public boolean weak() {
        return ZERO.equals(this);
    }

}
