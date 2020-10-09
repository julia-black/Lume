package com.singlelab.lume.ui.receive_reward.interactor

import com.singlelab.net.model.person.FeedbackRequest

interface ReceiveRewardInteractor {
    suspend fun addFeedback(request: FeedbackRequest)
}