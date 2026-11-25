# Room Studio

Room Studio is a Java application for planning home interiors and estimating project costs. Use the floor plan to size a room, arrange furniture in an interactive 3D view, and see how the design affects material quantities and labor.

## Run in IntelliJ IDEA

1. Extract the project and open its `pom.xml` as a Maven project in IntelliJ. Use JDK 21 or newer and let Maven finish importing the dependencies.
2. Run `RoomStudioApplication.main()`.
3. Open [Room Studio](http://127.0.0.1:8086) in your browser.

From a terminal with Maven installed, you can also start it with `mvn spring-boot:run`. To use the packaged browser version from the ZIP, run `java -jar RoomStudio.jar`.

## Run the JavaFX desktop app

Stop the browser-mode server first if it is running on port 8086. From the project folder, run `mvn javafx:run`. Maven downloads the JavaFX dependencies for your operating system. The FXML view defines the toolbar, WebView, and status bar. The desktop app starts Spring Boot and loads the same room designer and estimate API in its WebView. Designs are stored separately in each browser or WebView profile.

## Configure the server

Edit `src/main/resources/application.yaml` and restart the application.

```yaml
spring:
  application:
    name: room-studio

server:
  address: 127.0.0.1
  port: 8086
```

If port 8086 is in use, change `server.port` to 8090 and open [port 8090](http://127.0.0.1:8090).

## Design a room

1. **Choose a home and floor.** Select a home style, open a floor-plan template, and choose a room.
2. **Set the room dimensions and finishes.** Drag a wall handle on the floor plan or the corner handle in the 3D editor. Choose a floor finish and pattern, wall colors, and window placement.
3. **Furnish the room.** Continue to decoration, choose pieces from the catalog, and drag or rotate them in the editor.
4. **Review the estimate and save.** Check the itemized materials, furniture, and labor. Use **Price comparison** to see example Home Depot prices for selected materials or compare written contractor quotes. **Save design** stores the project in the current browser.

## How the estimate works

The browser sends the room dimensions, finish selections, and fixture layout to the Java backend. Java uses floor area and wall measurements to determine the quantities of flooring, drywall, paint or tile, and trim. It counts selected catalog items and uses fixture positions to approximate pipe and cable lengths.

Java estimates labor by assigning crew sizes and task durations to trades. It calculates worker-hours, peak staffing, and labor cost using sample rates. The app combines those figures with material and furniture costs to produce the room estimate. **Estimate all rooms** adds the individual interior estimates for a floor.

The figures are planning estimates. Material quantities include a waste allowance. Costs exclude taxes, permits, and demolition. Example store prices can vary by location. Contractor quotes can be entered directly, and the app links to BBB guidance for finding and comparing bids.

## Project structure

| File or directory | Purpose |
| --- | --- |
| `pom.xml` | Maven dependencies and build plugins |
| `src/main/resources/application.yaml` | Spring Boot application name and server settings |
| `RoomStudioApplication.java` | Starts Spring Boot and manages the estimator lifecycle |
| `EstimateController.java` | Handles `GET /api/estimate` and returns JSON |
| `RoomEstimator.java` | Validates room inputs and calculates material and labor estimates |
| `StudioCatalog.java`, `Fixture.java`, `Item.java`, `Material.java`, `Work.java` | Catalog and estimate data |
| `src/main/resources/static/index.html` | Floor-plan editor, 3D preview, furniture catalog, and estimate interface |
| `RoomStudioDesktop.java`, `RoomDesignerController.java`, `src/main/resources/room-designer.fxml` | JavaFX window and WebView |

The backend uses regular expressions to validate numeric inputs and streams and lambdas to aggregate quantities and costs. A thread pool calculates materials and labor concurrently using `CompletableFuture`.

## Build a runnable JAR

From the project folder, run:

```powershell
mvn clean package
java -jar target/room-studio-1.0.0.jar
```
