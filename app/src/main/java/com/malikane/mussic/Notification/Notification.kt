package com.malikane.mussic.Notification

import android.app.Activity
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.res.Resources
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import com.malikane.mussic.Enum.PlayerAction
import com.malikane.mussic.Enum.PlayerStatus
import com.malikane.mussic.MainActivity
import com.malikane.mussic.R
import com.malikane.mussic.Service.PlayerBroadCastReceiver

class Notification(private var activity: Activity, private var notificationManager: NotificationManager) {

	//we can create multiple notification channel, now we define one channel for our app
	private val NOTIFICATION_ID=308
	fun buildNotification(songName:String,playerStatus: PlayerStatus){
		//when user clicked to content, our app will be open
		val activityIntent=Intent(activity,MainActivity::class.java)
		val contentIntent=PendingIntent.getActivity(activity,0,activityIntent,0)

		val largeIcon=BitmapFactory.decodeResource(Resources.getSystem(),R.drawable.audiotrack_black)
		var notfAction=R.drawable.pause_black
		//Allow to user make some action on notification button
		val playerAction: PendingIntent?
		val playPause:String

		if(playerStatus==PlayerStatus.PLAYING){
			playPause="pause"
			playerAction=playerAction(PlayerAction.ACTION_PAUSE)
		}
		else{
			playPause="play"
			notfAction=R.drawable.play_button
			playerAction=playerAction(PlayerAction.ACTION_PLAY)
		}
		//create and customize notification
		val builder=NotificationCompat.Builder(activity)
			.setShowWhen(false)
			.setLargeIcon(largeIcon)
			.setContentTitle(songName)
			.setContentText("Mussic")
			.setSmallIcon(R.drawable.audiotrack_black)
				//When user clicked to content, our app will open
			.setContentIntent(contentIntent)
			.addAction(notfAction,playPause,playerAction)
			.addAction(R.drawable.shuffle_black,"shuffle",playerAction(PlayerAction.ACTION_SHUFFLE_PLAY))
			.addAction(R.drawable.replay_black,"replay",playerAction(PlayerAction.ACTION_REPLAY))
			//
			notificationManager.notify(NOTIFICATION_ID,builder.build())
	}

	fun deleteNotification() = notificationManager.cancel(NOTIFICATION_ID)

	//create Pending Intent for notification
	private fun playerAction(playerAction:PlayerAction):PendingIntent?{
		val intent = Intent(activity.applicationContext,PlayerBroadCastReceiver::class.java)
		when(playerAction){
			PlayerAction.ACTION_PLAY->intent.action=PlayerAction.ACTION_PLAY.action

			PlayerAction.ACTION_PAUSE->intent.action=PlayerAction.ACTION_PAUSE.action

			PlayerAction.ACTION_SHUFFLE_PLAY->intent.action=PlayerAction.ACTION_SHUFFLE_PLAY.action

			PlayerAction.ACTION_REPLAY->intent.action=PlayerAction.ACTION_REPLAY.action
		}
		return PendingIntent.getBroadcast(activity.applicationContext,0,intent,0)
	}
}