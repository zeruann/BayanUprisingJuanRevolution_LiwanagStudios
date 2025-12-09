package CG_01_Main;

import Aa_02_GameMenu.GameMenuFrame;
import Aa_02_GameMenu.GameMenuPanel;
import Aa_03_GameMap.MapFrame;
import Aa_03_GameMap.MapPanel;
public class RunMain {

	public static void main(String[] args) {
		  //GameMenuPanel panel = new GameMenuPanel("/GameMenu_Assets/GameHomePage_00.png"); 
        GameMenuPanel panel = new GameMenuPanel("/GameMenu_Assets/GameHomePage_12.png"); 
        //GameMenuPanel panel = new GameMenuPanel("/GameMenu_Assets/GameHomePage_huhu.png"); 
        new GameMenuFrame(panel);

	}

}
