#!/usr/bin/env bash
set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
SRC="$SCRIPT_DIR/src"
OUT="$SCRIPT_DIR/out"

mkdir -p "$OUT"

echo "Compiling..."
javac -d "$OUT" "$SRC"/PackageSorter.java "$SRC"/PackageSorterTest.java "$SRC"/Main.java
echo "Done."
echo ""

echo "Running tests..."
java -cp "$OUT" PackageSorterTest
echo ""

echo "Running demo..."
java -cp "$OUT" Main
