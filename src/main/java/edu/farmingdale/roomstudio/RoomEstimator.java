package edu.farmingdale.roomstudio;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

final class RoomEstimator {

    private static final Pattern NUMBER = Pattern.compile("[0-9]{1,2}(?:\\.[0-9]{1,2})?");
    private final ExecutorService workers = Executors.newFixedThreadPool(4);

    public void shutdown() {
        workers.shutdownNow();
    }

    private static double number(String value, double min, double max) {
        if (!NUMBER.matcher(value).matches()) throw new IllegalArgumentException("Enter a number with up to two decimals.");
        double n = Double.parseDouble(value);
        if (n < min || n > max) throw new IllegalArgumentException("Room dimensions or fixture coordinates are out of range.");
        return n;
    }
    private static int ceil(double n) { return (int)Math.ceil(n); }
    private static int slots(double hours) { return ceil(hours * 2); }
    private static int rate(Map<String,String> form, String key, Map<String,Integer> catalog) {
        Integer value = catalog.get(form.getOrDefault(key, ""));
        if (value == null) throw new IllegalArgumentException("Choose a valid finish.");
        return value;
    }
    String estimate(Map<String,String> form) {
        double w = number(form.getOrDefault("w",""), 5, 30);
        double d = number(form.getOrDefault("d",""), 5, 30);
        double h = number(form.getOrDefault("h",""), 7, 14);
        int floorRate = rate(form,"floor",StudioCatalog.FLOORS);
        int wallRate = rate(form,"wall",StudioCatalog.WALLS);
        boolean paint = form.get("wall").equals("Paint");
        String shape = form.getOrDefault("shape", "rectangle");
        if (!Set.of("rectangle", "l").contains(shape)) throw new IllegalArgumentException("Choose a valid footprint.");
        boolean lShaped = shape.equals("l");
        double floor = w*d*(lShaped ? .75 : 1);
        // Removing a corner replaces two exterior segments with two equal interior segments.
        double walls = 2*(w+d)*h - 21;
        List<Item> items = new ArrayList<>();
        String placement = form.getOrDefault("items","");
        if (placement.length() > 1000) throw new IllegalArgumentException("Too many fixtures.");
        if (!placement.isEmpty()) for (String entry : placement.split(";", -1)) {
            String[] fields = entry.split(",", -1);
            if ((fields.length != 4 && fields.length != 6) || !StudioCatalog.FIXTURES.containsKey(fields[0]) || !Set.of("0","1").contains(fields[3]))
                throw new IllegalArgumentException("Invalid fixture placement.");
            Fixture fixture = StudioCatalog.FIXTURES.get(fields[0]);
            double width = fields.length == 6 ? number(fields[4],.5,12) : fixture.width();
            double depth = fields.length == 6 ? number(fields[5],.5,12) : fixture.depth();
            Item item = new Item(fixture,number(fields[1],0,30),number(fields[2],0,30),fields[3].equals("1"),width,depth);
            if (item.x() + item.width() > w+.001 || item.y() + item.depth() > d+.001 ||
                    lShaped && item.x()+item.width()>w/2+.001 && item.y()<d/2-.001)
                throw new IllegalArgumentException("Place every fixture inside the room.");
            if (items.stream().anyMatch(other -> item.ceiling()==other.ceiling() && item.x() < other.x()+other.width()-.001 && item.x()+item.width() > other.x()+.001 &&
                    item.y() < other.y()+other.depth()-.001 && item.y()+item.depth() > other.y()+.001))
                throw new IllegalArgumentException("Move overlapping fixtures apart to estimate your design.");
            items.add(item);
        }
        var quantities = CompletableFuture.supplyAsync(() -> {
            List<Material> rows = new ArrayList<>(List.of(
                new Material(form.get("floor"),"sq ft",ceil(floor*1.10),floorRate),
                new Material("Drywall","4 × 8 sheets",ceil(walls*1.10/32),19),
                new Material(form.get("wall"),paint?"gal":"sq ft",paint?ceil(walls*2*1.10/350):ceil(walls*1.10),wallRate),
                new Material("Base trim","linear ft",ceil((2*(w+d)-3)*1.10),3)));
            int pipes = ceil(items.stream().filter(i->i.fixture().wet()).mapToDouble(i->i.route()*2).sum()*1.10);
            int wires = ceil(items.stream().filter(i->i.fixture().name().equals("Light fixture")).mapToDouble(Item::route).sum()*1.10);
            if (pipes>0) rows.add(new Material("Plumbing line allowance","linear ft",pipes,4));
            if (wires>0) rows.add(new Material("Cable allowance","linear ft",wires,2));
            items.stream().collect(Collectors.groupingBy(Item::fixture,Collectors.counting())).entrySet().stream()
                .sorted(Comparator.comparing(e->e.getKey().name()))
                .forEach(e->rows.add(new Material(e.getKey().name(),"each",e.getValue().intValue(),e.getKey().price())));
            return rows;
        },workers);
        var labor = CompletableFuture.supplyAsync(() -> {
            List<Work> jobs = new ArrayList<>();
            Work prep = new Work("Carpenters", walls>300?2:1,slots(walls/110+1),48,0);
            jobs.add(prep);
            double pipes = items.stream().filter(i->i.fixture().wet()).mapToDouble(i->1.5+i.route()/10).sum();
            double power = items.stream().filter(i->i.fixture().name().equals("Light fixture")).mapToDouble(i->1+i.route()/16).sum();
            int afterRough = prep.end() + slots(Math.max(pipes,power));
            if (pipes>0) jobs.add(new Work("Plumbers",1,slots(pipes),85,prep.end()));
            if (power>0) jobs.add(new Work("Electricians",1,slots(power),74,prep.end()));
            Work drywall = new Work("Drywall workers",walls>280?2:1,slots(walls/90+1),46,afterRough);
            Work flooring = new Work("Flooring installers",floor>110?2:1,slots(floor/(floorRate==4?30:18)+1),52,drywall.end());
            Work finish = new Work(paint?"Painters":"Wall tile installers",walls>280?2:1,slots(walls/(paint?95:32)+1),paint?42:56,flooring.end());
            jobs.addAll(List.of(drywall,flooring,finish));
            long furniture = items.stream().filter(i->StudioCatalog.FURNITURE.contains(i.fixture().name())).count();
            if (furniture>0) jobs.add(new Work("Furniture installers",furniture>4?2:1,slots(furniture*.5),44,finish.end()));
            return jobs;
        },workers);
        List<Material> materials = quantities.join();
        List<Work> jobs = labor.join();
        int materialCost = materials.stream().mapToInt(m->m.quantity()*m.rate()).sum();
        int furnitureCost = materials.stream().filter(m->StudioCatalog.FURNITURE.contains(m.name())).mapToInt(m->m.quantity()*m.rate()).sum();
        double laborCost = jobs.stream().mapToDouble(Work::cost).sum();
        int finish = jobs.stream().mapToInt(Work::end).max().orElse(0);
        int peak = IntStream.range(0,finish).map(slot->jobs.stream()
            .filter(job->job.start()<=slot && job.end()>slot).mapToInt(Work::crew).sum()).max().orElse(0);
        String lines = materials.stream().map(m->"{\"name\":\"%s\",\"unit\":\"%s\",\"quantity\":%d,\"rate\":%d}".formatted(m.name(),m.unit(),m.quantity(),m.rate())).collect(Collectors.joining(","));
        String tasks = jobs.stream().map(j->"{\"name\":\"%s\",\"crew\":%d,\"start\":%d,\"slots\":%d,\"cost\":%s}".formatted(j.name(),j.crew(),j.start(),j.slots(),j.cost())).collect(Collectors.joining(","));
        return "{\"materials\":[%s],\"labor\":[%s],\"materialCost\":%d,\"furnitureCost\":%d,\"laborCost\":%s,\"finish\":%d,\"peak\":%d}"
            .formatted(lines,tasks,materialCost,furnitureCost,laborCost,finish,peak);
    }

}
