package mazeDevelopmentPackage;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class MazeGenerator extends JFrame {

    //The container to hold the JPanels in for the GUI
    Container container;

    //Used to fill in spots for the maze
    public int[][] maze;

    //Cell size and how big each cell should be on the JPanel
    public int cellSize = 30, mazeSize = 20;

    /*
     *   Creates the constructor of the maze. This includes:
     *
     *   - Creating a template with default dimensions
     *   - Sets up proper closing operations for the GUI
     *   - Configures visibility of the GUI
     */
    public MazeGenerator() {
        super();

        setTitle("Maze Generator");
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(900, 800));
        setLayout(new GridLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        container = getContentPane();

        JPanel mazeTitle = new TitlePanel();
        container.add(mazeTitle);

        container.setLayout(new GridLayout());
        setVisible(true);
        pack();
    }

    /*
     *   Creates the title panel for the project. Features:
     *
     *   - Title Text
     *   - Old Maze Generation
     *   - New Maze Generation
     *   - Quit Button
     */

    class TitlePanel extends JPanel {

        private final JButton quitButton, oldMaze, newMaze;

        public TitlePanel() {
            super();
            setPreferredSize(new Dimension(800, 800));
            setLayout(new GridLayout(4,1));
            setBackground(Color.black);
            ButtonActionHandler theActionListener = new ButtonActionHandler();

            /* Creates three buttons for the user to interact with the maze
             *
             *   oldMaze - A button that creates a new instance of the originally constructed maze
             *   newMaze - A button that creates a new instance of the newly implemented algorithm
             *   quitButton - A button that quits the application
             */

            oldMaze = new JButton("Maze With Algorithmic Bias");
            oldMaze.setSize(200, 50);
            oldMaze.addActionListener(theActionListener);
            oldMaze.setBackground(Color.GREEN);

            newMaze = new JButton("Maze Without Extreme Biases");
            newMaze.setSize(200, 50);
            newMaze.addActionListener(theActionListener);
            newMaze.setBackground(Color.cyan);

            quitButton = new JButton("Quit Button");
            quitButton.setSize(200, 50);
            quitButton.addActionListener(theActionListener);
            quitButton.setBackground(new Color(195, 155, 119));

            JLabel titleText = new JLabel("Maze Generator");
            titleText.setForeground(Color.lightGray);
            titleText.setHorizontalAlignment(JLabel.CENTER);
            titleText.setFont(new Font("Times New Roman", 1, 50));

            // Adds the JButtons and the Title Text to the JPanel

            add(titleText);
            add(oldMaze);
            add(newMaze);
            add(quitButton);

            setVisible(true);
        }

        // Creates an ActionListener object that reacts when a
        // button is pressed.

        class ButtonActionHandler implements ActionListener {

            @Override
            public void actionPerformed(ActionEvent evt) {
                Object source = evt.getSource();
                if (source == quitButton) {
                    System.exit(0);
                } else if (source == oldMaze) {
                    System.out.println("startingGame()");
                    generateMaze(0);
                }else{
                    generateMaze(1);
                }
            }

            public void generateMaze(int which) {
                setVisible(false);
                JPanel mazePanel;
                container.removeAll();
                mazePanel = which == 0 ? new OldMazeModel() : new NewMazeModel();
                container.add(mazePanel);
                setVisible(true);
            }
        }

    }

    /*
     *   Creates an instance of this when the user requests for a hard maze panel
     *
     *   How the new model works:
     *
     *       The program creates a grid where it keeps track of the various plots visited.
     *       There will be a position that is kept track, and whenever the position visits
     *       a new random adjacent location, it will create a tunnel between the new location and the
     *       old location if it has not been visited yet. If it has already been visited, then
     *       the program attempts to move towards another plot on the grid. This process is
     *       done arbitrarily until all plot grids have been filled and connected to the main
     *       maze. The program also restricts going out of bounds.
     *
     */

    class NewMazeModel extends JPanel {

        private final JButton backButton;

        public boolean[][] visited;
        private int c;
        private int r;
        private int randomNext;

        //Creates the backdrop for the JPanel
        public NewMazeModel() {
            setBackground(Color.BLACK);
            setPreferredSize(new Dimension(800, 800));
            setLayout(new BorderLayout());

            ButtonActionHandler theActionListener = new ButtonActionHandler();
            backButton = new JButton("Back Button");
            backButton.setSize(200, 50);
            backButton.addActionListener(theActionListener);
            backButton.setBackground(new Color(195, 155, 119));

            add(backButton, BorderLayout.SOUTH);
            pack();
        }

        //checks to see how many cells have been left unvisited
        private int AmountLeft() {
            int cnt = mazeSize * mazeSize;
            for (boolean[] i : visited) {
                for (boolean j : i) {
                    if (j) {
                        cnt--;
                    }
                }
            }
            return cnt;
        }

        //Generates a random direction for the visitor to move around in
        private void Randomize() {
            randomNext = (int) (Math.random() * 4);
        }

        //Checks to see if the determined location has already been visited
        private boolean CheckAvailability(int row, int col) {
            return visited[row][col];
        }

        //Destroys a wall depending on the relative direction
        private void Carve(int direction, Graphics g) {
            CheckBounds();
            if(direction == 3){
                g.fillRect(101 + (c - 1) * cellSize, 101 + r * cellSize, cellSize + 2, cellSize - 2);
            }else if (direction == 2){
                g.fillRect(101 + c * cellSize, 98 + (r + 1) * cellSize, cellSize - 2, cellSize + 2);
            }else if(direction == 1){
                g.fillRect(101 + c * cellSize, 101 + r * cellSize, cellSize + 2, cellSize - 2);
            }else{
                g.fillRect(101 + c * cellSize, 98 + r * cellSize, cellSize - 2, cellSize + 2);
            }
            Move(direction);
            visited[r][c] = true;
        }

        //Moves the visitor in a relative direction
        private void Move(int direction) {
            if(direction == 3){
                c--;
            }else if (direction == 2){
                r++;
            }else if (direction == 1){
                c++;
            }else{
                r--;
            }
            CheckBounds();
        }

        //Checks to make sure that the visitor never goes out of bounds
        private void CheckBounds() {
            if (c < 0) {
                c = 0;
            } else if (c >= mazeSize) {
                c = mazeSize - 1;
            } else if (r < 0) {
                r = 0;
            } else if (r >= mazeSize) {
                r = mazeSize - 1;
            }
        }

        //STARTS THE DRAWING HERE

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            maze = new int[mazeSize][mazeSize];
            g.setColor(Color.cyan);
            for (int row = 0; row < maze.length; row++) {
                for (int col = 0; col < maze[row].length; col++) {
                    g.drawRect(100 + row * cellSize, 100 + col * cellSize, cellSize, cellSize);
                }
            }
            g.setColor(Color.BLACK);
            visited = new boolean[mazeSize][mazeSize];
            c = (int) (Math.random() * mazeSize - 1);
            r = (int) (Math.random() * mazeSize - 1);
            visited[r][c] = true;


            //While there are unvisited cells, move in a random direction. If it is unvisited, carve it out. Otherwise just move there only.

            while (AmountLeft() != 0) {
                Randomize();
                if(randomNext == 3){
                    if (c != 0) {
                        if (!CheckAvailability(r, c - 1)) {
                            Carve(randomNext, g);
                        } else {
                            Move(randomNext);
                        }
                    }
                }else if (randomNext == 2){
                    if (r != mazeSize - 1) {
                        if (!CheckAvailability(r + 1, c)) {
                            Carve(randomNext, g);
                        } else {
                            Move(randomNext);
                        }
                    }
                }else if (randomNext == 1){
                    if (c != mazeSize - 1) {
                        if (!CheckAvailability(r, c + 1)) {
                            Carve(randomNext, g);
                        } else {
                            Move(randomNext);
                        }
                    }
                }else if(randomNext == 0){
                    if (r != 0) {
                        if (!CheckAvailability(r - 1, c)) {
                            Carve(randomNext, g);
                        } else {
                            Move(randomNext);
                        }
                    }
                }else{
                    throw new IndexOutOfBoundsException("random is producing a value of " + randomNext);
                }
            }

            //Generates a preset of a start and finish position

            int tmpX = (int) (Math.random() * 4);
            int tmpY = (int) (Math.random() * mazeSize);
            g.setColor(Color.GREEN);
            g.fillOval(110 + tmpX * cellSize, 110 + tmpY * cellSize, 10, 10);
            tmpX = mazeSize - 1 - (int) (Math.random() * 4);
            tmpY = (int) (Math.random() * mazeSize);
            g.setColor(Color.RED);
            g.fillOval(110 + tmpX * cellSize, 110 + tmpY * cellSize, 10, 10);
        }

        class ButtonActionHandler implements ActionListener {

            @Override
            public void actionPerformed(ActionEvent evt) {
                setVisible(false);
                container.removeAll();
                JPanel mazeTitle = new TitlePanel();
                container.add(mazeTitle);
                setVisible(true);
            }
        }

    }

    //Creates an instance of this when the user requests for an easy maze panel
    class OldMazeModel extends JPanel {

        private final JButton backButton;

        //Creates the JPanel background

        public OldMazeModel() {
            setBackground(Color.BLACK);
            setPreferredSize(new Dimension(800, 800));
            setLayout(new BorderLayout());

            ButtonActionHandler theActionListener = new ButtonActionHandler();
            backButton = new JButton("Back Button");
            backButton.setSize(200, 50);
            backButton.addActionListener(theActionListener);
            backButton.setBackground(new Color(195, 155, 119));

            add(backButton, BorderLayout.SOUTH);
            pack();

        }

        //Creates the grid and performs a binary decision-making method

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            maze = new int[mazeSize][mazeSize];
            g.setColor(Color.GREEN);
            for (int row = 0; row < maze.length; row++) {
                for (int col = 0; col < maze[row].length; col++) {
                    g.drawRect(100 + row * cellSize, 100 + col * cellSize, cellSize, cellSize);
                }
            }

            g.setColor(Color.BLACK);
            for (int col = 0; col < maze.length; col++) {
                for (int row = 0; row < maze[col].length; row++) {
                    if (col == maze.length - 1 && row == 0) {
                    } else {
                        if (row == 0 && col != maze.length - 1) {
                            g.fillRect(101 + col * cellSize, 101 + row * cellSize, cellSize + 2, cellSize - 2);
                            maze[row][col] = 1;
                        } else if (col == maze.length - 1 && row != 0) {
                            g.fillRect(101 + col * cellSize, 98 + row * cellSize, cellSize - 2, cellSize + 2);
                            maze[row][col] = 2;
                        } else {
                            int temp = (int) (Math.random() * 10);
                            if (temp > 4) {
                                g.fillRect(101 + col * cellSize, 101 + row * cellSize, cellSize + 2, cellSize - 2);
                                maze[row][col] = 1;
                            } else {
                                g.fillRect(101 + col * cellSize, 98 + row * cellSize, cellSize - 2, cellSize + 2);
                                maze[row][col] = 2;
                            }
                        }
                    }
                }
            }

            //Generates a preset of a start and finish position

            int tmpX = (int) (Math.random() * 4);
            int tmpY = (int) (Math.random() * mazeSize);
            g.setColor(Color.GREEN);
            g.fillOval(110 + tmpX * cellSize, 110 + tmpY * cellSize, 10, 10);
            tmpX = mazeSize - 1 - (int) (Math.random() * 4);
            tmpY = (int) (Math.random() * mazeSize);
            g.setColor(Color.RED);
            g.fillOval(110 + tmpX * cellSize, 110 + tmpY * cellSize, 10, 10);
        }

        class ButtonActionHandler implements ActionListener {

            @Override
            public void actionPerformed(ActionEvent evt) {
                setVisible(false);
                container.removeAll();
                JPanel mazeTitle = new TitlePanel();
                container.add(mazeTitle);
                setVisible(true);
            }
        }

    }

}
