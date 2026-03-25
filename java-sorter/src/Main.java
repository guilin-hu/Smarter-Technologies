/**
 * Command-line interface for the PackageSorter.
 *
 * Usage:
 *   java Main <width> <height> <length> <mass>
 *
 * Example:
 *   java Main 10 10 10 5        →  STANDARD
 *   java Main 200 10 10 5       →  SPECIAL  (bulky dimension)
 *   java Main 10 10 10 25       →  SPECIAL  (heavy)
 *   java Main 200 200 200 25    →  REJECTED (both)
 */
public class Main {

    public static void main(String[] args) {
        if (args.length == 4) {
            runFromArgs(args);
        } else if (args.length == 0) {
            runDemo();
        } else {
            System.err.println("Usage: java Main <width> <height> <length> <mass>");
            System.err.println("   or: java Main   (runs built-in demo)");
            System.exit(1);
        }
    }

    private static void runFromArgs(String[] args) {
        try {
            double width  = Double.parseDouble(args[0]);
            double height = Double.parseDouble(args[1]);
            double length = Double.parseDouble(args[2]);
            double mass   = Double.parseDouble(args[3]);

            String stack = PackageSorter.sort(width, height, length, mass);
            System.out.printf("Package (%.1f × %.1f × %.1f cm, %.1f kg)  →  %s%n",
                    width, height, length, mass, stack);
        } catch (NumberFormatException e) {
            System.err.println("Error: all arguments must be numeric.");
            System.exit(1);
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }

    private static void runDemo() {
        System.out.println("=== Package Sorter Demo ===");
        System.out.println();

        Object[][] cases = {
            // { width, height, length, mass, label }
            {  10.0,  10.0,  10.0,   5.0, "Small, light package"                },
            { 100.0, 100.0, 100.0,  10.0, "Volume = 1,000,000 (bulky boundary)" },
            { 100.0,  99.0, 100.0,  10.0, "Volume just under threshold"         },
            { 150.0,   1.0,   1.0,   1.0, "One dimension = 150 (bulky)"         },
            { 149.9,   1.0,   1.0,   1.0, "One dimension just under 150"        },
            {  10.0,  10.0,  10.0,  20.0, "Mass = 20 kg (heavy boundary)"       },
            {  10.0,  10.0,  10.0,  19.9, "Mass just under heavy threshold"     },
            { 200.0, 200.0, 200.0,  25.0, "Both bulky and heavy"                },
        };

        System.out.printf("%-40s  %8s  %8s  %8s  %6s  %-9s%n",
                "Description", "W(cm)", "H(cm)", "L(cm)", "kg", "Stack");
        System.out.println("-".repeat(95));

        for (Object[] c : cases) {
            double w = (double) c[0];
            double h = (double) c[1];
            double l = (double) c[2];
            double m = (double) c[3];
            String label = (String) c[4];
            String stack = PackageSorter.sort(w, h, l, m);
            System.out.printf("%-40s  %8.1f  %8.1f  %8.1f  %6.1f  %-9s%n",
                    label, w, h, l, m, stack);
        }
    }
}
