package org.godotengine.plugin.android.firebase

import android.app.Activity
import android.view.View
import com.google.firebase.inappmessaging.FirebaseInAppMessaging
import com.google.firebase.messaging.FirebaseMessaging
import org.godotengine.godot.Godot
import org.godotengine.godot.plugin.GodotPlugin
import org.godotengine.godot.plugin.SignalInfo


@Suppress("Unused")
class FirebasePlugin(godot: Godot): GodotPlugin(godot) {
	override fun getPluginName(): String {
		return "FirebasePlugin"
	}

	override fun onMainCreate(activity: Activity?): View? {
		super.onMainCreate(activity)

		if (activity == null) {
			// Handle safely if activity is null
			return null
		}
		FirebaseInAppMessaging.getInstance().isAutomaticDataCollectionEnabled = true
		FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
			if (task.isSuccessful && task.result != null) {
				val token = task.result
				runOnRenderThread {
					emitSignal("fcm_token_received", token)
				}
			} else {
				runOnRenderThread {
					emitSignal("fcm_token_received", "")
				}
			}
		}

		return null // <--- You must explicitly return null (or a valid View instance)
	}
	override fun getPluginSignals(): Set<SignalInfo> {
		return setOf(SignalInfo("fcm_token_received", String::class.java))
	}
}