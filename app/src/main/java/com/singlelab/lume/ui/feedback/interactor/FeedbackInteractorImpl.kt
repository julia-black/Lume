package com.singlelab.lume.ui.feedback.interactor

import com.singlelab.lume.base.BaseInteractor
import com.singlelab.net.model.person.FeedbackRequest
import com.singlelab.net.repositories.BaseRepository
import com.singlelab.net.repositories.person.PersonRepository

class FeedbackInteractorImpl(private val repository: PersonRepository) :
    BaseInteractor(repository as BaseRepository), FeedbackInteractor {

    override suspend fun addFeedback(request: FeedbackRequest) {
        repository.addFeedback(request)
    }
}