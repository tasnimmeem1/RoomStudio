package edu.farmingdale.roomstudio;

record Item(Fixture fixture, double x, double y, boolean rotated, double baseWidth, double baseDepth) {
    double width() { return rotated ? baseDepth : baseWidth; }
    double depth() { return rotated ? baseWidth : baseDepth; }
    boolean ceiling() { return fixture.name().equals("Light fixture"); }
    double route() { return x + width() / 2 + y + depth() / 2; }
}
