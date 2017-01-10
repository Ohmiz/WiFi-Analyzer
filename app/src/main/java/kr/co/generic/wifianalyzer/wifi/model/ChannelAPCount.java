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

import android.support.annotation.NonNull;

import kr.co.generic.wifianalyzer.wifi.band.WiFiChannel;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class ChannelAPCount implements Comparable<ChannelAPCount> {
    private final WiFiChannel wiFiChannel;
    private final int count;

    public ChannelAPCount(@NonNull WiFiChannel wiFiChannel, int count) {
        this.wiFiChannel = wiFiChannel;
        this.count = count;
    }

    public WiFiChannel getWiFiChannel() {
        return wiFiChannel;
    }

    int getCount() {
        return count;
    }

    @Override
    public int compareTo(@NonNull ChannelAPCount another) {
        return new CompareToBuilder()
            .append(getCount(), another.getCount())
            .append(getWiFiChannel(), another.getWiFiChannel())
            .toComparison();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
