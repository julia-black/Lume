package com.singlelab.lume.model

import java.util.*

object Const {
    const val URL_KEY = "Url"
    const val APP_NAME = "Lume"
    const val FOLDER_NAME = "Lume Images"
    const val BASE_URL = "https://lumeprod.azurewebsites.net/api/"
    const val STORE_URL = "https://play.google.com/store/apps/details?id=com.singlelab.lume"
    const val GOOGLE_PLAY = "play.google"

    const val PREF = "lume_pref"
    const val LOG_TAG = "lume_app"
    const val ANDROID = "android"

    const val DATE_FORMAT_TIME_ZONE = "yyyy-MM-dd'T'HH:mm:ss.SSS"
    const val DATE_FORMAT_OUTPUT = "dd.MM.yyyy HH:mm"
    const val DATE_FORMAT_OUTPUT_WITH_NAME_MONTH = "dd MMMM HH:mm"
    const val DATE_FORMAT_OUTPUT_WITH_NAME_MONTH_SHORT = "dd MMM HH:mm"
    const val DATE_FORMAT_ON_CARD = "dd MMMM, HH:mm"
    const val DATE_FORMAT_ISO = "yyyy-MM-dd"
    const val DATE_FORMAT_SIMPLE = "dd MMMM yyyy"
    const val DATE_FORMAT_ONLY_TIME = "HH:mm"
    const val DATE_FORMAT_SHORT_DATE = "dd.MM"
    const val UTC = "UTC"

    const val PAGE_SIZE = 20

    const val ERROR_CODE_NO_MATCHING_EVENTS = 25
    const val ERROR_CODE_NO_MATCHING_PERSONS = 27
    const val ERROR_CODE_NEW_PUSH_TOKEN = 41

    const val MIN_AGE = 0
    const val MAX_AGE = 100

    const val MAX_COUNT_IMAGES = 5
    const val SELECT_IMAGE_REQUEST_CODE = 102

    const val CARD_NUM_LENGTH = 16
    const val MIN_DELAY_FOR_TRANSITION = 350L

    const val CODE_CHAR_SPACE = 160

    const val REGEX_LOGIN = "[a-z0-9_.]+"

    val RUS_LOCALE = Locale("ru", "RU")
}