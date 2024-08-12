package com.example.yogabook

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

enum class Difficulty {
    EASY,
    MEDIUM,
    HARD
}

data class Pose(val name: String, val imageResId: Int, val durationSeconds: Int, val difficulty: Difficulty)

val poses = listOf(
    Pose("Cat", R.drawable.pose_cat, 30, Difficulty.MEDIUM),
    Pose("Cow", R.drawable.pose_cow, 30, Difficulty.MEDIUM),
    Pose("Banana", R.drawable.pose_banana, 120, Difficulty.EASY),
    Pose("Bound Angle", R.drawable.pose_bound_angle, 90, Difficulty.EASY),
    Pose("Box Neutral", R.drawable.pose_box_neutral, 45, Difficulty.EASY),
    Pose("Butterfly", R.drawable.pose_butterfly, 10, Difficulty.HARD),
    Pose("Chair", R.drawable.pose_chair, 90, Difficulty.MEDIUM),
    Pose("Downward Dog", R.drawable.pose_downward_dog, 30, Difficulty.MEDIUM),
    Pose("Goddess", R.drawable.pose_goddess, 180, Difficulty.MEDIUM),
    Pose("Crescent Lunge on the Knee", R.drawable.pose_warrior_kneeling, 30, Difficulty.HARD),
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

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
    }

    override fun onCardClick(pose: Pose) {
        val intent = Intent(this, DetailsActivity::class.java)
        intent.putExtra("poseName", pose.name)
        intent.putExtra("poseImageResId", pose.imageResId)
        intent.putExtra("durationSeconds", pose.durationSeconds)
        intent.putExtra("difficulty", pose.difficulty.name)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_open_dialog -> {
                val dialogFragment = InfoDialogFragment()
                dialogFragment.show(supportFragmentManager, "info_dialog")
                true
            }
            else -> true
        }
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