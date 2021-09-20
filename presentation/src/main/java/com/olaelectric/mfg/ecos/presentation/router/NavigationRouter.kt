package com.olaelectric.mfg.ecos.presentation.router

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavGraph
import androidx.navigation.NavOptions
import com.olaelectric.mfg.ecos.presentation.R
import com.olaelectric.mfg.ecos.presentation.activities.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.Serializable
import kotlin.coroutines.CoroutineContext

interface INavigationRouter<Dest> {
    fun goto(destination: Dest, bundle: Bundle? = null, forceNav: Boolean = false)
    fun startDestination(destination: Dest, bundle: Bundle? = null)
    fun backPressed()
    fun recreate()
    fun popToDestination(id: Dest): Boolean
    fun gotoFragmentFromDiffActivity(destination: Dest, bundle: Bundle? = null)
}

sealed class Destination(val layoutIdRes: Int) : Serializable {
    object StartFragment : Destination(R.id.startFragment)
}

class NavigationRouter(private val appLifecycle: IAppLifecycle) :
    INavigationRouter<Destination>,
    CoroutineScope {
    private val navController: NavController
        get() = (appLifecycle.foregroundActivity as MainActivity).navController

    private val navGraph: NavGraph
        get() = (appLifecycle.foregroundActivity as MainActivity).navGraph

    // this method recreates the current fragment
    override fun recreate() {
        navController.currentDestination?.id?.let { currentScreenId ->

            navController.navigate(
                currentScreenId,
                null,
                NavOptions.Builder()
                    .setPopUpTo(currentScreenId, true)
                    .build()
            )
        }
    }

    private fun setStartDestination(destination: Destination, bundle: Bundle? = null) {
        // val newBundle = addBundle(destination.screenName, bundle,destination.layoutIdRes)
        launch {
            navGraph.startDestination = destination.layoutIdRes
            navController.setGraph(navGraph, bundle)
        }
    }

    private fun launchFragment(destination: Destination, directions: NavDirections) {
        launch {
            navController.navigate(
                directions,
                getNavOptions(destination.layoutIdRes)
            )
        }
    }

    private fun launchFragment(destination: Destination, layoutIdResClear: Int, bundle: Bundle?) {
        val newBundle = addBundle(bundle)
        launch {
            navController.navigate(
                destination.layoutIdRes,
                newBundle,
                getNavOptions(layoutIdResClear)
            )
        }
    }

    private fun launchFragment(destination: Destination, bundle: Bundle?) {
        val newBundle = addBundle(bundle)
        launch {
            navController.navigate(
                destination.layoutIdRes,
                newBundle,
                getNavOptions(destination.layoutIdRes)
            )
        }
    }

    private fun launchFragmentWhileComingFromDiffActivity(
        destination: Destination,
        bundle: Bundle?
    ) {
        launch {
            navController.navigate(
                destination.layoutIdRes,
                bundle,
                getNavOptions(destination.layoutIdRes)
            )
        }
    }

    private fun getNavOptions(layoutIdRes: Int): NavOptions {
        return NavOptions.Builder()
            .setPopUpTo(layoutIdRes, true)
            .setEnterAnim(R.anim.anim_fragment_fade_in)
            .setExitAnim(R.anim.anim_fragment_fade_out)
            .setPopEnterAnim(R.anim.anim_fragment_fade_in)
            .setPopExitAnim(R.anim.anim_fragment_fade_out)
            .build()
    }

    private fun addBundle(bundle: Bundle?): Bundle {
        val newBundle = bundle ?: Bundle()
        return newBundle
    }

    // created method because while coming from different activity
    // source is of activity in analytics
    override fun gotoFragmentFromDiffActivity(destination: Destination, bundle: Bundle?) {
        launchFragmentWhileComingFromDiffActivity(destination, bundle)
    }

    // for navigating to another fragment
    override fun goto(
        destination: Destination,
        bundle: Bundle?,
        forceNav: Boolean
    ) {
        if (destination.layoutIdRes == navController.currentDestination?.id) return
        if (forceNav) {
            launchFragment(destination, bundle)
            return
        }
        launchFragment(destination, bundle)
    }

    // for setting initial fragment in activity
    override fun startDestination(destination: Destination, bundle: Bundle?) {
        setStartDestination(destination, bundle)
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()

    override fun backPressed() {
        navController.popBackStack()
    }

    // returns true if back stack is null
    private fun isBackStackNull(): Boolean = navController.previousBackStackEntry == null

    override fun popToDestination(id: Destination): Boolean {
        return navController.popBackStack(id.layoutIdRes, false)
    }
}
