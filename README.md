# [Bài tập] Ứng dụng Product Discount Calculator

Ứng dụng Java Web tính chiết khấu sản phẩm, sử dụng Maven, JSP và Jakarta Servlet, tương thích Apache Tomcat 10.1.

## Yêu cầu môi trường

- JDK 17 trở lên; môi trường triển khai dùng JDK 21.
- Maven.
- Apache Tomcat 10.1, Jakarta Servlet 6.0 (`jakarta.servlet`).

## Chức năng theo đề bài

Trang `index.jsp` có form POST gửi tới `display-discount`, gồm:

- **Product Description**: `productDescription`.
- **List Price**: `listPrice`.
- **Discount Percent**: `discountPercent`.
- Nút **Calculate Discount**.

`DiscountServlet` nhận dữ liệu tại `/display-discount`, hiển thị lại ba thông tin đã nhập cùng hai kết quả:

```text
Discount Amount = List Price × Discount Percent × 0.01
Discount Price  = List Price − Discount Amount
```

Ví dụ: giá niêm yết `100000`, chiết khấu `10%` → lượng chiết khấu `10000`, giá sau chiết khấu `90000`.

## Xử lý dữ liệu

- Dùng `BigDecimal` để tính đúng số thập phân theo công thức, không tự làm tròn kết quả.
- Không giả định đơn vị tiền tệ; kết quả dùng cùng đơn vị với giá niêm yết.
- Giá không âm; tỷ lệ chiết khấu từ 0 đến 100%, bao gồm hai đầu mút.
- Mô tả sản phẩm bắt buộc, tối đa 1000 ký tự; ký tự HTML được mã hóa khi hiển thị.
- Thiếu hoặc sai dữ liệu: thông báo lỗi và HTTP 400.
- Mỗi trường số nhận tối đa 64 ký tự, 30 chữ số có nghĩa và scale từ -18 đến 18 để giới hạn kích thước dữ liệu.

## Cấu trúc Maven

```text
pom.xml
src/main/java/com/codegym/DiscountServlet.java
src/main/webapp/index.jsp
src/main/webapp/WEB-INF/web.xml
src/main/webapp/assets/styles.css
```

## Build và triển khai trên Windows

Chạy PowerShell trong thư mục chứa `pom.xml`:

```powershell
mvn clean package
Copy-Item -LiteralPath '.\target\jsp-servlet-product-discount-calculator.war' -Destination 'C:\Tools\apache-tomcat-10.1.60\webapps\jsp-servlet-product-discount-calculator.war'
# Chỉ khởi động khi Tomcat chưa chạy:
& 'C:\Tools\apache-tomcat-10.1.60\bin\startup.bat'
```

Mở <http://localhost:8080/jsp-servlet-product-discount-calculator/>. Nếu Tomcat cấu hình cổng khác, thay cổng theo connector HTTP trong `conf/server.xml`.

## Kiểm thử

- `100000`, `10%` → giảm `10000`, còn `90000`.
- `199.99`, `12.5%` → giảm `24.99875`, còn `174.99125`.
- `100`, `0%` → giảm `0`, còn `100`.
- `100`, `100%` → giảm `100`, còn `0`.
- Thiếu dữ liệu, giá âm hoặc phần trăm ngoài 0–100: thông báo lỗi.

Ví dụ POST trực tiếp:

```powershell
Invoke-WebRequest -Uri 'http://localhost:8080/jsp-servlet-product-discount-calculator/display-discount' -Method POST -Body @{productDescription='Áo thun'; listPrice='100000'; discountPercent='10'} -UseBasicParsing
```

## Nộp bài

Nộp link repository GitHub, nhánh `main`. `pom.xml` và cây `src/` nằm ngay tại gốc repository. File build trong `target/` được loại khỏi Git bằng `.gitignore`.

## Kết quả kiểm thử thực tế

Đã kiểm tra ngày 18/10/2026 trên JDK 21, Apache Tomcat 10.1.60, cổng HTTP 8080:

- `mvn clean package`: `BUILD SUCCESS`.
- Form JSP, CSS và kết quả POST `/display-discount`: HTTP 200.
- Năm trường hợp tính toán đều đúng: giá `100000` giảm `10%`; giá `199.99` giảm `12.5%`; mức giảm `0%`, `100%`; giá bằng `0`.
- Cả ba thông tin nhập vào được hiển thị lại đúng, bao gồm mô tả tiếng Việt.
- Chín trường hợp thiếu hoặc sai dữ liệu được báo lỗi với HTTP 400, không gây HTTP 500.
- Mô tả chứa ký tự HTML được mã hóa trước khi hiển thị.
