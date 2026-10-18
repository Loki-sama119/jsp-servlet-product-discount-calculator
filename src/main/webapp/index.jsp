<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Product Discount Calculator</title>
    <link rel="stylesheet" href="assets/styles.css">
</head>
<body>
    <main class="card">
        <p class="tag">CODEGYM · JAVA SERVLET</p>
        <h1>Product Discount Calculator</h1>
        <p class="intro">Tính số tiền được giảm và giá sản phẩm sau chiết khấu.</p>
        <form action="display-discount" method="POST">
            <label for="productDescription">Product Description</label>
            <textarea id="productDescription" name="productDescription" rows="3"
                      maxlength="1000" placeholder="Nhập mô tả sản phẩm" required></textarea>
            <label for="listPrice">List Price</label>
            <input id="listPrice" type="number" name="listPrice" min="0" step="any"
                   placeholder="Giá niêm yết, ví dụ: 100000" required>
            <label for="discountPercent">Discount Percent (%)</label>
            <input id="discountPercent" type="number" name="discountPercent" min="0" max="100"
                   step="any" placeholder="Ví dụ: 10" required aria-describedby="discount-hint">
            <p id="discount-hint" class="hint">Nhập tỷ lệ chiết khấu từ 0 đến 100%.</p>
            <button type="submit">Calculate Discount</button>
        </form>
        <p class="formula">Discount Amount = List Price × Discount Percent × 0.01</p>
    </main>
</body>
</html>
