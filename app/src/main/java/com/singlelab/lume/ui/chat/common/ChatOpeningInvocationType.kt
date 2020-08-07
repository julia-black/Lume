package com.singlelab.lume.ui.chat.common

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
open class ChatOpeningInvocationType(
    open val title: String,
    open val isGroup: Boolean
) : Parcelable {

    @Parcelize
    class Person(
        override val title: String,
        val personUid: String,
        override val isGroup: Boolean = false
    ) : ChatOpeningInvocationType(
        title,
        isGroup
    )

    @Parcelize
    class Common(
        override val title: String,
        val chatUid: String,
        override val isGroup: Boolean
    ) : ChatOpeningInvocationType(
        title,
        isGroup
    )
}