package it.polimi.ingsw.Models.Game;

import it.polimi.ingsw.Models.Interfaces.AbleToGetPoints;
import it.polimi.ingsw.Models.Item.Color;
import it.polimi.ingsw.Models.Item.Item;

import javax.swing.*;
import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.*;

/**
 * @author Matteo
 *
 * <p>
 * This class represents a bookshelf.
 * It has 5 rows and 6 columns.
 * Each cell can contain an item.
 * @see Item
 */
public class Bookshelf implements AbleToGetPoints {
    private static int rows;
    private static int columns;
    private final boolean[][] booleanMatrix;

    @SuppressWarnings("unchecked")
    private final Optional<Item>[][] items;

    /**
     * Creates a new bookshelf. All cells are empty.
     * <p>
     * booleanMatrix is a matrix of booleans that is used to check if a cell can be visited (and it has not been visited yet).
     * It is initialized to true.
     */
    public Bookshelf() {
        try (InputStream input = new FileInputStream("settings/settings.properties")) {

            Properties prop = new Properties();

            // load a properties file
            prop.load(input);

            rows = Integer.parseInt(prop.getProperty("bookshelf.rows"));
            columns = Integer.parseInt(prop.getProperty("bookshelf.columns"));

        } catch (IOException ex) {
            ex.printStackTrace();
            rows = 6;
            columns = 5;
        }

        items = (Optional<Item>[][]) new Optional[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                this.items[i][j] = Optional.empty();
            }
        }

        booleanMatrix = new boolean[rows][columns];
        for (boolean[] row : booleanMatrix) {
            Arrays.fill(row, true);
        }
    }

    // NOTE: this method is used only for testing purposes, it will be removed in the final version.
/*    public static void main(String[] args) throws IOException {
        Bookshelf b = new Bookshelf();
        List<Item> itemList = new ArrayList<>();
        for (int i = 0; i < b.getColumns(); i++) {
            itemList.clear();
            for (int j = 0; j < b.getRows(); j++) {
                itemList.add(new Item(it.polimi.ingsw.Models.Item.Color.randomColor(), 0));
            }
            b.insert(i, itemList);
        }
        b.cli_print();
        b.print();
    }*/

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public int getSize() {
        return rows * columns;
    }

    /**
     * Returns the number of free cells in a column.
     *
     * @param column the column index
     * @return the number of free cells in the column
     */
    public int getFreeCellsInColumn(int column) {
        int freeCells = 0;
        for (int i = 0; i < getRows(); i++) {
            if (this.items[i][column].isEmpty()) {
                freeCells++;
            }
        }
        return freeCells;
    }

    public int getCellsInColumn(int column) {
        int usedCells = 0;
        for (int i = 0; i < getRows(); i++) {
            if (this.items[i][column].isPresent()) {
                usedCells++;
            }
        }
        return usedCells;
    }

    /**
     * Inserts a list of items in a column.
     *
     * @param column the column index
     * @param items  the list of items to insert
     * @throws IllegalArgumentException if the column index is not between 0 and the number of columns
     * @throws IllegalArgumentException if the list of items is empty
     * @throws IllegalArgumentException if the number of items to insert is greater than the number of free cells in the column
     */
    public void insert(int column, List<Item> items) {
        if (column < 0 || column > columns) {
            throw new IllegalArgumentException("Column index must be between 0 and " + columns);
        }

        if (items.size() > getFreeCellsInColumn(column)) {
            throw new IllegalArgumentException("Not enough free cells in column " + column + " available: " + getFreeCellsInColumn(column) + " required: " + items.size());
        }

        if (items.size() == 0) {
            throw new IllegalArgumentException("No items to insert");
        }

        int freeCells = getFreeCellsInColumn(column);
        for (int i = 0; i < items.size(); i++) {
            this.items[(rows - freeCells) + i][column] = Optional.ofNullable(items.get(i));
        }
    }

    /**
     * This function returns the item in the position (row, column).
     *
     * @param row    row (starting from 0)
     * @param column column (starting from 0)
     * @return the item in the position (row, column)
     * @author Simone
     */
    public Optional<Item> getItemAt(int row, int column) throws ArrayIndexOutOfBoundsException {
        if (row >= rows || column >= columns || row < 0 || column < 0) {
            throw new ArrayIndexOutOfBoundsException("Invalid row or column for the method getItemAt -row:" + row + " -column: " + column);
        }
        return items[row][column];
    }

    /**
     * This function tells whether a row is full or not.
     *
     * @param row row to check for completion
     * @return true if the row is completely full, false otherwise
     */
    public boolean isRowFull(int row) throws IllegalArgumentException {
        if (row >= rows || row < 0) {
            throw new IllegalArgumentException("Invalid row for the method isRowFull -row:" + row);
        }
        for (int col = 0; col < columns; col++) {
            if (getItemAt(row, col).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public boolean isColumnFull(int col) throws IllegalArgumentException {
        if (col >= columns || col < 0) {
            throw new IllegalArgumentException("Invalid column for the method isColumnFull -column:" + col);
        }
        return getItemAt(rows - 1, col).isPresent();
    }

    /**
     * This method calculates the points of adjacent groups of items of the bookshelf.
     *
     * @return the total score of adjacent groups of items of the given bookshelf
     * @author Valeria
     */
    public int getPoints() {
        int points = 0;
        for (int j = 0; j < columns; j++) {
            for (int i = 0; i < rows; i++) {
                if (items[i][j].isPresent()) {
                    points += calculatePoints(adjacentGroups(items[i][j].get().color(), i, j));
                }
            }
        }
        return points;
    }

    /**
     * Counts the number of items, in a given bookshelf, that match the given color
     *
     * @param color  the color to match
     * @param row    the starting row
     * @param column the starting column
     * @return the number of items that match the given color
     * @author Valeria
     */
    public int adjacentGroups(Color color, int row, int column) {
        int matches;

        if (row >= rows || column >= columns || row < 0 || column < 0) {
            return 0;
        }

        if (items[row][column].isEmpty()) {
            return 0;
        }

        if (!items[row][column].get().color().equals(color)) {
            return 0;
        }

        if (!booleanMatrix[row][column]) {
            matches = adjacentGroups(color, row + 1, column) + adjacentGroups(color, row, column + 1);

        } else {
            booleanMatrix[row][column] = false;
            matches = 1 + adjacentGroups(color, row + 1, column) + adjacentGroups(color, row, column + 1) + adjacentGroups(color, row - 1, column) + adjacentGroups(color, row, column - 1);
        }
        return matches;
    }

    /**
     * Calculates the points for a given number of matches
     *
     * @param matches the number of matches
     * @return the points for the given number of matches
     */
    public int calculatePoints(int matches) {
        if (matches < 3)
            return 0;
        else if (matches == 3)
            return 2;
        else if (matches == 4)
            return 3;
        else if (matches == 5)
            return 5;
        else
            return 8;
    }

    /**
     * This method returns the item contained in a column.
     * Empty cells are not included.
     *
     * @param col the column index
     * @return the content of the column
     * @throws IllegalArgumentException if the column index is not between 0 and the number of columns
     */

    public List<Item> getColumnContent(int col) throws IllegalArgumentException {
        if (col < 0 || col > columns) {
            throw new IllegalArgumentException("Invalid number of column (" + col + ")");
        }
        List<Item> content = new ArrayList<>();
        Optional<Item> item;
        for (int i = 0; i < getCellsInColumn(col); i++) {
            item = getItemAt(i, col);
            item.ifPresent(content::add);
        }
        return content;
    }


    /**
     * This method returns the item contained in a column.
     * Empty cells are not included.
     *
     * @param row the row index
     * @return the content of the column
     * @throws IllegalArgumentException if the column index is not between 0 and the number of columns
     */

    public List<Item> getRowContent(int row) throws IllegalArgumentException {
        if (row < 0 || row > getRows()) {
            throw new IllegalArgumentException("Invalid number of row (" + row + ")");
        }
        List<Item> content = new ArrayList<>();
        Optional<Item> item;
        for (int i = 0; i < getColumns(); i++) {
            item = getItemAt(row, i);
            item.ifPresent(content::add);
        }
        return content;
    }

// public void print() throws IOException {
//     int left_offset=11;
//     int vertical_space=11;
//     int bottom_offset=11;
//     int col_width=90;
//     int col_height=90;
//
//
//
//     /*
//     JFrame frame = new JFrame(); //JFrame Creation
//     frame.setTitle("Bookshelf"); //Add the title to frame
//     frame.setLayout(null); //Terminates default flow layout
//     //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Terminate program on close button
//
//     Container c = frame.getContentPane(); //Gets the content layer
//     JLabel bookshelf = new JLabel(); //JLabel Creation
//     JLabel item;
//     bookshelf.setIcon(new ImageIcon("resources/OurVariants/back.jpg")); //Sets the image to be displayed as an icon
//
//     Dimension bookshelfSize = bookshelf.getPreferredSize(); //Gets the size of the image
//     Dimension itemSize;
//     frame.setBounds(100, 200, bookshelfSize.width, bookshelfSize.height); //Sets the position of the frame
//
//     bookshelf.setBounds(0, 0, bookshelfSize.width, bookshelfSize.height); //Sets the location of the image
//
//     c.add(bookshelf); //Adds objects to the container
//     List<Item> itemList;
//
//     for(int col=0; col<getColumns(); col++){
//         itemList=getColumnContent(col);
//         for(int i=0; i< itemList.size(); i++){
//             item=new JLabel();
//             item.setIcon(new ImageIcon("resources/OurVariants/"+itemList.get(i).getColor().toString().toLowerCase()+".png")); //Sets the image to be displayed as an icon
//             itemSize=item.getPreferredSize();
//             item.setBounds(left_offset+col_width*col, bookshelfSize.height-bottom_offset-(i+1)*col_height, itemSize.width, itemSize.height); //Sets the location of the image
//             c.add(item);
//         }
//     }
//
//     frame.setVisible(true); // Exhibit the frame
//
//     // Open a JPEG file, load into a BufferedImage.
//     BufferedImage img = ImageIO.read(new File("resources/OurVariants/back.jpg"));
//
//     // Obtain the Graphics2D context associated with the BufferedImage.
//     Graphics2D g = img.createGraphics();
//
//     //g.
// }

    public void cli_print() {
        StringBuilder singleRow;
        List<String> stringBook = new ArrayList<>();
        for (int row = 0; row < getRows(); row++) {
            singleRow = new StringBuilder(row + "\t");
            for (int col = 0; col < getColumns(); col++) {
                singleRow.append("\t").append(getItemAt(row, col).isPresent() ? getItemAt(row, col).get().color().ordinal() : " ");
            }
            stringBook.add(singleRow.toString());
        }

        for (int i = 0; i < getRows(); i++) {
            System.out.println(stringBook.get(stringBook.size() - i - 1));
        }

        StringBuilder colNum = new StringBuilder(" ");
        for (int i = 0; i < getColumns(); i++) {
            colNum.append("\t").append(i);
        }
        System.out.println(" \t" + colNum);
    }

    public void print() {
        // Without JPanel images would be added to JFrame on top of each other.
        // That way only last image would be visible.
        int left_offset = 11;
        int vertical_space = 11;
        int bottom_offset = 11;
        int col_width = 90;
        int col_height = 90;

        JPanel panel = new JPanel();

        ImageIcon BackgroundImage = new ImageIcon("resources/OurVariants/back.jpg");

        panel.setLayout(null);
        panel.add(new JLabel(BackgroundImage));
//        panel.setAlignmentX(1);
        Dimension bookshelfSize = new Dimension(BackgroundImage.getIconWidth(), BackgroundImage.getIconHeight());

        JFrame frame = new JFrame("Bookshelf Display V1.0");
        List<Item> itemList;
        JLabel item;
        ImageIcon itemImage;

        for (int col = 0; col < getColumns(); col++) {
            itemList = getColumnContent(col);
            for (int i = 0; i < itemList.size(); i++) {
//                System.out.println("resources/OurVariants/"+itemList.get(i).getColor().toString().toLowerCase()+".png");
                itemImage = new ImageIcon("resources/OurVariants/" + itemList.get(i).color().toString().toLowerCase() + ".png");
                item = new JLabel(itemImage);
                item.setLayout(null);
                item.setLocation(left_offset + col_width * col, bookshelfSize.height - bottom_offset - (i + 1) * col_height);
                item.setVisible(true);
                item.getWidth();
                panel.add(item);
            }
        }
        frame.getContentPane().add(panel);
        frame.pack();
        frame.setVisible(true);
//        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


}
