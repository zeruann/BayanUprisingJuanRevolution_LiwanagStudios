package CG_05_Utilz;

public class Constants {
	
	public static class Directions{ 
		public static final int LEFT = 0;
		public static final int UP = 1;
		public static final int RIGHT = 2;
		public static final int DOWN = 3;
	}
	
	
	public static class PlayerConstants{
		public static final int IDLE = 0;
		public static final int ATTACK = 1;
		public static final int HURT = 2;
		public static final int RUNNING = 3;
		//public static final int HEAL = 4;
		//public static final int SHIELD = 5;
		
		public static int GetSpriteAmount(int player_Action) {
			
			switch(player_Action) {
			
			case IDLE:
				return 2; 
			case ATTACK:
				return 6;
			case HURT:
				return 6; 
			case RUNNING:
				return 8;
			default:
				return 1;
			}
		}

	}
	
	public static class EnemyConstants {
	    public static final int IDLE = 0;
	    public static final int ATTACK = 1;
	    public static final int HURT = 2;
	    public static final int DEATH = 3;

	    public static int GetSpriteAmount(int enemy_Action) {
	        switch(enemy_Action) {
	            case IDLE: return 2;
	            case ATTACK: return 8;
	            case HURT: return 6;
	            default: return 1;
	        }
	    }
	}

}
