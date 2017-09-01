import java.util.HashSet;
/*This class is initialized for each player.
It keeps track of the spikes that contain the
players pieces in the variable owned. The 
class contains the most code since it takes
into account the rules of the game
*/
public class Player {
	////
	int total_pieces = 15;
	////
	int number;
	int removed_pieces;
	int direction;

	////For prison
	boolean jailed;
	Spike prison;
	////
	int[] moves;
	HashSet<Integer> owned = new HashSet<Integer>(); //numbers of spikes that are occupied by the player
	//for selection and moving
	boolean check_selected;
	boolean moved;
	////the other player
	Player other;


	///For part 2 of the game
	int part;

////Constructor
	public Player(int pin, boolean shortcut) { //
		number = pin; //player number
		removed_pieces = 0; //number of pieces removed in part 2
		jailed = false; //
		check_selected = false;
		moved = false;
		direction = 2*number - 3;
		part = 1;
		////setting up the jail

		prison = new Spike(direction * 69, 0, pin);
		prison.xpos = .5;
		prison.ypos = -1*direction * .025 + .5;
		////
		int[] initial_array;
		if (pin == 1) {
			initial_array = new int[] {6,8,13,24};
			if (shortcut) {
				initial_array = new int[] {4,5,6,7};
			}
		}
		else {
			initial_array = new int[] {1,12,17,19};
			if (shortcut) {
				initial_array = new int[] {18,19,20,21};
			}
		}
		for (int i : initial_array) {
			owned.add(i);
		}
	}

////dealing with the moves list
	public int[] remove_move(int[] original, int used) { //not destructive way to remove a used move or an impossible move
		int[] new_list = new int[original.length - 1];
		int count_o = 0;
		int count_n = 0;
		boolean flag = true;  //just in case used appears twice
		while (count_o < original.length) {
			if (original[count_o] == used && flag) { 
				flag = false;
			}
			else {
				new_list[count_n] = original[count_o];
				count_n = count_n + 1;
			}
			count_o = count_o + 1;
		}
		return new_list;
	}


	public int[] initial_moves(int die1,int die2) { //gets the initial moves from the two die (not taking into account the current board)
		int[] result;
		if (die1 == die2) {
			result = new int[] {die1, die1, die1, die1};
		}
		else {
			result = new int[] {die1, die2};
		}
		return result;
	}
	public boolean check_part_two_exception(int origin, int move, Spike[] spikes) {
		int distance_from_edge = 25*(number - 1) - direction*origin;
		if (move > distance_from_edge) {
			int i = 1;
			while (i <= 6 - distance_from_edge) {
				if (spikes[origin- direction*i].number_of_pieces > 0) {
					return false;
				}
				i = i + 1;
			}
		}
		return true;
	}

	public boolean check_legality(int origin, int move, Spike[] spikes) { //returns true if move is legal
		// System.out.println(part);
		int direction = 2*number - 3;
		int destination = origin + direction*move;
		if (part == 1) {
			return !((destination > 24 || destination < 1) || 
				(spikes[destination].number_of_pieces > 1 && spikes[destination].player_number != number));
		}
		else {
			if (check_part_two_exception(origin, move, spikes)) {
				return true;
			}
			else {
				return !((destination > 25 || destination < 0) || 
				(spikes[destination].number_of_pieces > 1 && spikes[destination].player_number != number));
			}
		}
	}
	public int[] usable_moves(int[] moves, Spike[] spikes) { //gives a list of the di that can be used during the turn
		int[] result = moves;
		int i = 0;
		if (gele(moves, spikes)) {
			StdDraw.text(.5, .5, "Gele :(((");
			int[] g = new int[0];
			return g;
		}
		if (jailed) {
			if (prison.number_of_pieces > 1) {
				for (int m : result) {
					if (!check_legality((number % 2) * 25, m, spikes)) {
						remove_move(result, m);
					}
				}
				return result;
			}
		}
		if (moves.length == 4) { //for doubles 
			for (int s : owned) { 
				while (i < 4) {
					if (check_legality(s, moves[0], spikes)) {
						i = i + spikes[s].number_of_pieces;
					}
					else {
						break;
					}
				}
			}
			while (i < 4) {                          //removes all impossible moves 
				result = remove_move(moves, moves[0]);
				i = i + 1;
			}
			return result;
		}
		while (i < 2) {
			boolean flag = false;
			for (int s : owned) {  //for non-doubles
				if (flag) {
					break;
				}
				flag = check_legality(s,moves[i],spikes);
			}
			if (!flag) {
				result = remove_move(moves,moves[i]);
			}
			i = i + 1;
		}
		return result;
	}


	public boolean check_part_two(Spike[] spikes) { //checks if any collecting corners are full
		int start = (number - 1) * 18 + 1;
		int finish = start + 5;
		int s = start;
		int pieces = 0;
		while (s <= finish) {
			if (spikes[s].player_number == number) {
				pieces = pieces + spikes[s].number_of_pieces;
			}
			s = s + 1;
		}
		pieces = pieces + removed_pieces;
		// System.out.println(pieces);
		return pieces == total_pieces;
	}






/////the main function
	public void take_turn(Spike[] spikes, int die1, int die2) throws InterruptedException {
		if (check_part_two(spikes)) {
				part = 2;
			}
			else {
				part = 1;
			}
		moves = usable_moves(initial_moves(die1,die2), spikes);
		while (moves.length > 0) {
			if (check_part_two(spikes)) {
				part = 2;
			}
			else {
				part = 1;
			}
			// System.out.println(part);
			use_move(spikes);
		}
	}






//Selecting and moving
	public void use_move(Spike[] spikes) throws InterruptedException {
		while (!moved) { //exits when you move a piece
			move_or_not(select_spike(spikes), spikes);
			check_selected = false;
		}
		moved = false;
	}
	public boolean move_or_not(int origin, Spike[] spikes) throws InterruptedException {
		HashSet<Integer> possible_numbers = new HashSet<Integer>(); //possible block numbers 
		int edge = 25*(number - 1);
		for (int i : moves) {
			possible_numbers.add((origin + direction * i));
		}
		while (check_mouse_touching(spikes[origin])) {
			Thread.sleep(100);
		}
		// System.out.println(possible_numbers);
		Integer[] pnumbers = possible_numbers.toArray(new Integer[possible_numbers.size()]);
		int first_move = pnumbers[0];
 		while (check_if_any_possible(pnumbers, spikes)) {
			if (StdDraw.mousePressed()) {
				// System.out.println(possible_numbers)
				int mv;
				for (int i : possible_numbers) {
					if (i < 0 || i > 25) {
						if (check_part_two_exception(origin, direction*(i-origin),spikes)) {
							if (check_mouse_touching(spikes[edge])) {
								mv = direction * (i -  origin);
								move(spikes[origin], spikes[edge], mv);
							}
						}
						else {
							continue;
						}
					}
					else if (check_mouse_touching(spikes[i])) {
						mv = direction * (i -  origin);
						move(spikes[origin], spikes[i], mv);
					}	
				}
				for (int i : possible_numbers) {
					if (i < 0 || i > 25) {
						if (spikes[edge].possibled) {
							undisplay_as_possible(spikes[edge],spikes);
						}
					}
					else {
						if (spikes[i].possibled) {	
							undisplay_as_possible(spikes[i], spikes);
						}
					}
				}
				display_all(spikes);
				return true;
			}
			Thread.sleep(100);
		}
		return false;
	}
	public boolean check_if_any_possible(Integer[] pnums, Spike[] spikes) { //checks if its possible to move - move_or_not helper

		for (int n : pnums) {
			if (spikes[0].possibled || spikes[25].possibled) {
				return true;
			}
			if (n < 0 || n > 25) {
				continue;
			}
			if (spikes[n].possibled) {
				return true;
			}
		}
		return false;
	} 
	public void print_moves(int[] moves) {
		// System.out.println("[");
		// for (int i : moves) {
		// 	System.out.println(i);
		// }
		// System.out.println("]");
	}
	public void move(Spike s, Spike s1, int mv) {
		if (s1.player_number != number) { //if the player is "eating" another piecex
			jail(s1, other);
			s1.player_number = number;
			
		}
		if (jailed) {
			prison.number_of_pieces = prison.number_of_pieces - 1;
			if (prison.number_of_pieces == 0) {
				jailed = false;
			}
		}
		s.number_of_pieces = s.number_of_pieces - 1;
		s1.number_of_pieces = s1.number_of_pieces + 1;
		print_moves(moves);
		moves = remove_move(moves, mv);
		moved = true;
		owned.add(s1.block_number);
		if (s1.block_number == 0 || s1.block_number == 25) {
			s1.number_of_pieces = s1.number_of_pieces - 1;
			removed_pieces = removed_pieces + 1;
		}
		if (s.number_of_pieces == 0) {
			owned.remove(s.block_number);
		}
	} 
	public void jail(Spike sp, Player o) { //puts a piece in prison
		o.jailed = true;
		o.owned.remove(sp.block_number);
		sp.number_of_pieces = sp.number_of_pieces - 1;
		other.prison.number_of_pieces = other.prison.number_of_pieces + 1;

	}
	public boolean gele(int[] moves, Spike[] spikes) {
		int o = (number % 2) * 25;
		if (jailed) {
			for (int m : moves) {
				if (check_legality(o,m,spikes)) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	public int select_spike(Spike[] spikes) throws InterruptedException { //only finishes running when a spike is selected
		if (jailed) {
			check_selected = true;
			prison.highlight();
			display_possible_moves(spikes, spikes[get_jail_origin(prison)]);
			return get_jail_origin(prison);
		}
		while (!check_selected) {
			for (int i : owned) {
				if (check_mouse_touching(spikes[i])) {
					spikes[i].highlight();
					while (check_mouse_touching(spikes[i])) {
						if (StdDraw.mousePressed()) {
							check_selected = true;
							display_possible_moves(spikes, spikes[i]);
							return i;
						}
						Thread.sleep(100);
					}
					spikes[i].unhighlight();
				}
			}
			Thread.sleep(100);
		}
		return -20;  //this was just to make the compiler happy -
	}


//// Prison methods
	public int get_jail_origin(Spike pris) {
		return (number % 2) * 25;
	}





//Display functions
	public void redisplay_helper(int section, Spike[] spikes) {
		int start = (section - 1)*6 + 1;
		int finish = start + 5;
		int i = start;
		while (i <= finish) {
			spikes[i].display();
			i = i + 1;
		}
	}

	public void redisplay(Spike s, Spike[] spikes) {
		String image = "images/board-" + Integer.toString(s.section) + ".jpg";
		if (s.section == 1) {
			StdDraw.picture(.73,.33,image);
			redisplay_helper(s.section, spikes);
		}
		if (s.section == 2) {
			StdDraw.picture(.277,.33,image);
			redisplay_helper(s.section, spikes);
		}
		if (s.section == 3) {
			StdDraw.picture(.28,.697,image);
			redisplay_helper(s.section, spikes);
		}
		if (s.section == 4) {
			StdDraw.picture(.732,.695,image);
			redisplay_helper(s.section, spikes);
		}
	}

	public boolean check_mouse_touching(Spike s) {
		double x1 = StdDraw.mouseX();
		double y1 = StdDraw.mouseY();
		double x2 = s.xpos;
		double y2 = s.get_top();
		if ((((x2-x1)*(x2-x1) + (y2 -y1)*(y2-y1)) < s.piece_radius*s.piece_radius)) {
			return true;
		}
		else {
			return false;
		}
	}
	public boolean display_possible_moves(Spike[] spikes, Spike original) {
		int origin = original.block_number;
		int direction = 2*number - 3;
		if (moves.length > 1) {
			if (moves[0] == moves[1]) {
				if (check_legality(origin, moves[0],spikes)) {
					int m = moves[0];
					int dest = origin + direction*m;
					if (dest > 25 || dest < 0) {
						display_as_possible(spikes[25*(number -1)]);
					}
					else {
						display_as_possible(spikes[origin + direction*m]);
					}
					return true;
				}
				else {
					return false;
				}
			} 
		}
		for (int m : moves) {
			if (check_legality(origin, m, spikes)) {
				// System.out.println(m);
				int dest = origin + direction*m;
				if (dest > 25 || dest < 0) {
					display_as_possible(spikes[25*(number -1)]);
				}
				else {
					display_as_possible(spikes[origin + direction*m]);
				}
			}
		}

		//System.out.println("done!");
		return true;
	}
	public void display_all(Spike[] spikes) {
		String board = "images/board.jpg";
		StdDraw.picture(.51,.51, board);
		for (Spike s : spikes) {
			s.display();
		}
		prison.display();
		other.prison.display();
	}

	/// could be moved to spike class
	public void display_as_possible(Spike s) {
		if (!(s.number_of_pieces == 1 && s.player_number != number)) { //cases except taking piece
			s.player_number = number;
		}
		s.possible = true;
		s.possibled = true;
		s.number_of_pieces = s.number_of_pieces + 1;
		// System.out.println(s.number_of_pieces);
		if (s.block_number == 0 || s.block_number == 25) {
			s.number_of_pieces = 1;
		}
		s.display();
		
	}
	public void undisplay_as_possible(Spike s, Spike[] spikes) {  ////does not redisplay!!
		s.possible = false;
		s.possibled = false;
		s.number_of_pieces = s.number_of_pieces - 1;
		if (s.number_of_pieces == 0) {
			s.player_number = 0;
		}
	}
}
