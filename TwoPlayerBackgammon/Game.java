/* This class is essentially the main method for
the whole game. It sets up the board and keeps 
track of the status of the game, alternating 
between each player and calling their take turn 
methods until one of them wins
*/
public class Game {
	public static Spike[] setup() {
		Spike[] spikes = new Spike[26];
		int i = 0;
		while (i < 26) {
			spikes[i] = new Spike(i,0,0);
			i = i + 1;
		}
		spikes[1] = new Spike(1,2,2);
		spikes[6] = new Spike(6,5,1);
		spikes[8] = new Spike(8,3,1);
		spikes[12] = new Spike(12,5,2);
		spikes[13] = new Spike(13,5,1);
		spikes[17] = new Spike(17,3,2);
		spikes[19] = new Spike(19,5,2);
		spikes[24] = new Spike(24,2,1);
		return spikes;
	}
	public static void display_all(Spike[] spikes) {
		String board = "images/board.jpg";
		StdDraw.picture(.51,.51, board);
		for (Spike s : spikes) {
			s.display();
		}
	}
	public static Spike[] part_two_setup() {
		Spike[] spikes = new Spike[28];
		int i = 0;
		while (i < 28) {
			spikes[i] = new Spike(i,0,0);
			i = i + 1;
		}
		spikes[5] = new Spike(5,5,1);
		spikes[6] = new Spike(6,3,1);
		spikes[4] = new Spike(4,5,1);
		spikes[7] = new Spike(7,2,1);
		spikes[20] = new Spike(20,5,2);
		spikes[19] = new Spike(19,5,2);
		spikes[21] = new Spike(21,3,2);
		spikes[18] = new Spike(18,2,2);
		return spikes;
	}
	public static void main(String[] args) throws InterruptedException {
		Spike[] spikes;
		boolean part_two = false;
		if (args[0].equals("part_two")) {
			spikes = part_two_setup();
			part_two = true;
		}
		else {
			spikes = setup();
		}
		Dice dice = new Dice();
		Player p1 = new Player(1,part_two);
		Player p2 = new Player(2, part_two);
		p1.other = p2;
		p2.other = p1;
		Player[] players = new Player[2];
		players[0] = p1;
		players[1] = p2;
		Player p = p1;
		StdDraw.setCanvasSize(600, 600);
		display_all(spikes);
		while (p1.removed_pieces < 15 && p2.removed_pieces < 15) {
			// dice.roll();
			// dice.display();
			dice.click_roll();
			p.take_turn(spikes, dice.getDie1(), dice.getDie2());
			p = players[p.number% 2];
		}
		if (p1.removed_pieces >= 15) {
			System.out.println("Game Over: White Wins!");
			StdDraw.text(.5, .5, "Game Over: White Wins!");
		}
		else {
			System.out.println("Game Over: Black Wins!");
			StdDraw.text(.5, .5, "Game Over: Black Wins!");
		}
		return;
	}
}