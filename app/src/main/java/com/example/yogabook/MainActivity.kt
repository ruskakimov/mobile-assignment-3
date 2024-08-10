package com.example.yogabook

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

data class Pose(val name: String, val imageResId: Int)

val poses = listOf(
    Pose("Cat", R.drawable.pose_cat),
    Pose("Cow", R.drawable.pose_cow),
    Pose("Banana", R.drawable.pose_banana),
)

class MainActivity : AppCompatActivity(), OnCardClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // UI elements
        val recyclerView: RecyclerView = findViewById(R.id.poses_recycler_view)

        // Populate game list with games.
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        val poseAdapter = PoseAdapter(poses, this)
        recyclerView.adapter = poseAdapter
    }

    override fun onCardClick(pose: Pose) {
    }
}

interface OnCardClickListener {
    fun onCardClick(pose: Pose)
}

class PoseAdapter(
    private var poses: List<Pose>,
    private val listener: OnCardClickListener
) : RecyclerView.Adapter<PoseAdapter.PoseViewHolder>() {

    class PoseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val poseImage: ImageView = view.findViewById(R.id.pose_image)
        val poseTitle: TextView = view.findViewById(R.id.pose_title)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PoseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_pose, parent, false)
        return PoseViewHolder(view)
    }

    override fun onBindViewHolder(holder: PoseViewHolder, position: Int) {
        val pose = poses[position]
        holder.poseTitle.text = pose.name

        if (pose.imageResId != 0) {
            holder.poseImage.setImageResource(pose.imageResId)
        }

        holder.itemView.setOnClickListener {
            listener.onCardClick(pose)
        }
    }

    override fun getItemCount() = poses.size
}