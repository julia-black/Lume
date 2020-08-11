package com.singlelab.lume.model.event

import com.singlelab.lume.R

enum class EventType(
    val id: Int,
    val resId: Int,
    val titleRes: Int,
    val colorRes: Int = R.color.colorParty
) {
    PARTY(0, R.drawable.ic_emoji_party, R.string.party, R.color.colorParty),
    CULTURE(1, R.drawable.ic_emoji_picture, R.string.culture),
    SPORT(2, R.drawable.ic_emoji_sport, R.string.sport),
    NATURE(3, R.drawable.ic_emoji_nature, R.string.nature),
    COMMUNICATION(4, R.drawable.ic_emoji_talk, R.string.talk),
    GAME(5, R.drawable.ic_emoji_game, R.string.game),
    STUDY(6, R.drawable.ic_emoji_book, R.string.study),
    FOOD(7, R.drawable.ic_emoji_food, R.string.food),
    CONCERT(8, R.drawable.ic_emoji_speech, R.string.concert),
    TRAVEL(9, R.drawable.ic_emoji_travel, R.string.travel);

    companion object {
        fun findById(id: Int): EventType {
            return values().find {
                it.id == id
            } ?: COMMUNICATION
        }
    }

}