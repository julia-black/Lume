package com.singlelab.lume.ui.receive_reward.interactor

import com.singlelab.lume.base.BaseInteractor
import com.singlelab.net.model.person.FeedbackRequest
import com.singlelab.net.repositories.BaseRepository
import com.singlelab.net.repositories.person.PersonRepository

class ReceiveRewardInteractorImpl(private val repository: PersonRepository) :
    BaseInteractor(repository as BaseRepository), ReceiveRewardInteractor {

    override suspend fun addFeedback(request: FeedbackRequest) {
        repository.addFeedback(request)
    }
}