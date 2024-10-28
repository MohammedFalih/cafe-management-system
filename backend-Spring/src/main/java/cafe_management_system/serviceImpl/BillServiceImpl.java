package cafe_management_system.serviceImpl;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;
import java.io.File;
import java.io.FileInputStream;

import com.itextpdf.text.Element;

import org.apache.pdfbox.io.IOUtils;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import cafe_management_system.JWT.JwtFilter;
import cafe_management_system.JWT.JwtUtil;
import cafe_management_system.constants.CafeConstants;
import cafe_management_system.dao.BillDao;
import cafe_management_system.model.Bill;
import cafe_management_system.service.BillService;
import cafe_management_system.utils.CafeUtils;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BillServiceImpl implements BillService {

    @Autowired
    JwtFilter jwtFilter;

    @Autowired
    BillDao billDao;

    @Autowired
    JwtUtil jwtUtil;

    private Claims getClaimsFromToken() {
        String token = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest().getHeader("Authorization").substring(7);
        return jwtUtil.extractAllClaims(token);
    }

    private boolean isAdmin(Claims claims) {
        return jwtFilter.isAdmin(claims);
    }

    @Override
    public ResponseEntity<String> generateReport(Map<String, Object> request) {
        try {
            String fileName;
            if (validateRequestMap(request)) {
                if (request.containsKey("isGenerate") && !(Boolean) request.get("isGenerate")) {
                    fileName = (String) request.get("uuid");
                } else {
                    fileName = CafeUtils.getUUId();
                    request.put("uuid", fileName);
                    insertBill(request);
                }

                String data = "Name: " + request.get("name") + "\n" + "Contact Number: " + request.get("contactNumber")
                        +
                        "\n" + "Email: " + request.get("email") + "\n" + "Payment Method: "
                        + request.get("paymentMethod");

                // PdfWriter.getInstance(document,
                // new FileOutputStream(CafeConstants.SAVE_LOCATION + "\\" + fileName +
                // ".pdf"));

                Document document = new Document();
                String fullPath = CafeConstants.SAVE_LOCATION + "/" + fileName + ".pdf";
                System.out.println("Saving PDF to: " + fullPath);

                document = new Document();
                PdfWriter.getInstance(document, new FileOutputStream(fullPath));

                document.open();
                setRectangleInPdf(document);

                Paragraph chunk = new Paragraph("Cafe Management System", getFont("Header"));
                chunk.setAlignment(Element.ALIGN_CENTER);
                document.add(chunk);

                Paragraph paragraph = new Paragraph(data + "\n \n", getFont("Data"));
                document.add(paragraph);

                PdfPTable table = new PdfPTable(5);
                table.setWidthPercentage(100);
                addTableHeader(table);

                JSONArray jsonArray = CafeUtils.getJsonArrayFromString((String) request.get("productDetails"));
                for (int i = 0; i < jsonArray.length(); i++) {
                    addRows(table, CafeUtils.getMapFromJson(jsonArray.getString(i)));
                }
                document.add(table);

                Paragraph footer = new Paragraph("Total: " + request.get("totalAmount") + "\n"
                        + "Thank you for visiting. Please visit again!!", getFont("Data"));
                document.add(footer);
                document.close();

                return new ResponseEntity<>("{\"uuid\":\"" + fileName + "\"}", HttpStatus.OK);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void addRows(PdfPTable table, Map<String, Object> data) {
        log.info("addRows method");
        table.addCell((String) data.get("name"));
        table.addCell((String) data.get("category"));
        table.addCell((String) data.get("quantity"));
        table.addCell(Double.toString((Double) data.get("price")));
        table.addCell(Double.toString((Double) data.get("total")));
    }

    private void addTableHeader(PdfPTable table) {
        log.info("addTableHeader method");
        Stream.of("Name", "Category", "Quantity", "Price", "Sub Total")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setBorderWidth(2);
                    header.setPhrase(new Phrase(columnTitle));
                    header.setBackgroundColor(BaseColor.YELLOW);
                    header.setHorizontalAlignment(Element.ALIGN_CENTER);
                    header.setVerticalAlignment(Element.ALIGN_CENTER);
                    table.addCell(header);
                });
    }

    private Font getFont(String type) {
        log.info("getFont method");
        switch (type) {
            case "Header":
                Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLDOBLIQUE, 18, BaseColor.BLACK);
                // headerFont.setSize(Font.BOLD);
                return headerFont;

            case "Data":
                Font dataFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, BaseColor.BLACK);
                dataFont.setStyle(Font.BOLD);
                return dataFont;

            default:
                return new Font();
        }
    }

    private void setRectangleInPdf(Document document) throws DocumentException {
        log.info("setRectangleInPdf method");
        Rectangle rectangle = new Rectangle(577, 825, 18, 15);
        rectangle.enableBorderSide(1);
        rectangle.enableBorderSide(2);
        rectangle.enableBorderSide(4);
        rectangle.enableBorderSide(8);
        rectangle.setBorderColor(BaseColor.BLACK);
        rectangle.setBorderWidth(1);
        document.add(rectangle);
    }

    private void insertBill(Map<String, Object> request) {
        try {
            Bill bill = new Bill();
            bill.setUuid((String) request.get("uuid"));
            bill.setName((String) request.get("name"));
            bill.setEmail((String) request.get("email"));
            bill.setContactNumber((String) request.get("contactNumber"));
            bill.setPaymentMethod((String) request.get("paymentMethod"));
            bill.setProductDetail((String) request.get("productDetails"));
            bill.setTotal(Integer.parseInt((String) request.get("totalAmount")));
            bill.setCreatedBy(jwtFilter.getCurrentUser());
            billDao.save(bill);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean validateRequestMap(Map<String, Object> request) {
        return request.containsKey("name") &&
                request.containsKey("contactNumber") &&
                request.containsKey("email") &&
                request.containsKey("paymentMethod") &&
                request.containsKey("productDetails") &&
                request.containsKey("totalAmount");
    }

    @Override
    public ResponseEntity<List<Bill>> getBills() {

        List<Bill> list = new ArrayList<>();
        Claims claims = this.getClaimsFromToken();

        try {
            if (this.isAdmin(claims)) {
                list = billDao.getAllBills();
            } else {
                list = billDao.getBillByUserName(jwtFilter.getCurrentUser());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<byte[]> getPdf(Map<String, Object> request) {
        log.info("getPdf() method" + request);
        try {
            byte[] byteArray = new byte[0];
            if (!request.containsKey("uuid") && validateRequestMap(request)) {
                return new ResponseEntity<>(byteArray, HttpStatus.BAD_REQUEST);
            }
            String filePath = CafeConstants.SAVE_LOCATION + "/" + (String) request.get("uuid") + ".pdf";
            if (CafeUtils.isFileExist(filePath)) {
                byteArray = getByteArray(filePath);
                return new ResponseEntity<>(byteArray, HttpStatus.OK);
            } else {
                request.put("isGenerate", false);
                generateReport(request);
                byteArray = getByteArray(filePath);
                return new ResponseEntity<>(byteArray, HttpStatus.OK);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private byte[] getByteArray(String filePath) throws IOException {
        File initialFile = new File(filePath);
        InputStream targetStream = new FileInputStream(initialFile);
        byte[] byteArray = IOUtils.toByteArray(targetStream);
        targetStream.close();
        return byteArray;
    }

    @Override
    public ResponseEntity<String> deleteBill(Integer id) {
        try {
            @SuppressWarnings("rawtypes")
            Optional billDeletion = billDao.findById(id);
            if (!billDeletion.isEmpty()) {
                billDao.deleteById(id);
                return CafeUtils.getResponseEntity("Bill deleted successfully.", HttpStatus.OK);
            }
            return CafeUtils.getResponseEntity("Bill Id doesn't exists", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
