package openworld.gui;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

import openworld.Coordinates;
import openworld.terrain.Mountain;
import openworld.terrain.Terrain;

public class MovementAdapter extends KeyAdapter {

    private GameWorld gameWorld;
    private GameWindow gameWindow;

    public MovementAdapter(GameWorld gameWorld, GameWindow gameWindow) {
        this.gameWorld = gameWorld;
        this.gameWindow = gameWindow;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                moveAdventurer(Coordinates.NORTH_VECTOR);
                break;

            case KeyEvent.VK_RIGHT:
                moveAdventurer(Coordinates.EAST_VECTOR);
                break;

            case KeyEvent.VK_DOWN:
                moveAdventurer(Coordinates.SOUTH_VECTOR);
                break;

            case KeyEvent.VK_LEFT:
                moveAdventurer(Coordinates.WEST_VECTOR);
                break;

            
        }
    }


    public void moveAdventurer(Coordinates vector) {
        if (!gameWorld.getAdventurer().isConscious()) {
            return;
        }

        Coordinates newLocation = new Coordinates(
            gameWorld.getAdventurer().getLocation().getX() + vector.getX(),
            gameWorld.getAdventurer().getLocation().getY() + vector.getY()
        );
        
        List<Terrain> terrain = gameWorld.getWorld().getTerrainHere(newLocation);
        
        if (terrain.isEmpty()) {
            return;
        }

        gameWorld.getAdventurer().move(vector);

        if (terrain.get(0) instanceof Mountain) {
            Mountain mountain = (Mountain) terrain.get(0);
 
            MountainDialog dlg = new MountainDialog(gameWindow, mountain);
            dlg.setVisible(true);

            boolean clickedYes = dlg.getYesClicked();

            if (clickedYes) {
                mountain.explore(gameWorld.getAdventurer());
            }
       }
       
        gameWindow.getControlPanel().update();


    }
}
