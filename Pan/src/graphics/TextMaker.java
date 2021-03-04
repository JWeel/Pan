package graphics;

import java.awt.image.BufferedImage;

import repository.GraphicsManager;

// this object handles matching strings with relevant typography images
public class TextMaker {

	public static final int CHARACTER_WIDTH = 6;
	public static final int CHARACTER_HEIGHT = 8;
	
	
	//
	public static BufferedImage getTextDisplay(String s){

		return null;
	}
	
	// returns image matching given character
	public static BufferedImage getTypographyImage(char c){
		switch(c){
			case 'a': return GraphicsManager.typographyA;
			case 'b': return GraphicsManager.typographyB;
			case 'c': return GraphicsManager.typographyC;
			case 'd': return GraphicsManager.typographyD;
			case 'e': return GraphicsManager.typographyE;
			case 'f': return GraphicsManager.typographyF;
			case 'g': return GraphicsManager.typographyG;
			case 'h': return GraphicsManager.typographyH;
			case 'i': return GraphicsManager.typographyI;
			case 'j': return GraphicsManager.typographyJ;
			case 'k': return GraphicsManager.typographyK;
			case 'l': return GraphicsManager.typographyL;
			case 'm': return GraphicsManager.typographyM;
			case 'n': return GraphicsManager.typographyN;
			case 'o': return GraphicsManager.typographyO;
			case 'p': return GraphicsManager.typographyP;
			case 'q': return GraphicsManager.typographyQ;
			case 'r': return GraphicsManager.typographyR;
			case 's': return GraphicsManager.typographyS;
			case 't': return GraphicsManager.typographyT;
			case 'u': return GraphicsManager.typographyU;
			case 'v': return GraphicsManager.typographyV;
			case 'w': return GraphicsManager.typographyW;
			case 'x': return GraphicsManager.typographyX;
			case 'y': return GraphicsManager.typographyY;
			case 'z': return GraphicsManager.typographyZ;
			case 'A': return GraphicsManager.typographyCapitalA;
			case 'B': return GraphicsManager.typographyCapitalB;
			case 'C': return GraphicsManager.typographyCapitalC;
			case 'D': return GraphicsManager.typographyCapitalD;
			case 'E': return GraphicsManager.typographyCapitalE;
			case 'F': return GraphicsManager.typographyCapitalF;
			case 'G': return GraphicsManager.typographyCapitalG;
			case 'H': return GraphicsManager.typographyCapitalH;
			case 'I': return GraphicsManager.typographyCapitalI;
			case 'J': return GraphicsManager.typographyCapitalJ;
			case 'K': return GraphicsManager.typographyCapitalK;
			case 'L': return GraphicsManager.typographyCapitalL;
			case 'M': return GraphicsManager.typographyCapitalM;
			case 'N': return GraphicsManager.typographyCapitalN;
			case 'O': return GraphicsManager.typographyCapitalO;
			case 'P': return GraphicsManager.typographyCapitalP;
			case 'Q': return GraphicsManager.typographyCapitalQ;
			case 'R': return GraphicsManager.typographyCapitalR;
			case 'S': return GraphicsManager.typographyCapitalS;
			case 'T': return GraphicsManager.typographyCapitalT;
			case 'U': return GraphicsManager.typographyCapitalU;
			case 'V': return GraphicsManager.typographyCapitalV;
			case 'W': return GraphicsManager.typographyCapitalW;
			case 'X': return GraphicsManager.typographyCapitalX;
			case 'Y': return GraphicsManager.typographyCapitalY;
			case 'Z': return GraphicsManager.typographyCapitalZ;
			case '0': return GraphicsManager.typography0;
			case '1': return GraphicsManager.typography1;
			case '2': return GraphicsManager.typography2;
			case '3': return GraphicsManager.typography3;
			case '4': return GraphicsManager.typography4;
			case '5': return GraphicsManager.typography5;
			case '6': return GraphicsManager.typography6;
			case '7': return GraphicsManager.typography7;
			case '8': return GraphicsManager.typography8;
			case '9': return GraphicsManager.typography9;
			case '.': return GraphicsManager.typographyDot;
			case ',': return GraphicsManager.typographyComma;
			case '!': return GraphicsManager.typographyExclamation;
			case '?': return GraphicsManager.typographyQuestion;
			case '(': return GraphicsManager.typographyBracketOpen;
			case ')': return GraphicsManager.typographyBracketClose;
			case ' ': return GraphicsManager.typographySpace;
			case '\'':return GraphicsManager.typographyApostrophe;
			case '-': return GraphicsManager.typographyDash;
			case '/': return GraphicsManager.typographySlash;
			default: return null;
		}
	}
	
	// returns the size of a string in image form
	public static int getStringWidth(String s){
		return CHARACTER_WIDTH * s.length();
	}
	
	// getDialogueSize(){}
	/*
	  should return the width of a text so painter can paint that area
	  in menu color
	  or perhaps have them here be put on area already
	  note that painter also may need to draw the edges of menu box around it
	 */
	
}
