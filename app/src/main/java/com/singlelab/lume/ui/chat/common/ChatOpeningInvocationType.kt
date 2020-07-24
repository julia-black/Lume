package com.singlelab.lume.ui.chat.common

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
open class ChatOpeningInvocationType(
    open val title: String
) : Parcelable {

    @Parcelize
    class Person(
        override val title: String,
        val personUid: String
    ) : ChatOpeningInvocationType(
        title
    )

    @Parcelize
    class Common(
        override val title: String,
        val chatUid: String
    ) : ChatOpeningInvocationType(
        title
    )
}