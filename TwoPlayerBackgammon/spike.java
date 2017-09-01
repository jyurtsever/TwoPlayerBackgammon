/*This class represents the triangular shapes on the board,
which I called "spikes". The spikes keep track of variables
such as how many pieces are stacked on them and which player
effectively occupies the space. 
*/

public class Spike {
	int block_number;
	int number_of_pieces = 0;
	int player_number = 0;
	double xpos;
	double ypos;
	String img;
	int t = 0;
	boolean highlight = false;
	boolean clicked = false;
	boolean possible = false;
	boolean possibled = false;
	int section;
	double bottom_edge = .13;
	double left_edge = .125;
	double right_edge = .888;
	double top_edge = .895;
	double piece_radius = .024;
	double distance_between_spikes = .0638;
	public Spike(int b, int n, int p) {
		block_number = b;
		number_of_pieces = n;
		player_number = p;
		if (b < 7) {
			xpos = right_edge - (b - 1) * distance_between_spikes;
			section = 1;
		}
		else if (b < 13) {
			xpos = left_edge + (12 - b) * distance_between_spikes;
			section = 2;
		}
		else if (b < 19) {
			xpos = left_edge + (b - 13) * distance_between_spikes;
			section = 3;
		}
		else {
			xpos = right_edge - (24 - b) * distance_between_spikes;
			section = 4;
		}
		if (b > 12) {
			ypos = top_edge;
		}
		else {
			ypos = bottom_edge;
		}
	}

	//highlight and not highlight
	public void highlight() {
		highlight = true;
		display();
	}

	public void unhighlight() {
		highlight = false;
		display();
	}
	public void display_as_possible() {
		possible = true;
		display();
	}
	public void undisplay_as_possible() {
		possible = false;
		display();
	}

	public void get_image() {
		if (player_number == 1) {
			img = "images/white.png";
			if (highlight == true) {
				img = "images/white-highlight.png";
			}
			else if (possible == true) {
				img = "images/possible.png";
			}
		}
		if (player_number == 2) {
			img = "images/black.png";
			if (highlight == true) {
				img = "images/black-highlight.png";
			}
			else if (possible == true) {
				img = "images/possible.png";
			}

		}
	}
	public double get_top() {
		if (block_number < 13) {
			return ypos + 2*number_of_pieces*piece_radius;
		}
		else {
			return ypos - 2*number_of_pieces*piece_radius;
		}
	}
	public double get_location(int i) {
		if (block_number < 13) {
			return ypos + 2*i*piece_radius;
		}
		else {
			return ypos - 2*i*piece_radius;
		}
	}
	public void display() {
		int i = number_of_pieces;
		possibled = possible;
		while (i > 0) {
			get_image();
			StdDraw.picture(xpos, get_location(i), img);
			highlight = false;
			possible = false;
			i = i - 1;
		}
	}
}