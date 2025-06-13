package ca.buildsystem.reports.model;

/**
 * Enumeration of report types supported by the system.
 * Each type represents a different category of financial report.
 */
public enum ReportType {
    FINANCIAL_STATEMENT,    // Balance sheets, income statements, cash flow statements
    ACCOUNTS_PAYABLE,       // Reports on outstanding debts to suppliers/vendors
    ACCOUNTS_RECEIVABLE,    // Reports on outstanding payments from clients
    PROJECT_PROFITABILITY,  // Profitability analysis for specific projects
    CASH_FLOW,              // Cash flow analysis and projections
    EXPENSE,                // Expense reports by category, project, or time period
    REVENUE,                // Revenue reports by source, project, or time period
    TAX,                    // Tax-related financial reports
    ASSET,                  // Reports on company assets and depreciation
    CUSTOM                  // Custom report types defined by users
}
