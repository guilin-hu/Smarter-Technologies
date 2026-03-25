/**
 * Sorts packages for the Smarter Technology robotic automation factory.
 *
 * Rules:
 *   - Bulky: volume (w × h × l) >= 1,000,000 cm³  OR  any dimension >= 150 cm
 *   - Heavy: mass >= 20 kg
 *
 * Stacks:
 *   - STANDARD : not bulky, not heavy
 *   - SPECIAL  : bulky XOR heavy  (one condition, not both)
 *   - REJECTED : both bulky AND heavy
 */
public class PackageSorter {

    private static final long   VOLUME_THRESHOLD    = 1_000_000L; // cm³
    private static final double DIMENSION_THRESHOLD = 150.0;       // cm
    private static final double MASS_THRESHOLD      = 20.0;        // kg

    /**
     * Determines the correct dispatch stack for a package.
     *
     * @param width  Width  of the package in centimetres (must be > 0)
     * @param height Height of the package in centimetres (must be > 0)
     * @param length Length of the package in centimetres (must be > 0)
     * @param mass   Mass   of the package in kilograms   (must be >= 0)
     * @return "STANDARD", "SPECIAL", or "REJECTED"
     * @throws IllegalArgumentException if any dimension is non-positive or mass is negative
     */
    public static String sort(double width, double height, double length, double mass) {
        validateInputs(width, height, length, mass);

        boolean bulky = isBulky(width, height, length);
        boolean heavy = isHeavy(mass);

        if (bulky && heavy) {
            return "REJECTED";
        } else if (bulky || heavy) {
            return "SPECIAL";
        } else {
            return "STANDARD";
        }
    }

    // -------------------------------------------------------------------------
    // Private helpers
    // -------------------------------------------------------------------------

    private static boolean isBulky(double width, double height, double length) {
        long volume = (long) width * (long) height * (long) length;
        boolean oversizedVolume    = volume >= VOLUME_THRESHOLD;
        boolean oversizedDimension = width  >= DIMENSION_THRESHOLD
                                  || height >= DIMENSION_THRESHOLD
                                  || length >= DIMENSION_THRESHOLD;
        return oversizedVolume || oversizedDimension;
    }

    private static boolean isHeavy(double mass) {
        return mass >= MASS_THRESHOLD;
    }

    private static void validateInputs(double width, double height, double length, double mass) {
        if (width  <= 0) throw new IllegalArgumentException("Width must be positive, got: "  + width);
        if (height <= 0) throw new IllegalArgumentException("Height must be positive, got: " + height);
        if (length <= 0) throw new IllegalArgumentException("Length must be positive, got: " + length);
        if (mass   <  0) throw new IllegalArgumentException("Mass must be non-negative, got: " + mass);
    }
}
