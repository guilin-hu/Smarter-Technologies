# Package Sorter — Smarter Technology Robotic Arm

A Java implementation of the package dispatching function for Smarter Technology's robotic automation factory.

## Problem

Sort packages into the correct stack based on their volume and mass:

| Condition                                              | Result   |
|--------------------------------------------------------|----------|
| Not bulky **and** not heavy                            | STANDARD |
| Bulky **or** heavy (but not both)                      | SPECIAL  |
| Bulky **and** heavy                                    | REJECTED |

**Bulky** = volume (W × H × L) ≥ 1,000,000 cm³ **or** any single dimension ≥ 150 cm  
**Heavy** = mass ≥ 20 kg

## Project Structure

```
java-sorter/
├── src/
│   ├── PackageSorter.java      # Core sorting logic
│   ├── PackageSorterTest.java  # Unit tests (zero external dependencies)
│   └── Main.java               # CLI entry point + demo
└── run.sh                      # Build, test, and demo script
```

## Running

```bash
bash java-sorter/run.sh
```

This compiles all sources, runs the full test suite, then prints a demo table.

### Sort a specific package via CLI

```bash
cd java-sorter
mkdir -p out
javac -d out src/PackageSorter.java src/Main.java
java -cp out Main <width> <height> <length> <mass>

# Examples
java -cp out Main 10 10 10 5       # → STANDARD
java -cp out Main 100 100 100 10   # → SPECIAL  (volume boundary)
java -cp out Main 150 1 1 1        # → SPECIAL  (dimension boundary)
java -cp out Main 10 10 10 20      # → SPECIAL  (mass boundary)
java -cp out Main 200 200 200 25   # → REJECTED (both)
```

## API

```java
String stack = PackageSorter.sort(width, height, length, mass);
// Returns: "STANDARD" | "SPECIAL" | "REJECTED"
```

**Parameters** (all numeric):
- `width`, `height`, `length` — dimensions in centimetres (must be > 0)
- `mass` — weight in kilograms (must be ≥ 0)

Throws `IllegalArgumentException` for invalid inputs.

## Test Coverage

29 test cases covering:
- All three output categories (STANDARD, SPECIAL, REJECTED)
- Volume boundary (exactly 1,000,000 cm³ and just below)
- Dimension boundary (each axis independently at exactly 150 cm and just below)
- Mass boundary (exactly 20 kg and just below)
- Fractional values and zero mass
- Invalid inputs (non-positive dimensions, negative mass)
