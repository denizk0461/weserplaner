package com.denizk0461.weserplaner.values

/**
 * Used to determine the order in which tasks should be shown.
 *
 * @param key   index by which to tell which order method to use
 */
enum class TaskOrder(key: Int) {

    /**
     * Order tasks by date due.
     */
    DATE_DUE(0),

    /**
     * Order tasks by date created.
     */
    DATE_CREATED(1),

    /**
     * Order tasks alphabetically.
     */
    ALPHABETICALLY(2),
    ;
}