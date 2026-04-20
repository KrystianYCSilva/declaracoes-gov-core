---
name: document-reader
description: |
  Convert binary documents (PDF, XLSX/XLS, DOCX, PPTX) to plain text or markdown so AI agents can read them.
  Use when: the user references a PDF, Excel, Word, or PowerPoint file, or when you need to extract
  content from a binary document for analysis, summarization, or code generation.
---

# Document Reader

Extracts readable text from binary document formats that AI agents cannot natively open.

## How to Convert a Document

1. Identify the file extension (`.pdf`, `.xlsx`, `.xls`, `.docx`, `.pptx`, `.doc`)
2. Run the conversion script:
   ```
   python <skill-dir>/scripts/convert.py "<file-path>"
   ```
3. The script outputs plain text (or markdown for tables) to **stdout**
4. Use the output directly in your context for analysis

### Supported formats

| Extension | Output format | Library used |
|-----------|--------------|--------------|
| `.pdf` | Plain text | `PyPDF2` |
| `.xlsx` / `.xls` | Markdown tables | `openpyxl` |
| `.docx` | Plain text | `python-docx` |
| `.pptx` | Slide-by-slide text | `python-pptx` |
| `.doc` | Plain text (via `textract` or `antiword`) | `textract` (optional) |

### Batch conversion

Pass multiple files:
```
python <skill-dir>/scripts/convert.py file1.pdf file2.xlsx file3.docx
```

## How to Install Dependencies

Run once per environment:
```
pip install PyPDF2 openpyxl python-docx python-pptx
```

Optional for `.doc` (legacy Word):
```
pip install textract
```

## How to Use the MCP Alternative

If you prefer an always-available MCP server instead of on-demand scripts, see
`references/mcp-servers.md` for ready-to-install MCP document readers.

## How to Handle Edge Cases

- **Scanned PDFs** (image-only): the script extracts no text. Suggest the user install `pytesseract` + Tesseract OCR, then use `references/ocr-fallback.md`.
- **Password-protected files**: the script will fail. Ask the user for the password or an unprotected copy.
- **Very large files** (>50 MB): warn the user about context limits. Use `--pages 1-5` or `--sheets Sheet1` flags to extract subsets.
- **Encoding issues**: the script defaults to UTF-8. If garbled output appears, try `--encoding latin-1`.
