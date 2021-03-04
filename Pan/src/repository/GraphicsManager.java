package repository;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

// this class adjusts images
public class GraphicsManager {
	
	/*============================= PRIVATE ==============================*/
	
	// the color that the menus will be displayed in
	private static int menuColor = 0;
	
	//
	private static final float SHADOW_TRANSPARENCY = 0.5f;
	
	/*============================== PUBLIC ==============================*/
	
	//
	public static float menuTransparency = 0.17f;
	
	
	/*============================== IMAGES ==============================*/
	
	
		/*=========================== AREA ===========================*/
	
	
			/*======================= TILE =======================*/

	
	public static final BufferedImage tileGrass1 = createImage("tileGrass1");
	public static final BufferedImage tileGrass2 = createImage("tileGrass2");
	public static final BufferedImage tileGrass3 = createImage("tileGrass3");
	public static final BufferedImage tileGrass4 = createImage("tileGrass4");
	
	public static final BufferedImage tileTestBlock1 = createImage("tileTestBlock1");
	
	
			/*=================== BATTLESCREEN ===================*/

	public static final BufferedImage battleScreen1 = createImage("battleScreen1");
	
		/*========================== OBJECT ==========================*/

	
	public static final BufferedImage objectRock1 = removeAddedPixels(createImage("objectRock1"));

	
		/*========================= CHARACTER ========================*/
	
	
			/*======================= IDLE =======================*/
	
	
	public static final BufferedImage panDown1 = removeAddedPixels(createImage("panDown1"));
	
	public static final BufferedImage panRight1 = removeAddedPixels(createImage("panRight1"));
	
	public static final BufferedImage panLeft1 = flipHorizontally(createImage("panRight1"));

	public static final BufferedImage panUp1 = removeAddedPixels(createImage("panUp1"));
	
	
			/*===================== MOVEMENT =====================*/
	
	
	public static final BufferedImage panMoveDown1 = removeAddedPixels(createImage("panMoveDown1"));
	public static final BufferedImage panMoveDown2 = removeAddedPixels(createImage("panMoveDown2"));
	
	public static final BufferedImage panMoveRight1 = removeAddedPixels(createImage("panMoveRight1"));
	public static final BufferedImage panMoveRight2 = removeAddedPixels(createImage("panMoveRight2"));

	public static final BufferedImage panMoveLeft1 = flipHorizontally(createImage("panMoveRight1"));
	public static final BufferedImage panMoveLeft2 = flipHorizontally(createImage("panMoveRight2"));

	public static final BufferedImage panMoveUp1 = removeAddedPixels(createImage("panMoveUp1"));
	public static final BufferedImage panMoveUp2 = removeAddedPixels(createImage("panMoveUp2"));

	
			/*=================== INTERACTION ====================*/

	public static final BufferedImage panTouchDown1 = removeAddedPixels(createImage("panTouchDown1"));
	public static final BufferedImage panTouchRight1 = removeAddedPixels(createImage("panTouchRight1"));
	public static final BufferedImage panTouchLeft1 = flipHorizontally(createImage("panTouchRight1"));
	public static final BufferedImage panTouchUp1 = removeAddedPixels(createImage("panTouchUp1"));

	
			/*====================== BATTLE ======================*/
	
	
	public static final BufferedImage panBattleStand1 = removeAddedPixels(createImage("panBattleStand1"));
	
	
			/*====================== AVATAR ======================*/
	
	
	public static final BufferedImage panAvatar = removeAddedPixels(createImage("panAvatar"));
	public static final BufferedImage mocaAvatar = removeAddedPixels(createImage("MocaAvatar"));

	
			/*====================== SHADOW ======================*/
	
	
	public static final BufferedImage shadowCharacter1 = makeTransparent(createImage("shadowCharacter1"));


		/*========================== MONSTER ==========================*/
			
			
			/*======================= STAND ======================*/
	
	
	public static final BufferedImage minion1Stand1 = removeAddedPixels(createImage("monsterMinion1Stand1"));
	public static final BufferedImage minion1Stand2 = removeAddedPixels(createImage("monsterMinion1Stand2"));
	public static final BufferedImage minion1Stand3 = removeAddedPixels(createImage("monsterMinion1Stand3"));
	public static final BufferedImage minion1Down1 = removeAddedPixels(createImage("monsterMinion1Down1"));
	
	public static final BufferedImage stem1Stand1 = removeAddedPixels(createImage("monsterStem1Stand1"));
	public static final BufferedImage stem1Stand2 = removeAddedPixels(createImage("monsterStem1Stand2"));

	public static final BufferedImage mollusca1Stand1 = removeAddedPixels(createImage("monsterMollusca1Stand1"));
	public static final BufferedImage mollusca1Stand2 = removeAddedPixels(createImage("monsterMollusca1Stand2"));
	public static final BufferedImage mollusca1Stand3 = removeAddedPixels(createImage("monsterMollusca1Stand3"));

	
		/*=========================== MENU ==========================*/
	
	
			/*====================== SCREEN ======================*/
	
	
	private static final BufferedImage menuBase1 = removeAddedPixels(createImage("menuBase1"));
	private static final BufferedImage menuBase2 = removeAddedPixels(createImage("menuBase2"));
	private static final BufferedImage menuBase3 = removeAddedPixels(createImage("menuBase3"));
	private static final BufferedImage menuBase4 = removeAddedPixels(createImage("menuBase4"));
	private static final BufferedImage menuBase5 = removeAddedPixels(createImage("menuBase5"));
	
	private static final BufferedImage menuOverlay1 = createImage("menuOverlay1");
	private static final BufferedImage menuOverlay2 = createImage("menuOverlay2");
	private static final BufferedImage menuOverlay3 = createImage("menuOverlay3");
	private static final BufferedImage menuOverlay4 = createImage("menuOverlay4");

	public static BufferedImage menu1 = mergeImages(makeTransparent(menuOverlay1,menuTransparency), menuBase1);
	public static BufferedImage menu2 = mergeImages(makeTransparent(menuOverlay2,menuTransparency), menuBase2);
	public static BufferedImage menu3 = mergeImages(makeTransparent(menuOverlay2,menuTransparency), menuBase3);
	public static BufferedImage menu4 = mergeImages(makeTransparent(menuOverlay3,menuTransparency), menuBase4);
	public static BufferedImage menu5 = mergeImages(makeTransparent(menuOverlay4,menuTransparency), menuBase5);

	public static BufferedImage menuItemSelection1 = removeAddedPixels(createImage("menuItemSelection1"));
	public static BufferedImage menuItemSelection2 = removeAddedPixels(createImage("menuItemSelection2"));
	public static BufferedImage menuItemSelection3 = removeAddedPixels(createImage("menuItemSelection3"));
	public static BufferedImage menuItemSelection4 = removeAddedPixels(createImage("menuItemSelection4"));
	public static BufferedImage menuItemSelection5a = removeAddedPixels(createImage("menuItemSelection5a"));
	public static BufferedImage menuItemSelection5b = removeAddedPixels(createImage("menuItemSelection5b"));
	public static BufferedImage menuItemSelection5c = removeAddedPixels(createImage("menuItemSelection5c"));
	public static BufferedImage menuItemSelection5d = removeAddedPixels(createImage("menuItemSelection5d"));

	public static final BufferedImage menuItemFadeOut1 = makeTransparent(createImage("menuItemFadeOut1"));
	public static final BufferedImage menuItemFadeOut2 = makeTransparent(createImage("menuItemFadeOut2"));
	public static final BufferedImage menuItemFadeOut3 = makeTransparent(createImage("menuItemFadeOut3"));
	
	public static final BufferedImage menuCheckmark = removeAddedPixels(createImage("menuCheckmark1"));
	public static final BufferedImage menuArrow1 = removeAddedPixels(createImage("menuArrow1"));
	public static final BufferedImage menuArrow2 = removeAddedPixels(createImage("menuArrow2"));

	public static final BufferedImage badgeAttack = removeAddedPixels(createImage("badgeAttack1"));
	public static final BufferedImage badgeArmor = removeAddedPixels(createImage("badgeArmor1"));
	public static final BufferedImage badgeSpeed = removeAddedPixels(createImage("badgeSpeed1"));
	public static final BufferedImage badgeImmunity = removeAddedPixels(createImage("badgeImmunity1"));
	public static final BufferedImage badgeRegenLife = removeAddedPixels(createImage("badgeRegen1"));
	public static final BufferedImage badgeRegenMana = removeAddedPixels(createImage("badgeRegen2"));
	
	
			/*====================== BATTLE ======================*/

	
	private static final BufferedImage battleMenuBase1 = removeAddedPixels(createImage("battleMenuBase1"));
	private static final BufferedImage battleMenuBase2 = removeAddedPixels(createImage("battleMenuBase2"));
	private static final BufferedImage battleMenuBase3 = removeAddedPixels(createImage("battleMenuBase3"));
	private static final BufferedImage battleMenuBase4 = removeAddedPixels(createImage("battleMenuBase4"));
	private static final BufferedImage battleMenuBase5 = removeAddedPixels(createImage("battleMenuBase5"));
	private static final BufferedImage battleMenuBase6 = removeAddedPixels(createImage("battleMenuBase6"));
	private static final BufferedImage battleMenuBase7 = removeAddedPixels(createImage("battleMenuBase7"));
	private static final BufferedImage battleMenuBase8 = removeAddedPixels(createImage("battleMenuBase8"));

	private static final BufferedImage battleMenuOverlay1 = createImage("battleMenuOverlay1");
	private static final BufferedImage battleMenuOverlay2 = createImage("battleMenuOverlay2");
	private static final BufferedImage battleMenuOverlay3 = createImage("battleMenuOverlay3");
	private static final BufferedImage battleMenuOverlay4 = createImage("battleMenuOverlay4");
	private static final BufferedImage battleMenuOverlay5 = createImage("battleMenuOverlay5");
	private static final BufferedImage battleMenuOverlay6 = createImage("battleMenuOverlay6");
	private static final BufferedImage battleMenuOverlay7 = createImage("battleMenuOverlay7");
	private static final BufferedImage battleMenuOverlay8 = createImage("battleMenuOverlay8");

	public static BufferedImage battleMenu1 = mergeImages(makeTransparent(battleMenuOverlay1,menuTransparency), battleMenuBase1);
	public static BufferedImage battleMenu2 = mergeImages(makeTransparent(battleMenuOverlay2,menuTransparency), battleMenuBase2);
	public static BufferedImage battleMenu3 = mergeImages(makeTransparent(battleMenuOverlay3,menuTransparency), battleMenuBase3);
	public static BufferedImage battleMenu4 = mergeImages(makeTransparent(battleMenuOverlay4,menuTransparency), battleMenuBase4);
	public static BufferedImage battleMenu5 = mergeImages(makeTransparent(battleMenuOverlay5,menuTransparency), battleMenuBase5);
	public static BufferedImage battleMenu6 = mergeImages(makeTransparent(battleMenuOverlay6,menuTransparency), battleMenuBase6);
	public static BufferedImage battleMenu7 = mergeImages(makeTransparent(battleMenuOverlay7,menuTransparency), battleMenuBase7);
	public static BufferedImage battleMenu8 = mergeImages(makeTransparent(battleMenuOverlay8,menuTransparency), battleMenuBase8);

	public static BufferedImage battleMenuSelection1 = removeAddedPixels(createImage("battleMenuSelection1"));
	public static BufferedImage battleMenuSelection2 = removeAddedPixels(createImage("battleMenuSelection2"));
	public static BufferedImage battleMenuSelection3 = removeAddedPixels(createImage("battleMenuSelection3"));

	public static BufferedImage battleMenuPointer1 = removeAddedPixels(createImage("battleMenuPointer1"));
	public static BufferedImage battleMenuPointer2 = removeAddedPixels(createImage("battleMenuPointer2"));
	public static BufferedImage battleMenuPointer3 = removeAddedPixels(createImage("battleMenuPointer3"));
	public static BufferedImage battleMenuPointer4 = removeAddedPixels(createImage("battleMenuPointer4"));

	public static final BufferedImage battleMenuFadeOut1 = makeTransparent(createImage("battleMenuFadeOut1"));
	public static final BufferedImage battleMenuFadeOut2 = makeTransparent(createImage("battleMenuFadeOut2"));

	public static final BufferedImage battleMenuIcon1 = removeAddedPixels(createImage("battleMenuIconFight"));
	public static final BufferedImage battleMenuIcon2 = removeAddedPixels(createImage("battleMenuIconFlight"));

	public static final BufferedImage battleMenuBar = createImage("battleMenuBar1");
	public static final BufferedImage battleMenuBarLife = createImage("battleMenuBarLife1");
	public static final BufferedImage battleMenuBarMana = createImage("battleMenuBarMana1");
	public static final BufferedImage battleMenuBarDamage = createImage("battleMenuBarDamage1");
	
	
			/*======================== MAP =======================*/
	
	public static final BufferedImage worldMap1 = createImage("tempmap");
	public static final BufferedImage worldMapCursor1 = removeAddedPixels(createImage("mapCursor1"));
	
	
		/*====================== SCREEN OVERLAY =====================*/


			/*==================== TRANSITION ====================*/

	
	public static final BufferedImage darkenedScreen1 = createImage("overlayBlack1");
	public static final BufferedImage darkenedScreen2 = makeTransparent(darkenedScreen1,0.9f);
	public static final BufferedImage darkenedScreen3 = makeTransparent(darkenedScreen1,0.8f);
	public static final BufferedImage darkenedScreen4 = makeTransparent(darkenedScreen1,0.7f);
	public static final BufferedImage darkenedScreen5 = makeTransparent(darkenedScreen1,0.6f);
	public static final BufferedImage darkenedScreen6 = makeTransparent(darkenedScreen1,0.5f);
	public static final BufferedImage darkenedScreen7 = makeTransparent(darkenedScreen1,0.4f);
	public static final BufferedImage darkenedScreen8 = makeTransparent(darkenedScreen1,0.3f);
	public static final BufferedImage darkenedScreen9 = makeTransparent(darkenedScreen1,0.2f);
	public static final BufferedImage darkenedScreen10 = makeTransparent(darkenedScreen1,0.1f);

	
			/*===================== LIGHTNING ====================*/

	
	public static final BufferedImage lightningScreen1 = createImage("overlayWhite1");
	
	
		/*========================== ABILITY ========================*/


			/*====================== BADGE =======================*/

	
	public static final BufferedImage badgeFire = removeAddedPixels(createImage("elementBadge1"));
	public static final BufferedImage badgeFirePower = removeAddedPixels(createImage("elementBadge1a"));
	public static final BufferedImage badgeFireResistance = removeAddedPixels(createImage("elementBadge1b"));
	public static final BufferedImage badgeWater = removeAddedPixels(createImage("elementBadge2"));
	public static final BufferedImage badgeWaterPower = removeAddedPixels(createImage("elementBadge2a"));
	public static final BufferedImage badgeWaterResistance = removeAddedPixels(createImage("elementBadge2b"));
	public static final BufferedImage badgeAir = removeAddedPixels(createImage("elementBadge3"));
	public static final BufferedImage badgeAirPower = removeAddedPixels(createImage("elementBadge3a"));
	public static final BufferedImage badgeAirResistance = removeAddedPixels(createImage("elementBadge3b"));
	public static final BufferedImage badgeEarth = removeAddedPixels(createImage("elementBadge4"));
	public static final BufferedImage badgeEarthPower = removeAddedPixels(createImage("elementBadge4a"));
	public static final BufferedImage badgeEarthResistance = removeAddedPixels(createImage("elementBadge4b"));
	public static final BufferedImage badgeArcane = removeAddedPixels(createImage("elementBadge5"));
	public static final BufferedImage badgeArcanePower = removeAddedPixels(createImage("elementBadge5a"));
	public static final BufferedImage badgeArcaneResistance = removeAddedPixels(createImage("elementBadge5b"));
	
	
			/*======================= FIRE =======================*/
	
	
	public static final BufferedImage flame1Icon = createImage("abilityFlame1Icon1");

	
			/*====================== WATER =======================*/
	
	
	public static final BufferedImage treatBurn1Icon = createImage("abilityTreatBurn1Icon1");


			/*======================= AIR ========================*/

			
	public static final BufferedImage mend1Icon = createImage("abilityMend1Icon1");
	
	
			/*====================== REACH =======================*/

	
	public static final BufferedImage reachSingle = removeAddedPixels(createImage("abilityReachSingle"));
	public static final BufferedImage reachDimThree = removeAddedPixels(createImage("abilityReachDimThree"));
	public static final BufferedImage reachDimFive = removeAddedPixels(createImage("abilityReachDimFive"));
	public static final BufferedImage reachPureThree = removeAddedPixels(createImage("abilityReachPureThree"));
	public static final BufferedImage reachPureFive = removeAddedPixels(createImage("abilityReachPureFive"));
	public static final BufferedImage reachRightTwo = removeAddedPixels(createImage("abilityReachRightTwo"));
	public static final BufferedImage reachRightFour = removeAddedPixels(createImage("abilityReachRightFour"));
	public static final BufferedImage reachLeftTwo = removeAddedPixels(createImage("abilityReachLeftTwo"));
	public static final BufferedImage reachLeftFour = removeAddedPixels(createImage("abilityReachLeftFour"));

	
			/*===================== CONDITION ====================*/

	
	public static final BufferedImage conditionStun = removeAddedPixels(createImage("conditionStun1"));
	public static final BufferedImage conditionCurse = removeAddedPixels(createImage("conditionCurse1"));
	public static final BufferedImage conditionPoison = removeAddedPixels(createImage("conditionPoison1"));
	public static final BufferedImage conditionBurn = removeAddedPixels(createImage("conditionBurn1"));
	public static final BufferedImage conditionReap = removeAddedPixels(createImage("conditionReap1"));
	public static final BufferedImage conditionDisarm = removeAddedPixels(createImage("conditionLockWeapon1"));
	public static final BufferedImage conditionLockSpell = removeAddedPixels(createImage("conditionLockSpell1"));
	public static final BufferedImage conditionLockItem = removeAddedPixels(createImage("conditionLockItem1"));
	public static final BufferedImage conditionFreeze = removeAddedPixels(createImage("conditionFreeze1"));
	public static final BufferedImage conditionConfusion = removeAddedPixels(createImage("conditionConfuse1"));
	
		
		/*=========================== ITEM ==========================*/


			/*====================== SWORD ========================*/
	
	
	public static final BufferedImage sword1Icon = createImage("itemSword1Icon1");
	public static final BufferedImage appleIcon = createImage("itemApple1Icon1");
	
	
		/*======================== TYPOGRAPHY =======================*/


			/*==================== LOWER CASE =====================*/
	
	
	public static final BufferedImage typographyA = removeAddedPixels(createImage("typographyA"));
	public static final BufferedImage typographyB = removeAddedPixels(createImage("typographyB"));
	public static final BufferedImage typographyC = removeAddedPixels(createImage("typographyC"));
	public static final BufferedImage typographyD = removeAddedPixels(createImage("typographyD"));
	public static final BufferedImage typographyE = removeAddedPixels(createImage("typographyE"));
	public static final BufferedImage typographyF = removeAddedPixels(createImage("typographyF"));
	public static final BufferedImage typographyG = removeAddedPixels(createImage("typographyG"));
	public static final BufferedImage typographyH = removeAddedPixels(createImage("typographyH"));
	public static final BufferedImage typographyI = removeAddedPixels(createImage("typographyI"));
	public static final BufferedImage typographyJ = removeAddedPixels(createImage("typographyJ"));
	public static final BufferedImage typographyK = removeAddedPixels(createImage("typographyK"));
	public static final BufferedImage typographyL = removeAddedPixels(createImage("typographyL"));
	public static final BufferedImage typographyM = removeAddedPixels(createImage("typographyM"));
	public static final BufferedImage typographyN = removeAddedPixels(createImage("typographyN"));
	public static final BufferedImage typographyO = removeAddedPixels(createImage("typographyO"));
	public static final BufferedImage typographyP = removeAddedPixels(createImage("typographyP"));
	public static final BufferedImage typographyQ = removeAddedPixels(createImage("typographyQ"));
	public static final BufferedImage typographyR = removeAddedPixels(createImage("typographyR"));
	public static final BufferedImage typographyS = removeAddedPixels(createImage("typographyS"));
	public static final BufferedImage typographyT = removeAddedPixels(createImage("typographyT"));
	public static final BufferedImage typographyU = removeAddedPixels(createImage("typographyU"));
	public static final BufferedImage typographyV = removeAddedPixels(createImage("typographyV"));
	public static final BufferedImage typographyW = removeAddedPixels(createImage("typographyW"));
	public static final BufferedImage typographyX = removeAddedPixels(createImage("typographyX"));
	public static final BufferedImage typographyY = removeAddedPixels(createImage("typographyY"));
	public static final BufferedImage typographyZ = removeAddedPixels(createImage("typographyZ"));


			/*==================== UPPER CASE =====================*/


	public static final BufferedImage typographyCapitalA = removeAddedPixels(createImage("typographyCapitalA"));
	public static final BufferedImage typographyCapitalB = removeAddedPixels(createImage("typographyCapitalB"));
	public static final BufferedImage typographyCapitalC = removeAddedPixels(createImage("typographyCapitalC"));
	public static final BufferedImage typographyCapitalD = removeAddedPixels(createImage("typographyCapitalD"));
	public static final BufferedImage typographyCapitalE = removeAddedPixels(createImage("typographyCapitalE"));
	public static final BufferedImage typographyCapitalF = removeAddedPixels(createImage("typographyCapitalF"));
	public static final BufferedImage typographyCapitalG = removeAddedPixels(createImage("typographyCapitalG"));
	public static final BufferedImage typographyCapitalH = removeAddedPixels(createImage("typographyCapitalH"));
	public static final BufferedImage typographyCapitalI = removeAddedPixels(createImage("typographyCapitalI"));
	public static final BufferedImage typographyCapitalJ = removeAddedPixels(createImage("typographyCapitalJ"));
	public static final BufferedImage typographyCapitalK = removeAddedPixels(createImage("typographyCapitalK"));
	public static final BufferedImage typographyCapitalL = removeAddedPixels(createImage("typographyCapitalL"));
	public static final BufferedImage typographyCapitalM = removeAddedPixels(createImage("typographyCapitalM"));
	public static final BufferedImage typographyCapitalN = removeAddedPixels(createImage("typographyCapitalN"));
	public static final BufferedImage typographyCapitalO = removeAddedPixels(createImage("typographyCapitalO"));
	public static final BufferedImage typographyCapitalP = removeAddedPixels(createImage("typographyCapitalP"));
	public static final BufferedImage typographyCapitalQ = removeAddedPixels(createImage("typographyCapitalQ"));
	public static final BufferedImage typographyCapitalR = removeAddedPixels(createImage("typographyCapitalR"));
	public static final BufferedImage typographyCapitalS = removeAddedPixels(createImage("typographyCapitalS"));
	public static final BufferedImage typographyCapitalT = removeAddedPixels(createImage("typographyCapitalT"));
	public static final BufferedImage typographyCapitalU = removeAddedPixels(createImage("typographyCapitalU"));
	public static final BufferedImage typographyCapitalV = removeAddedPixels(createImage("typographyCapitalV"));
	public static final BufferedImage typographyCapitalW = removeAddedPixels(createImage("typographyCapitalW"));
	public static final BufferedImage typographyCapitalX = removeAddedPixels(createImage("typographyCapitalX"));
	public static final BufferedImage typographyCapitalY = removeAddedPixels(createImage("typographyCapitalY"));
	public static final BufferedImage typographyCapitalZ = removeAddedPixels(createImage("typographyCapitalZ"));

	
			/*====================== NUMBERS ======================*/
	
	
	public static final BufferedImage typography0 = removeAddedPixels(createImage("typography0"));
	public static final BufferedImage typography1 = removeAddedPixels(createImage("typography1"));
	public static final BufferedImage typography2 = removeAddedPixels(createImage("typography2"));
	public static final BufferedImage typography3 = removeAddedPixels(createImage("typography3"));
	public static final BufferedImage typography4 = removeAddedPixels(createImage("typography4"));
	public static final BufferedImage typography5 = removeAddedPixels(createImage("typography5"));
	public static final BufferedImage typography6 = removeAddedPixels(createImage("typography6"));
	public static final BufferedImage typography7 = removeAddedPixels(createImage("typography7"));
	public static final BufferedImage typography8 = removeAddedPixels(createImage("typography8"));
	public static final BufferedImage typography9 = removeAddedPixels(createImage("typography9"));

	
			/*==================== PUNCTUATION ====================*/
	
	public static final BufferedImage typographyDot = removeAddedPixels(createImage("typographyDot"));
	public static final BufferedImage typographyComma = removeAddedPixels(createImage("typographyComma"));
	public static final BufferedImage typographyExclamation = removeAddedPixels(createImage("typographyExclamation"));
	public static final BufferedImage typographyQuestion = removeAddedPixels(createImage("typographyQuestion"));
	public static final BufferedImage typographyBracketOpen = removeAddedPixels(createImage("typographyBracketOpen"));
	public static final BufferedImage typographyBracketClose = removeAddedPixels(createImage("typographyBracketClose"));
	public static final BufferedImage typographySpace = removeAddedPixels(createImage("typographySpace"));
	public static final BufferedImage typographyApostrophe = removeAddedPixels(createImage("typographyApostrophe"));
	public static final BufferedImage typographyDash = removeAddedPixels(createImage("typographyDash"));
	public static final BufferedImage typographySlash = removeAddedPixels(createImage("typographySlash"));
	public static final BufferedImage typographyPlus = removeAddedPixels(createImage("typographyPlus"));

	
	
	
	
	
	/*================================ METHOD ================================*/

	
	// loads images straight from file
	private static BufferedImage createImage(String name) {
		try {
			return ImageIO.read(new File("src/art/" + name + ".png"));
		} catch (IOException e){
			e.printStackTrace();
			return null;
		}
	}

	// removes white pixels that are added to supposed transparent images
	private static BufferedImage removeAddedPixels(BufferedImage oldImage) {
        BufferedImage newImage = new BufferedImage(oldImage.getWidth(),oldImage.getHeight(),BufferedImage.TYPE_INT_ARGB);
        Graphics2D g= newImage.createGraphics();
        for (int pixelX = 0; pixelX < newImage.getWidth(); pixelX++) {
            for (int pixelY = 0; pixelY < newImage.getHeight(); pixelY++) {
                if (oldImage.getRGB(pixelX,pixelY)!=Color.WHITE.getRGB()) newImage.setRGB(pixelX,pixelY, oldImage.getRGB(pixelX,pixelY));
            }
        }
        g.dispose();
        return newImage;
    }
    	
    // makes an image transparent using default shadow transparency
    private static BufferedImage makeTransparent(BufferedImage oldImage){
    	return makeTransparent(oldImage,SHADOW_TRANSPARENCY);
    }
    
    // makes an image transparent
    private static BufferedImage makeTransparent(BufferedImage oldImage, float transparency){
    	// first removes any added white pixels
    	BufferedImage updatedOldImage = removeAddedPixels(oldImage);
    	
    	BufferedImage transparentImage = new BufferedImage(updatedOldImage.getWidth(), updatedOldImage.getHeight(),BufferedImage.TYPE_INT_ARGB);
    	Graphics2D g = transparentImage.createGraphics();
		g.setComposite(AlphaComposite.SrcOver.derive(transparency)); 
		g.drawImage(updatedOldImage, 0, 0, null);
		g.dispose();
		return transparentImage;
    }
    
    // flips image horizontally
    private static BufferedImage flipHorizontally(BufferedImage image){
    	
    	// first removes any added white pixels
    	BufferedImage updatedImage = removeAddedPixels(image);
    	
    	AffineTransform transformer = AffineTransform.getScaleInstance(-1, 1);
    	transformer.translate(-updatedImage.getWidth(null), 0);
    	AffineTransformOp transformerOp = new AffineTransformOp(transformer, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
    	updatedImage = transformerOp.filter(updatedImage, null);
    	
		return updatedImage;
    }
    
    // merges images so one appears over the other
    private static BufferedImage mergeImages(BufferedImage over, BufferedImage under){
    	BufferedImage mergedImage = new BufferedImage(over.getWidth(), over.getHeight(),BufferedImage.TYPE_INT_ARGB);
    	Graphics2D g = mergedImage.createGraphics();
        for (int pixelX = 0; pixelX < mergedImage.getWidth(); pixelX++) {
            for (int pixelY = 0; pixelY < mergedImage.getHeight(); pixelY++) {
                mergedImage.setRGB(pixelX,pixelY, under.getRGB(pixelX,pixelY));
            }
        }
		g.drawImage(over, 0, 0, null);
		g.dispose();
		return mergedImage;
    }
    
    //
    public static BufferedImage resizeImage(BufferedImage oldImage, double widthMultiplier, double heightMultiplier){
    	int width = (int)(oldImage.getWidth() * widthMultiplier);
    	int height = (int)(oldImage.getHeight() * heightMultiplier);
    	BufferedImage resizedImage = new BufferedImage(width, height,BufferedImage.TYPE_INT_ARGB);
    	Graphics2D g = resizedImage.createGraphics();
    	g.drawImage(oldImage,0,0,width,height,null);
    	g.dispose();
    	return resizedImage;
    }
    
    // changes the color of the menu
    public static void changeMenuColor(){ // changeMenuColor(int color) ?
    							// could store int (in core?) use^ when loading file
        int color = getMenuColorRGB();

        BufferedImage newOverlayA = createOverlay(menuOverlay1, color);
        BufferedImage newOverlayB = createOverlay(menuOverlay2, color);
        BufferedImage newOverlayC = createOverlay(menuOverlay3, color);
        BufferedImage newOverlayD = createOverlay(menuOverlay4, color);

        BufferedImage newOverlay0 = createOverlay(battleMenuOverlay1, color);
        BufferedImage newOverlay1 = createOverlay(battleMenuOverlay2, color);
        BufferedImage newOverlay2 = createOverlay(battleMenuOverlay3, color);
        BufferedImage newOverlay3 = createOverlay(battleMenuOverlay4, color);
        BufferedImage newOverlay4 = createOverlay(battleMenuOverlay5, color);
        BufferedImage newOverlay5 = createOverlay(battleMenuOverlay6, color);
        BufferedImage newOverlay6 = createOverlay(battleMenuOverlay7, color);
        BufferedImage newOverlay7 = createOverlay(battleMenuOverlay8, color);

        menu1 = mergeImages(makeTransparent(newOverlayA, menuTransparency), menuBase1);
        menu2 = mergeImages(makeTransparent(newOverlayB, menuTransparency), menuBase2);
        menu3 = mergeImages(makeTransparent(newOverlayB, menuTransparency), menuBase3);
        menu4 = mergeImages(makeTransparent(newOverlayC, menuTransparency), menuBase4);
        menu5 = mergeImages(makeTransparent(newOverlayD, menuTransparency), menuBase5);
        
        battleMenu1 = mergeImages(makeTransparent(newOverlay0, menuTransparency), battleMenuBase1);
        battleMenu2 = mergeImages(makeTransparent(newOverlay1, menuTransparency), battleMenuBase2);
        battleMenu3 = mergeImages(makeTransparent(newOverlay2, menuTransparency), battleMenuBase3);
        battleMenu4 = mergeImages(makeTransparent(newOverlay3, menuTransparency), battleMenuBase4);
        battleMenu5 = mergeImages(makeTransparent(newOverlay4, menuTransparency), battleMenuBase5);
        battleMenu6 = mergeImages(makeTransparent(newOverlay5, menuTransparency), battleMenuBase6);
        battleMenu7 = mergeImages(makeTransparent(newOverlay6, menuTransparency), battleMenuBase7);
        battleMenu8 = mergeImages(makeTransparent(newOverlay7, menuTransparency), battleMenuBase8);
    }
    
    //
    private static BufferedImage createOverlay(BufferedImage oldOverlay, int color){
        BufferedImage newOverlay = new BufferedImage(oldOverlay.getWidth(),oldOverlay.getHeight(),BufferedImage.TYPE_INT_ARGB);
        Graphics2D g= newOverlay.createGraphics();
        for (int pixelX = 0; pixelX < newOverlay.getWidth(); pixelX++) {
            for (int pixelY = 0; pixelY < newOverlay.getHeight(); pixelY++) {
                if (oldOverlay.getRGB(pixelX,pixelY)!=Color.WHITE.getRGB()) 
                	newOverlay.setRGB(pixelX,pixelY, color);
            }
        }
        g.dispose();
        return newOverlay;
    }
    
    // changes color. TODO add changing the transparency (other button(and method))
    private static int getMenuColorRGB(){
    	if (menuColor > 27) menuColor = 0;
    	switch (menuColor++) {
    		case 0: return new Color(0, 200, 255).getRGB();
    		case 1: return new Color(0, 150, 255).getRGB();
    		case 2: return new Color(0, 100, 255).getRGB();
    		case 3: return new Color(0, 50, 255).getRGB();
    		case 4: return new Color(0, 0, 255).getRGB();
    		case 5: return new Color(50, 0, 255).getRGB();
    		case 6: return new Color(100, 0, 255).getRGB();
    		case 7: return new Color(150, 0, 255).getRGB();
    		case 8: return new Color(200, 0, 255).getRGB();
    		case 9: return new Color(255, 0, 200).getRGB();
    		case 10: return new Color(255, 0, 150).getRGB();
    		case 11: return new Color(255, 0, 100).getRGB();
    		case 12: return new Color(255, 0, 50).getRGB();
    		case 13: return new Color(255,50, 0).getRGB();
    		case 14: return new Color(255, 100, 0).getRGB();
    		case 15: return new Color(255, 150, 0).getRGB();
    		case 16: return new Color(255, 200, 0).getRGB();
    		case 17: return new Color(255, 255, 0).getRGB();
    		case 18: return new Color(200, 255, 0).getRGB();
    		case 19: return new Color(150, 255, 0).getRGB();
    		case 20: return new Color(100, 255, 0).getRGB();
    		case 21: return new Color(50, 255, 0).getRGB();
    		case 22: return new Color(0, 255, 0).getRGB();
    		case 23: return new Color(0, 255, 50).getRGB();
    		case 24: return new Color(0, 255, 100).getRGB();
    		case 25: return new Color(0, 255, 150).getRGB();
    		case 26: return new Color(0, 255, 200).getRGB();
    		case 27: return new Color(0, 255, 255).getRGB();

    		default: return 0;
    	}
    }
}
