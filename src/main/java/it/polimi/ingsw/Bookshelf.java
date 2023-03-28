package it.polimi.ingsw;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.swing.*;
/**
 * @author Matteo
 * @see Item
 * <p>
 * This class represents a bookshelf.
 * It has 5 rows and 6 columns.
 * Each cell can contain an item.
 */
public class Bookshelf implements AbleToGetPoints{
    private final int rows = 6;
    private final int columns = 5;

    @SuppressWarnings("unchecked")
    private final Optional<Item>[][] items = (Optional<Item>[][]) new Optional[rows][columns];

    /**
     * Creates a new bookshelf. All cells are empty.
     */
    public Bookshelf() {
        for (int i = 0; i < getRows(); i++) {
            for (int j = 0; j < getColumns(); j++) {
                this.items[i][j] = Optional.empty();
            }
        }
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
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
    public boolean isRowFull(int row) {
        for (int col = 0; col < columns; col++) {
            if (getItemAt(row, col).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public int getPoints() {
        int points = 0;
        // TODO: Implement this method
        return points;
    }

    public List<Item> getColumnContent(int col) throws IllegalArgumentException{
        if(col<0 || col>columns){throw new IllegalArgumentException("Invalid number of column ("+col+")");}
        List<Item> content = new ArrayList<>();
        Optional<Item> item;
        for (int i = 0; i < getCellsInColumn(col); i++) {
            item = getItemAt(i, col);
            item.ifPresent(content::add);
        }
        return content;
    }

    public void cli_print() {
        String singlerow;
        List<String> stringbook = new ArrayList<>();
        for (int row = 0; row < getRows(); row++) {
            singlerow = row + "\t";
            for (int col = 0; col < getColumns(); col++) {
                singlerow += "\t" +
                        (getItemAt(row, col).isPresent() ? getItemAt(row, col).get().getColor().ordinal() : " ");
            }
            stringbook.add(singlerow);
        }

        for (int i = 0; i < getRows(); i++) {
            System.out.println(stringbook.get(stringbook.size() - i - 1));
        }

        String colnum = " ";
        for (int i = 0; i < getColumns(); i++) {
            colnum += "\t" + i;
        }
        System.out.println(" \t" + colnum);
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

    public void print(){
        //Without JPanel images would be added to JFrame on top of each other.
        //That way only last image would be visible.
        int left_offset=11;
        int vertical_space=11;
        int bottom_offset=11;
        int col_width=90;
        int col_height=90;


        JPanel panel= new JPanel ();

        ImageIcon BackgroundImage= new ImageIcon("resources/OurVariants/back.jpg");

        panel.setLayout(null);
        panel.add(new JLabel(BackgroundImage));
        //panel.setAlignmentX(1);
        Dimension bookshelfSize = new Dimension(BackgroundImage.getIconWidth(), BackgroundImage.getIconHeight());


        JFrame frame= new JFrame ("Bookshelf Display V1.0");
        List<Item> itemList=new ArrayList<>();
        JLabel item;
        ImageIcon itemImage;


        for(int col=0; col<getColumns(); col++){
            itemList=getColumnContent(col);
                for(int i=0; i< itemList.size(); i++){
                    //System.out.println("resources/OurVariants/"+itemList.get(i).getColor().toString().toLowerCase()+".png");
                    itemImage=new ImageIcon("resources/OurVariants/"+itemList.get(i).getColor().toString().toLowerCase()+".png");
                    item=new JLabel(itemImage);
                    item.setLayout(null);
                    item.setLocation(left_offset+col_width*col, bookshelfSize.height-bottom_offset-(i+1)*col_height);
                    item.setVisible(true);
                    item.getWidth();
                    panel.add(item);
                }
        }
        frame.getContentPane().add(panel);
        frame.pack();
        frame.setVisible(true);
        //frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        Bookshelf b=new Bookshelf();
        List<Item> itemList=new ArrayList<>();
        for(int i=0; i< b.getColumns(); i++){
            itemList.clear();
            for(int j=0; j<b.getRows(); j++){
                itemList.add(new Item(Color.randomColor(), 0));
            }
            b.insert(i, itemList);
        }
        b.cli_print();
        b.print();
    }

}
