import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
/**
 * Paint Class: gives the user the opportunity to draw on a canvas and other drawing related things.
 *
 * @author Nathan Schlutz
 * @version Nov 19, 2021
 */


public class Paint extends JComponent implements Runnable {

    Image image; //canvas
    Graphics2D graphics2D; // used for drawing
    int curX; //current mouse x coordinate
    int curY; // current mouse y coordinate
    int oldX; // previous mouse x coordinate
    int oldY; //previous mouse y coordinate
    JButton clrButton; // button to enter information;
    JButton fillButton;
    JButton eraseButton;
    JButton randomButton;
    Color color;

    JButton hexButton;
    JTextField hexText;
    JTextField rText;
    JTextField gText;
    JTextField bText;
    JButton rgbButton;
    Paint paint;
    Color eraser;


    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == clrButton) {
                setText();
                paint.clrButtonPressed();
            }
            if (e.getSource() == eraseButton) {
                paint.eraseButtonPressed();
            }
            if (e.getSource() == randomButton) {
                Random random = new Random();
                int r = random.nextInt(255);
                int g = random.nextInt(255);
                int b = random.nextInt(255);
                String hex = String.format("#%02x%02x%02x", r, g, b);
                color = new Color(r, g, b);
                rText.setText(String.valueOf(color.getRed()));
                gText.setText(String.valueOf(color.getGreen()));
                bText.setText(String.valueOf(color.getBlue()));
                hexText.setText(hex);
                paint.randomButtonPressed(color);
            }
            if (e.getSource() == fillButton) {
                paint.fillButtonPressed();
            }
            if (e.getSource() == hexButton) {
                try {
                    color = Color.decode(hexText.getText());
                    rText.setText(String.valueOf(color.getRed()));
                    gText.setText(String.valueOf(color.getGreen()));
                    bText.setText(String.valueOf(color.getBlue()));
                    paint.hexButtonPressed(color);
                } catch (NumberFormatException a) {
                    JOptionPane.showMessageDialog(null, "Not a valid Hex Value", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            if (e.getSource() == rgbButton) {
                if (rText.getText().isEmpty()) {
                    rText.setText("0");
                }
                if (gText.getText().isEmpty()) {
                    gText.setText("0");
                }
                if (bText.getText().isEmpty()) {
                    bText.setText("0");
                }
                try {
                    if (Integer.valueOf(rText.getText()) < 256 && Integer.valueOf(rText.getText()) > -1) {
                        if (Integer.valueOf(gText.getText()) < 256 && Integer.valueOf(gText.getText()) > -1) {
                            if (Integer.valueOf(bText.getText()) < 256 && Integer.valueOf(bText.getText()) > -1) {
                                int r = Integer.parseInt(rText.getText());
                                int g = Integer.parseInt(gText.getText());
                                int b = Integer.parseInt(bText.getText());
                                Color c = new Color(r, g, b);
                                hexText.setText(String.format("#%02x%02x%02x", r, g, b));
                                paint.rgbButtonPressed(c);
                            } else {
                                JOptionPane.showMessageDialog(null,
                                        "Not a valid RGB Value", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        } else {
                            JOptionPane.showMessageDialog(null,
                                    "Not a valid RGB Value", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null,
                                "Not a valid RGB Value", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception a) {
                    JOptionPane.showMessageDialog(null,
                            "Not a valid RGB Value", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    };

    public void clrButtonPressed() {
        graphics2D.setPaint(Color.WHITE);
        graphics2D.fillRect(0, 0, getSize().width, getSize().height);
        graphics2D.setPaint(Color.BLACK);
        repaint();
    }

    public void fillButtonPressed() {
        graphics2D.fillRect(0, 0, getSize().width, getSize().height);
        eraser = (Color) graphics2D.getPaint();
        graphics2D.setPaint(Color.black);
        repaint();
    }

    public void eraseButtonPressed() {
        graphics2D.setPaint(eraser);
    }

    public void randomButtonPressed(Color c) {
        graphics2D.setPaint(c);
    }

    public void hexButtonPressed(Color c) {
        graphics2D.setPaint(c);
    }

    public void rgbButtonPressed(Color c) {
        graphics2D.setPaint(c);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Paint());
    }

    protected void paintComponent(Graphics g) {
        if (image == null) {
            image = createImage(getSize().width, getSize().height);
            graphics2D = (Graphics2D) image.getGraphics();
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            graphics2D.setPaint(Color.white);
            graphics2D.fillRect(0, 0, getSize().width, getSize().height);
            color = Color.black;
            graphics2D.setPaint(color);
            eraser = Color.white;
            graphics2D.setStroke(new BasicStroke(5));
            repaint();
        }
        g.drawImage(image, 0, 0, null);
    }

    public void run() {
        JFrame frame = new JFrame();
        frame.setTitle("HW-12 Challenge");
        Container content = frame.getContentPane();
        content.setLayout(new BorderLayout());
        this.paint = new Paint();
        content.add(paint, BorderLayout.CENTER);
        clrButton = new JButton("Clear");
        fillButton = new JButton("Fill");
        eraseButton = new JButton("Erase");
        randomButton = new JButton("Random");
        JPanel panelTop = new JPanel();
        panelTop.add(clrButton);
        panelTop.add(fillButton);
        panelTop.add(eraseButton);
        panelTop.add(randomButton);
        content.add(panelTop, BorderLayout.NORTH);
        hexText = new JTextField("#", 10);
        hexButton = new JButton("Hex");
        rText = new JTextField("0", 5);
        gText = new JTextField("0", 5);
        bText = new JTextField("0", 5);
        rgbButton = new JButton("RGB");
        JPanel panelBottom = new JPanel();
        panelBottom.add(hexText);
        panelBottom.add(hexButton);
        panelBottom.add(rText);
        panelBottom.add(gText);
        panelBottom.add(bText);
        panelBottom.add(rgbButton);
        content.add(panelBottom, BorderLayout.SOUTH);
        clrButton.addActionListener(actionListener);
        eraseButton.addActionListener(actionListener);
        randomButton.addActionListener(actionListener);
        fillButton.addActionListener(actionListener);
        hexButton.addActionListener(actionListener);
        rgbButton.addActionListener(actionListener);
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);


    }

    public Paint() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // set oldX and oldY coordinates to beginning mouse press
                oldX = e.getX();
                oldY = e.getY();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                // set current coordinates to where mouse is being dragged
                curX = e.getX();
                curY = e.getY();

                // draw the line between old coordinates and new ones
                graphics2D.drawLine(oldX, oldY, curX, curY);

                // refresh frame and reset old coordinates
                repaint();
                oldX = curX;
                oldY = curY;
            }
        });
    }

    public void setText() {
        hexText.setText("#");
        rText.setText("");
        gText.setText("");
        bText.setText("");
    }
}
