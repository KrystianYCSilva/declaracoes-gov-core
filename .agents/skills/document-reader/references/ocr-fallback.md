---
name: ocr-fallback
description: |
  OCR fallback for scanned PDFs and image-only documents.
  Use when: the main convert script returns no text from a PDF.
---

# OCR Fallback for Scanned PDFs

When `convert.py` returns "(No extractable text found)", the PDF likely contains scanned images.

## Prerequisites

1. Install Tesseract OCR engine:
   - **Windows**: `winget install UB-Mannheim.TesseractOCR` or download from https://github.com/UB-Mannheim/tesseract/wiki
   - **macOS**: `brew install tesseract`
   - **Linux**: `sudo apt install tesseract-ocr`

2. Install Python bindings:
   ```bash
   pip install pytesseract pdf2image Pillow
   ```

3. Install Poppler (required by pdf2image):
   - **Windows**: download from https://github.com/oschwartz10612/poppler-windows/releases and add to PATH
   - **macOS**: `brew install poppler`
   - **Linux**: `sudo apt install poppler-utils`

## Usage

```python
from pdf2image import convert_from_path
import pytesseract

images = convert_from_path("scanned.pdf")
for i, img in enumerate(images):
    text = pytesseract.image_to_string(img)
    print(f"--- Page {i+1} ---")
    print(text)
```

## Limitations

- OCR accuracy depends on scan quality
- Non-Latin scripts may need additional Tesseract language packs
- Processing is slower than native text extraction
