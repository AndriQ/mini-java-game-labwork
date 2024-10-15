package openworld.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

public class EnvironmentPanel extends JPanel implements ActionListener {
	
	private GameWorld gameWorld;
	
	private JButton respawnButton, startButton, stopButton; 

	private boolean isMoving = false;

	private Thread movementThread;
	
	public EnvironmentPanel(GameWorld gameWorld) {
		this.gameWorld = gameWorld;
		
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		setBorder(new TitledBorder("Monster and NPC controls"));
		
		respawnButton = new JButton("Respawn all");
		respawnButton.addActionListener(this);
		startButton = new JButton("Start moving");
		startButton.addActionListener(this);
		stopButton = new JButton("Stop moving");
		stopButton.addActionListener(this);
		
		add(respawnButton);
		add(startButton);
		add(stopButton);
	}


	@Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == respawnButton) {
            gameWorld.respawnWorld();
        } else if (e.getSource() == startButton) {
            if (!isMoving) {
                movementThread = new Thread(() -> {
                    while (isMoving) {
                        gameWorld.getWorld().monsterMove();
                        gameWorld.getWorld().nonPlayerCharactersMove();
                        try {
                            Thread.sleep(1000); 
                        } catch (InterruptedException ex) {
                            break;
                        } if (Thread.interrupted()) {
							break;
						}
                    }
                });
                movementThread.start();
                isMoving = true;
            }
        } else if (e.getSource() == stopButton) {
            if (isMoving) {
                isMoving = false;
                try {
					movementThread.interrupt();
                    movementThread.join(); 
                } catch (InterruptedException ex) {
                }
            }
        }
    }

	public void disableAll() {
		startButton.setEnabled(false);
		stopButton.setEnabled(false);
		respawnButton.setEnabled(false);
	}

}
