package com.singlelab.lume.model.event

enum class EventStatus(private val id: Int) {
    CANCELLED(3),
    ENDED(2),
    IN_PROGRESS(1),
    PREPARING(0);

    companion object {
        fun findById(id: Int): EventStatus {
            return values().find {
                it.id == id
            } ?: PREPARING
        }
    }
}