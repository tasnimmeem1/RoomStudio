package edu.farmingdale.roomstudio;

record Work(String name, int crew, int slots, int rate, int start) {
    int end() { return start + slots; }
    double cost() { return slots * crew * rate / 2.0; }
}
