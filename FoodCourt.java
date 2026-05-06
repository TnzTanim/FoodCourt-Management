import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FoodCourt
{
    public static void main(String[] args)
    {
        new OrderFrame();
    }
}

class FoodItem
{
    private String name;
    private int    price;

    public FoodItem(String name, int price)
    {
        this.name  = name;
        this.price = price;
    }

    public String getName()  { return name;  }
    public int    getPrice() { return price; }
}

class Order
{
    private String customerName;
    private String tableNo;
    private String orderType;
    private String items;
    private int    total;

    public Order(String customerName, String tableNo, String orderType, String items, int total)
    {
        this.customerName = customerName;
        this.tableNo      = tableNo;
        this.orderType    = orderType;
        this.items        = items;
        this.total        = total;
    }

    public String buildReceipt()
    {
        String dateTime = LocalDateTime.now()
                          .format(DateTimeFormatter.ofPattern("dd/MM/yyyy  hh:mm a"));

        return  "=====================================\n"
              + "       FOOD COURT SYSTEM\n"
              + "=====================================\n"
              + "Date      : " + dateTime + "\n"
              + "Customer  : " + customerName + "\n"
              + "Table No  : " + tableNo + "\n"
              + "Order Type: " + orderType + "\n"
              + "-------------------------------------\n"
              + items
              + "-------------------------------------\n"
              + String.format("  TOTAL     :%20d tk\n", total)
              + "=====================================\n"
              + "    Thank you! Come again :)\n"
              + "=====================================\n";
    }

    public void saveToFile()
    {
        try
        {
            File file = new File("./Data/orders.txt");
            if (!file.exists())
            {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file, true);
            fw.write(buildReceipt() + "\n");
            fw.flush();
            fw.close();
        }
        catch (IOException ioe)
        {
            JOptionPane.showMessageDialog(null, "Error saving order!");
        }
    }
}

class OrderFrame extends JFrame implements ActionListener, MouseListener
{
    private Font  fTitle, fLabel, fInput, fMono;
    private Color cGreen, cDarkGreen, cBlue, cOrange, cGray, cRed, cBg;

    private JTextField   tfName;
    private JComboBox    cbTable;
    private JRadioButton rbDineIn, rbTakeaway;
    private ButtonGroup  bgType;

    private JCheckBox cbBurger, cbFries, cbPizza, cbDrink, cbCoffee, cbPasta;
    private FoodItem[] menu;

    private JButton   btnPlace, btnSearch, btnDaily, btnClear, btnExit;
    private JTextArea taOutput;
    private JPanel    panel;

    public OrderFrame()
    {
        super("Food Court Order System");
        setBounds(200, 60, 860, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        fTitle = new Font("Segoe UI", Font.BOLD,  20);
        fLabel = new Font("Segoe UI", Font.BOLD,  13);
        fInput = new Font("Segoe UI", Font.PLAIN, 13);
        fMono  = new Font("Courier New", Font.PLAIN, 12);

        cGreen     = new Color(46,  125, 50);
        cDarkGreen = new Color(27,   94, 32);
        cBlue      = new Color(21,  101, 192);
        cOrange    = new Color(230,  81,  0);
        cGray      = new Color(90,   90, 90);
        cRed       = new Color(183,  28, 28);
        cBg        = new Color(245, 245, 245);

        panel = new JPanel(null);
        panel.setBackground(cBg);

        // header
        JPanel header = new JPanel(null);
        header.setBounds(0, 0, 860, 58);
        header.setBackground(new Color(27, 94, 32));

        JLabel lblTitle = new JLabel("  FOOD COURT ORDER SYSTEM");
        lblTitle.setBounds(10, 6, 500, 30);
        lblTitle.setFont(fTitle);
        lblTitle.setForeground(Color.WHITE);
        header.add(lblTitle);

        JLabel lblSub = new JLabel("  Server / Cashier Panel");
        lblSub.setBounds(14, 34, 300, 18);
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblSub.setForeground(new Color(165, 214, 167));
        header.add(lblSub);

        panel.add(header);

        addLabel("Customer Name", 20, 72);
        tfName = new JTextField();
        tfName.setBounds(155, 72, 200, 28);
        tfName.setFont(fInput);
        panel.add(tfName);

        addLabel("Table No", 20, 108);
        cbTable = new JComboBox(new String[]{"T1","T2","T3","T4","T5","T6","T7","T8","T9","T10"});
        cbTable.setBounds(155, 108, 100, 28);
        cbTable.setFont(fInput);
        panel.add(cbTable);

        addLabel("Order Type", 20, 144);
        rbDineIn = makeRadio("Dine-in",   155, 144, true);
        rbTakeaway = makeRadio("Takeaway", 250, 144, false);
        bgType = new ButtonGroup();
        bgType.add(rbDineIn);
        bgType.add(rbTakeaway);

        addLabel("Select Items", 20, 182);

        menu = new FoodItem[]
        {
            new FoodItem("Burger",     120),
            new FoodItem("Fries",       60),
            new FoodItem("Pizza",      200),
            new FoodItem("Cold Drink",  40),
            new FoodItem("Coffee",      50),
            new FoodItem("Pasta",      150)
        };

        JCheckBox[] boxes = new JCheckBox[6];
        for (int i = 0; i < menu.length; i++)
        {
            int x = 155 + (i % 2) * 190;
            int y = 182 + (i / 2) * 32;
            boxes[i] = new JCheckBox(menu[i].getName() + "  (" + menu[i].getPrice() + " tk)");
            boxes[i].setBounds(x, y, 185, 28);
            boxes[i].setFont(fInput);
            boxes[i].setBackground(cBg);
            panel.add(boxes[i]);
        }

        cbBurger = boxes[0];  cbFries  = boxes[1];
        cbPizza  = boxes[2];  cbDrink  = boxes[3];
        cbCoffee = boxes[4];  cbPasta  = boxes[5];

        JSeparator line = new JSeparator();
        line.setBounds(14, 285, 530, 2);
        line.setForeground(new Color(200, 200, 200));
        panel.add(line);

        btnPlace  = makeButton("Place Order", 20,  298, 130, cGreen,  true);
        btnSearch = makeButton("Search",     160,  298, 100, cBlue,  false);
        btnDaily  = makeButton("Daily Total", 270, 298, 110, cOrange, false);
        btnClear  = makeButton("Clear",       390, 298,  80, cGray,  false);
        btnExit   = makeButton("Exit",        480, 298,  70, cRed,   false);

        JLabel lblOutput = new JLabel("Receipt / Output");
        lblOutput.setBounds(580, 66, 200, 22);
        lblOutput.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblOutput.setForeground(new Color(80, 80, 80));
        panel.add(lblOutput);

        taOutput = new JTextArea("  Ready to take orders...");
        taOutput.setFont(fMono);
        taOutput.setEditable(false);
        taOutput.setBackground(new Color(250, 250, 245));
        taOutput.setForeground(new Color(30, 30, 30));
        taOutput.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JScrollPane scroll = new JScrollPane(taOutput);
        scroll.setBounds(575, 90, 268, 468);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        panel.add(scroll);

        JSeparator vline = new JSeparator(JSeparator.VERTICAL);
        vline.setBounds(563, 60, 2, 510);
        vline.setForeground(new Color(210, 210, 210));
        panel.add(vline);

        add(panel);
        setVisible(true);
    }

    private void addLabel(String text, int x, int y)
    {
        JLabel lbl = new JLabel(text);
        lbl.setBounds(x, y, 130, 26);
        lbl.setFont(fLabel);
        panel.add(lbl);
    }

    private JRadioButton makeRadio(String text, int x, int y, boolean selected)
    {
        JRadioButton rb = new JRadioButton(text);
        rb.setBounds(x, y, 90, 26);
        rb.setFont(fInput);
        rb.setBackground(cBg);
        rb.setSelected(selected);
        panel.add(rb);
        return rb;
    }

    private JButton makeButton(String text, int x, int y, int w, Color bg, boolean hover)
    {
        JButton btn = new JButton(text);
        btn.setBounds(x, y, w, 36);
        btn.setFont(fLabel);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.addActionListener(this);
        if (hover) btn.addMouseListener(this);
        panel.add(btn);
        return btn;
    }

    public void actionPerformed(ActionEvent ae)
    {
        if (ae.getSource() == btnPlace)  placeOrder();
        if (ae.getSource() == btnSearch) searchOrder();
        if (ae.getSource() == btnDaily)  dailyTotal();
        if (ae.getSource() == btnClear)  clearForm();
        if (ae.getSource() == btnExit)   System.exit(0);
    }

    private void placeOrder()
    {
        String name = tfName.getText().trim();
        if (name.isEmpty())
        {
            JOptionPane.showMessageDialog(this, "Please enter customer name!");
            return;
        }

        JCheckBox[] boxes = {cbBurger, cbFries, cbPizza, cbDrink, cbCoffee, cbPasta};
        String itemsText  = "";
        int    total      = 0;
        boolean none      = true;

        for (int i = 0; i < boxes.length; i++)
        {
            if (boxes[i].isSelected())
            {
                none       = false;
                total     += menu[i].getPrice();
                itemsText += String.format("  %-18s%6d tk\n", menu[i].getName(), menu[i].getPrice());
            }
        }

        if (none)
        {
            JOptionPane.showMessageDialog(this, "Please select at least one item!");
            return;
        }

        String orderType = rbDineIn.isSelected() ? "Dine-in" : "Takeaway";
        Order order = new Order(name, cbTable.getSelectedItem().toString(), orderType, itemsText, total);
        order.saveToFile();
        taOutput.setText(order.buildReceipt());
        JOptionPane.showMessageDialog(this, "Order placed successfully!");
    }

    private void searchOrder()
    {
        String name = tfName.getText().trim();
        if (name.isEmpty())
        {
            JOptionPane.showMessageDialog(this, "Enter customer name to search!");
            return;
        }

        try
        {
            File file = new File("./Data/orders.txt");
            if (!file.exists()) { taOutput.setText("No orders found."); return; }

            BufferedReader br       = new BufferedReader(new FileReader(file));
            String         line;
            String         fullText = "";

            while ((line = br.readLine()) != null)
                fullText += line + "\n";
            br.close();

            String[] blocks = fullText.split("=====================================\n");
            String   result = "";

            for (int i = 0; i < blocks.length; i++)
            {
                if (blocks[i].toLowerCase().contains(("customer  : " + name).toLowerCase()))
                    result += "=====================================\n" + blocks[i];
            }

            taOutput.setText(result.isEmpty() ? "No orders found for: " + name : result);
        }
        catch (IOException ioe)
        {
            JOptionPane.showMessageDialog(this, "Error reading file!");
        }
    }

    private void dailyTotal()
    {
        try
        {
            File file = new File("./Data/orders.txt");
            if (!file.exists()) { taOutput.setText("No orders yet."); return; }

            BufferedReader br         = new BufferedReader(new FileReader(file));
            String         line;
            int            grandTotal = 0;
            int            count      = 0;

            while ((line = br.readLine()) != null)
            {
                if (line.trim().startsWith("TOTAL"))
                {
                    String   cleaned = line.replace("TOTAL","").replace(":","").replace("tk","").trim();
                    String[] parts   = cleaned.split("\\s+");
                    grandTotal      += Integer.parseInt(parts[parts.length - 1]);
                    count++;
                }
            }
            br.close();

            taOutput.setText(
                  "=====================================\n"
                + "          DAILY SUMMARY\n"
                + "=====================================\n"
                + "  Total Orders  : " + count      + "\n"
                + "  Grand Total   : " + grandTotal + " tk\n"
                + "=====================================\n");
        }
        catch (IOException ioe)
        {
            JOptionPane.showMessageDialog(this, "Error reading file!");
        }
    }

    private void clearForm()
    {
        tfName.setText("");
        cbTable.setSelectedIndex(0);
        rbDineIn.setSelected(true);
        cbBurger.setSelected(false);  cbFries.setSelected(false);
        cbPizza.setSelected(false);   cbDrink.setSelected(false);
        cbCoffee.setSelected(false);  cbPasta.setSelected(false);
        taOutput.setText("  Ready to take orders...");
    }

    public void mouseEntered(MouseEvent me)  { if (me.getSource() == btnPlace) btnPlace.setBackground(cDarkGreen); }
    public void mouseExited(MouseEvent me)   { if (me.getSource() == btnPlace) btnPlace.setBackground(cGreen);     }
    public void mouseClicked(MouseEvent me)  { }
    public void mousePressed(MouseEvent me)  { }
    public void mouseReleased(MouseEvent me) { }
}
