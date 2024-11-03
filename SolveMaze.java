import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;

class SolveMaze{
  /**
   * Checks if the maze is solvable and solves it if yes, highlighting the path 
   * @param maze
   * @param current
   * @return true or false
   */
  public static boolean mazeTracker(Maze maze, MazeLocation current){
    try { Thread.sleep(10);} catch (InterruptedException e) {}; //animation to highlight the process

    //initializes important fields--- start, finish, rowCurrent, and columCurrent
    MazeLocation start = maze.getStart();
    MazeLocation finish = maze.getFinish();
    int rowCurrent = current.getRow();
    int columnCurrent = current.getCol();
    
    //Stop conditions--- specify the conditions under which the loop should return 
    //Condition 1: If current location is the finish, mark the location as PATH and return True
    if (current.equals(finish)){
      maze.mazeGrid[rowCurrent][columnCurrent] = MazeContents.PATH;
      System.out.println("The path has been found to be " + current.toString());
      return true;
    }
    //Condition 2: If the current location is not explorable, return False
    if (!(maze.checkExplorable(rowCurrent, columnCurrent))){
      System.out.println("Not explorable: " + current.toString());
      return false;
    }

    //Recursive steps
    maze.mazeGrid[rowCurrent][columnCurrent] = MazeContents.VISITED; //Mark the current location as VISITED
    //Stores the different neighbouring cells at specific variables
    MazeLocation north = current.neighbor(MazeDirection.NORTH);
    MazeLocation south = current.neighbor(MazeDirection.SOUTH);
    MazeLocation west = current.neighbor(MazeDirection.WEST);
    MazeLocation east = current.neighbor(MazeDirection.EAST);
    //recursively calls the neighboring cells and returns true if the path is found via any of them
    if (mazeTracker(maze, north) || mazeTracker(maze, south) || mazeTracker(maze, west) || mazeTracker(maze, east)){
      maze.mazeGrid[rowCurrent][columnCurrent] = MazeContents.PATH;
      return true;
    }  
    //returns False if no path exists in the maze after checking all cells
    else{
      maze.mazeGrid[rowCurrent][columnCurrent] = MazeContents.DEAD_END;
      System.out.println("This maze does not have an exit.");
      return false;
    }
  }


  /**
   * reads in the file containing the maze to be solved
   * @param fname
   * @return
   */
  public static Scanner readMaze(String fname){
    Scanner file = null;
    try {
      file = new Scanner(new File(fname));
    } catch (FileNotFoundException e) {
      System.err.println("Cannot locate file.");
      System.exit(-1);  
    }
    return file;
  }


  /**
   * Sets a given cell in the mazeGrid to a MazeContent enum type
   * @param maze
   * @param code
   * @param row
   * @param column
   */
  public static void setContent(Maze maze, String code, Integer row, Integer column){
    //index into the mazegrid cell marked by row, column and set to a specific enum type depending on the char
    switch (code){
      case "#":
        maze.mazeGrid[row][column] = MazeContents.WALL;
      case ".":
        maze.mazeGrid[row][column] = MazeContents.OPEN;
      case " ":
        maze.mazeGrid[row][column] = MazeContents.OPEN;
      case "S":
        maze.setStart(row, column);
      case "F":
        maze.setFinish(row, column);
    }
  }
  

  public static void main(String[] args) {
    //Reads in the file containing the maze
    if(args.length <= 0){
      System.err.println("Please provide the name of the maze file.");
      System.exit(-1);
    }
    Scanner file = readMaze(args[0]);

    //converts the data in the maze file to an array of arrays
    ArrayList<char []> newArray = new ArrayList<>(); //create a new "container" arraylist 
    //loop through file using its iterator
    while (file.hasNextLine()){
      char[] subArray = file.nextLine().toCharArray(); //convert each line to a character array
      newArray.add(subArray); //convert the next line to an array and add to the new_array
    }
    
    //stores the array of arrays created in the previous step in a maze grid
    Maze mazeSample = new Maze(); //initializes the new maze instance
    mazeSample.height = newArray.size();
    mazeSample.width = newArray.get(0).length;

    mazeSample.mazeGrid = new MazeContents[mazeSample.height][mazeSample.width]; //initializes the mazeGrid variable of the created maze
    //loop through the rows
    for (int i = 0; i < newArray.size(); i++){
      //loop through the columns
      for (int j = 0; j < newArray.get(0).length; j++){
        setContent(mazeSample, String.valueOf(newArray.get(i)[j]), i, j); //set the content of row,column in the mazeGrid to the appropriate MazeContent
      }
    }

    //call the mazeTracker algorithm the maze to solve it
    SolveMaze.mazeTracker(mazeSample, mazeSample.getStart());
    //MazeViewer viewer = new MazeViewer(mazeSample);

  }
}
