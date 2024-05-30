import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.border.DashedBorder;
import com.itextpdf.layout.border.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.Document;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class GeneratePDF {
    public static void main(String[] args) {
        // Create the GUI
        JFrame frame = new JFrame("Invoice Generator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 800);
        frame.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 2));

        // Invoice details
        panel.add(new JLabel("Invoice No:"));
        JTextField invoiceNoField = new JTextField();
        panel.add(invoiceNoField);

        panel.add(new JLabel("Invoice Date:"));
        JTextField invoiceDateField = new JTextField();
        panel.add(invoiceDateField);

        // Billing information
        panel.add(new JLabel("Billing Company:"));
        JTextField billingCompanyField = new JTextField();
        panel.add(billingCompanyField);

        panel.add(new JLabel("Billing Name:"));
        JTextField billingNameField = new JTextField();
        panel.add(billingNameField);

        panel.add(new JLabel("Billing Address:"));
        JTextField billingAddressField = new JTextField();
        panel.add(billingAddressField);

        panel.add(new JLabel("Billing Email:"));
        JTextField billingEmailField = new JTextField();
        panel.add(billingEmailField);

        // Shipping information
        panel.add(new JLabel("Shipping Company:"));
        JTextField shippingCompanyField = new JTextField();
        panel.add(shippingCompanyField);

        panel.add(new JLabel("Shipping Name:"));
        JTextField shippingNameField = new JTextField();
        panel.add(shippingNameField);

        panel.add(new JLabel("Shipping Address:"));
        JTextField shippingAddressField = new JTextField();
        panel.add(shippingAddressField);

        panel.add(new JLabel("Shipping Email:"));
        JTextField shippingEmailField = new JTextField();
        panel.add(shippingEmailField);

        // Product details
        panel.add(new JLabel("Product Name:"));
        JTextField productNameField = new JTextField();
        panel.add(productNameField);

        panel.add(new JLabel("Product Quantity:"));
        JTextField productQuantityField = new JTextField();
        panel.add(productQuantityField);

        panel.add(new JLabel("Product Price:"));
        JTextField productPriceField = new JTextField();
        panel.add(productPriceField);

        JButton addButton = new JButton("Add Product");
        panel.add(addButton);
        panel.add(new JLabel()); // empty cell to keep grid structure

        JButton generateButton = new JButton("Generate PDF");
        panel.add(generateButton);

        frame.add(panel, BorderLayout.CENTER);
        frame.setVisible(true);

        List<Product> productList = new ArrayList<>();

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String productName = productNameField.getText();
                int productQuantity = Integer.parseInt(productQuantityField.getText());
                float productPrice = Float.parseFloat(productPriceField.getText());

                productList.add(new Product(productName, productQuantity, productPrice));

                productNameField.setText("");
                productQuantityField.setText("");
                productPriceField.setText("");
            }
        });

        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String invoiceNo = invoiceNoField.getText();
                String invoiceDate = invoiceDateField.getText();
                String billingCompany = billingCompanyField.getText();
                String billingName = billingNameField.getText();
                String billingAddress = billingAddressField.getText();
                String billingEmail = billingEmailField.getText();
                String shippingCompany = shippingCompanyField.getText();
                String shippingName = shippingNameField.getText();
                String shippingAddress = shippingAddressField.getText();
                String shippingEmail = shippingEmailField.getText();

                try {
                    generatePDF(invoiceNo, invoiceDate, billingCompany, billingName, billingAddress, billingEmail,
                            shippingCompany, shippingName, shippingAddress, shippingEmail, productList);
                    JOptionPane.showMessageDialog(frame, "PDF Generated Successfully!");
                } catch (FileNotFoundException | MalformedURLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "Error generating PDF: " + ex.getMessage());
                }
            }
        });
    }

    public static void generatePDF(String invoiceNo, String invoiceDate, String billingCompany, String billingName,
                                   String billingAddress, String billingEmail, String shippingCompany, String shippingName,
                                   String shippingAddress, String shippingEmail, List<Product> productList)
            throws FileNotFoundException, MalformedURLException {

        String path = "invoice.pdf";
        PdfWriter pdfWriter = new PdfWriter(path);
        PdfDocument pdfDocument = new PdfDocument(pdfWriter);
        pdfDocument.setDefaultPageSize(PageSize.A4);
        Document document = new Document(pdfDocument);

        String ImagePath = "D:\\Programs\\JaVa_programs\\pdf_invoice\\watermark.png";
        ImageData imageData = ImageDataFactory.create(ImagePath);
        Image image = new Image(imageData);
        float x = pdfDocument.getDefaultPageSize().getWidth() / 2;
        float y = pdfDocument.getDefaultPageSize().getHeight() / 2;
        image.setFixedPosition(x - 140, y - 50).setRotationAngle(-50);
        image.setOpacity(0.2f);
        document.add(image);

        float threecol = 190f;
        float twocol = 285f;
        float twocol150 = twocol + 150f;
        float threecolWidth[] = {threecol, threecol, threecol};
        float twocolumnWidth[] = {twocol150, twocol};
        float fullwidth[] = {threecol * 3};
        Paragraph onesp = new Paragraph("\n"); // to add some space before the line.

        Table table = new Table(twocolumnWidth);
        table.addCell(new Cell().add("Invoice").setFontSize(20f).setBorder(Border.NO_BORDER).setBold());
        Table nestedtable = new Table(new float[]{twocol / 2, twocol / 2});
        nestedtable.addCell(getHeaderTextCell("Invoice no:"));
        nestedtable.addCell(getHeaderTextCellValue(invoiceNo));
        nestedtable.addCell(getHeaderTextCell("Invoice Date:"));
        nestedtable.addCell(getHeaderTextCellValue(invoiceDate));
        table.addCell(new Cell().add(nestedtable).setBorder(Border.NO_BORDER).setBold());

        Border gb = new SolidBorder(Color.GRAY, 2f);
        Table divider = new Table(fullwidth);
        divider.setBorder(gb);
        document.add(table);
        document.add(onesp);
        document.add(divider);
        document.add(onesp);

        Table twocolTable = new Table(twocolumnWidth);
        twocolTable.addCell(getBillingandShippingCell("Billing Information"));
        twocolTable.addCell(getBillingandShippingCell("Shipping Information"));
        document.add(twocolTable.setMarginBottom(12f));

        Table twoColTable2 = new Table(twocolumnWidth);
        twoColTable2.addCell(getCell10fLeft("Company", true));
        twoColTable2.addCell(getCell10fLeft("Name", true));
        twoColTable2.addCell(getCell10fLeft(billingCompany, false));
        twoColTable2.addCell(getCell10fLeft(billingName, false));
        document.add(twoColTable2);

        Table twoColTable3 = new Table(twocolumnWidth);
        twoColTable3.addCell(getCell10fLeft("Name", true));
        twoColTable3.addCell(getCell10fLeft("Address", true));
        twoColTable3.addCell(getCell10fLeft(billingName, false));
        twoColTable3.addCell(getCell10fLeft(billingAddress, false));
        document.add(twoColTable3);

        float oneColumnwidth[] = {twocol150};

        Table oneColTable1 = new Table(oneColumnwidth);
        oneColTable1.addCell(getCell10fLeft("Address", true));
        oneColTable1.addCell(getCell10fLeft(billingAddress, false));
        oneColTable1.addCell(getCell10fLeft("Email", true));
        oneColTable1.addCell(getCell10fLeft(billingEmail, false));
        document.add(oneColTable1.setMarginBottom(10f));

        Table tableDivider2 = new Table(fullwidth);
        Border dgb = new DashedBorder(Color.GRAY, 0.5f);
        document.add(tableDivider2.setBorder(dgb));

        Paragraph producPara = new Paragraph("Products");
        document.add(producPara.setBold());

        Table threeColTable1 = new Table(threecolWidth);
        threeColTable1.setBackgroundColor(Color.BLACK, 0.7f);

        threeColTable1.addCell(new Cell().add("Description").setBold().setFontColor(Color.WHITE).setBorder(Border.NO_BORDER));
        threeColTable1.addCell(new Cell().add("Quantity").setBold().setFontColor(Color.WHITE).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));
        threeColTable1.addCell(new Cell().add("Price").setBold().setFontColor(Color.WHITE).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER));
        document.add(threeColTable1);

        Table threeColTable2 = new Table(threecolWidth);
        float totalSum = 0f;

        for (Product product : productList) {
            float total = product.getQuantity() * product.getPriceperpeice();
            totalSum += total;
            threeColTable2.addCell(new Cell().add(product.getPname()).setBorder(Border.NO_BORDER).setMarginLeft(10f));
            threeColTable2.addCell(new Cell().add(String.valueOf(product.getQuantity())).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));
            threeColTable2.addCell(new Cell().add(String.valueOf(total)).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER));
        }

        document.add(threeColTable2);

        Table threeColTable3 = new Table(threecolWidth);
        threeColTable3.addCell(new Cell().add("").setBorder(Border.NO_BORDER));
        threeColTable3.addCell(new Cell().add("Total").setBold().setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));
        threeColTable3.addCell(new Cell().add(String.valueOf(totalSum)).setBold().setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER));
        document.add(threeColTable3);

        document.close();
    }

    private static Cell getHeaderTextCell(String text) {
        return new Cell().add(text).setBold().setBorder(Border.NO_BORDER);
    }

    private static Cell getHeaderTextCellValue(String text) {
        return new Cell().add(text).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER);
    }

    private static Cell getBillingandShippingCell(String text) {
        return new Cell().add(text).setBackgroundColor(Color.GRAY, 0.3f).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER);
    }

    private static Cell getCell10fLeft(String text, boolean isBold) {
        Cell cell = new Cell().add(text).setFontSize(10f).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT);
        return isBold ? cell.setBold() : cell;
    }

    // Product class to hold product details
    public static class Product {
        private String pname;
        private int quantity;
        private float priceperpeice;

        public Product(String pname, int quantity, float priceperpeice) {
            this.pname = pname;
            this.quantity = quantity;
            this.priceperpeice = priceperpeice;
        }

        public String getPname() {
            return pname;
        }

        public int getQuantity() {
            return quantity;
        }

        public float getPriceperpeice() {
            return priceperpeice;
        }
    }
}
