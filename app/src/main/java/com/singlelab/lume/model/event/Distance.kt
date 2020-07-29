package com.singlelab.lume.model.event

enum class Distance(val id: Int, val title: String) {
    NEAR(0, "На расстоянии 2 км"),
    MIDDLE(1, "На расстоянии 5 км"),
    FAR(2, "В рамках города");

    companion object {
        fun find(id: Int): Distance {
            return values().find {
                it.id == id
            } ?: FAR
        }
    }
}