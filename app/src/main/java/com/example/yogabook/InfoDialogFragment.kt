package com.example.yogabook

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class InfoDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle("YogaBook - How It Works")
                .setMessage("1. Browse Yoga Poses:\n" +
                        "Explore the list of yoga poses. Each pose is accompanied by a helpful image to guide you.\n" +
                        "\n" +
                        "2. Select a Pose:\n" +
                        "Tap on a pose to start the timer. This will begin your focused session on that particular pose.\n" +
                        "\n" +
                        "3. Timer & Notification:\n" +
                        "The timer will count down the duration for holding the pose. When the timer ends, you'll receive a gentle notification letting you know that the time is up.\n" +
                        "\n" +
                        "4. Rest or Continue:\n" +
                        "After the timer ends, you have the option to take a short rest or move directly to the next pose. Simply select your next pose to start the timer again.\n" +
                        "\n" +
                        "Stay mindful and enjoy your practice!")
                .setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}
