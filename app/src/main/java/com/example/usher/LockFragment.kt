package com.example.usher

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.os.Build
import androidx.core.content.ContextCompat.getSystemService
import android.app.NotificationManager



// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [LockFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [LockFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LockFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

    private var pinned: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View =  inflater.inflate(R.layout.fragment_lock, container, false)

        val button: Button = view.findViewById(R.id.button)
        button.setOnClickListener {

            val money: TextView = view.findViewById(R.id.money)
            val discount_money: TextView = view.findViewById(R.id.discount_money)
            if (!pinned) {
                // Set pinned and do not disturb mode
                (context as Activity).startLockTask()
                val bottomNavView: BottomNavigationView =
                    activity!!.findViewById(R.id.bottom_navigation)
                bottomNavView.visibility = View.INVISIBLE
                button.text = "Unlock phone"

                // Fade out old money, fade in new
                money.startAnimation( AnimationUtils.loadAnimation(view.context, android.R.anim.slide_out_right))
                money.visibility = View.INVISIBLE
                discount_money.startAnimation( AnimationUtils.loadAnimation(view.context, android.R.anim.slide_in_left))
                discount_money.visibility = View.VISIBLE

                val notificationManager =
                    (context as FragmentActivity).getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !notificationManager.isNotificationPolicyAccessGranted) {

                    val intent = Intent(
                        android.provider.Settings
                            .ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS
                    )

                    startActivity(intent)
                }

                val am: AudioManager = (activity as FragmentActivity).baseContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager
                am.ringerMode = AudioManager.RINGER_MODE_SILENT


            } else {
                // Unpin and reset mode to whatever it was before
                (context as Activity).stopLockTask()
                val bottomNavView: BottomNavigationView =
                    activity!!.findViewById(R.id.bottom_navigation)
                bottomNavView.visibility = View.VISIBLE
                button.text = "Lock phone"

                discount_money.startAnimation( AnimationUtils.loadAnimation(view.context, android.R.anim.slide_out_right))
                discount_money.visibility = View.INVISIBLE
                money.startAnimation( AnimationUtils.loadAnimation(view.context, android.R.anim.slide_in_left))
                money.visibility = View.VISIBLE
            }

            pinned = !pinned
        }

        return view
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LockFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LockFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
