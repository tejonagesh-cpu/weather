package com.app.weather.base

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.app.weather.MyApplication
import com.app.weather.webservice.ApiServiceInterface
import javax.inject.Inject

abstract class BaseFragment : Fragment() {

    lateinit var myApplication: MyApplication

    lateinit var baseActivity: BaseActivity

    @Inject
    lateinit var webApiService: ApiServiceInterface

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myApplication = baseActivity.application as MyApplication
        myApplication.appComponent.inject(this)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        baseActivity = context as BaseActivity
    }
}