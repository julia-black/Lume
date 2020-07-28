package com.singlelab.lume.base

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.singlelab.lume.MainActivity
import com.singlelab.lume.R
import com.singlelab.lume.base.listeners.OnActivityResultListener
import com.singlelab.lume.base.listeners.OnLogoutListener
import com.singlelab.lume.base.listeners.OnSearchListener
import com.singlelab.lume.base.view.ErrorView
import com.singlelab.lume.base.view.LoadingView
import com.singlelab.net.model.auth.AuthData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import moxy.MvpAppCompatFragment
import kotlin.coroutines.CoroutineContext

open class BaseFragment : MvpAppCompatFragment(), ErrorView, LoadingView {
    private val parentJob = Job()

    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default

    private val scope = CoroutineScope(coroutineContext)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showLoading(false)
        if (AuthData.isAnon && this is OnlyForAuthFragments) {
            //todo подумать, как можно получше сделать общий обработчик того, что если
            // пользователь не залогинен, то с некоторых экранов должен осуществляться переход на авторизацию
            toAuth()
        } else {
            if (this is OnLogoutListener) {
                (activity as MainActivity?)?.setLogoutListener(this)
            }
            if (this is OnSearchListener) {
                (activity as MainActivity?)?.setSearchListener(this)
            }
            if (this is OnActivityResultListener) {
                (activity as MainActivity?)?.setActivityListener(this)
            }
        }
    }

    override fun showError(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    override fun showLoading(isShow: Boolean) {
        (activity as MainActivity?)?.showLoading(isShow)
    }

    override fun toAuth() {
        showLoading(false)
        findNavController().popBackStack()
        findNavController().navigate(R.id.auth)
    }

    protected fun invokeSuspend(block: suspend () -> Unit) {
        scope.launch { block.invoke() }
    }

    protected fun runOnMainThread(block: () -> Unit) {
        scope.launch(CoroutineContextProvider().main) {
            block.invoke()
        }
    }

    fun hideKeyboard() {
        val imm: InputMethodManager =
            activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = activity?.currentFocus
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}