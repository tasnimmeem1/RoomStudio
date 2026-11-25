package edu.farmingdale.roomstudio;

import java.util.Map;
import java.util.Set;

final class StudioCatalog {
    private StudioCatalog() {}

    static final Map<String, Fixture> FIXTURES = Map.ofEntries(
        Map.entry("vanity", new Fixture("Vanity", 2.5, 1.75, 390, true)),
        Map.entry("toilet", new Fixture("Toilet", 1.5, 2.5, 240, true)),
        Map.entry("shower", new Fixture("Shower kit", 3, 3, 820, true)),
        Map.entry("tub", new Fixture("Bathtub", 5, 2.5, 650, true)),
        Map.entry("cabinet", new Fixture("Linen cabinet", 2, 1.25, 195, false)),
        Map.entry("light", new Fixture("Light fixture", 1, 1, 95, false)),
        Map.entry("sofa", new Fixture("Linen sofa", 6.5, 2.75, 890, false)),
        Map.entry("coffee", new Fixture("Coffee table", 3, 1.75, 215, false)),
        Map.entry("console", new Fixture("Media console", 5, 1.5, 325, false)),
        Map.entry("bed", new Fixture("Queen bed", 5, 6.75, 710, false)),
        Map.entry("nightstand", new Fixture("Nightstand", 1.75, 1.5, 140, false)),
        Map.entry("dresser", new Fixture("Dresser", 4, 1.75, 375, false)),
        Map.entry("dining", new Fixture("Dining table", 5, 3, 540, false)),
        Map.entry("chair", new Fixture("Dining chair", 1.5, 1.5, 120, false)),
        Map.entry("island", new Fixture("Kitchen island", 5, 2.5, 940, false)),
        Map.entry("range", new Fixture("Range", 2.5, 2.5, 720, false)),
        Map.entry("fridge", new Fixture("Refrigerator", 3, 2.75, 1050, false)),
        Map.entry("basecab", new Fixture("Kitchen base cabinet", 4, 2, 540, false)),
        Map.entry("sink", new Fixture("Sink cabinet", 3, 2, 510, true)),
        Map.entry("desk", new Fixture("Writing desk", 4, 2, 290, false)),
        Map.entry("bookshelf", new Fixture("Bookshelf", 3, 1.25, 245, false)),
        Map.entry("armchair", new Fixture("Accent chair", 2.75, 2.75, 315, false)),
        Map.entry("loveseat", new Fixture("Bouclé loveseat", 4.75, 2.9, 760, false)),
        Map.entry("sectional", new Fixture("Chaise sectional", 8, 5.4, 1480, false)),
        Map.entry("wingback", new Fixture("Wingback chair", 2.8, 2.9, 410, false)),
        Map.entry("platform", new Fixture("Walnut platform bed", 5.75, 7.2, 980, false)),
        Map.entry("tufted", new Fixture("Tufted king bed", 6.75, 7.35, 1290, false)),
        Map.entry("roundtable", new Fixture("Round pedestal table", 4.2, 4.2, 690, false)),
        Map.entry("ovalcoffee", new Fixture("Oval coffee table", 3.7, 2.1, 320, false)),
        Map.entry("olive", new Fixture("Potted olive tree", 2.15, 2.15, 145, false)),
        Map.entry("ficus", new Fixture("Potted ficus", 1.85, 1.85, 110, false)),
        Map.entry("wishbone", new Fixture("Wishbone chair", 1.85, 1.8, 195, false)),
        Map.entry("curve", new Fixture("Curved bouclé sofa", 7.2, 3, 1280, false)),
        Map.entry("taper", new Fixture("Tapered-leg leather sofa", 6.8, 2.7, 1160, false)),
        Map.entry("chester", new Fixture("Tufted loveseat", 5.25, 2.9, 985, false)),
        Map.entry("roll", new Fixture("Rolled-arm linen sofa", 7.1, 3.2, 1080, false)),
        Map.entry("modular", new Fixture("Low modular sofa", 7.8, 3.35, 1390, false)),
        Map.entry("lounge", new Fixture("Low lounge chair", 2.8, 2.8, 395, false)),
        Map.entry("barrel", new Fixture("Barrel chair", 2.65, 2.6, 440, false)),
        Map.entry("drum", new Fixture("Walnut drum table", 3.2, 3.2, 410, false)),
        Map.entry("stone", new Fixture("Travertine side table", 1.75, 1.75, 295, false)),
        Map.entry("sideboard", new Fixture("Fluted oak sideboard", 5.5, 1.55, 780, false)),
        Map.entry("trestle", new Fixture("Trestle dining table", 6.6, 3.1, 830, false)),
        Map.entry("sleigh", new Fixture("Oak sleigh bed", 5.6, 7.1, 1125, false)));
    static final Set<String> FURNITURE = Set.of("Linen sofa", "Coffee table", "Media console", "Queen bed", "Nightstand",
        "Dresser", "Dining table", "Dining chair", "Kitchen island", "Range", "Refrigerator", "Writing desk", "Bookshelf", "Accent chair", "Bouclé loveseat", "Chaise sectional", "Wingback chair", "Walnut platform bed", "Tufted king bed", "Round pedestal table", "Oval coffee table", "Wishbone chair", "Curved bouclé sofa", "Tapered-leg leather sofa", "Tufted loveseat", "Rolled-arm linen sofa", "Low modular sofa", "Low lounge chair", "Barrel chair", "Walnut drum table", "Travertine side table", "Fluted oak sideboard", "Trestle dining table", "Oak sleigh bed", "Potted olive tree", "Potted ficus");
    static final Map<String, Integer> FLOORS = Map.of("Oak-look vinyl", 4, "Porcelain tile", 8, "Terrazzo tile", 12);
    static final Map<String, Integer> WALLS = Map.of("Paint", 36, "Wall tile", 9);
}
