package gregfluxology.util;

import gregtech.api.capability.FeCompat;

public class GFyUtility {

    /**
     * Copied from GTCEu
     * Safely cast a Long to an Int without overflow.
     *
     * @param v The Long value to cast to an Int.
     * @return v, cast to Int, or Integer.MAX_VALUE if it would overflow.
     */
    public static int safeCastLongToInt(long v) {
        return v > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) v;
    }

    public static int safeConvertEUToFE(long eu) {
        return safeCastLongToInt(eu * FeCompat.ratio(false));
    }
}
