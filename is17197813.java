import javax.swing.JOptionPane;
import java.util.*;

/**
 * A* search project by:
 * @author Dylan King - 17197813
 * @author Louise Madden - 17198232
 * @author Szymon Sztyrmer - 17200296
 */
public class is17197813
{
	private static int [] end;
	private static int puzzleSize = 0;
	/**
	 * The main driver for the project I guess
	 */
	public static void main(String[] args)
	{
		// Variable space as usual
		int valid = 0;
		int control = 0;
		int [] start;
		int [] current;
		String state = "";

		// Make an object of validator type because I can
		validator validator = new validator();

		welcome();
		// Get the puzzle type for this
		choosePuzzle();
		start = new int [puzzleSize];
		end = new int [puzzleSize];
		// Get the user input for the start state and end state
		while(valid < 3)
		{
			// Get the start state 0 is start
			state = enterState(valid);
			// Now to validate the start state
			if(state.equals(""))
				JOptionPane.showMessageDialog(null, "At least put some numbers in will you??");	
			else if(state.equals("quit"))
				break;
			else
			{
				control = validator.checkInput(puzzleSize, state);
				valid += control;
			}

			if(valid == 1)
			{
				start = converter(state);
				valid++;
			}
			if(valid == 3)
				end = converter(state);
		}
		// Okay I think I'm done with validation...
		// Feel free to use start and end which are int arrays containing the inputed start and end states.
		current = Arrays.copyOf(start, start.length);
		Node currentNode = new Node(current, 0);
		ArrayList<Node> open = new ArrayList<Node>();
		ArrayList<Node> closed = new ArrayList<Node>();

		open.add(currentNode);
		Node finalState = aStar(currentNode, end, open, closed, null);

		if(!Arrays.equals(start, end))
		{
			int rows = (int)Math.sqrt((current.length));
			System.out.print(currentNode.toString());
		}
		else
		{
			JOptionPane.showMessageDialog(null, "Congratulations! You got to the end");
		}
	}

	/**
	 * Just a simple greeting message to be displayed at the start of the program
	 * @author Szymon Sztyrmer
	 */
	public static void welcome()
	{
		JOptionPane.showMessageDialog(null, "Welcome to our A* search  project." +
																	"\nMade by:" + 
																	"\nDylan King - 17197813" +
																	"\nLouise Madden - 17198232" +
																	"\nSzymon Sztyrmer - 17200296" + 
																	"\nClick ok to continue.", "Greetings", 1);
	}

	/**
	 * The method responsible for allowing the user to choose between the 8 and 15 puzzles
	 * @return int puzzleSize -> is it the 8-puzzle or the 15-puzzle?
	 * @author Szymon Sztyrmer
	 */
	public static void choosePuzzle()
	{
		// Displays some lovely buttons to select from
		String [] puzzleTypes = {"8-Puzzle", "15-Puzzle"};
		puzzleSize = JOptionPane.showOptionDialog(null, "Would you like to use the 8-Puzzle or the 15-Puzzle?",
												 "Puzzle Selector", JOptionPane.YES_NO_OPTION,
												 JOptionPane.QUESTION_MESSAGE, null, puzzleTypes, puzzleTypes[0]);
	}

	/**
	 * The method asks the user to input either the
	 * start state or the end state depending on the
	 * parameter passed into the method
	 * @param int state -> the message to be displayed 
	 * @return String input -> the state returned to the main
	 * @author Szymon Sztyrmer
	 */
	public static String enterState(int state)
	{
		// Behold this beautiful turnory statement to differenciate between start and end
		String message = (state == 0) ? "Please enter the start state:" : "Please enter the end state";

		String input = JOptionPane.showInputDialog(null, message);
		if(input == null)
		{
			JOptionPane.showMessageDialog(null, "Process aborted.", "Abort Report", 0);
			return "quit";
		}
		return input;
	}

	/**
	 * This method converts the validated string input int an array of integers
	 * @param String state -> the validated string of numeric values
	 * @return int [] numbers -> the array of numbers
	 * @author Szymon Sztyrmer
	 */
	public static int [] converter(String state)
	{
		int [] numbers;
		String [] numbersString = state.split(" ");
		numbers = new int [numbersString.length]; 
		for(int i = 0; i < numbersString.length; i++)
			numbers[i] = Integer.parseInt(numbersString[i]);
		return numbers;
	}

	
	public static Node aStar(Node c, int[] g, ArrayList<Node> open, ArrayList<Node> closed, Node finalState)
	{
		if (c.equalsLayoutOnly(g))
		{
			try
			{
				finalState = (Node)c.clone();
			}
			catch (Exception e)
			{
				System.out.print(e.getMessage());
			}
			/* change to node finalNode = c
			 * DONE!
			 * call again if there is another state with a lower fscore
			 * DONE!
			 * if there is no lower fscore, return finalNode
			 * DONE!
			 */
			c = lowerFScore(finalState, open);
			if (!c.equals(finalState))
				return aStar(c, g, open, closed, finalState);
			else
				return finalState;
		}
		else
		{
			//move line 146 to before call
			// DONE!
			//here get children and add all to open
			//move c to closed
			//loop through open to check if child already in closed if true remove
			//and find the one with the lowest f score
			//recursion astar lowest fscore = c
			//the rest is the same
			// I'll start here I guess Ss

			closed.add(c);
			open.remove(open.indexOf(c));

			// Check if new child is in closed -> if true remove
			for(int i = open.size() - 1 - c.getNumberOfChildren(); i < open.size(); i++)
			{
				for(int j = 0; j < closed.size(); j++)
				{
					if(open.get(i).equalsLayoutOnly(closed.get(j).getLayout()))
					{
						if(open.get(i).getfScore() < closed.get(j).getfScore())
						{
							closed.remove(closed.indexOf(j));
						}
						else
						{
							open.remove(open.indexOf(i));
							// If i need to add it back to closed do it here
						}
					}
				}
			}
			c = lowerFScore(open.get(0), open);
			return aStar(c, g, open, closed, finalState);
		}
	}

	public static Node lowerFScore(Node aNode, ArrayList<Node> open)
	{
		Node newLowest = aNode;
		for (Node n : open)
			if (aNode.getfScore() > n.getfScore())
				newLowest = n;
		return newLowest;
	}

	public static int calculateHScore(int[] current)
	{
		int hScore = 0;
		for (int i = 0; i < current.length; i++)
		{
			int num = current[i];
			int goalIndex = findGoalIndex(num);
			hScore += findGoalDistance(i, goalIndex);
		}
		return hScore;
	}

	public static int findGoalDistance(int start, int end)
	{
		int score = 0;
		/* Compares the current index of the value in question with 
		   the goal index, based on the 1D array we use */
		int distance = Math.abs(end - start);
		// Checks if it's an 8-puzzle or 15-puzzle 
		if (puzzleSize == 0)
		{
			// if the goal is directly above/below/to the side
			if (distance == 1 || distance == 3)
				score = 1;

			/* covers all other cases except top-left <-> bottom-middle
			   calculates moving 2 spaces in any direction, and corner to corner */
			else if (distance == 2 || distance == 4 || distance == 6 || distance == 8)
			{
				// assumes the goal is two spaces away by default
				score = 2;

				// checks if the goal is in the opposite corner
				if ((start == 2 && end == 6) || (start == 6 && end == 2) || distance == 8)
					score = 4;
			}
			// covers final case: top left <-> bottom middle
			else if (distance == 7)
				score = 3;
		}
		else // For the 15-puzzle
		{
			if (distance == 1 || distance == 4)
				score = 1;
			else if (distance == 2 || distance == 3 || distance == 5 || distance == 6 || distance == 8)
			{
				score = 2;
				if ((start % 4 == 3 && end % 4 == 1) || (start % 4 == 0 && end % 4 == 2) ||
					(start % 4 == 1 && end % 4 == 3) || (start % 4 == 2 && end % 4 == 0) ||
					(start / 4 == end / 4) || distance == 6)
					score = 3;
			}
			else if (distance == 7)
			{
				score = 3;
				if ((start % 4 == 3 && end % 4 == 0) || (start % 4 == 0 && end % 4 == 3))
					score = 4;
			}		
			else if (distance == 9)
			{
				score = 3;
				if (start == 3 || start == 12)
					score = 6;
			}
			else if (distance == 10 || distance == 11)
			{
				score = 4;
				if ((start == 2 || start == 3 || end == 3 || end == 3) ||
					(start == 0 || start == 4 || end == 0 || end == 4))
					score = 5;
			}
			else if (distance == 12)
				score = 3;
			else if (distance == 13)
				score = 4;
			else if (distance == 14)
				score = 5;
			else if (distance == 15)
				score = 6;
		}
		return score;
	}

	public static int findGoalIndex(int n)
	{
		int goal = -1;
		boolean found = false;
		for (int i = 0; i < end.length && !found; i++)
		{
			if (end[i] == n)
			{
				goal = i;
				found = true;
			}
		}
		return goal;
	}

	/**
	 * This method prints the current state of the puzzle
	 * @param int[] the current state
	 * @param int rows so I know when to new line
	 * @author Louise Madden
	 */
	public static void printState(int current[], int rows)
	{
		for(int i = 0; i < current.length; i++)
		{
			if(current[i] != 0)
				System.out.print(current[i] + " ");
			else
				System.out.print("  ");

			if (i % (rows) == rows - 1)
				System.out.print("\n");
		}
	}

	/**
	 * Method to determne what directions are valid moves.
	 * Void for the moment until I figure out what I need to change it to if anything
	 * @param int[] current state because I cant get the next move if I don't know where I am now
	 * @param int rows, need to know the row size to know possible directions
	 * @author Louise Madden
	 */
	public static void validMoves(int current[], int rows)//change to pass in a node and open and closed
	{
		//stop printing
		//generate them all as new nodes
		//add them to the open list
		//remember to pass current in as the parent of the new nodes
		
		int zeroIndex = 0;
		
		for(int i = 0; i < current.length; i++)
		{
			if(current[i] == 0)
			{
				zeroIndex = i;
				break;
			}
		}
		char[] options = {'a', 'b', 'c', 'd'};
		int optionCount = 0;
		int[] layout = Arrays.copyOf(current, current.length);

		if(zeroIndex >= rows)
		{
			System.out.println("(" + options[optionCount] + ") " + current[zeroIndex - rows] + " to the south");
			optionCount++;
			layout[zeroIndex] = layout[zeroIndex - rows];
			layout[zeroIndex - rows] = 0;

			Node south = new Node(layout, 1);
			System.out.println("\th score = " + south.gethScore() + "\n");
		}

		if(zeroIndex <= (current.length - rows))
		{
			System.out.println("(" + options[optionCount] + ") "   + current[zeroIndex + rows] + " to the north");
			optionCount++;
			layout = Arrays.copyOf(current, current.length);
			layout[zeroIndex] = layout[zeroIndex + rows];
			layout[zeroIndex + rows] = 0;

			Node north = new Node(layout, 1);
			System.out.println("\th score = " + north.gethScore() + "\n");
		}

		if(zeroIndex % rows != (rows-1))
		{
			System.out.println("(" + options[optionCount] + ") "  + current[zeroIndex + 1] + " to the west");
			optionCount++;
			layout = Arrays.copyOf(current, current.length);
			layout[zeroIndex] = layout[zeroIndex + 1];
			layout[zeroIndex + 1] = 0;

			Node west = new Node(layout, 1);
			System.out.println("\th score = " + west.gethScore() + "\n");
		}

		if(zeroIndex % rows != 0)
		{
			System.out.println("(" + options[optionCount] + ") "  + current[zeroIndex - 1] + " to the east");
			optionCount++;
			layout = Arrays.copyOf(current, current.length);
			layout[zeroIndex] = layout[zeroIndex - 1];
			layout[zeroIndex - 1] = 0;

			Node east = new Node(layout, 1);
			System.out.println("\th score = " + east.gethScore() + "\n");
		}
	}
}

/**
 * An inner class that will be used to
 * do all the validation and checking of input
 * to keep this nice an object oriented
 * while keeping everything in 1 file... For whatever reason that was enforced...
 * @author Szymon Sztyrmer
 */
class validator
{
	// Private datafields area
	private int inputSize;
	private int limit;
	private String [] numbers;
	private String [] errorMessages;
	private String pattern;

	/**
	 * Constructor for the validator
	 * @author Szymon Sztyrmer
	 */
	public validator()
	{
		//System.out.println("I am the constructor,\nFear me\nROOOAAAARR!!"); just checking that this works and it does, yay!
		errorMessages = new String [] {"Sorry, it would appear that you did something wrong.\nIt's your fault not mine!", 
										"I mean you could do that...\nBut you could also follow the instructions I guess.",
										"Did your hand slip?\nI mean there is no way you typed that in deliberately right?",
										"You can keep on going trying to break this code.\nOr you could actually do what it asks."};
	}

	/**
	 * This method check that the input is of correct size and all numeric
	 * @param int puzzleSize -> type of puzzle
	 * @return boolean -> was it successful?
	 * @author Szymon Sztyrmer
	 */
	public int checkInput(int puzzleSize, String state)
	{
		// Another beautiful statement
		inputSize = (puzzleSize == 0) ? 9 : 16;
		pattern = (puzzleSize == 0) ? "[0-9]{1}" : "[0-9]{1,2}";
		limit = (puzzleSize == 0) ? 9 : 15;
		
		// Check for the correct number of elements
		numbers = state.split(" ");
		if(numbers.length != inputSize)
		{
			JOptionPane.showMessageDialog(null, errorMessages[(int)(Math.random() * errorMessages.length)], 
																"Size Mismatch Error", 0);
			return 0;
		}
		// Check that all are numeric
		for(int i = 0; i < numbers.length - 1; i++)
		{
			if(!(numbers[i].matches(pattern)) || Double.parseDouble(numbers[i]) > limit)
			{
				JOptionPane.showMessageDialog(null, errorMessages[(int)(Math.random() * errorMessages.length)], 
																	"Numeric Input Error", 0);
				return 0;
			}
			for(int j = i + 1; j < numbers.length; j++)
				if(numbers[i].equals(numbers[j]) || !(numbers[j].matches(pattern)))
				{
					JOptionPane.showMessageDialog(null, errorMessages[(int)(Math.random() * errorMessages.length)], 
																		"duplicated Input Error", 0);
					return 0;
				}
		}
		return 1;
	}
}

class Node
{
	private int[] layout;
	private int hScore, gScore, fScore;
	private ArrayList<Node> children;
	private Node previous;
	public Node(int[] layout, int gScore)
	{
		this.layout = layout;
		this.gScore = gScore;
		hScore = is17197813.calculateHScore(layout);
		fScore = gScore + hScore;
		children = new ArrayList<Node>();
		previous = null;
	}

	public Node(int[] layout, int gScore, Node previous)
	{
		this.layout = layout;
		this.gScore = gScore;
		hScore = is17197813.calculateHScore(layout);
		fScore = gScore + hScore;
		children = new ArrayList<Node>();
		this.previous = previous;
	}

	private Node(int[] layout, int gScore, int hScore, int fScore,
				 ArrayList<Node> children, Node previous)
	{
		this.layout = layout;
		this.gScore = gScore;
		this.hScore = hScore;
		this.fScore = fScore;
		this.children = children;
		this.previous = previous;
	}

	/**
	 * @return the gScore
	 */
	public int getgScore()
	{
		return gScore;
	}

	/**
	 * @return the fScore
	 */
	public int getfScore()
	{
		return fScore;
	}

	/**
	 * @return the hScore
	 */
	public int gethScore()
	{
		return hScore;
	}

	/**
	 * @return the layout
	 */
	public int[] getLayout()
	{
		return layout;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof Node)
			return gScore == ((Node)obj).getgScore() &&
				   hScore == ((Node)obj).gethScore() &&
				   Arrays.equals(layout, ((Node)obj).getLayout());
		else
			return false;
	}

	public boolean equalsLayoutOnly(int[] end)
	{
		return Arrays.equals(layout, end);
	}

	@Override
	protected Object clone() throws CloneNotSupportedException
	{
		return new Node(Arrays.copyOf(layout, layout.length), gScore,
						hScore, fScore, children, previous);
	}

	public String toString()
	{
		int[] current = layout;
		int rows = 0;
		String output = "";

		for(int i = 0; i < current.length; i++)
		{
			if(current[i] != 0)
				output += current[i] + " ";
			else
				output += "  ";

			if (i % (rows) == rows - 1)
				output += "\n";
		}

		return output;
	}

	public void generateChildren()
	{
		
	}

	/**
	 * @return the children
	 */
	public int getNumOfChildren()
	{
		return children.size();
	}
}