/**
 * Unit tests for PackageSorter.
 *
 * Uses a lightweight, zero-dependency test runner so the project stays
 * self-contained (no Maven/Gradle required).
 *
 * Run: java PackageSorterTest
 */
public class PackageSorterTest {

    // -------------------------------------------------------------------------
    // Minimal test runner
    // -------------------------------------------------------------------------

    private static int passed = 0;
    private static int failed = 0;

    private static void expect(String testName, String expected, String actual) {
        if (expected.equals(actual)) {
            System.out.printf("  [PASS] %s%n", testName);
            passed++;
        } else {
            System.out.printf("  [FAIL] %s%n         expected: %s%n           actual: %s%n",
                    testName, expected, actual);
            failed++;
        }
    }

    private static void expectException(String testName, Runnable block) {
        try {
            block.run();
            System.out.printf("  [FAIL] %s — expected IllegalArgumentException but none was thrown%n", testName);
            failed++;
        } catch (IllegalArgumentException e) {
            System.out.printf("  [PASS] %s%n", testName);
            passed++;
        }
    }

    // -------------------------------------------------------------------------
    // Test cases
    // -------------------------------------------------------------------------

    private static void testStandardPackages() {
        System.out.println("\n── STANDARD packages ──");
        expect("Small, light package",
                "STANDARD", PackageSorter.sort(10, 10, 10, 5));
        expect("Zero mass is still standard",
                "STANDARD", PackageSorter.sort(10, 10, 10, 0));
        expect("Volume just under 1,000,000 and light",
                "STANDARD", PackageSorter.sort(99, 100, 100, 10));
        expect("All dimensions just under 150, volume under limit",
                "STANDARD", PackageSorter.sort(99.9, 99.9, 99.9, 1));
        expect("Mass just under 20 kg",
                "STANDARD", PackageSorter.sort(10, 10, 10, 19.9));
        expect("1×1×1 cm, 0 kg — smallest possible package",
                "STANDARD", PackageSorter.sort(1, 1, 1, 0));
    }

    private static void testBulkyOnlyPackages() {
        System.out.println("\n── SPECIAL (bulky only) packages ──");
        expect("Volume exactly 1,000,000 cm³",
                "SPECIAL", PackageSorter.sort(100, 100, 100, 10));
        expect("Volume over 1,000,000 cm³",
                "SPECIAL", PackageSorter.sort(200, 100, 100, 10));
        expect("Width exactly 150 cm",
                "SPECIAL", PackageSorter.sort(150, 1, 1, 1));
        expect("Height exactly 150 cm",
                "SPECIAL", PackageSorter.sort(1, 150, 1, 1));
        expect("Length exactly 150 cm",
                "SPECIAL", PackageSorter.sort(1, 1, 150, 1));
        expect("Width over 150 cm",
                "SPECIAL", PackageSorter.sort(200, 10, 10, 1));
        expect("Large volume, light",
                "SPECIAL", PackageSorter.sort(500, 500, 500, 1));
    }

    private static void testHeavyOnlyPackages() {
        System.out.println("\n── SPECIAL (heavy only) packages ──");
        expect("Mass exactly 20 kg",
                "SPECIAL", PackageSorter.sort(10, 10, 10, 20));
        expect("Mass over 20 kg",
                "SPECIAL", PackageSorter.sort(10, 10, 10, 100));
        expect("Very heavy, small package",
                "SPECIAL", PackageSorter.sort(1, 1, 1, 999));
    }

    private static void testRejectedPackages() {
        System.out.println("\n── REJECTED (bulky AND heavy) packages ──");
        expect("Volume = 1,000,000 and mass = 20 (both at boundary)",
                "REJECTED", PackageSorter.sort(100, 100, 100, 20));
        expect("Large volume and heavy",
                "REJECTED", PackageSorter.sort(200, 200, 200, 25));
        expect("Oversized dimension and heavy",
                "REJECTED", PackageSorter.sort(150, 1, 1, 20));
        expect("Width over 150 and mass over 20",
                "REJECTED", PackageSorter.sort(200, 10, 10, 50));
        expect("All extremes",
                "REJECTED", PackageSorter.sort(1000, 1000, 1000, 1000));
    }

    private static void testEdgeCases() {
        System.out.println("\n── Edge cases ──");
        expect("Fractional dimensions produce correct volume classification",
                "STANDARD", PackageSorter.sort(99.9, 100.0, 100.0, 5));
        expect("Exactly at all three boundaries",
                "REJECTED", PackageSorter.sort(100, 100, 100, 20));
        expect("Two dimensions trigger bulky via dimension rule",
                "SPECIAL", PackageSorter.sort(150, 150, 1, 1));
    }

    private static void testInvalidInputs() {
        System.out.println("\n── Invalid inputs (must throw) ──");
        expectException("Zero width throws",       () -> PackageSorter.sort(0,  10, 10, 5));
        expectException("Negative width throws",   () -> PackageSorter.sort(-1, 10, 10, 5));
        expectException("Zero height throws",      () -> PackageSorter.sort(10, 0,  10, 5));
        expectException("Zero length throws",      () -> PackageSorter.sort(10, 10,  0, 5));
        expectException("Negative mass throws",    () -> PackageSorter.sort(10, 10, 10, -1));
    }

    // -------------------------------------------------------------------------
    // Entry point
    // -------------------------------------------------------------------------

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║   PackageSorter — Test Suite         ║");
        System.out.println("╚══════════════════════════════════════╝");

        testStandardPackages();
        testBulkyOnlyPackages();
        testHeavyOnlyPackages();
        testRejectedPackages();
        testEdgeCases();
        testInvalidInputs();

        System.out.println();
        System.out.printf("Results: %d passed, %d failed%n", passed, failed);

        if (failed > 0) {
            System.exit(1);
        }
    }
}
