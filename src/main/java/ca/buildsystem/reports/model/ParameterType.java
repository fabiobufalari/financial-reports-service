package ca.buildsystem.reports.model;

/**
 * Enumeration of parameter types supported in report templates.
 * These types define the data type and validation rules for report parameters.
 */
public enum ParameterType {
    STRING,     // Text input
    NUMBER,     // Numeric input (integer or decimal)
    DATE,       // Date input
    DATETIME,   // Date and time input
    BOOLEAN,    // Boolean/checkbox input
    LIST,       // Selection from a predefined list of values
    MULTI_LIST, // Multiple selections from a predefined list
    CURRENCY,   // Currency amount (with CAD as default)
    PERCENTAGE  // Percentage value
}
