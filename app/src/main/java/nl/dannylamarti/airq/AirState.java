package nl.dannylamarti.airq;

import android.content.Context;
import android.support.v4.content.ContextCompat;

/**
 * Created by danny.lamarti on 10/12/16.
 */

public enum AirState {
    VERY_HIGH,
    HIGH,
    MEDIUM,
    LOW,
    VERY_LOW;

    public static AirState fromAirQuality(float airQuality) {
        if (airQuality <= 20) {
            return VERY_LOW;
        } else if (airQuality <= 40) {
            return LOW;
        } else if (airQuality <= 60) {
            return MEDIUM;
        } else if (airQuality <= 80) {
            return HIGH;
        } else {
            return HIGH;
        }
    }

    public CharSequence getRemark(Context context) {
        final int id;

        switch (this) {
            case VERY_HIGH:
                id = R.string.excellent_air;
                break;
            case HIGH:
                id = R.string.fair_air;
                break;
            case MEDIUM:
                id = R.string.moderate_air;
                break;
            case LOW:
                id = R.string.low_air;
                break;
            case VERY_LOW:
                id = R.string.poor_air;
                break;
            default:
                throw new IllegalStateException();
        }

        return context.getText(id);
    }

    public CharSequence getQuote(Context context) {
        return "test 123";
    }

    public int getColor(Context context) {
        final int id;

        switch (this) {
            case VERY_HIGH:
                id = R.color.emerald;
                break;
            case HIGH:
                id = R.color.nephritis;
                break;
            case MEDIUM:
                id = R.color.sunflower;
                break;
            case LOW:
                id = R.color.pumpkin;
                break;
            case VERY_LOW:
                id = R.color.promegate;
                break;
            default:
                throw new IllegalStateException();
        }

        return ContextCompat.getColor(context, id);
    }
}
