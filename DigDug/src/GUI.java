import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
public class GUI extends JPanel implements KeyListener {
    private Graphics2D g2d;
    private int highScore, score;
    private String gameEndScreen;
    private boolean pausePlay, gameOver, gunOut;
    private Color pauseFade;
    private Image imgDigDug, pauseButton, weapon;
    private final int width, height;
    private final Player player;
    private final Image ICN, plus300, plus1000, imgScore, imgHiScore, watermelon, pineapple, carrot;
    private final Map<String, Image> scoreTiles;
    private final ArrayList<Fruit> fruitList;
    private final ArrayList<Enemy> enemyList;
    private final ArrayList<Rectangle> scoreList;


    public GUI() throws FileNotFoundException {

        g2d = null;

        player = new Player();
        enemyList = new ArrayList<>();

        Enemy one = new Enemy("Goggles", "Right");
        Enemy two = new Enemy("Dragon", "Left");
        Enemy three = new Enemy("Goggles", "Right");
        Enemy four = new Enemy("Dragon", "Left");
        Enemy five = new Enemy("Dragon", "Right");

        one.setPos(250, 425);
        two.setPos(10, 485);
        three.setPos(150, 625);
        four.setPos(325, 765);
        five.setPos(515, 900);

        one.setVelocity(2);
        two.setVelocity(2);
        three.setVelocity(3);
        four.setVelocity(2);
        five.setVelocity(3);

        enemyList.add(one);
        enemyList.add(two);
        enemyList.add(three);
        enemyList.add(four);
        enemyList.add(five);

        pauseFade = new Color(0, 0, 0, 0);
        pausePlay = true;
        gameOver = false;
        gunOut = false;

        width = 915;
        height = 1040;
        score = -5;

        gameEndScreen = "Win";

        highScore = new Scanner(new File("highscore.txt")).nextInt();

        scoreTiles = new HashMap<>();
        scoreList = new ArrayList<>();
        fruitList = new ArrayList<>();

        Fruit melon = new Fruit();
        Fruit karot = new Fruit();
        Fruit carot = new Fruit();
        Fruit pineP = new Fruit();

        melon.setBounds(500, 625, 48, 27);
        melon.setType("Watermelon");

        karot.setBounds(125, 350, 42, 39);
        karot.setType("Carrot");

        carot.setBounds(457, 850, 42, 39);
        carot.setType("Carrot");

        pineP.setBounds(75, 950, 30, 45);
        pineP.setType("Pineapple");

        fruitList.add(melon);
        fruitList.add(karot);
        fruitList.add(carot);
        fruitList.add(pineP);

        for (int i = 0; i < 10; i++) {
            scoreTiles.put(String.valueOf(i), new ImageIcon(Objects.requireNonNull(getClass().getResource("Assets/Scoreboard/Score" + i + ".png"))).getImage());
        }

        imgDigDug = new ImageIcon(Objects.requireNonNull(getClass().getResource("Assets/Characters/DougSide.png"))).getImage();
        ICN = new ImageIcon(Objects.requireNonNull(getClass().getResource("Assets/Background/DigDugIcon.jpg"))).getImage();
        pauseButton = new ImageIcon(Objects.requireNonNull(getClass().getResource("/Assets/Empty.png"))).getImage();
        weapon = new ImageIcon(Objects.requireNonNull(getClass().getResource("Assets/Empty.png"))).getImage();

        watermelon = new ImageIcon(Objects.requireNonNull(getClass().getResource("Assets/Background/Watermelon.png"))).getImage();
        carrot = new ImageIcon(Objects.requireNonNull(getClass().getResource("Assets/Background/Carrot.png"))).getImage();
        pineapple = new ImageIcon(Objects.requireNonNull(getClass().getResource("Assets/Background/Pineapple.png"))).getImage();

        plus300 = new ImageIcon(Objects.requireNonNull(getClass().getResource("Assets/Scoreboard/Points300.png"))).getImage();
        plus1000 = new ImageIcon(Objects.requireNonNull(getClass().getResource("Assets/Scoreboard/Points1000.png"))).getImage();
        imgScore = new ImageIcon(Objects.requireNonNull(getClass().getResource("Assets/Scoreboard/imgScore.png"))).getImage();
        imgHiScore = new ImageIcon(Objects.requireNonNull(getClass().getResource("Assets/Scoreboard/imgHi-Score.png"))).getImage();
    }

    public void display() {

        JFrame frame = new JFrame("Dig Dug");
        frame.add(this);
        frame.addKeyListener(this);
        frame.setIconImage(ICN);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setSize(width, height);
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        frame.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                saveScore();
            }
        });
        audioPlay();
    }

    public void prepGUI() {

        Image imgBackground = new ImageIcon(Objects.requireNonNull(getClass().getResource("Assets/Background/background.png"))).getImage();
        Image imgLogo = new ImageIcon(Objects.requireNonNull(getClass().getResource("Assets/Background/Logo.png"))).getImage();

        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, width, height);

        g2d.drawImage(imgLogo, 100, -10, 714, 318, null);
        g2d.drawImage(imgScore, 700, 400, 117, 21, null);
        g2d.drawImage(imgHiScore, 700, 500, 144, 48, null);

        g2d.drawImage(imgBackground, 0, 300, 181, 700, null);
        g2d.drawImage(imgBackground, 180, 300, 181, 700, null);
        g2d.drawImage(imgBackground, 360, 300, 181, 700, null);
        g2d.drawImage(imgBackground, 470, 300, 181, 700, null);

        int tempPos = 675;

        for (int i = 0; i < player.getLives() - 1; i++) {

            g2d.drawImage(new ImageIcon(Objects.requireNonNull(getClass().getResource("Assets/Characters/DougSide.png"))).getImage(), tempPos, 720, 100, 100, null);
            tempPos += 110;
        }

        g2d.fillRect(460, 612, 128, 40);
        g2d.fillRect(79, 339, 128, 50);
        g2d.fillRect(412, 839, 128, 50);
        g2d.fillRect(28, 937, 128, 58);
    }

    public void paintComponent(Graphics window) {

        super.paintComponent(window);
        g2d = (Graphics2D) window;

        if (gameOver) closeGame();

        else {

            prepGUI();

            drawScore();

            drawPath();

            drawFruit();

            drawWeapon();

            drawDigDug();

            drawEnemy();

            collisionCheck();

            drawPause();
        }
    }

    public void drawDigDug() {

        g2d.fillRect(player.getX(), player.getY(), 40, 40);
        g2d.drawImage(imgDigDug, player.getX() + player.getAdd(), player.getY(), 40 * player.getFlip(), 40, null);
        player.path.add(new Point(player.getX(), player.getY()));
    }

    public void drawPath() {

        for (Point p : player.path) {
            g2d.fillRect(p.x, p.y, 40, 40);
        }
    }

    public void drawWeapon() {

        switch (player.getDirection()) {

            case "Right", "Left" -> g2d.drawImage(weapon, player.getX() + 20, player.getY() + 20, 64 * player.getFlip(), 10, null);
            case "Up" -> g2d.drawImage(weapon, player.getX() + 20, player.getY() - 40, 10 * player.getFlip(),64, null);
            case "Down" -> g2d.drawImage(weapon, player.getX() + 20, player.getY() + 80, -10 * player.getFlip(),-64, null);
        }
    }

    public void drawEnemy() {

        for (Enemy enemy : enemyList) {
            if (enemy.isAlive()) {
                enemy.move(player.getX(), player.getY());
            }
        }

        for (Enemy enemy : enemyList) {

            if (enemy.isAlive()) {

                String temp = enemy.getDirection();

                if (temp.equals("Left") || temp.equals("Right")) {
                    temp = "Side";
                }

                Image image = new ImageIcon(Objects.requireNonNull(getClass().getResource("Assets/Characters/" + enemy.getType() + temp + ".png"))).getImage();
                g2d.drawImage(image, enemy.getX() + enemy.getAdd(), enemy.getY(), 40 * enemy.getFlip(), 40, null);

            }
            else if (enemy.getAppearCNT() < 40) {
                g2d.drawImage(plus1000,enemy.getX() + 20 - 24,enemy.getY() - enemy.getAppearCNT(), 48, 27, null);
                enemy.upAppearCNT();
            }
        }
    }

    public void drawFruit() {

        for (Fruit f : fruitList) {

            if (f.Appears()) {

                switch (f.getType()) {

                    case "Watermelon" -> g2d.drawImage(watermelon, f.getBounds().x, f.getBounds().y, f.getBounds().width, f.getBounds().height, null);
                    case "Carrot" -> g2d.drawImage(carrot, f.getBounds().x, f.getBounds().y, f.getBounds().width, f.getBounds().height, null);
                    case "Pineapple" -> g2d.drawImage(pineapple, f.getBounds().x, f.getBounds().y, f.getBounds().width, f.getBounds().height, null);
                }
            }
            else if (f.getAppearCNT() < 40) {
                g2d.drawImage(plus300,f.getBounds().x + (f.getBounds().width / 2) - 24,f.getBounds().y - f.getAppearCNT(), 48, 27, null);
                f.upAppearCNT();
            }
        }
    }

    public void drawPause() {

        g2d.setColor(pauseFade);
        g2d.fillRect(0, 0, width, height);
        g2d.drawImage(pauseButton, width / 2 - 400, (height / 2 - 150), 796, 252, null);
    }

    public void drawScore() {

        Rectangle rec = new Rectangle(player.getX(), player.getY(), 40, 40);

        boolean check = true;

        for (Rectangle rc : scoreList) {

            if (rc.intersects(rec)) {
                check = false;
            }
        }

        if (check) {
            score += 5;
            scoreList.add(rec);
        }

        if (score > highScore) {
            highScore = score;
        }

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        String sc = "" + score;
        String hc = "" + highScore;

        int xTemp = 750;

        if (!gameOver) {

            for (int i = 0; i < sc.length(); i++) {

                g2d.drawImage(scoreTiles.get(sc.substring(i, i + 1)), xTemp, 445, 24, 24, null);
                xTemp += 24;
            }

            xTemp = 750;

            for (int i = 0; i < hc.length(); i++) {

                g2d.drawImage(scoreTiles.get(hc.substring(i, i + 1)), xTemp, 583, 24, 24, null);
                xTemp += 24;
            }
        }

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        else {

            xTemp = ((195 - (sc.length() * 48)) / 2) + 561;

            for (int i = 0; i < sc.length(); i++) {

                g2d.drawImage(scoreTiles.get(sc.substring(i, i + 1)), xTemp, 625, 48, 48, null);
                xTemp += 48;
            }

            xTemp = ((230 - (hc.length() * 48)) / 2) + 125;

            for (int i = 0; i < hc.length(); i++) {

                g2d.drawImage(scoreTiles.get(hc.substring(i, i + 1)), xTemp, 625, 48, 48, null);
                xTemp += 48;
            }
        }
    }

    public void collisionCheck() {

        Rectangle plyr = new Rectangle(player.getX(), player.getY(), 40, 40);
        Rectangle weap = new Rectangle();

        switch (player.getDirection()) {

            case "Right" -> weap.setBounds((player.getX() + 20), player.getY() + 20, 64 * player.getFlip(), 10);
            case "Left" -> weap.setBounds((player.getX() + 20) - 64, player.getY() + 20, 64, 10);

            case "Up" -> {

                if (player.getFlip() == 1) {
                    weap.setBounds(player.getX() + 20, player.getY() - 40, 10, 64);
                }
                else weap.setBounds(player.getX() + 10, player.getY() - 40, 10, 64);
            }

            case "Down" -> {

                if (player.getFlip() == 1) {
                    weap.setBounds(player.getX() + 10, player.getY() + 20, 10, 64);
                }
                else weap.setBounds(player.getX() + 20, player.getY() + 20, 10, 64);

            }
        }

        for (Enemy enemy : enemyList) {

            Rectangle enmy = new Rectangle(enemy.getX(), enemy.getY(), 40, 40);

            if (enemy.isAlive()) {

                if (plyr.intersects(enmy)) {
                    player.dropLives();
                    player.escapeEnemy(enemyList);
                    repaint();
                    break;
                }

                if (gunOut && weap.intersects(enmy)) {
                    enemy.die();
                    player.kill();
                    score += 1000;
                }
            }
        }

        for (Fruit f : fruitList) {

            if (f.getBounds().intersects(plyr) && f.Appears()) {
                f.eat();
                score += 300;
            }
        }
    }

    public void saveScore() {

        try {
            PrintWriter pw = new PrintWriter("highscore.txt");
            pw.append(String.valueOf(highScore));
            pw.close();
        } catch (Exception ignored) {}
    }

    public void closeGame() {

        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, width, height);

        Image screen = new ImageIcon(Objects.requireNonNull(getClass().getResource("Assets/Background/You" + gameEndScreen + ".png"))).getImage();

        if (gameEndScreen.equals("Lose")) {
            g2d.drawImage(screen, 302, 150, 310, 170, null);
        }
        else g2d.drawImage(screen, (width / 2) - (300 / 2), 150, 300, 150, null);

        g2d.drawImage(imgHiScore, 125, 500, 230, 80, null);
        g2d.drawImage(imgScore, 561, 545, 195, 35, null);

        drawScore();
    }

    public void keyPressed(KeyEvent e) {

        player.setVelX(0);
        player.setVelY(0);

        int k = e.getKeyCode();

        if (k == KeyEvent.VK_SPACE) {

            if (player.getDirection().equals("Up") || player.getDirection().equals("Down")) {
                weapon = new ImageIcon(Objects.requireNonNull(getClass().getResource("Assets/Characters/weaponUP.png"))).getImage();
            }
            else weapon = new ImageIcon(Objects.requireNonNull(getClass().getResource("Assets/Characters/weaponSide.png"))).getImage();

            gunOut = true;
        }

        if (!gunOut) {

            if (k == KeyEvent.VK_LEFT || k == KeyEvent.VK_A) {
                player.setDirection("Left");
                player.setVelX(-player.getVelocity());
            }
            else if (k == KeyEvent.VK_RIGHT || k == KeyEvent.VK_D){
                player.setDirection("Right");
                player.setVelX(player.getVelocity());
            }
            else if (player.getVelX() == 0) {

                if (k == KeyEvent.VK_UP || k == KeyEvent.VK_W) {
                    player.setDirection("Up");
                    player.setVelY(-player.getVelocity());
                }

                if (k == KeyEvent.VK_DOWN || k == KeyEvent.VK_S) {
                    player.setDirection("Down");
                    player.setVelY(player.getVelocity());
                }
            }
            String temp = player.getDirection();

            if (temp.equals("Left") || temp.equals("Right")) {
                temp = "Side";
            }
            imgDigDug = new ImageIcon(Objects.requireNonNull(getClass().getResource("Assets/Characters/Doug" + temp + ".png"))).getImage();
        }

        if (k == KeyEvent.VK_ESCAPE) {

            Image pB = new ImageIcon(Objects.requireNonNull(getClass().getResource("Assets/PauseButton.png"))).getImage();

            if (!pauseButton.equals(pB)) {
                pauseButton = pB;
                pauseFade = new Color(0, 0, 0, 180);
                repaint();

            }
            else {
                pauseButton = new ImageIcon(Objects.requireNonNull(getClass().getResource("Assets/Empty.png"))).getImage();
                pauseFade = new Color(0, 0, 0, 0);
            }
            pausePlay = !pausePlay;
        }

        player.checkBounds();
    }

    public void keyReleased(KeyEvent e) {

        switch (e.getKeyCode()) {

            case KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_W, KeyEvent.VK_S -> player.setVelY(0);
            case KeyEvent.VK_RIGHT, KeyEvent.VK_LEFT, KeyEvent.VK_A, KeyEvent.VK_D -> player.setVelX(0);
            case KeyEvent.VK_SPACE -> {
                weapon = new ImageIcon(Objects.requireNonNull(getClass().getResource("Assets/Empty.png"))).getImage();
                gunOut = false;
            }
        }
    }

    public void audioPlay() {

        try {
            AudioInputStream system = AudioSystem.getAudioInputStream(Objects.requireNonNull(getClass().getResource("Assets/theme.wav")));
            Clip sound = AudioSystem.getClip();
            sound.open(system);
            sound.start();
            sound.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception ignored) {}
    }

    public void keyTyped(KeyEvent e) {}

    public ArrayList<Enemy> getEnemyList() {
        return enemyList;
    }

    public Player getPlayer() {
        return player;
    }

    public void setGameEndScreen(String gameEndScreen) {
        this.gameEndScreen = gameEndScreen;
    }

    public boolean isPausePlay() {
        return pausePlay;
    }

    public void endGame() {
        gameOver = !gameOver;
    }
}
