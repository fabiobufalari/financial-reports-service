package ca.buildsystem.reports.model;

/**
 * Enumeration of supported report output formats.
 * Defines the file formats in which reports can be generated and exported.
 */
public enum ReportFormat {
    PDF,    // Portable Document Format
    EXCEL,  // Microsoft Excel format
    CSV,    // Comma-Separated Values
    HTML,   // HyperText Markup Language
    JSON    // JavaScript Object Notation (for API responses)
}
