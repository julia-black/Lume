package com.singlelab.lume.model.event

enum class Distance(val id: Int, val value: Double? = null, val title: String) {
    NEAR(id = 0, value = 0.03, title = "Близко ко мне"),
    MIDDLE(id = 1, value = 0.09, title = "На среднем расстоянии от меня"),
    FAR(id = 2, title = "В рамках города");

    companion object {
        fun find(id: Int): Distance {
            return values().find {
                it.id == id
            } ?: FAR
        }
    }
}