package com.nodesagency.logviewer.data

import androidx.paging.DataSource
import com.nodesagency.logviewer.data.model.Category
import com.nodesagency.logviewer.data.model.LogDetails
import com.nodesagency.logviewer.data.model.LogEntry
import com.nodesagency.logviewer.data.model.Severity


interface LogRepository {

    fun getAlphabeticallySortedCategories(): DataSource.Factory<Int, Category>

    fun getChronologicallySortedLogEntries(categoryId: Long): DataSource.Factory<Int, LogEntry>

    /**
     * Inserts a category if the ID is null or replaces it if the ID already exists.
     *
     * @return the ID of the inserted category
     */
    fun put(category: Category): Long

    /**
     * Inserts a severity if the ID is null or replaces it if the ID already exists.
     *
     * @return the ID of the inserted severity
     */
    fun put(severity: Severity): Long

    fun getLogEntriesForCategoryId(categoryId: Long): List<LogEntry>

    fun put(logEntry: LogEntry)

    /**
     * @return the ID of the category entry for the provided category name
     */
    fun getIdForCategoryName(name: String): Long?

    /**
     * @return the ID of the severity entry for a provided level
     */
    fun getIdForSeverityLevel(severityLevel: String): Long?

    fun deleteAllCategoriesAndLogs()

    fun getLogDetails(logEntryId: Long): LogDetails
}