package asciitexttetris;

import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

/**
 *
 * @author leonardo
 */
public class TetrisView extends JFrame implements KeyListener {
    
    private TetrisModel model = new TetrisModel();
    private JTextArea text = new JTextArea();
    private char[][] screen = new char[40][24];
    
    public TetrisView() throws HeadlessException {
        setTitle("ASCII Text Tetris Test");
        setSize(410, 490);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        text.setFont(new Font("Courier New", Font.PLAIN, 16));
        text.addKeyListener(this);
        add(text);
        text.requestFocus();
        
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (!model.isGameOver()) {
                        model.update();
                    }
                    draw();
                    text.setText(convertScreenToText());
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException ex) { }
                }
            }
        }).start();
    }

    private void clear() {
        for (int y=0; y<24; y++) {
            for (int x=0; x<40; x++) {
                screen[x][y] = '.';
            }
        }
    }

    private String convertScreenToText() {
        StringBuilder sb = new StringBuilder();
        for (int y=0; y<24; y++) {
            for (int x=0; x<40; x++) {
                sb.append(screen[x][y]);
            }
            sb.append("\n");
        }
        return sb.toString();
    }
    
    private void print(int x, int y, String c) {
        for (int col = 0; col < c.length(); col++) {
            screen[x + col][y] = c.charAt(col);
        }
    }
    
    public void draw() {
        clear();
        print(32, 23, " by O.L.");
        drawScore();
        drawGrid();
        drawNextPiece();
        if (model.isGameOver()) {
            drawGameOver();
        }
    }

    private void drawScore() {
        print(17, 1, "+---------------+");
        print(17, 2, "|               |");
        print(17, 3, "+---------------+");
        print(19, 2, "SCORE: " + model.getScore());
    }
    
    private void drawGrid() {
        print(3, 1, "+----------+");
        print(3, 22, "+----------+");
        for (int row = 4; row < model.getGridRows(); row++) {
            print(3, row - 2, "|          |");
            for (int col = 0; col < model.getGridCols(); col++) {
                int c = model.getGridValue(col, row);
                if (c > 0) {
                    print (col + 4, row - 2, "#");
                }
                else {
                    print (col + 4, row - 2, " ");
                }
            }
        }
    }
    
    private void drawNextPiece() {
        print(17,  5, " NEXT: ");
        print(17,  6, "+----+");
        print(17, 11, "+----+");
        for (int row = 0; row < 4; row++) {
            print(17,  row + 7, "|    |");
            for (int col = 0; col < 4; col++) {
                int c = model.getNextPieceValue(col, row);
                if (c > 0) {
                    print (col + 18, row + 7, "#");
                }
                else {
                    print (col + 18, row + 7, " ");
                }
            }
        }
    }

    public void drawGameOver() {
        print(8,  9, "+=======================+");
        print(8, 10, "|       GAME OVER       |");
        print(8, 11, "|                       |");
        print(8, 12, "|  PRESS SPACE TO PLAY  |");
        print(8, 13, "+=======================+");
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getID() == KeyEvent.KEY_PRESSED) {
            if (model.isGameOver()) {
                if (e.getKeyCode() == 32) {
                    model.start();
                }
            }
            else {
                switch (e.getKeyCode()) {
                    case 37: model.move(-1); break;
                    case 39: model.move(1); break;
                    case 38: model.rotate(); break;
                    case 40: model.down(); break;
                    case 65: model.update(); break;
                }
            }
        }
        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                TetrisView view = new TetrisView();
                view.setVisible(true);
            }
        });
    }
    
}
