public class  gameRunner {

    public static void main(String[] args) throws Exception {

        GUI gui = new GUI();
        gui.display();

        framerateThread frameThread = new framerateThread(gui);
        frameThread.start();
        //test

        while (true) {

            //Needs Thread Sync/Locking
            System.out.print("");
            if (gui.getPlayer().getLives() < 1 || (gui.getPlayer().getKillCNT() == gui.getEnemyList().size())) {

                if (gui.getPlayer().getLives() < 1) {
                    gui.setGameEndScreen("Lose");
                }

                frameThread.interrupt();
                gui.saveScore();
                gui.endGame();
                gui.repaint();

                try {
                    Thread.sleep(5000);
                } catch (Exception ignored) {}

                System.exit(999999999);
            }
        }


    }
}
