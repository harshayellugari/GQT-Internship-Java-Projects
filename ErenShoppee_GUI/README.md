# ErenShoppee - GUI E-Commerce Application

A fully-featured Swing GUI version of the ErenShoppee e-commerce app.

## Requirements
- Java JDK 8 or higher

## Project Structure
```
ErenShoppee_GUI/
├── src/main/
│   ├── Product.java        # Product model
│   ├── SubCategory.java    # SubCategory model
│   ├── Category.java       # Category model
│   ├── Cart.java           # Shopping cart (with remove support)
│   ├── DataLoader.java     # All product data (from original)
│   └── ErenShoppee.java    # Main GUI application
├── run.bat                 # Windows build & run
├── run.sh                  # Linux/Mac build & run
└── README.md
```

## How to Run

### Windows
```
run.bat
```

### Linux / Mac
```bash
chmod +x run.sh && ./run.sh
```

### Manual
```bash
mkdir out
javac -d out src/main/Product.java src/main/SubCategory.java src/main/Category.java src/main/Cart.java src/main/DataLoader.java src/main/ErenShoppee.java
java -cp out main.ErenShoppee
```

## Features
- 🏠 Home screen with category grid (5 categories with icons & descriptions)
- 📂 Sub-category browser (4 per category)
- 🛍 Product list with hover effects
- 📄 Product detail page (ID, brand, stock, price, description)
- 🛒 Add to Cart / Buy Now buttons
- 🛒 Cart screen with item removal and order summary
- 💳 Checkout with card/CVV/OTP fields
- ✅ Order success screen
- 🔔 Toast notification on add-to-cart
- 🎨 Dark theme with orange-red accent (professional e-commerce look)
