import java.awt.*;
public class Dice {
	/*
	An object of class Dice represents a pair of dice,
	where each die shows a number between 1 and 6.  The dice
	can be rolled, which randomizes the numbers showing on the
	dice.
	*/
	public int die1;   // Number showing on the first die.
	public int die2;   // Number showing on the second die.
	public String[] images = new String[6];
	public int used = 0;
	////box methods
	Font font = new Font("Arial", Font.BOLD, 12);
	double e = .3;
	double w = .2;
	double s = .895;
	double n = .945;
	public Dice() {
	    // Constructor.  Rolls the dice, so that they initially
	    // show some random values.
		roll();  // Call the roll() method to roll the dice.
		int i = 0;
		while (i < 6) {
			images[i] = "images/Die-" + Integer.toString(i + 1) + ".gif";
			i = i + 1;
		}
	}
	public boolean mouse_in_box() {
		double xpos = StdDraw.mouseX();
		double ypos = StdDraw.mouseY();
		if (ypos < n && ypos > s) {
			if (xpos < e && xpos > w) {
				return true;
			}
		}
		return false; 

	}
	public void click_roll() throws InterruptedException {
		StdDraw.rectangle(.25, .92, .05, .025);
		StdDraw.setFont(font);
		StdDraw.text(0.25, 0.92, "Roll");
		while (!StdDraw.mousePressed() || !mouse_in_box()) {
			Thread.sleep(100);
		}
		animate_roll();
	}

	public void roll() {
	    // Roll the dice by setting each of the dice to be
	    // a random number between 1 and 6.
		die1 = (int)(Math.random()*6) + 1;
		die2 = (int)(Math.random()*6) + 1;
	}
	     
	public int getDie1() {
	  // Return the number showing on the first die.
		return die1;
	}

	public int getDie2() {
	  // Return the number showing on the second die.
		return die2;
	}

	public int getTotal() {
	  // Return the total showing on the two dice.
		return die1 + die2;
	}
	public void display() {
		StdDraw.picture(.05,.92, images[die1 -1]);
		StdDraw.picture(.1,.92, images[die2 -1]);
	}
	public void animate_roll() throws InterruptedException {
		int t = 0;
		while (t < 10) {
			roll();
			display();
			Thread.sleep(50);
			t = t + 1;
		}
	}
}