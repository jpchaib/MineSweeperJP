package minesweeper;

//import BorderLayout library
import java.awt.BorderLayout;
//import the JFrame library which the MineSweeper Class will extend
import javax.swing.JFrame;
//import the JLabel library to be used in the 
import javax.swing.JLabel;

//create Class MineSweeper that extends JFrame, which extends java.awt.Frame itself: https://docs.oracle.com/javase/7/docs/api/javax/swing/JFrame.html 
public class MineSweeper extends JFrame {
	//create a field: a JLabel status, which is a display area for a brief text or image. In this case
	// it will be reserved for the game messages: (Number of Mines left in the game, "You won" and "Game Over")
	private JLabel message;
	
	//create a constructor of the class
	public MineSweeper() {
		// sets the status as an empty string
		message = new JLabel("");
		// Adds the specified component to the end of this container. Also notifies the layout manager to add the component to this container's layout using the specified constraints object 
		add(message, BorderLayout.SOUTH);
		//initialize and add the Board class, which extends JPanel, a JComponent (also a awt.Container);
		add(new Board(message));
		//set the window as not resizable, using the method from awt.Frame
		setResizable(false);
		//Causes this Window to be sized to fit the preferred size and layouts of its subcomponents.
		pack();
		//set the Title, shown in the window container, as MineSweeper
		setTitle("MineSweeperJP");
		//sets the location where the window will pop-up
		//If the component is null, or the GraphicsConfiguration associated with this component is null, the window is placed in the center of the screen.
		setLocationRelativeTo(null);
		//sets the operation when user closes the MineSweeper Frame.
		// in this case it exits the application using System exit method. 
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
}
