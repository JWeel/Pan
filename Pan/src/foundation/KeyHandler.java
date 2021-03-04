package foundation;

import graphics.Painter;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

// this object handles all key-bindings
@SuppressWarnings("serial")
public class KeyHandler {
	
	// this allows the keys to work with the Painter even if it is not focusable
	private static final int FOCUS = JComponent.WHEN_IN_FOCUSED_WINDOW;
	
	// all key-bindings, set to their default values
	public String upKey = "W";
	public String downKey = "S";
	public String leftKey = "A";
	public String rightKey = "D";
	public String accessKey = "C";
	public String primaryKey = "Z";
	public String secondaryKey = "X";
	public String portKey = "Q";
	public String starboardKey = "E";
	
	// boolean to give short break after non-move buttons
	private boolean justPressedAccess = false;
	private boolean justPressedPrimary = false;
	private boolean justPressedSecondary = false;
	private boolean justPressedPort = false;
	private boolean justPressedStarboard = false;

	
	// upon creation of this object, all key-binding objects are created as well
	public KeyHandler(Painter painter) {
		
		
		/*============================= MOVEMENT =============================*/
		
		
			/*============================ UP ============================*/

		
		// move the character, menu, or world map view, up
		Action moveUpPress = new AbstractAction(){
			@Override
			public void actionPerformed(ActionEvent e) {
				Core.upPressed = true;
			}
		};
		KeyStroke upPressStroke = KeyStroke.getKeyStroke(upKey);
		painter.getInputMap(FOCUS).put(upPressStroke, "press up");
		painter.getActionMap().put("press up", moveUpPress);
		
		
		// stop moving the character, or world map view, up
		Action moveUpRelease = new AbstractAction(){
			@Override
			public void actionPerformed(ActionEvent e) {
				Core.upPressed = false;
			}
		};
		KeyStroke upReleaseStroke = KeyStroke.getKeyStroke("released " + upKey);
		painter.getInputMap(FOCUS).put(upReleaseStroke, "release up");
		painter.getActionMap().put("release up", moveUpRelease);
		

		
			/*=========================== DOWN ===========================*/


		// move the character, menu, or world map view, down
		Action moveDownPress = new AbstractAction(){
			@Override
			public void actionPerformed(ActionEvent e) {
				Core.downPressed = true;
			}
		};
		KeyStroke downPressStroke = KeyStroke.getKeyStroke(downKey);
		painter.getInputMap(FOCUS).put(downPressStroke, "press down");
		painter.getActionMap().put("press down", moveDownPress);
		
		
		// stop moving the character, or world map view, down
		Action moveDownRelease = new AbstractAction(){
			@Override
			public void actionPerformed(ActionEvent e) {
				Core.downPressed = false;
			}
		};
		KeyStroke downReleaseStroke = KeyStroke.getKeyStroke("released " + downKey);
		painter.getInputMap(FOCUS).put(downReleaseStroke, "release down");
		painter.getActionMap().put("release down", moveDownRelease);
		
		
		
			/*=========================== LEFT ===========================*/


		// move the character, or world map view, left
		Action moveLeftPress = new AbstractAction(){
			@Override
			public void actionPerformed(ActionEvent e) {
				Core.leftPressed = true;
			}
		};
		KeyStroke leftPressStroke = KeyStroke.getKeyStroke(leftKey);
		painter.getInputMap(FOCUS).put(leftPressStroke, "press left");
		painter.getActionMap().put("press left", moveLeftPress);
		
		
		// stop moving the character, or world map view, left
		Action moveLeftRelease = new AbstractAction(){
			@Override
			public void actionPerformed(ActionEvent e) {
				Core.leftPressed = false;
			}
		};
		KeyStroke leftReleaseStroke = KeyStroke.getKeyStroke("released " + leftKey);
		painter.getInputMap(FOCUS).put(leftReleaseStroke, "release left");
		painter.getActionMap().put("release left", moveLeftRelease);
		

		
			/*========================== RIGHT ===========================*/


		// move the character, or world map view, right
		Action moveRightPress = new AbstractAction(){
			@Override
			public void actionPerformed(ActionEvent e) {
				Core.rightPressed = true;
			}
		};
		KeyStroke rightPressStroke = KeyStroke.getKeyStroke(rightKey);
		painter.getInputMap(FOCUS).put(rightPressStroke, "press right");
		painter.getActionMap().put("press right", moveRightPress);
		
		
		// stop moving the character, or world map view, right
		Action moveRightRelease = new AbstractAction(){
			@Override
			public void actionPerformed(ActionEvent e) {
				Core.rightPressed = false;
			}
		};
		KeyStroke rightReleaseStroke = KeyStroke.getKeyStroke("released "+ rightKey);
		painter.getInputMap(FOCUS).put(rightReleaseStroke, "release right");
		painter.getActionMap().put("release right", moveRightRelease);

		
		
		/*=============================== MENU ===============================*/
		
		
			/*========================== ACCESS ==========================*/

		
		// access or leave menu. key must be released to fire action again
		Action accessPress = new AbstractAction(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!Core.movementSuspended) {
					if (!justPressedAccess){
						justPressedAccess = true;
						Core.accessPressed = true;
					} 
				}
			}
		};
		KeyStroke accessPressStroke = KeyStroke.getKeyStroke(accessKey);
		painter.getInputMap(FOCUS).put(accessPressStroke, "press access");
		painter.getActionMap().put("press access", accessPress);
		
		
		// allow access button to be pressed again
		Action accessRelease = new AbstractAction(){
			@Override
			public void actionPerformed(ActionEvent e) {	
				if (justPressedAccess) justPressedAccess = false;
			}
		};
		KeyStroke accessReleaseStroke = KeyStroke.getKeyStroke("released " + accessKey);
		painter.getInputMap(FOCUS).put(accessReleaseStroke, "release access");
		painter.getActionMap().put("release access", accessRelease);
		
		
		
		/*============================== ACTION ==============================*/

		
			/*========================= PRIMARY ==========================*/

		
		// perform primary action. key must be released to fire action again
		Action primaryPress = new AbstractAction(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!justPressedPrimary){
					justPressedPrimary = true;
					Core.primaryPressed = true;
				}
			}
		};
		KeyStroke primaryPressStroke = KeyStroke.getKeyStroke(primaryKey);
		painter.getInputMap(FOCUS).put(primaryPressStroke, "press primary");
		painter.getActionMap().put("press primary", primaryPress);
		
		
		// allow primary button to be pressed again
		Action primaryRelease = new AbstractAction(){
			@Override
			public void actionPerformed(ActionEvent e) {	
				if (justPressedPrimary) justPressedPrimary = false;
				Core.primaryPressed = false;
			}
		};
		KeyStroke primaryReleaseStroke = KeyStroke.getKeyStroke("released " + primaryKey);
		painter.getInputMap(FOCUS).put(primaryReleaseStroke, "release primary");
		painter.getActionMap().put("release primary", primaryRelease);
		
		
		
			/*======================== SECONDARY =========================*/

		
		// perform secondary action. key must be released to fire action again
		Action secondaryPress = new AbstractAction(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!justPressedSecondary){
					justPressedSecondary = true;
					Core.secondaryPressed = true;
				}
			}
		};
		KeyStroke secondaryPressStroke = KeyStroke.getKeyStroke(secondaryKey);
		painter.getInputMap(FOCUS).put(secondaryPressStroke, "press secondary");
		painter.getActionMap().put("press secondary", secondaryPress);
		
	
		// allow secondary button to be pressed again, cancels running
		Action secondaryRelease = new AbstractAction(){
			@Override
			public void actionPerformed(ActionEvent e) {	
				if (justPressedSecondary) justPressedSecondary = false;
				Core.secondaryPressed = false;
				Core.currentCharacter.isRunning = false;
			}
		};
		KeyStroke secondaryReleaseStroke = KeyStroke.getKeyStroke("released " + secondaryKey);
		painter.getInputMap(FOCUS).put(secondaryReleaseStroke, "release secondary");
		painter.getActionMap().put("release secondary", secondaryRelease);

		
	
		/*============================== EXTRA ===============================*/

	
			/*=========================== PORT ===========================*/

	
		// perform port action. key must be released to fire action again
		Action portPress = new AbstractAction(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!justPressedPort){
					justPressedPort = true;
					Core.portPressed = true;
				}
			}
		};
		KeyStroke portPressStroke = KeyStroke.getKeyStroke(portKey);
		painter.getInputMap(FOCUS).put(portPressStroke, "press port");
		painter.getActionMap().put("press port", portPress);
		
		
		// allow port button to be pressed again
		Action portRelease = new AbstractAction(){
			@Override
			public void actionPerformed(ActionEvent e) {	
				if (justPressedPort) justPressedPort = false;
				Core.portPressed = false;
			}
		};
		KeyStroke portReleaseStroke = KeyStroke.getKeyStroke("released " + portKey);
		painter.getInputMap(FOCUS).put(portReleaseStroke, "release port");
		painter.getActionMap().put("release port", portRelease);
		
		
		
			/*========================= STARBOARD ========================*/
		
		
		// perform starboard action. key must be released to fire action again
		Action starboardPress = new AbstractAction(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!justPressedStarboard){
					justPressedStarboard = true;
					Core.starboardPressed = true;
				}
			}
		};
		KeyStroke starboardPressStroke = KeyStroke.getKeyStroke(starboardKey);
		painter.getInputMap(FOCUS).put(starboardPressStroke, "press starboard");
		painter.getActionMap().put("press starboard", starboardPress);
		
		
		// allow starboard button to be pressed again, cancels running
		Action starboardRelease = new AbstractAction(){
			@Override
			public void actionPerformed(ActionEvent e) {	
				if (justPressedStarboard) justPressedStarboard = false;
				Core.starboardPressed = false;
			}
		};
		KeyStroke starboardReleaseStroke = KeyStroke.getKeyStroke("released " + starboardKey);
		painter.getInputMap(FOCUS).put(starboardReleaseStroke, "release starboard");
		painter.getActionMap().put("release starboard", starboardRelease);
		
		
	}
	
	
	
	/*============================ OTHER METHODS =============================*/

	
	/*
	// replaces the key set to a key-binding with a given key
	public void replaceKey(String bindingToBeChanged, String newKey){
		
		// make sure the bindingToBeChanged exists
		
		// TODO
		
		// check if the newKey is already used, in which case swap
		
		// TODO
		
		
		// set new key to key-binding
		switch (bindingToBeChanged){
			case "up":
				upPressStroke = KeyStroke.getKeyStroke(upKey);
				upReleaseStroke = KeyStroke.getKeyStroke("released " + upKey);
				//painter.getInputMap(FOCUS).put(upPressStroke, "press up");
				//painter.getActionMap().put("press up", moveUpPress);
		}
		
	}*/
}