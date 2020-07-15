package com.singlelab.lume.ui.dashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.singlelab.lume.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_dashboard.*

@AndroidEntryPoint
class DashboardFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        text_dashboard.text = "is dashboard"
    }
}