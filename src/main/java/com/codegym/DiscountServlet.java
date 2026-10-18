package com.codegym;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;

@WebServlet(name = "DiscountServlet", urlPatterns = "/display-discount")
public class DiscountServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        String description = request.getParameter("productDescription");
        BigDecimal listPrice;
        BigDecimal discountPercent;
        try {
            if (description == null || description.isBlank() || description.length() > 1000) {
                throw new IllegalArgumentException();
            }
            listPrice = parseNumber(request.getParameter("listPrice"));
            discountPercent = parseNumber(request.getParameter("discountPercent"));
            if (listPrice.signum() < 0 || discountPercent.signum() < 0
                    || discountPercent.compareTo(new BigDecimal("100")) > 0) {
                throw new IllegalArgumentException();
            }
        } catch (IllegalArgumentException exception) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            try (PrintWriter out = response.getWriter()) {
                writeHeader(out, "Kiểm tra dữ liệu nhập");
                out.println("<div class='error'>Vui lòng nhập mô tả sản phẩm hợp lệ, giá không âm và chiết khấu từ 0 đến 100%.</div>");
                writeFooter(out);
            }
            return;
        }

        // Discount Amount = List Price * Discount Percent * 0.01
        BigDecimal discountAmount = listPrice.multiply(discountPercent).movePointLeft(2);
        BigDecimal discountPrice = listPrice.subtract(discountAmount);

        try (PrintWriter out = response.getWriter()) {
            writeHeader(out, "Discount Result");
            out.println("<p class='intro'>Thông tin sản phẩm và kết quả tính chiết khấu.</p>");
            out.println("<dl class='summary'><dt>Product Description</dt>");
            out.println("<dd id='product-description'>" + escapeHtml(description.strip()) + "</dd>");
            out.println("<dt>List Price</dt><dd id='list-price'>" + format(listPrice) + "</dd>");
            out.println("<dt>Discount Percent</dt><dd><span id='discount-percent'>" + format(discountPercent) + "</span>%</dd></dl>");
            out.println("<div class='amount'><span>Discount Amount</span><strong id='discount-amount'>" + format(discountAmount) + "</strong></div>");
            out.println("<div class='price'><span>Discount Price</span><strong id='discount-price'>" + format(discountPrice) + "</strong></div>");
            writeFooter(out);
        }
    }

    private static BigDecimal parseNumber(String value) {
        if (value == null || value.isBlank() || value.length() > 64) {
            throw new NumberFormatException();
        }
        BigDecimal number = new BigDecimal(value.trim());
        if (number.precision() > 30 || number.scale() < -18 || number.scale() > 18) {
            throw new NumberFormatException();
        }
        return number;
    }

    private static String format(BigDecimal number) {
        return number.stripTrailingZeros().toPlainString();
    }

    private static String escapeHtml(String value) {
        return value.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }

    private static void writeHeader(PrintWriter out, String title) {
        out.println("<!DOCTYPE html><html lang='vi'><head><meta charset='UTF-8'>");
        out.println("<meta name='viewport' content='width=device-width, initial-scale=1'>");
        out.println("<title>" + title + "</title><link rel='stylesheet' href='assets/styles.css'></head>");
        out.println("<body><main class='card'><p class='tag'>CODEGYM · JAVA SERVLET</p><h1>" + title + "</h1>");
    }

    private static void writeFooter(PrintWriter out) {
        out.println("<a class='button' href='index.jsp'>Calculate Again</a></main></body></html>");
    }
}
