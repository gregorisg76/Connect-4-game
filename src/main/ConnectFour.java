package main;

import game.Controller;

/**
 * DO NOT CHANGE THIS FILE IN ANY WAY!
 *
 * IF YOU DO, YOU MAY GET ZERO FOR THE ASSIGNMENT!
 * This class creates a controller to start the game.
 *
 * @author David Symons
 */
public class ConnectFour
{
	public static void main(String[] args)
	{
		Controller controller = new Controller();
		controller.startSession();
	}
}
