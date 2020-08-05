import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;


public class StartOne extends JFrame {

    private JPanel contentPane;

    private static int count = 0; //this determines if the player is setting the initial coordinate or playing a battle.
    private JPanel sidePanel;
    private JTextField textField;
    private static boolean isPlayer = true; // Used to manage input turns for Player/Opponent

    public StartOne() {
        
        final JButton[][] grid = new JButton[5][5]; // coordinate at each JButton object
        final JTextArea txtrTxtbox = new JTextArea(); 
        getContentPane().setLayout(null); 
        /*
         There is one pane and one panel. In pane, there is no specific layout. 
         For the panel within the pane, it uses box layout, while the pane has no layout (null). 
         The pane must be null because then I can set a buttons flexibly, while the panel (sidePanel) uses boxlayout
         in order to fit the box scroll pane correctly
         */
        
        
        final LinkedList<Move> playerMoves = new LinkedList<Move>() {// player moves and its result
            @Override
            public boolean add(Move m) {// this is possible because txtrTxtbox is final and is on the parent scope
                txtrTxtbox.append("Player's move: " + m.x + "," + m.y + "\n");
                return super.add(m);
            }
        };
        // opponent moves and its result
        final LinkedList<Move> opponentMoves = new LinkedList<Move>() {
            @Override
            public boolean add(Move m) {  // this is possible because txtrTxtbox is final and is on the parent scope
                txtrTxtbox.append("Opponente's move: " + m.x + "," + m.y + "\n");
                return super.add(m);
            }
        };
        
        // player & opponent ship location
        // Initialized with negative values, so further on we can check if playerShip is set or not 
        final Cell playerShip = new Cell(-1, -1);
        final Cell opponentShip = new Cell(-1, -1);

    
       
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 521, 352);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane); //initializing the pane
        
        contentPane.setLayout(null);
        
        int maxX = 5; //number of cells can change flexibly if interval changes
        int maxY = 5;
        int startX = 38;
        int intervalX = 46;
        int intervalY = 41;
        for (int y = 0; y < maxY; y++) {
            for (int x = 0; x < maxX; x++) {
                JButton gridD = new JButton(""); //generates a button object at a certain coordinate defined below
                gridD.setBounds(startX + (x * intervalX), startX + (y * intervalY), 32, 29);
                grid[y][x] = gridD;
                contentPane.add(gridD);
            }
        }
        
        sidePanel = new JPanel();
        sidePanel.setBounds(270, 25, 232, 265);
        contentPane.add(sidePanel);
                sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.PAGE_AXIS));
                
                
                txtrTxtbox.setLineWrap(true);
                txtrTxtbox.setText("Welcome to Battleship!" + "\n");
                txtrTxtbox.append("Player, please insert your initial " + "\n" + " coordinates. For instance, if you " + "\n" + " wish your initial coordinate to be at 3,2, then insert '3,2'"+"\n");
                txtrTxtbox.setAlignmentX(Component.LEFT_ALIGNMENT);
                
                JScrollPane scrollpane = new JScrollPane(txtrTxtbox);
                sidePanel.add(scrollpane); //enables to scroll the text box
                
                textField = new JTextField();
                textField.setBounds(16, 296, 134, 28);
                contentPane.add(textField);
                textField.setColumns(10); //text field to interact with users
                
                JButton btnNewButton = new JButton("Go");
                btnNewButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) { //once user puts an input and presses "Go", it performs
                        String str = textField.getText(); //convert into a string
                    	try{
                    		Character.getNumericValue(str.charAt(0));
                    		Character.getNumericValue(str.charAt(2)); //AWT-EventQueue-0 would return if there is no input
                    	}catch(Exception ex){
                    		txtrTxtbox.append("No input observed\n");
                    	}
                        int c1 = Character.getNumericValue(str.charAt(0)); 
                        int c2 = Character.getNumericValue(str.charAt(2));//interprets the first and second characters of user input
                        if(c1>=0&&c1<=4&&c2>=0&&c2<=4){ //verify that the user input is an integer in the range
                            count++; //odd: Player chooses the initial coordinate, even: Opponent does so
                            if(count==1){  //chooses the initial coordinate of the player
                                //playerShip is shared as final on parent 
                               	//calls the object to set the initial coordinate
                                playerShip.x = c1;
                                playerShip.y = c2;
                                txtrTxtbox.append("Player's coordinate is at (*,*)\nOpponent, please insert the next line\n");
                             }else if(count==2){ //chooses the initial coordinate of the opponent
                                 //opponentShip is shared as final on parent
                                 opponentShip.x = c1;
                                 opponentShip.y = c2;
                                 txtrTxtbox.append("Opponent's coordinate is at (*,*)" + "\n");
                                 txtrTxtbox.append("Player: Please insert your next line" + "\n");
                             }
                            else{ 
                                LinkedList<Move> currentMoves = isPlayer ? playerMoves : opponentMoves;
                                //deciding which player's move to store
                                Cell currentEnemyShip = isPlayer ? opponentShip : playerShip;
                                if (c1 == currentEnemyShip.x && c2 == currentEnemyShip.y) { //hit 
                                    currentMoves.add(new Move(c1, c2, true));
                                    txtrTxtbox.setText("Congrats "+(isPlayer ? "Player" : "Opponent" )+ ", you won!" + "\n");
                                }else { //miss
                                    currentMoves.add(new Move(c1, c2, false)); //add the coordinate to the linked list to store missed hits 
                                    if(c1==currentEnemyShip.x-1||c1==currentEnemyShip.x+1||c2==currentEnemyShip.y-1||c2==currentEnemyShip.y+1){
                                    	txtrTxtbox.append("What a shame! That was close!! \n" );
                                    }
                                    txtrTxtbox.append((isPlayer ? "Opponent: " : "Player: " ) + "Please insert your next line" + "\n");
                                }
                            }
                            textField.setText(""); // Clears textField
                            for(int i=0;i<5;i++){
                            	for(int j=0; j<5;j++){
                            		grid[i][j].setText(""); //clear out all text
                            	}
                            }
                            isPlayer = !isPlayer; // Flip isPlayer
                    		int lv = isPlayer ? playerMoves.size() : opponentMoves.size(); //size of the linked list
                    		for(int i=0;i<lv;i++){
                    			if(isPlayer){
                        			grid[playerMoves.get(i).x][playerMoves.get(i).y].setText("X"); 
                    			}else{
                    				grid[opponentMoves.get(i).x][opponentMoves.get(i).y].setText("X");
                    			} //each player's mistaken grids are set as "X" from the linked list
                    		}
                    		
                        }
                    }
                    
                });
                btnNewButton.setBounds(153, 297, 71, 29);
                contentPane.add(btnNewButton);
    }
    
}
