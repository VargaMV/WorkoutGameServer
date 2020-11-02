package com.msh.WorkoutGameServer.model;

import com.msh.WorkoutGameServer.logic.PriceCalculator;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.File;
import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
@Document(collection = "Games")
public class Game {
    @Id
    private String id;
    private String title;
    private int mapSize;
    private Field[][] map;
    private List<Player> players = new ArrayList<>();
    private Map<String, Integer> totalStockNumbers = new LinkedHashMap<>();
    private Map<String, Integer> exerciseValues = new LinkedHashMap<>();
    private List<Color> freeColors = Arrays.asList(
            Color.RED, Color.BLUE, Color.GREEN, Color.ORANGE, Color.CYAN,
            Color.NAVY, Color.BROWN, Color.PURPLE, Color.YELLOW, Color.MAGENTA,
            Color.MAROON, Color.TEAL, Color.OLIVE, Color.LIME, Color.GOLD
    );

    private boolean subscriptionOn;
    private boolean running;

    private LocalDateTime creationDate;

    public Game(String title, int mapSize) {
        this.title = title;
        this.mapSize = mapSize;
        this.creationDate = LocalDateTime.now();
        this.subscriptionOn = true;
        this.map = new Field[mapSize][mapSize];
        for (int i = 0; i < mapSize; i++) {
            for (int j = 0; j < mapSize; j++) {
                map[i][j] = new Field();
            }
        }

        File data = new File("data.csv");
        try (Scanner scanner = new Scanner(data)) {
            while (scanner.hasNextLine()) {
                String dataLine = scanner.nextLine();
                String[] parts = dataLine.split(",");
                String name = parts[0].trim();
                int value = Integer.parseInt(parts[1]);
                exerciseValues.put(name, value);
                totalStockNumbers.put(name, 0);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public String toString() {
        return "Game{" +
                "title=" + title +
                ", mapSize=" + mapSize +
                ", subscriptionOn=" + subscriptionOn +
                ", started=" + running +
                ", creationDate=" + creationDate +
                '}';
    }

    public void randomizePlayerPositions() {
        Random random = new Random();
        int x;
        int y;
        for (var player : players) {
            x = random.nextInt(mapSize);
            y = random.nextInt(mapSize);
            player.setPosition(new Coordinate(x, y));
            map[x][y].addPlayerToField(player);
        }
    }

    public void setVisionIncPriceForPlayers() {
        int price = Constants.DEFAULT_VISION_INC_PRICE / players.size();
        for (var player : players) {
            player.setVisionIncPrice(price);
        }
    }

    public void setPlayerPosition(String playerName, Coordinate position) {
        players.stream()
                .filter(p -> p.getName().equals(playerName))
                .forEach(p -> p.setPosition(position));
    }

    public void setPlayerPositionOnMap(String playerName, Coordinate from, Coordinate to) {
        List<SimplePlayer> fromFieldPlayers = map[from.getX()][from.getY()].getPlayersOnField();
        SimplePlayer player = fromFieldPlayers.stream().filter(p -> p.getName().equals(playerName)).findFirst().orElse(null);
        if (player != null) {
            fromFieldPlayers.remove(player);
            map[from.getX()][from.getY()].setPlayersOnField(fromFieldPlayers);
            map[to.getX()][to.getY()].getPlayersOnField().add(player);
        }
    }

    public void setFieldOwner(Coordinate field, Player newOwner) {
        map[field.getX()][field.getY()].setOwner(new SimplePlayer(newOwner));
    }

    public SimplePlayer getFieldOwner(Coordinate field) {
        return map[field.getX()][field.getY()].getOwner();
    }

    public void addPlayer(String playerName) {
        Random rand = new Random();
        Color color = freeColors.remove(rand.nextInt(freeColors.size()));
        Player newPlayer = new Player(playerName, color);
        players.add(newPlayer);
    }

    public boolean isPlayerExist(String name) {
        return players.stream().anyMatch(p -> p.getName().equals(name));
    }

    public Player getPlayerByName(String name) {
        return players.stream().filter(p -> p.getName().equals(name)).findFirst().orElse(null);
    }

    public Player occupy(Coordinate target, Player newOwner) {
        Player prevOwner = getPlayerByName(getFieldOwner(target).getName());
        if (prevOwner != null) {
            prevOwner.decFieldsOwned();
        }
        setFieldOwner(target, newOwner);
        int playerIndex = players.indexOf(newOwner);
        int value = players.get(playerIndex).getCurrentScore();
        map[target.getX()][target.getY()].setValue(value);
        players.get(playerIndex).setCurrentScore(0);
        players.get(playerIndex).incFieldsOwned();
        return prevOwner;
    }

    public void stockBought(String playerName, String exercise) {
        Player player = getPlayerByName(playerName);
        if (player.isStockAffordable(exercise)) {
            int prevValue = totalStockNumbers.get(exercise);
            totalStockNumbers.put(exercise, prevValue + 1);

            int playerPrevValue = player.getStockNumbers().get(exercise);
            player.getStockNumbers().put(exercise, playerPrevValue + 1);

            int price = PriceCalculator.calculate(totalStockNumbers.get(exercise));
            player.decMoney(price);
        }
    }

    public void exerciseDone(String playerName, String exercise, int amount) {
        Player player = getPlayerByName(playerName);
        player.incExerciseValue(exercise, amount);
        player.incScore((int) Math.ceil(exerciseValues.get(exercise) * getSharePercentage(player, exercise) / 100.0) * amount);
    }

    public int getSharePercentage(Player player, String exercise) {
        return (int) Math.floor(player.getStockNumbers().get(exercise) * 100 / (double) totalStockNumbers.get(exercise));
    }

    public void incVision(String playerName) {
        Player player = getPlayerByName(playerName);
        player.incRangeOfVision();
    }

    public void convertScoreToMoney(String playerName, int amount) {
        Player player = getPlayerByName(playerName);
        player.incMoney(amount);
        player.decScore(amount);
    }
}
