#!/usr/bin/env python3
"""
Convert binary documents (PDF, XLSX, DOCX, PPTX) to plain text / markdown.

Usage:
    python convert.py <file> [<file2> ...]
    python convert.py --pages 1-5 report.pdf
    python convert.py --sheets Sheet1,Sheet2 data.xlsx
    python convert.py --encoding latin-1 legacy.docx
"""

import argparse
import sys
import os


def read_pdf(path: str, pages: str | None = None) -> str:
    try:
        from PyPDF2 import PdfReader
    except ImportError:
        return _missing("PyPDF2", "pip install PyPDF2")

    reader = PdfReader(path)
    page_range = _parse_range(pages, len(reader.pages))
    lines: list[str] = []
    for i in page_range:
        text = reader.pages[i].extract_text()
        if text:
            lines.append(f"--- Page {i + 1} ---")
            lines.append(text.strip())
    if not lines:
        return "(No extractable text found — the PDF may contain only images. Try OCR.)"
    return "\n\n".join(lines)


def read_xlsx(path: str, sheets: str | None = None) -> str:
    try:
        from openpyxl import load_workbook
    except ImportError:
        return _missing("openpyxl", "pip install openpyxl")

    wb = load_workbook(path, read_only=True, data_only=True)
    target_sheets = sheets.split(",") if sheets else wb.sheetnames
    parts: list[str] = []

    for name in target_sheets:
        if name not in wb.sheetnames:
            parts.append(f"## Sheet: {name}\n\n(Sheet not found)")
            continue
        ws = wb[name]
        rows = list(ws.iter_rows(values_only=True))
        if not rows:
            parts.append(f"## Sheet: {name}\n\n(Empty sheet)")
            continue

        # Build markdown table
        header = rows[0]
        col_names = [str(c) if c is not None else "" for c in header]
        md = f"## Sheet: {name}\n\n"
        md += "| " + " | ".join(col_names) + " |\n"
        md += "| " + " | ".join(["---"] * len(col_names)) + " |\n"
        for row in rows[1:]:
            cells = [str(c) if c is not None else "" for c in row]
            md += "| " + " | ".join(cells) + " |\n"
        parts.append(md)

    wb.close()
    return "\n\n".join(parts) if parts else "(No data found)"


def read_docx(path: str) -> str:
    try:
        from docx import Document
    except ImportError:
        return _missing("python-docx", "pip install python-docx")

    doc = Document(path)
    paragraphs = [p.text for p in doc.paragraphs if p.text.strip()]
    if not paragraphs:
        return "(No text found in document)"
    return "\n\n".join(paragraphs)


def read_pptx(path: str) -> str:
    try:
        from pptx import Presentation
    except ImportError:
        return _missing("python-pptx", "pip install python-pptx")

    prs = Presentation(path)
    parts: list[str] = []
    for i, slide in enumerate(prs.slides, 1):
        texts: list[str] = []
        for shape in slide.shapes:
            if shape.has_text_frame:
                for para in shape.text_frame.paragraphs:
                    t = para.text.strip()
                    if t:
                        texts.append(t)
        if texts:
            parts.append(f"--- Slide {i} ---\n" + "\n".join(texts))
    if not parts:
        return "(No text found in presentation)"
    return "\n\n".join(parts)


def read_doc(path: str) -> str:
    try:
        import textract
        text = textract.process(path).decode("utf-8", errors="replace")
        return text.strip() if text.strip() else "(No text found)"
    except ImportError:
        return _missing("textract", "pip install textract")
    except Exception as e:
        return f"(Failed to read .doc: {e})"


# --- Helpers ---

def _missing(pkg: str, cmd: str) -> str:
    return f"ERROR: '{pkg}' is not installed. Run: {cmd}"


def _parse_range(spec: str | None, total: int) -> range:
    if not spec:
        return range(total)
    parts = spec.split("-")
    start = max(int(parts[0]) - 1, 0)
    end = min(int(parts[1]), total) if len(parts) > 1 else start + 1
    return range(start, end)


READERS = {
    ".pdf": read_pdf,
    ".xlsx": read_xlsx,
    ".xls": read_xlsx,
    ".docx": read_docx,
    ".pptx": read_pptx,
    ".doc": read_doc,
}


def convert_file(path: str, pages=None, sheets=None, encoding=None) -> str:
    if not os.path.isfile(path):
        return f"ERROR: File not found: {path}"

    ext = os.path.splitext(path)[1].lower()
    reader = READERS.get(ext)
    if not reader:
        supported = ", ".join(READERS.keys())
        return f"ERROR: Unsupported format '{ext}'. Supported: {supported}"

    if ext == ".pdf":
        return reader(path, pages=pages)
    elif ext in (".xlsx", ".xls"):
        return reader(path, sheets=sheets)
    else:
        return reader(path)


def main():
    parser = argparse.ArgumentParser(
        description="Convert binary documents to plain text / markdown"
    )
    parser.add_argument("files", nargs="+", help="File(s) to convert")
    parser.add_argument("--pages", help="Page range for PDFs (e.g. 1-5)")
    parser.add_argument("--sheets", help="Comma-separated sheet names for Excel")
    parser.add_argument("--encoding", default="utf-8", help="Text encoding (default: utf-8)")
    args = parser.parse_args()

    for filepath in args.files:
        if len(args.files) > 1:
            print(f"\n{'='*60}")
            print(f"FILE: {filepath}")
            print(f"{'='*60}\n")
        result = convert_file(filepath, pages=args.pages, sheets=args.sheets, encoding=args.encoding)
        print(result)


if __name__ == "__main__":
    main()
