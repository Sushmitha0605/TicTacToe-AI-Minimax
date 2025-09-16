import java.util.*;

public class tictactoe {

    static char[][] board = new char[3][3];
    static Scanner sc = new Scanner(System.in);
    static int difficulty; // 1 = Easy, 2 = Medium, 3 = Hard

    // Scoreboard
    static int scoreX = 0, scoreO = 0, draws = 0;
    static List<String> history = new ArrayList<>();

    // Initialize board
    public static void initBoard() {
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                board[i][j] = ' ';
    }

    // Print board
    public static void printBoard() {
        System.out.println("-------------");
        for (int i = 0; i < 3; i++) {
            System.out.print("| ");
            for (int j = 0; j < 3; j++) {
                System.out.print(board[i][j] + " | ");
            }
            System.out.println("\n-------------");
        }
    }

    // Check if player wins
    public static boolean isWinner(char player) {
        for (int i = 0; i < 3; i++)
            if (board[i][0] == player && board[i][1] == player && board[i][2] == player) return true;
        for (int i = 0; i < 3; i++)
            if (board[0][i] == player && board[1][i] == player && board[2][i] == player) return true;
        if (board[0][0] == player && board[1][1] == player && board[2][2] == player) return true;
        if (board[0][2] == player && board[1][1] == player && board[2][0] == player) return true;
        return false;
    }

    // Check if board is full
    public static boolean isDraw() {
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (board[i][j] == ' ') return false;
        return true;
    }

    // Get valid move from player
    public static void playerMove(char currentPlayer) {
        while (true) {
            System.out.print("Player " + currentPlayer + ", enter row (0-2) and column (0-2): ");
            int row = sc.nextInt();
            int col = sc.nextInt();

            if (row < 0 || row > 2 || col < 0 || col > 2) {
                System.out.println("Invalid input! Please enter values between 0 and 2.");
                continue;
            }
            if (board[row][col] != ' ') {
                System.out.println("Cell already taken. Try again.");
                continue;
            }
            board[row][col] = currentPlayer;
            break;
        }
    }

    // AI Easy: Random move
    public static void aiRandomMove(char aiPlayer) {
        Random rand = new Random();
        int row, col;
        while (true) {
            row = rand.nextInt(3);
            col = rand.nextInt(3);
            if (board[row][col] == ' ') {
                board[row][col] = aiPlayer;
                System.out.println("AI (" + aiPlayer + ") chose row " + row + " and col " + col);
                break;
            }
        }
    }

    // AI Medium: Win if possible, else block, else random
    public static boolean tryWinOrBlock(char player, char aiPlayer) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ') {
                    board[i][j] = player;
                    if (isWinner(player)) {
                        if (player == aiPlayer) {
                            System.out.println("AI (" + aiPlayer + ") plays at " + i + "," + j);
                            return true; // Winning move
                        } else {
                            board[i][j] = aiPlayer;
                            System.out.println("AI (" + aiPlayer + ") blocks at " + i + "," + j);
                            return true; // Block
                        }
                    }
                    board[i][j] = ' ';
                }
            }
        }
        return false;
    }

    // AI Hard: Minimax
    public static int evaluateBoard() {
        if (isWinner('O')) return +10;
        if (isWinner('X')) return -10;
        return 0;
    }

    public static int[] minimax(char[][] b, char player) {
        int score = evaluateBoard();
        if (score == 10 || score == -10) return new int[] {score, -1, -1};
        if (isDraw()) return new int[] {0, -1, -1};

        int bestRow = -1, bestCol = -1;
        if (player == 'O') { // AI
            int bestScore = Integer.MIN_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (b[i][j] == ' ') {
                        b[i][j] = 'O';
                        int currentScore = minimax(b, 'X')[0];
                        b[i][j] = ' ';
                        if (currentScore > bestScore) {
                            bestScore = currentScore;
                            bestRow = i;
                            bestCol = j;
                        }
                    }
                }
            }
            return new int[] {bestScore, bestRow, bestCol};
        } else { // Human
            int bestScore = Integer.MAX_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (b[i][j] == ' ') {
                        b[i][j] = 'X';
                        int currentScore = minimax(b, 'O')[0];
                        b[i][j] = ' ';
                        if (currentScore < bestScore) {
                            bestScore = currentScore;
                            bestRow = i;
                            bestCol = j;
                        }
                    }
                }
            }
            return new int[] {bestScore, bestRow, bestCol};
        }
    }

    // AI Move depending on difficulty
    public static void aiMove(char aiPlayer) {
        if (difficulty == 1) {
            aiRandomMove(aiPlayer);
        } else if (difficulty == 2) {
            if (!tryWinOrBlock(aiPlayer, aiPlayer)) {
                if (!tryWinOrBlock('X', aiPlayer)) {
                    aiRandomMove(aiPlayer);
                }
            }
        } else {
            int[] bestMove = minimax(board, aiPlayer);
            board[bestMove[1]][bestMove[2]] = aiPlayer;
            System.out.println("AI (" + aiPlayer + ") plays at " + bestMove[1] + "," + bestMove[2]);
        }
    }

    // Play a single game
    public static void playGame(boolean singlePlayer) {
        initBoard();
        char currentPlayer = 'X';
        char aiPlayer = 'O';

        if (singlePlayer) {
            System.out.println("\nChoose Difficulty:");
            System.out.println("1. Easy\n2. Medium\n3. Hard (Unbeatable)");
            difficulty = sc.nextInt();

            while (difficulty < 1 || difficulty > 3) {
                System.out.println("Invalid choice! Please enter 1, 2, or 3:");
                difficulty = sc.nextInt();
            }
        }

        while (true) {
            printBoard();

            if (singlePlayer && currentPlayer == aiPlayer) {
                aiMove(aiPlayer);
            } else {
                playerMove(currentPlayer);
            }

            if (isWinner(currentPlayer)) {
                printBoard();
                System.out.println("Player " + currentPlayer + " wins!");

                if (currentPlayer == 'X') scoreX++;
                else scoreO++;
                history.add("Winner: Player " + currentPlayer);
                break;
            }

            if (isDraw()) {
                printBoard();
                System.out.println("It's a draw!");
                draws++;
                history.add("Draw");
                break;
            }

            currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
        }
    }

    // Show scorecard
    public static void showScorecard() {
        System.out.println("\nScorecard:");
        System.out.println("Player X: " + scoreX);
        System.out.println("Player O: " + scoreO);
        System.out.println("Draws: " + draws);
    }

    // Show history
    public static void showHistory() {
        System.out.println("\nGame History:");
        if (history.isEmpty()) {
            System.out.println("No games played yet.");
        } else {
            for (int i = 0; i < history.size(); i++) {
                System.out.println("Game " + (i + 1) + ": " + history.get(i));
            }
        }
    }

    // Main program
    public static void main(String[] args) {
        System.out.println("Welcome to Tic Tac Toe!");

        while (true) {
            System.out.println("\nChoose mode:");
            System.out.println("1. Two Player");
            System.out.println("2. Single Player vs AI");
            System.out.print("Enter choice: ");
            int choice = sc.nextInt();
            while (choice < 1 || choice > 2) {
                System.out.println("Invalid choice! Please enter 1 or 2:");
                choice = sc.nextInt();
            }

            boolean singlePlayer = (choice == 2);
            playGame(singlePlayer);

            showScorecard();
            showHistory();

            System.out.print("\nDo you want to play again? (y/n): ");
            char ans = sc.next().charAt(0);
            if (ans != 'y' && ans != 'Y') {
                System.out.println("Thanks for playing!");
                showScorecard();
                showHistory();
                break;
            }
        }

        sc.close();
    }
}

    
