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

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import kr.co.generic.wifianalyzer.MainContext;
import kr.co.generic.wifianalyzer.settings.Settings;
import kr.co.generic.wifianalyzer.wifi.band.WiFiBand;
import kr.co.generic.wifianalyzer.wifi.band.WiFiChannel;
import kr.co.generic.wifianalyzer.wifi.model.ChannelAPCount;
import kr.co.generic.wifianalyzer.wifi.model.ChannelRating;
import kr.co.generic.wifianalyzer.wifi.model.SortBy;
import kr.co.generic.wifianalyzer.wifi.model.Strength;
import kr.co.generic.wifianalyzer.wifi.model.WiFiData;
import kr.co.generic.wifianalyzer.wifi.scanner.UpdateNotifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

class ChannelRatingAdapter extends ArrayAdapter<WiFiChannel> implements UpdateNotifier {
    private static final int MAX_CHANNELS_TO_DISPLAY = 10;

    private final TextView bestChannels;
    private ChannelRating channelRating;

    ChannelRatingAdapter(@NonNull Context context, @NonNull TextView bestChannels) {
        super(context, kr.co.generic.wifianalyzer.R.layout.channel_rating_details, new ArrayList<WiFiChannel>());
        this.bestChannels = bestChannels;
        setChannelRating(new ChannelRating());
    }

    void setChannelRating(@NonNull ChannelRating channelRating) {
        this.channelRating = channelRating;
    }

    @Override
    public void update(@NonNull WiFiData wiFiData) {
        Settings settings = MainContext.INSTANCE.getSettings();
        WiFiBand wiFiBand = settings.getWiFiBand();
        List<WiFiChannel> wiFiChannels = setWiFiChannels(wiFiBand);
        channelRating.setWiFiDetails(wiFiData.getWiFiDetails(wiFiBand, SortBy.STRENGTH));
        bestChannels(wiFiBand, wiFiChannels);
        notifyDataSetChanged();
    }

    private List<WiFiChannel> setWiFiChannels(WiFiBand wiFiBand) {
        Settings settings = MainContext.INSTANCE.getSettings();
        String countryCode = settings.getCountryCode();
        List<WiFiChannel> wiFiChannels = wiFiBand.getWiFiChannels().getAvailableChannels(countryCode);
        clear();
        addAll(wiFiChannels);
        return wiFiChannels;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater layoutInflater = MainContext.INSTANCE.getMainActivity().getLayoutInflater();
            view = layoutInflater.inflate(kr.co.generic.wifianalyzer.R.layout.channel_rating_details, parent, false);
        }

        WiFiChannel wiFiChannel = getItem(position);
        if (wiFiChannel == null) {
            return view;
        }

        ((TextView) view.findViewById(kr.co.generic.wifianalyzer.R.id.channelNumber))
            .setText(String.format(Locale.ENGLISH, "%d", wiFiChannel.getChannel()));
        ((TextView) view.findViewById(kr.co.generic.wifianalyzer.R.id.accessPointCount))
            .setText(String.format(Locale.ENGLISH, "%d", channelRating.getCount(wiFiChannel)));
        Strength strength = Strength.reverse(channelRating.getStrength(wiFiChannel));
        RatingBar ratingBar = (RatingBar) view.findViewById(kr.co.generic.wifianalyzer.R.id.channelRating);
        int size = Strength.values().length;
        ratingBar.setMax(size);
        ratingBar.setNumStars(size);
        ratingBar.setRating(strength.ordinal() + 1);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            ratingBar.getProgressDrawable().setColorFilter(ContextCompat.getColor(getContext(), kr.co.generic.wifianalyzer.R.color.success_color), PorterDuff.Mode.SRC_ATOP);
        } else {
            ratingBar.setProgressTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), strength.colorResource())));
        }

        return view;
    }

    void bestChannels(@NonNull WiFiBand wiFiBand, @NonNull List<WiFiChannel> wiFiChannels) {
        List<ChannelAPCount> channelAPCounts = channelRating.getBestChannels(wiFiChannels);
        int channelCount = 0;
        StringBuilder result = new StringBuilder();
        for (ChannelAPCount channelAPCount : channelAPCounts) {
            if (channelCount > MAX_CHANNELS_TO_DISPLAY) {
                result.append("...");
                break;
            }
            if (result.length() > 0) {
                result.append(", ");
            }
            result.append(channelAPCount.getWiFiChannel().getChannel());
            channelCount++;
        }
        if (result.length() > 0) {
            bestChannels.setText(result.toString());
            bestChannels.setTextColor(ContextCompat.getColor(getContext(), kr.co.generic.wifianalyzer.R.color.success_color));
        } else {
            Resources resources = getContext().getResources();
            StringBuilder message = new StringBuilder(resources.getText(kr.co.generic.wifianalyzer.R.string.channel_rating_best_none));
            if (WiFiBand.GHZ2.equals(wiFiBand)) {
                message.append(resources.getText(kr.co.generic.wifianalyzer.R.string.channel_rating_best_alternative));
                message.append(" ");
                message.append(WiFiBand.GHZ5.getBand());
            }
            bestChannels.setText(message);
            bestChannels.setTextColor(ContextCompat.getColor(getContext(), kr.co.generic.wifianalyzer.R.color.error_color));
        }
    }

}