package mazeDevelopmentPackage;
/*
*
*   This was a beginner software project that was created in the past year to grasp a greater understanding on the fundamentals towards algorithms.
*   From this project I explored two separate algorithms that were used to develop two types of mazes, and provided an insight towards
*   the time and bias the end result of the maze had.
*
*   The navigation of this project is simple. On startup, there will be three buttons with their function names on the button. The first
*   two feature two types of mazes, the first being one with an algorithmic bias but faster to construct programmatically, whereas the
*   second one has less of a bias, but requires more computational power to construct. The quit button at the bottom exits the application.
*
*   Each time the button is pressed, the maze generates itself based on functions built in the  program. In order to navigate back to the main
*   page, click the brown button at the bottom of the application.
*
*   The green and red dots within the maze are in case of the user wishing to complete a sample of the maze themselves.
*
* */

import mazeDevelopmentPackage.MazeGenerator;

public class MazeGeneratorRunner {

    public static void main(String[] args) {

        //Creates a new instance of the maze

        MazeGenerator newMaze = new MazeGenerator();
    }

}
